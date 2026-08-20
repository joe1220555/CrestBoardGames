package tw.crestnetwork.boardgames.api;

/** A position relative to the physical table origin. */
public record TablePosition(double x, double y, double z, float yaw, float pitch, float scale) {
    public TablePosition {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z) || scale <= 0) {
            throw new IllegalArgumentException("Invalid table position");
        }
    }

    public static TablePosition at(double x, double y, double z) {
        return new TablePosition(x, y, z, 0, 0, 1);
    }
}
