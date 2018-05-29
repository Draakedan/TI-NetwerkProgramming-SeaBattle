import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SeabattleServer extends JFrame{

    JTextArea serverTextArea;

    public static void main(String[] args) {
        SeabattleServer seabattleServer = new SeabattleServer();
    }

    public SeabattleServer()
    {
        serverTextArea = new JTextArea();
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
            ex.printStackTrace();
        }
    }

    class gameLogic implements Runnable, SeabattleDataStreamConstants
    {
        private Socket player1;
        private Socket player2;

        private DataInputStream fromPlayer1;
        private DataOutputStream toPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer2;

        private int boatsHit = 0;

        public gameLogic(Socket player1, Socket player2)
        {
            this.player1 = player1;
            this.player2 = player2;
        }

        @Override
        public void run() {
            try {
                fromPlayer1 = new DataInputStream(player1.getInputStream());
                toPlayer1 = new DataOutputStream(player1.getOutputStream());
                fromPlayer2 = new DataInputStream(player2.getInputStream());
                toPlayer2 = new DataOutputStream(player2.getOutputStream());


                toPlayer1.writeInt(PLAYER1);
                toPlayer2.writeInt(PLAYER2);
                serverTextArea.append("wrote to player1 and player2 what players they are" + '\n');


                int boatsplacedfromplayer1 = fromPlayer1.readInt();
                int boatsplacedfromplayer2 = fromPlayer2.readInt();
                serverTextArea.append("boats placed from player1: " + boatsplacedfromplayer1+ '\n');
                serverTextArea.append("boats placed from player2: " + boatsplacedfromplayer2+ '\n');

                if (boatsplacedfromplayer1 == PLAYER1_BOATS_PLACED && boatsplacedfromplayer2 == PLAYER2_BOATS_PLACED)
                {
                    while (true) {
                        int row = fromPlayer1.readInt();
                        int column = fromPlayer1.readInt();
                        boatsHit = fromPlayer1.readInt();

                        serverTextArea.append("row from player1: " + row+ '\n');
                        serverTextArea.append("column from player1: " + column+ '\n');
                        serverTextArea.append("boats hit from player1: " + boatsHit+ '\n');

                        if (isThereAWinner()) {
                            serverTextArea.append("is there a winner yet? : " + isThereAWinner()+ '\n');
                            toPlayer1.writeInt(PLAYER1_WON);
                            toPlayer2.writeInt(PLAYER1_WON);
                            sendMove(toPlayer2, row, column);
                            break;
                        } else {
                            sendMove(toPlayer2, row, column);
                            serverTextArea.append("move send to player2:  row:" + row + " column: " + column+ '\n');
                        }


                        row = fromPlayer2.readInt();
                        column = fromPlayer2.readInt();
                        boatsHit = fromPlayer2.readInt();

                        serverTextArea.append("row from player2: " + row+ '\n');
                        serverTextArea.append("column from player2: " + column+ '\n');
                        serverTextArea.append("boats hit from player2: " + boatsHit+ '\n');

                        if (isThereAWinner()) {
                            serverTextArea.append("is there a winner yet? : " + isThereAWinner()+ '\n');
                            toPlayer1.writeInt(PLAYER2_WON);
                            toPlayer2.writeInt(PLAYER2_WON);
                            sendMove(toPlayer1, row, column);
                            break;
                        } else {
                            sendMove(toPlayer1, row, column);
                            serverTextArea.append("move send to player1:  row:" + row + " column: " + column+ '\n');
                        }
                    }
                }


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

            private boolean isThereAWinner()
            {
                boolean aWinner;
                if (boatsHit == 5)
                {
                    aWinner = true;
                }else
                {
                    aWinner = false;
                }
                return aWinner;
            }

            private void sendMove(DataOutputStream outputStream, int row, int column)
            {
                try {
                    outputStream.writeInt(row);
                    outputStream.writeInt(column);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

