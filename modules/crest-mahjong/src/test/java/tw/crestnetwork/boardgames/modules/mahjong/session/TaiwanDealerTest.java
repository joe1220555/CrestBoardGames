package tw.crestnetwork.boardgames.modules.mahjong.session;

import org.junit.jupiter.api.Test;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongHand;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongSeat;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongWall;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TaiwanDealerTest {
    @Test
    void dealerHas17AndOtherPlayersHave16AfterFlowerReplacement() {
        TaiwanDeal deal = new TaiwanDealer().deal(new Random(1220), MahjongSeat.SOUTH);

        for (MahjongSeat seat : MahjongSeat.values()) {
            MahjongHand hand = deal.hand(seat);
            assertEquals(seat == MahjongSeat.SOUTH ? 17 : 16, hand.concealedCount());
            assertFalse(hand.concealedTiles().stream().anyMatch(tile -> tile.type().isFlower()));
        }
    }

    @Test
    void dealingAndReplacementNeverCreatesOrLosesTiles() {
        for (int seed = 0; seed < 100; seed++) {
            TaiwanDeal deal = new TaiwanDealer().deal(new Random(seed), MahjongSeat.EAST);
            int tilesInHands = deal.hands().values().stream()
                    .mapToInt(hand -> hand.concealedCount() + hand.flowerCount()).sum();
            assertEquals(MahjongWall.TAIWAN_TILE_COUNT, tilesInHands + deal.wall().remaining());
        }
    }
}
