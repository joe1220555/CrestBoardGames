package tw.crestnetwork.boardgames.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tw.crestnetwork.boardgames.api.BoardGamesApi;
import tw.crestnetwork.boardgames.api.GameDefinition;
import tw.crestnetwork.boardgames.core.module.ModuleManager;
import tw.crestnetwork.boardgames.core.room.BoardGameRoom;
import tw.crestnetwork.boardgames.core.room.RoomManager;
import tw.crestnetwork.boardgames.core.table.TableInteractionRouter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CrestBoardGamesPlugin extends JavaPlugin implements BoardGamesApi {
    private final Map<String, GameDefinition> games = new ConcurrentHashMap<>();
    private ModuleManager moduleManager;
    private RoomManager roomManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        moduleManager = new ModuleManager(this);
        moduleManager.loadAll();
        TableInteractionRouter interactions = new TableInteractionRouter();
        getServer().getPluginManager().registerEvents(interactions, this);
        roomManager = new RoomManager(this, moduleManager, interactions);
        getServer().getPluginManager().registerEvents(roomManager, this);
        getLogger().info("CrestBoardGames 已啟用，共載入 " + moduleManager.loadedCount() + " 個遊戲模組。");
    }

    @Override
    public void onDisable() {
        if (roomManager != null) roomManager.shutdown();
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
        if (!(sender instanceof Player player)) {
            sender.sendMessage("這個指令必須由玩家執行。");
            return true;
        }
        try {
            if (args.length >= 2 && args[0].equalsIgnoreCase("create")) {
                BoardGameRoom room = roomManager.create(player, args[1].toLowerCase(java.util.Locale.ROOT));
                player.sendMessage(Component.text("已建立牌桌 " + room.shortId()
                        + "，請其他玩家輸入 /bg join " + room.shortId(), NamedTextColor.GREEN));
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("join")) {
                String roomId = args.length >= 2 ? args[1] : null;
                BoardGameRoom room = roomManager.join(player, roomId);
                player.sendMessage(Component.text("已加入牌桌 " + room.shortId(), NamedTextColor.GREEN));
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("start")) {
                roomManager.start(player);
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("leave")) {
                roomManager.leave(player);
                player.sendMessage(Component.text("已離開牌桌", NamedTextColor.YELLOW));
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("rooms")) {
                player.sendMessage(Component.text("目前牌桌：", NamedTextColor.GOLD));
                roomManager.rooms().forEach(room -> player.sendMessage(Component.text(
                        room.shortId() + " " + room.gameId() + " " + room.playerCount() + "/" + room.maximumPlayers()
                                + " " + room.status(), NamedTextColor.YELLOW)));
                return true;
            }
        } catch (RuntimeException exception) {
            roomManager.sendError(player, exception);
            return true;
        }
        player.sendMessage(Component.text("/bg create mahjong | /bg join [房號] | /bg start | /bg leave | /bg rooms",
                NamedTextColor.GOLD));
        return true;
    }
}
