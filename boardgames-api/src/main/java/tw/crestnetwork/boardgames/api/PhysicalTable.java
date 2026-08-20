package tw.crestnetwork.boardgames.api;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Server-owned world renderer for a real in-world table. Modules change game
 * pieces through this interface and never hand inventory GUI items to players.
 */
public interface PhysicalTable {
    UUID roomId();

    String worldName();

    void putPiece(PhysicalPiece piece);

    void removePiece(String pieceId);

    Optional<PhysicalPiece> piece(String pieceId);

    Collection<PhysicalPiece> pieces();

    void highlight(String pieceId, boolean highlighted);

    void clear();
}
