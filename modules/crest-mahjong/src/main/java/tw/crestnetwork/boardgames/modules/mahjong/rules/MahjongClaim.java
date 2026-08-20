package tw.crestnetwork.boardgames.modules.mahjong.rules;

import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongSeat;

import java.util.Objects;

public record MahjongClaim(MahjongSeat seat, MahjongClaimType type) {
    public MahjongClaim {
        Objects.requireNonNull(seat, "seat");
        Objects.requireNonNull(type, "type");
    }
}
