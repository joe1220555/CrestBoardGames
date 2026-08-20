package tw.crestnetwork.boardgames.api;

import java.util.UUID;

public interface GameSession {
    UUID roomId();

    GameStatus status();

    void start();

    void handleAction(UUID playerId, GameAction action);

    GameSnapshot snapshot();

    void shutdown();
}
