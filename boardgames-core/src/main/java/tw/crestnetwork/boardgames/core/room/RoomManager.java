package tw.crestnetwork.boardgames.core.room;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tw.crestnetwork.boardgames.api.GameCreationContext;
import tw.crestnetwork.boardgames.api.GameDefinition;
import tw.crestnetwork.boardgames.api.GameSession;
import tw.crestnetwork.boardgames.core.CrestBoardGamesPlugin;
import tw.crestnetwork.boardgames.core.module.ModuleManager;
import tw.crestnetwork.boardgames.core.table.PhysicalTableImpl;
import tw.crestnetwork.boardgames.core.table.TableInteractionRouter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class RoomManager implements Listener {
    private final CrestBoardGamesPlugin plugin;
    private final ModuleManager modules;
    private final TableInteractionRouter interactions;
    private final Map<UUID, BoardGameRoom> rooms = new LinkedHashMap<>();

    public RoomManager(CrestBoardGamesPlugin plugin, ModuleManager modules, TableInteractionRouter interactions) {
        this.plugin = plugin;
        this.modules = modules;
        this.interactions = interactions;
    }

    public BoardGameRoom create(Player host, String gameId) {
        if (roomOf(host.getUniqueId()).isPresent()) {
            throw new IllegalStateException("你已經在其他牌桌");
        }
        GameDefinition definition = plugin.game(gameId)
                .orElseThrow(() -> new IllegalArgumentException("找不到遊戲模組：" + gameId));
        UUID roomId = UUID.randomUUID();
        Location origin = host.getLocation().clone().add(host.getLocation().getDirection().setY(0).normalize().multiply(4));
        PhysicalTableImpl table = new PhysicalTableImpl(plugin, interactions, roomId, origin);
        try {
            GameCreationContext context = new GameCreationContext(roomId, Map.of(), table);
            GameSession session = modules.createSession(gameId, context)
                    .orElseThrow(() -> new IllegalStateException("模組無法建立遊戲工作階段"));
            BoardGameRoom room = new BoardGameRoom(roomId, gameId, host.getUniqueId(),
                    definition.maximumPlayers(), table, session);
            rooms.put(roomId, room);
            table.refreshVisibility(host);
            return room;
        } catch (RuntimeException exception) {
            table.close();
            throw exception;
        }
    }

    public BoardGameRoom join(Player player, String requestedId) {
        if (roomOf(player.getUniqueId()).isPresent()) {
            throw new IllegalStateException("你已經在其他牌桌");
        }
        BoardGameRoom room = rooms.values().stream()
                .filter(BoardGameRoom::canJoin)
                .filter(value -> requestedId == null || value.shortId().equalsIgnoreCase(requestedId))
                .findFirst().orElseThrow(() -> new IllegalStateException("目前沒有可加入的牌桌"));
        if (!room.join(player.getUniqueId())) {
            throw new IllegalStateException("無法加入這張牌桌");
        }
        room.table().refreshVisibility(player);
        return room;
    }

    public void start(Player player) {
        roomOf(player.getUniqueId()).orElseThrow(() -> new IllegalStateException("你不在牌桌中"))
                .start(player.getUniqueId());
    }

    public void leave(Player player) {
        BoardGameRoom room = roomOf(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("你不在牌桌中"));
        boolean close = room.hostId().equals(player.getUniqueId());
        room.leave(player.getUniqueId());
        if (close) {
            rooms.remove(room.id());
            room.close();
        }
    }

    public Optional<BoardGameRoom> roomOf(UUID playerId) {
        return rooms.values().stream().filter(room -> room.contains(playerId)).findFirst();
    }

    public Collection<BoardGameRoom> rooms() {
        return java.util.List.copyOf(rooms.values());
    }

    public void shutdown() {
        rooms.values().forEach(BoardGameRoom::close);
        rooms.clear();
    }

    public void sendError(Player player, RuntimeException exception) {
        player.sendMessage(Component.text(exception.getMessage(), NamedTextColor.RED));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        roomOf(event.getPlayer().getUniqueId()).ifPresent(room -> {
            boolean close = room.hostId().equals(event.getPlayer().getUniqueId());
            room.leave(event.getPlayer().getUniqueId());
            if (close) {
                rooms.remove(room.id());
                room.close();
            }
        });
    }
}
