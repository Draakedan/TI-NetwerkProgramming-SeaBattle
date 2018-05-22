import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SeaBattleClient {



    // Input and output streams from/to server
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    // Indicate if it runs as application
    private boolean isStandAlone = false;

    // Host name or ip
    private String host = "localhost";


    public static void main(String[] args) {
        SeaBattleClient seaBattleClient = new SeaBattleClient();
        seaBattleClient.connectToServer();
    }
    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket;
            if (isStandAlone)
                socket = new Socket(host, 8000);
            else
                socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            System.err.println(ex);
        }

        // Control the game on a separate thread
        //Thread thread = new Thread(this);
        //thread.start();
    }
}
