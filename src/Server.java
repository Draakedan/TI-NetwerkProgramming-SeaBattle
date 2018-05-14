public class Server {
    private static int gameID;

    public static void main(String[] args) {
        gameID++;

    }

    public Server() {

    }

    private static Server instance;

    public static Server getInstance() {
        return instance;
    }

    public int getGameID() {
        return gameID%2 + 1;
    }
}
