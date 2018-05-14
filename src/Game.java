import java.util.ArrayList;

public class Game {
    protected ArrayList<Boat> boats;
    protected int shipsInBattle;
    protected ArrayList<Position> hits;
    protected ArrayList<Boat> sunkenShips;
    protected Field field;
    protected boolean isGame;
    protected TurnCounter turn;
    protected int player;
    protected boolean currentTurn;

    public Game() {
        player = Client.getPlayerID();
        turn = new TurnCounter();
        isGame = true;
        boats = new ArrayList<>();
        shipsInBattle = 10;
        hits = new ArrayList<>();
        sunkenShips = new ArrayList<>();
    }

    protected void updateTurn() {

    }

    private boolean contains(Position position, ArrayList<Position> list) {
        boolean value = false;
        for (Position position1: list) {
            if (position.compare(position, position1) == 0)
                value = true;
        }
        return value;
    }

    public void attack(Position position) {
        updateTurn();
        if (currentTurn == true) {
            if (!contains(position, hits)) {
                hits.add(position);

                boolean hit = false;
                int i = 0;
                while (i < boats.size() && hit == false) {
                    if (contains(position, boats.get(i).getPositions())) {
                        boats.get(i).recieveDamage();
                        hit = true;
                        if (boats.get(i).getAlive() == false) {
                            sunkenShips.add(boats.get(i));
                            shipsInBattle = shipsInBattle - 1;
                        }
                    }
                    i++;
                }
            }
        }
    }

    public boolean isGame() {
        return isGame;
    }

}
