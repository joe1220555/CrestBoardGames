package tw.crestnetwork.boardgames.modules.mahjong.model;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MahjongWallTest {
    @Test
    void taiwanWallContains144TilesWithCorrectCopies() {
        List<MahjongTile> tiles = MahjongWall.orderedTiles();
        Map<MahjongTileType, Integer> counts = new EnumMap<>(MahjongTileType.class);
        tiles.forEach(tile -> counts.merge(tile.type(), 1, Integer::sum));

        assertEquals(144, tiles.size());
        assertEquals(144, new HashSet<>(tiles.stream().map(MahjongTile::id).toList()).size());
        for (MahjongTileType type : MahjongTileType.values()) {
            assertEquals(type.copiesInTaiwanSet(), counts.get(type));
        }
    }

    @Test
    void seededShuffleIsRepeatableAndDrawsFromBothEnds() {
        MahjongWall first = MahjongWall.shuffled(new Random(1220));
        MahjongWall second = MahjongWall.shuffled(new Random(1220));

        assertEquals(first.draw(), second.draw());
        MahjongTile replacement = first.drawReplacement();
        assertNotEquals(replacement, first.draw());
        assertEquals(141, first.remaining());
        assertTrue(replacement.id() >= 0);
    }
}
