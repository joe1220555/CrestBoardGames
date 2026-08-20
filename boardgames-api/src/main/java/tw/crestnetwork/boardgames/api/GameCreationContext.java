package tw.crestnetwork.boardgames.api;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record GameCreationContext(UUID roomId, Map<String, Object> settings, PhysicalTable table) {
    public GameCreationContext {
        Objects.requireNonNull(roomId, "roomId");
        Objects.requireNonNull(table, "table");
        settings = Map.copyOf(settings);
    }
}
