package tw.crestnetwork.boardgames.api;

import java.util.Map;
import java.util.Objects;

/** A card, tile, chess piece, token or other visible object on a table. */
public record PhysicalPiece(
        String id,
        String modelKey,
        TablePosition position,
        Map<String, Object> publicState
) {
    public PhysicalPiece {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(modelKey, "modelKey");
        Objects.requireNonNull(position, "position");
        publicState = Map.copyOf(publicState);
    }
}
