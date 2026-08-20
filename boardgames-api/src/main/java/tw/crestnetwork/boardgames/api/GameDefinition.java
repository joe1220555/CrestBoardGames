package tw.crestnetwork.boardgames.api;

import java.util.Objects;

public record GameDefinition(
        String id,
        String displayName,
        int minimumPlayers,
        int maximumPlayers,
        boolean supportsBots,
        boolean supportsSpectators
) {
    public GameDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        if (minimumPlayers < 1 || maximumPlayers < minimumPlayers) {
            throw new IllegalArgumentException("Invalid player range");
        }
    }
}
