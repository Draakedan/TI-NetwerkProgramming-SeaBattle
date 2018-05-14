import java.util.Comparator;

public class Position implements Comparator<Position> {
    int x; // The horizontal position.
    int y; // The vertical position.

    public Position(int x, int y) {
        if (x <= 10 && x > 0)
            this.x = x;
        if (y <= 10 && y > 0)
            this.y = y;
    }

    /**
     * @return The horizontal position.
     */
    public int getX() {
        return x;
    }

    /**
     * @return The vertical position.
     */
    public int getY() {
        return y;
    }

    @Override
    public int compare(Position o1, Position o2) {
        if (o1.hashCode() < o2.hashCode())
            return -1;
        else if (o1.hashCode() == o2.hashCode())
            return 0;
        else
            return 1;
    }

    @Override
    public int hashCode() {
        return (x*100) + y;
    }
}
