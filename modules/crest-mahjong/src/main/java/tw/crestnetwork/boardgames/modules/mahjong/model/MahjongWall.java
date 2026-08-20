package tw.crestnetwork.boardgames.modules.mahjong.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public final class MahjongWall {
    public static final int TAIWAN_TILE_COUNT = 144;

    private final Deque<MahjongTile> tiles;

    private MahjongWall(List<MahjongTile> shuffledTiles) {
        this.tiles = new ArrayDeque<>(shuffledTiles);
    }

    public static MahjongWall shuffled(Random random) {
        List<MahjongTile> tiles = orderedTiles();
        Collections.shuffle(tiles, random);
        return new MahjongWall(tiles);
    }

    public static List<MahjongTile> orderedTiles() {
        List<MahjongTile> tiles = new ArrayList<>(TAIWAN_TILE_COUNT);
        int id = 0;
        for (MahjongTileType type : MahjongTileType.values()) {
            for (int copy = 0; copy < type.copiesInTaiwanSet(); copy++) {
                tiles.add(new MahjongTile(id++, type));
            }
        }
        return tiles;
    }

    public MahjongTile draw() {
        MahjongTile tile = tiles.pollFirst();
        if (tile == null) {
            throw new NoSuchElementException("牌牆已經沒有牌");
        }
        return tile;
    }

    /** Draws from the back of the wall for flower and kong replacement. */
    public MahjongTile drawReplacement() {
        MahjongTile tile = tiles.pollLast();
        if (tile == null) {
            throw new NoSuchElementException("牌牆已經沒有補牌");
        }
        return tile;
    }

    public int remaining() {
        return tiles.size();
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }
}
