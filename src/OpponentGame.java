public class OpponentGame extends Game {

    public OpponentGame() {
        super();

        field = Field.getEnemyInstance();
    }

    private void recieve(Boat boat) {
        boats.add(boat);
    }
}
