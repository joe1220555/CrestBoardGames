package tw.crestnetwork.boardgames.modules.mahjong.model;

public enum MahjongSeat {
    EAST,
    SOUTH,
    WEST,
    NORTH;

    public MahjongSeat next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public int distanceAfter(MahjongSeat other) {
        int distance = ordinal() - other.ordinal();
        return distance > 0 ? distance : distance + values().length;
    }
}
