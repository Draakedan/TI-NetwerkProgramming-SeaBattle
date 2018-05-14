public class PlayerGame extends Game {


    public PlayerGame() {
        super();

        field = Field.getPlayerInstance();
        generateBoats();
    }

    @Override
    protected void updateTurn() {
        if (player == 1)
            currentTurn = turn.isPlayer1Turn();
        else
            currentTurn = turn.isPlayer2Turn();
    }

    private void generateBoats() {
        boats.add(new Boat(1));
        boats.add(new Boat(1));
        boats.add(new Boat(2));
        boats.add(new Boat(2));
        boats.add(new Boat(2));
        boats.add(new Boat(2));
        boats.add(new Boat(3));
        boats.add(new Boat(3));
        boats.add(new Boat(4));
        boats.add(new Boat(4));
    }

    public void placeBoat(int boat, Position position) {
        boolean succes = boats.get(boat).place(position);
        if (succes)
            System.out.println("the boat has been placed!");
        else
            System.out.println("the boat couldn't be placed here.");
    }

    @Override
    public void attack(Position position) {
        super.attack(position);

        if (currentTurn == true) {
            field.setHits(hits);

            if (shipsInBattle == 0)
                System.out.println("you have lost");
            turn.nextTurn();
            updateTurn();
        }
    }

    @Override
    public String toString() {
        return "There are " + shipsInBattle + "ships left in battle!" ;
    }
}
