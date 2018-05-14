public class TurnCounter implements Comparable<TurnCounter>{
    private boolean player1Turn;
    private boolean player2Turn;
    private int gameID;
    private Server server;

    public TurnCounter() {
        server = Server.getInstance();
        gameID = server.getGameID();
        player1Turn = true;
        player2Turn = false;
    }

    @Override
    public int compareTo(TurnCounter o) {
        if (o.gameID == gameID)
            return 1;
        else return 0;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    public boolean isPlayer2Turn() {
        return player2Turn;
    }

    public void nextTurn() {
        if (player1Turn == true) {
            player1Turn = false;
            player2Turn = true;
        }
        else {
            player1Turn = true;
            player2Turn = false;
        }

    }
}
