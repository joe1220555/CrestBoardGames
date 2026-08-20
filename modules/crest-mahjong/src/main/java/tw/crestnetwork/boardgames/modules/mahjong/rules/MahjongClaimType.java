package tw.crestnetwork.boardgames.modules.mahjong.rules;

public enum MahjongClaimType {
    CHOW(1),
    PONG(2),
    KONG(2),
    WIN(3);

    private final int priority;

    MahjongClaimType(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
