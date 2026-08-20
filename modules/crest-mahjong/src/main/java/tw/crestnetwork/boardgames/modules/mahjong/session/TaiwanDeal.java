package tw.crestnetwork.boardgames.modules.mahjong.session;

import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongHand;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongSeat;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongWall;

import java.util.Map;
import java.util.Objects;

public record TaiwanDeal(MahjongSeat dealer, MahjongWall wall, Map<MahjongSeat, MahjongHand> hands) {
    public TaiwanDeal {
        Objects.requireNonNull(dealer, "dealer");
        Objects.requireNonNull(wall, "wall");
        hands = Map.copyOf(hands);
        if (hands.size() != MahjongSeat.values().length) {
            throw new IllegalArgumentException("台灣麻將必須有四家手牌");
        }
    }

    public MahjongHand hand(MahjongSeat seat) {
        MahjongHand hand = hands.get(seat);
        if (hand == null) {
            throw new IllegalArgumentException("沒有座位 " + seat);
        }
        return hand;
    }
}
