package tw.crestnetwork.boardgames.modules.mahjong.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public final class MahjongHand {
    private final List<MahjongTile> concealed = new ArrayList<>();
    private final List<MahjongTile> flowers = new ArrayList<>();
    private final List<MahjongMeld> melds = new ArrayList<>();

    public void add(MahjongTile tile) {
        if (tile.type().isFlower()) {
            flowers.add(tile);
        } else {
            concealed.add(tile);
        }
    }

    public MahjongTile removeConcealed(int tileId) {
        for (int index = 0; index < concealed.size(); index++) {
            if (concealed.get(index).id() == tileId) {
                return concealed.remove(index);
            }
        }
        throw new NoSuchElementException("手牌中沒有 ID " + tileId);
    }

    public void addMeld(MahjongMeld meld) {
        if (melds.size() >= 5) {
            throw new IllegalStateException("台灣麻將最多只能有五組面子");
        }
        melds.add(meld);
    }

    public List<MahjongTile> concealedTiles() {
        return concealed.stream().sorted(Comparator.comparing(tile -> tile.type().ordinal())).toList();
    }

    public List<MahjongTile> flowers() {
        return List.copyOf(flowers);
    }

    public List<MahjongMeld> melds() {
        return List.copyOf(melds);
    }

    public int concealedCount() {
        return concealed.size();
    }

    public int flowerCount() {
        return flowers.size();
    }
}
