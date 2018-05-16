import javafx.application.Application;

public abstract class Client extends Application {
    private static int playerID;
    private Game enemyGame;
    private Game playerGame;



    public static void main(String[] args) {
    }

    public static int getPlayerID() {
        return playerID;
    }
}
