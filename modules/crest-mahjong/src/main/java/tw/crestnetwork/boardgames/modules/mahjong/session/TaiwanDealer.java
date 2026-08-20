package tw.crestnetwork.boardgames.modules.mahjong.session;

import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongHand;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongSeat;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongWall;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public final class TaiwanDealer {
    public static final int READY_HAND_SIZE = 16;

    public TaiwanDeal deal(Random random, MahjongSeat dealer) {
        MahjongWall wall = MahjongWall.shuffled(random);
        Map<MahjongSeat, MahjongHand> hands = new EnumMap<>(MahjongSeat.class);
        for (MahjongSeat seat : MahjongSeat.values()) {
            hands.put(seat, new MahjongHand());
        }

        for (int round = 0; round < READY_HAND_SIZE; round++) {
            for (MahjongSeat seat : seatsStartingAt(dealer)) {
                hands.get(seat).add(wall.draw());
            }
        }
        hands.get(dealer).add(wall.draw());

        for (MahjongSeat seat : MahjongSeat.values()) {
            int target = seat == dealer ? READY_HAND_SIZE + 1 : READY_HAND_SIZE;
            replaceFlowers(hands.get(seat), wall, target);
        }
        return new TaiwanDeal(dealer, wall, hands);
    }

    private void replaceFlowers(MahjongHand hand, MahjongWall wall, int targetConcealedCount) {
        while (hand.concealedCount() < targetConcealedCount) {
            hand.add(wall.drawReplacement());
        }
    }

    private MahjongSeat[] seatsStartingAt(MahjongSeat dealer) {
        MahjongSeat[] order = new MahjongSeat[MahjongSeat.values().length];
        MahjongSeat current = dealer;
        for (int index = 0; index < order.length; index++) {
            order[index] = current;
            current = current.next();
        }
        return order;
    }
}
