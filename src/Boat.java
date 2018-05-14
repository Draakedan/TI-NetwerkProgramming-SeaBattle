import java.util.ArrayList;

public class Boat {

    private int size; //The size of the boat on the field.
    private ArrayList<Position> positions; // An ArrayList of the positions where the boat is on the map.
    private String faceingPositon; // The direction the boat is facing.
    private int direction; // The value of with the positon of the boat shall be decided.
    private Boolean verticle; // The value with decides on with axis the boat wil be positoned on.
    private Field field; //the playeer field
    private int hits; // Amount of hits that a boat has recieved.
    private Boolean alive; // Status of if a ship is still in play or not.

    public Boat(int size) {
        positions = new ArrayList<>();
        field = Field.getPlayerInstance();

        this.size = size;
        this.direction = 1;
        this.verticle = true;
    }

    /**
     * @return The size of the boat.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the status of the boat.
     */
    public Boolean getAlive() {
        return alive;
    }

    /**
     * @return An arrayList of positions on which the boat stands on.
     */
    public ArrayList<Position> getPositions() {
        return positions;
    }

    /**
     * @return The direction which the boat is facing
     */
    public String getFaceingPositon() {
        return faceingPositon;
    }

    /**
     * @param direction The direction in text of with the boat must face. this must be "North", "South", "East" and "West".
     * The fields verticle and direction are changed depending on what the given direction is.
     */
    public void rotate(String direction) {
        if (direction.equals("North")) {
            this.direction = 1;
            this.verticle = true;
        }
        else if (direction.equals("East")) {
            this.direction = 1;
            this.verticle = false;
        }
        else if (direction.equals("South")) {
            this.direction = -1;
            this.verticle = true;
        }
        else if (direction.equals("West")) {
            this.direction = -1;
            this.verticle = false;
        }

        faceingPositon = direction;
        System.out.println("The ship is now facing " + direction);
    }

    /**
     * @param position The position of where the boat is being placed.
     * @return The status of if the position of the boat is possible on the board.
     * The boat is being placed on the board using recursion.
     */
    public boolean place(Position position) {
        boolean status = true;

        this.positions.add(position);
        if (positions.size() < size) {

            int x = position.getX();
            int y = position.getY();

            if (verticle = true) {
                y = y + direction;
            }
            else {
                x = x + direction;
            }

            if (x <= 10 && x > 0 && y <= 10 && y > 0) {
                if (!field.contains(new Position(x,y)))
                    place(new Position(x, y));
                else
                    status = false;
            }
            else {
                status = false;
            }

        }
        else {
            status = true;
            field.add(position);
        }

        return status;
    }

    public void recieveDamage() {
        hits++;
        if (hits == size) {
            alive = false;
            System.out.println("the ship has sunk");
        }
    }

    @Override
    public String toString() {
        return "the boat of " + size + " parts is facing " + faceingPositon.toLowerCase();
    }
}
