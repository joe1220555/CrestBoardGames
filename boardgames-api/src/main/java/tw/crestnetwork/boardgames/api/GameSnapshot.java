package tw.crestnetwork.boardgames.api;

import java.util.Map;

public record GameSnapshot(long revision, int schemaVersion, Map<String, Object> state) {
    public GameSnapshot {
        state = Map.copyOf(state);
    }
}
