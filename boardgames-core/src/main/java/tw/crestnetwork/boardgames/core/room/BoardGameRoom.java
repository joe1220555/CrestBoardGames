package tw.crestnetwork.boardgames.core.room;

import tw.crestnetwork.boardgames.api.GameAction;
import tw.crestnetwork.boardgames.api.GameSession;
import tw.crestnetwork.boardgames.api.GameStatus;
import tw.crestnetwork.boardgames.core.table.PhysicalTableImpl;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class BoardGameRoom {
    private final UUID id;
    private final String gameId;
    private final UUID hostId;
    private final int maximumPlayers;
    private final PhysicalTableImpl table;
    private final GameSession session;
    private final Set<UUID> players = new LinkedHashSet<>();

    public BoardGameRoom(UUID id, String gameId, UUID hostId, int maximumPlayers,
                         PhysicalTableImpl table, GameSession session) {
        this.id = id;
        this.gameId = gameId;
        this.hostId = hostId;
        this.maximumPlayers = maximumPlayers;
        this.table = table;
        this.session = session;
        join(hostId);
        table.setClickHandler((playerId, pieceId) -> session.handleAction(playerId,
                new GameAction("piece-click", Map.of("piece-id", pieceId))));
    }

    public boolean join(UUID playerId) {
        if (session.status() != GameStatus.WAITING || players.size() >= maximumPlayers || !players.add(playerId)) {
            return false;
        }
        session.handleAction(playerId, new GameAction("player-join", Map.of()));
        return true;
    }

    public void leave(UUID playerId) {
        if (players.remove(playerId)) {
            session.handleAction(playerId, new GameAction("player-leave", Map.of()));
        }
    }

    public void start(UUID requester) {
        if (!hostId.equals(requester)) {
            throw new IllegalStateException("只有房主可以開始遊戲");
        }
        session.start();
    }

    public void close() {
        session.shutdown();
        table.close();
        players.clear();
    }

    public UUID id() { return id; }
    public String shortId() { return id.toString().substring(0, 8); }
    public String gameId() { return gameId; }
    public UUID hostId() { return hostId; }
    public int playerCount() { return players.size(); }
    public int maximumPlayers() { return maximumPlayers; }
    public boolean contains(UUID playerId) { return players.contains(playerId); }
    public boolean canJoin() { return session.status() == GameStatus.WAITING && players.size() < maximumPlayers; }
    public PhysicalTableImpl table() { return table; }
    public GameStatus status() { return session.status(); }
}
