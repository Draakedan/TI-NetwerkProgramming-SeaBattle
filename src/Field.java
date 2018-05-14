import java.util.ArrayList;

public class Field {
    ArrayList<Position> battlefield;
    ArrayList<Position> boatPositions;
    ArrayList<Position> hits;

    private Field() {
        battlefield = new ArrayList<>();
        boatPositions = new ArrayList<>();

        for (int x = 1; x < 11; x++) {
            for (int y = 1; y < 11; y++) {
                battlefield.add(new Position(x, y));
            }
        }
    }

    private static Field playerInstance;
    private static Field enemyInstance;

    public static Field getEnemyInstance() {
        if (enemyInstance == null)
            enemyInstance = new Field();
        return enemyInstance;
    }

    public static Field getPlayerInstance() {
        if (playerInstance == null)
            playerInstance = new Field();

        return playerInstance;
    }

    public ArrayList<Position> getHits() {
        return hits;
    }

    public void setHits(ArrayList<Position> hits) {
        this.hits = hits;
    }

    public void setBoatPositions(ArrayList<Position> boatPositions) {
        if (boatPositions.size() == 0)
            this.boatPositions = boatPositions;
    }

    public ArrayList<Position> getBoatPositions() {
        return boatPositions;
    }

    public void add(Position position) {
        if (!boatPositions.contains(position))
            boatPositions.add(position);
        else
            System.out.println("the postiton is already taken!");
    }

    public boolean contains(Position position) {
        boolean value = false;
        for (Position position1: boatPositions) {
            if (position.compare(position, position1) == 0)
                value = true;
        }
        return value;
    }
}
