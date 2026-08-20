package tw.crestnetwork.boardgames.modules.mahjong.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record MahjongMeld(MahjongMeldType type, List<MahjongTile> tiles, boolean concealed) {
    public MahjongMeld {
        Objects.requireNonNull(type, "type");
        tiles = List.copyOf(tiles);
        int expectedSize = type == MahjongMeldType.KONG ? 4 : 3;
        if (tiles.size() != expectedSize || tiles.stream().anyMatch(tile -> tile.type().isFlower())) {
            throw new IllegalArgumentException("不合法的面子牌數");
        }
        validatePattern(type, tiles);
    }

    private static void validatePattern(MahjongMeldType type, List<MahjongTile> tiles) {
        if (type == MahjongMeldType.PONG || type == MahjongMeldType.KONG) {
            MahjongTileType first = tiles.getFirst().type();
            if (tiles.stream().anyMatch(tile -> tile.type() != first)) {
                throw new IllegalArgumentException("碰或槓必須使用相同牌");
            }
            return;
        }

        List<MahjongTileType> sorted = tiles.stream().map(MahjongTile::type)
                .sorted(Comparator.comparingInt(MahjongTileType::rank)).toList();
        if (!sorted.getFirst().isSuited()
                || sorted.stream().anyMatch(tile -> tile.suit() != sorted.getFirst().suit())
                || sorted.get(1).rank() != sorted.getFirst().rank() + 1
                || sorted.get(2).rank() != sorted.getFirst().rank() + 2) {
            throw new IllegalArgumentException("吃牌必須是同花色連續三張數牌");
        }
    }
}
