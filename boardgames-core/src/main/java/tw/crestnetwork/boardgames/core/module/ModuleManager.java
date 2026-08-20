package tw.crestnetwork.boardgames.core.module;

import org.bukkit.configuration.file.YamlConfiguration;
import tw.crestnetwork.boardgames.api.BoardGameModule;
import tw.crestnetwork.boardgames.api.BoardGamesApi;
import tw.crestnetwork.boardgames.api.ModuleDescription;
import tw.crestnetwork.boardgames.api.GameCreationContext;
import tw.crestnetwork.boardgames.api.GameSession;
import tw.crestnetwork.boardgames.core.CrestBoardGamesPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.Optional;

public final class ModuleManager {
    private static final String DESCRIPTOR = "crest-boardgame-module.yml";
    private static final String SAFE_NAME = "[A-Za-z][A-Za-z0-9_-]{2,63}";
    private static final String SAFE_ID = "[a-z][a-z0-9_-]{1,63}";

    private final CrestBoardGamesPlugin plugin;
    private final Path modulesDirectory;
    private final List<LoadedModule> loaded = new ArrayList<>();

    public ModuleManager(CrestBoardGamesPlugin plugin) {
        this.plugin = plugin;
        this.modulesDirectory = plugin.getDataFolder().toPath().resolve("modules").toAbsolutePath().normalize();
    }

    public void loadAll() {
        try {
            Files.createDirectories(modulesDirectory);
        } catch (IOException exception) {
            plugin.getLogger().log(Level.SEVERE, "無法建立模組資料夾", exception);
            return;
        }

        Set<String> ids = new HashSet<>();
        try (Stream<Path> paths = Files.list(modulesDirectory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".jar"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .forEach(path -> loadOne(path, ids));
        } catch (IOException exception) {
            plugin.getLogger().log(Level.SEVERE, "掃描模組失敗", exception);
        }
    }

    private void loadOne(Path jarPath, Set<String> ids) {
        URLClassLoader classLoader = null;
        try {
            ModuleDescription descriptor = readDescriptor(jarPath);
            validateDescriptor(jarPath, descriptor, ids);
            Path dataDirectory = modulesDirectory.resolve(descriptor.name()).normalize();
            if (!dataDirectory.getParent().equals(modulesDirectory)) {
                throw new IllegalArgumentException("不安全的模組資料夾路徑");
            }
            Files.createDirectories(dataDirectory.resolve("data"));
            installDefaultConfig(jarPath, dataDirectory);

            classLoader = new URLClassLoader(new java.net.URL[]{jarPath.toUri().toURL()}, plugin.getClass().getClassLoader());
            Class<?> entryClass = Class.forName(descriptor.mainClass(), true, classLoader);
            if (!BoardGameModule.class.isAssignableFrom(entryClass)) {
                throw new IllegalArgumentException("模組入口未實作 BoardGameModule");
            }
            BoardGameModule module = (BoardGameModule) entryClass.getDeclaredConstructor().newInstance();
            ModuleDescription runtime = module.description();
            if (!runtime.id().equals(descriptor.id()) || !runtime.name().equals(descriptor.name())
                    || runtime.apiVersion() != descriptor.apiVersion()) {
                throw new IllegalArgumentException("模組執行資訊與描述檔不一致");
            }

            module.onLoad(new ModuleContextImpl(plugin, dataDirectory, plugin.getLogger()));
            module.onEnable();
            ids.add(descriptor.id());
            loaded.add(new LoadedModule(descriptor, module, classLoader));
            plugin.getLogger().info("已載入 " + descriptor.name() + " " + descriptor.version()
                    + "，SHA-256 " + sha256(jarPath).substring(0, 12) + "…");
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "模組載入失敗：" + jarPath.getFileName(), exception);
            if (classLoader != null) {
                try {
                    classLoader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void installDefaultConfig(Path jarPath, Path dataDirectory) throws IOException {
        Path target = dataDirectory.resolve("config.yml");
        if (Files.exists(target)) {
            return;
        }
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            JarEntry entry = jar.getJarEntry("default-config.yml");
            if (entry == null) {
                return;
            }
            try (var input = jar.getInputStream(entry)) {
                Files.copy(input, target);
            }
        }
    }

    private ModuleDescription readDescriptor(Path jarPath) throws IOException {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            JarEntry entry = jar.getJarEntry(DESCRIPTOR);
            if (entry == null) throw new IllegalArgumentException("缺少 " + DESCRIPTOR);
            try (InputStreamReader reader = new InputStreamReader(jar.getInputStream(entry), StandardCharsets.UTF_8)) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(reader);
                return new ModuleDescription(
                        yaml.getString("id", ""),
                        yaml.getString("name", ""),
                        yaml.getString("display-name", ""),
                        yaml.getString("version", ""),
                        yaml.getInt("api-version", -1),
                        yaml.getString("main", "")
                );
            }
        }
    }

    private void validateDescriptor(Path jarPath, ModuleDescription descriptor, Set<String> ids) {
        if (!descriptor.id().matches(SAFE_ID)) throw new IllegalArgumentException("模組 ID 格式錯誤");
        if (!descriptor.name().matches(SAFE_NAME)) throw new IllegalArgumentException("模組名稱格式錯誤");
        if (descriptor.apiVersion() != BoardGamesApi.API_VERSION) {
            throw new IllegalArgumentException("不相容的 API 版本：" + descriptor.apiVersion());
        }
        if (ids.contains(descriptor.id())) throw new IllegalArgumentException("重複模組 ID：" + descriptor.id());
        if (plugin.getConfig().getBoolean("modules.require-exact-jar-name", true)
                && !jarPath.getFileName().toString().equals(descriptor.name() + ".jar")) {
            throw new IllegalArgumentException("JAR 必須命名為 " + descriptor.name() + ".jar");
        }
    }

    private String sha256(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (var input = Files.newInputStream(path)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) >= 0) digest.update(buffer, 0, read);
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    public void disableAll() {
        for (int index = loaded.size() - 1; index >= 0; index--) {
            LoadedModule module = loaded.get(index);
            try {
                module.instance().onDisable();
            } catch (Exception exception) {
                plugin.getLogger().log(Level.WARNING, "停用模組失敗：" + module.description().name(), exception);
            }
            try {
                module.classLoader().close();
            } catch (IOException ignored) {
            }
        }
        loaded.clear();
    }

    public int loadedCount() {
        return loaded.size();
    }

    public List<ModuleDescription> descriptions() {
        return loaded.stream().map(LoadedModule::description).toList();
    }

    public Optional<GameSession> createSession(String gameId, GameCreationContext context) {
        return loaded.stream()
                .filter(module -> module.description().id().equals(gameId))
                .findFirst()
                .map(module -> module.instance().createSession(context));
    }
}
