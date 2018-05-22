import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SeabattleServer extends JFrame{

    public static void main(String[] args) {
        SeabattleServer seabattleServer = new SeabattleServer();
    }

    public SeabattleServer()
    {
        JTextArea serverTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(serverTextArea);
        add(scrollPane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setTitle("Seabattle Server");
        setVisible(true);

        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            serverTextArea.append(new Date() + ": Server started at socket 8000\n");

            while (true) {

                serverTextArea.append(new Date() +": Waiting for player 1 to join the game " + '\n');
                Socket player1 = serverSocket.accept();
                serverTextArea.append(new Date() + ": Player 1 joined the game " + '\n');
                serverTextArea.append("Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');


                Socket player2 = serverSocket.accept();
                serverTextArea.append(new Date() + ": Player 2 joined the game " + '\n');
                serverTextArea.append("Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');

                serverTextArea.append(new Date() + ": The game can begin " + '\n');

                gameLogic game = new gameLogic(player1, player2);

                new Thread(game).start();
            }
        }
        catch(IOException ex) {
            System.err.println(ex);
        }
    }

    class gameLogic implements Runnable
    {
        private Socket player1;
        private Socket player2;

        private DataInputStream fromPlayer1;
        private DataOutputStream toPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer2;

        public gameLogic(Socket player1, Socket player2)
        {
            this.player1 = player1;
            this.player2 = player2;
        }

        @Override
        public void run() {
            try {
            DataInputStream fromPlayer1 = new DataInputStream(player1.getInputStream());
            DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
            DataInputStream fromPlayer2 = new DataInputStream(player2.getInputStream());
            DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());

            //rest of the game code here

                // - code to notify if both players have 5 ships on the field
                // - code to let player 1 take first shot


                //while true loop of sending data




            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        }
    }
}

