public class Target {
    private int x;
    private int y;

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHit(int x, int y) {
        return this.x == x && this.y == y;
    }
    public String toString() {
        return "Target at (" + x + ", " + y + ")";
    }
}