package tw.crestnetwork.boardgames.modules.mahjong.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MahjongMeldTest {
    @Test
    void acceptsValidChowAndRejectsHonorSequence() {
        assertDoesNotThrow(() -> new MahjongMeld(MahjongMeldType.CHOW, List.of(
                tile(1, MahjongTileType.BAMBOO_3),
                tile(2, MahjongTileType.BAMBOO_4),
                tile(3, MahjongTileType.BAMBOO_5)), false));

        assertThrows(IllegalArgumentException.class, () -> new MahjongMeld(MahjongMeldType.CHOW, List.of(
                tile(4, MahjongTileType.EAST),
                tile(5, MahjongTileType.SOUTH),
                tile(6, MahjongTileType.WEST)), false));
    }

    @Test
    void rejectsMixedPong() {
        assertThrows(IllegalArgumentException.class, () -> new MahjongMeld(MahjongMeldType.PONG, List.of(
                tile(1, MahjongTileType.RED_DRAGON),
                tile(2, MahjongTileType.RED_DRAGON),
                tile(3, MahjongTileType.GREEN_DRAGON)), false));
    }

    private MahjongTile tile(int id, MahjongTileType type) {
        return new MahjongTile(id, type);
    }
}
