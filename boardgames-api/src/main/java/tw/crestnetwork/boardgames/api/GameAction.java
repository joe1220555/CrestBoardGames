package tw.crestnetwork.boardgames.api;

import java.util.Map;
import java.util.Objects;

public record GameAction(String type, Map<String, Object> data) {
    public GameAction {
        Objects.requireNonNull(type, "type");
        data = Map.copyOf(data);
    }
}
