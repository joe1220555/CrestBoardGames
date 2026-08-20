package tw.crestnetwork.boardgames.modules.mahjong.rules;

import org.junit.jupiter.api.Test;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongSeat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClaimResolverTest {
    private final ClaimResolver resolver = new ClaimResolver();

    @Test
    void winBeatsPongAndChow() {
        List<MahjongClaim> result = resolver.resolve(MahjongSeat.EAST, List.of(
                new MahjongClaim(MahjongSeat.SOUTH, MahjongClaimType.CHOW),
                new MahjongClaim(MahjongSeat.WEST, MahjongClaimType.PONG),
                new MahjongClaim(MahjongSeat.NORTH, MahjongClaimType.WIN)
        ), false);

        assertEquals(List.of(new MahjongClaim(MahjongSeat.NORTH, MahjongClaimType.WIN)), result);
    }

    @Test
    void onlyNextSeatMayChow() {
        List<MahjongClaim> result = resolver.resolve(MahjongSeat.NORTH, List.of(
                new MahjongClaim(MahjongSeat.SOUTH, MahjongClaimType.CHOW),
                new MahjongClaim(MahjongSeat.EAST, MahjongClaimType.CHOW)
        ), false);

        assertEquals(List.of(new MahjongClaim(MahjongSeat.EAST, MahjongClaimType.CHOW)), result);
    }

    @Test
    void multipleWinnerRuleReturnsAllWinnersInTurnOrder() {
        List<MahjongClaim> result = resolver.resolve(MahjongSeat.SOUTH, List.of(
                new MahjongClaim(MahjongSeat.EAST, MahjongClaimType.WIN),
                new MahjongClaim(MahjongSeat.NORTH, MahjongClaimType.WIN),
                new MahjongClaim(MahjongSeat.WEST, MahjongClaimType.PONG)
        ), true);

        assertEquals(List.of(
                new MahjongClaim(MahjongSeat.NORTH, MahjongClaimType.WIN),
                new MahjongClaim(MahjongSeat.EAST, MahjongClaimType.WIN)
        ), result);
    }
}
