package tw.crestnetwork.boardgames.modules.mahjong.model;

import java.util.Objects;

public record MahjongTile(int id, MahjongTileType type) {
    public MahjongTile {
        if (id < 0) {
            throw new IllegalArgumentException("牌 ID 不可小於零");
        }
        Objects.requireNonNull(type, "type");
    }
}
