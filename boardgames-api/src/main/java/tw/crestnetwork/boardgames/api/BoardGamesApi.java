package tw.crestnetwork.boardgames.api;

import java.util.Collection;
import java.util.Optional;

public interface BoardGamesApi {
    int API_VERSION = 1;

    void registerGame(GameDefinition definition);

    Optional<GameDefinition> game(String id);

    Collection<GameDefinition> games();
}
