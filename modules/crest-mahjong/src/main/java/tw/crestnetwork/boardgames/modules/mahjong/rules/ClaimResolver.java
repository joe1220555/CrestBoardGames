package tw.crestnetwork.boardgames.modules.mahjong.rules;

import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongSeat;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class ClaimResolver {
    public List<MahjongClaim> resolve(
            MahjongSeat discarder,
            Collection<MahjongClaim> claims,
            boolean allowMultipleWinners
    ) {
        List<MahjongClaim> legalClaims = claims.stream()
                .filter(claim -> claim.seat() != discarder)
                .filter(claim -> claim.type() != MahjongClaimType.CHOW || claim.seat() == discarder.next())
                .sorted(Comparator
                        .comparingInt((MahjongClaim claim) -> claim.type().priority()).reversed()
                        .thenComparingInt(claim -> claim.seat().distanceAfter(discarder)))
                .toList();

        if (legalClaims.isEmpty()) {
            return List.of();
        }

        MahjongClaim first = legalClaims.getFirst();
        if (first.type() == MahjongClaimType.WIN && allowMultipleWinners) {
            return legalClaims.stream().filter(claim -> claim.type() == MahjongClaimType.WIN).toList();
        }
        return List.of(first);
    }
}
