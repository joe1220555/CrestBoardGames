package tw.crestnetwork.boardgames.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import tw.crestnetwork.boardgames.api.BoardGamesApi;
import tw.crestnetwork.boardgames.api.GameDefinition;
import tw.crestnetwork.boardgames.core.module.ModuleManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CrestBoardGamesPlugin extends JavaPlugin implements BoardGamesApi {
    private final Map<String, GameDefinition> games = new ConcurrentHashMap<>();
    private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        moduleManager = new ModuleManager(this);
        moduleManager.loadAll();
        getLogger().info("CrestBoardGames 已啟用，共載入 " + moduleManager.loadedCount() + " 個遊戲模組。");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) moduleManager.disableAll();
        games.clear();
    }

    @Override
    public void registerGame(GameDefinition definition) {
        GameDefinition previous = games.putIfAbsent(definition.id(), definition);
        if (previous != null) throw new IllegalStateException("重複遊戲 ID：" + definition.id());
    }

    @Override
    public Optional<GameDefinition> game(String id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public Collection<GameDefinition> games() {
        return List.copyOf(games.values());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("modules")) {
            if (!sender.hasPermission("crestboardgames.admin")) {
                sender.sendMessage(ChatColor.RED + "你沒有權限查看模組資訊。");
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "CrestBoardGames 模組：");
            moduleManager.descriptions().forEach(description -> sender.sendMessage(
                    ChatColor.YELLOW + "- " + description.name() + " " + description.version()
                            + ChatColor.GRAY + " (" + description.id() + ")"));
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Crest 桌遊" + ChatColor.GRAY + "：目前有 "
                + games.size() + " 種遊戲可用。大廳 GUI 將在下一階段加入。");
        return true;
    }
}
