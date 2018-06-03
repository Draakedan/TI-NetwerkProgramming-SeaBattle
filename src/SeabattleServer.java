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

        public gameLogic(Socket player1, Socket player2)
        {
            this.player1 = player1;
            this.player2 = player2;
        }

        @Override
        public void run() {
            try {
                serverTextArea.append("now entering the run() code" + '\n');
                fromPlayer1 = new DataInputStream(player1.getInputStream());
                toPlayer1 = new DataOutputStream(player1.getOutputStream());
                fromPlayer2 = new DataInputStream(player2.getInputStream());
                toPlayer2 = new DataOutputStream(player2.getOutputStream());


                toPlayer1.writeInt(PLAYER1);
                toPlayer2.writeInt(PLAYER2);
                serverTextArea.append("succesfully send to  player1 and player2 what players they are" + '\n');


                int boatsplacedfromplayer1 = fromPlayer1.readInt();
                int boatsplacedfromplayer2 = fromPlayer2.readInt();



                if (boatsplacedfromplayer1 == PLAYER1_BOATS_PLACED && boatsplacedfromplayer2 == PLAYER2_BOATS_PLACED)
                {
                    serverTextArea.append("player 1 and player 2 succesfully placed their boats" + '\n');
                    toPlayer1.writeInt(START_GAME);
                    toPlayer2.writeInt(START_GAME);
                    serverTextArea.append("wrote to player1 and player2 that the game can start" + '\n');
                    while (true) {
                        serverTextArea.append("we have now entered the while true loop" + '\n' + '\n');

                        int statuss = fromPlayer1.readInt();

                        toPlayer2.writeInt(statuss);

                        int status = fromPlayer1.readInt();
                        int row = fromPlayer1.readInt();
                        int column = fromPlayer1.readInt();
                        int boatsHit = fromPlayer1.readInt();


                        serverTextArea.append("succesfully read the row, column and amount of boats hit from player1:" + '\n');
                        serverTextArea.append("row from player1: " + row+ '\n');
                        serverTextArea.append("column from player1: " + column+ '\n');
                        serverTextArea.append("boats hit from player1: " + boatsHit+ '\n' + '\n');

                        if (boatsHit == 5)
                        {
                            status = PLAYER2_WON;
                            sendMove(toPlayer2, status, row, column, boatsHit);
                            break;
                        }else
                        {
                            sendMove(toPlayer2, status, row, column, boatsHit);
                        }



//                        serverTextArea.append("now checking if the first data send from client == player1_won or player2_won" + '\n');
//                        if (status == PLAYER1_WON || status == PLAYER2_WON) {
//                            sendMove(toPlayer2, row, column, boatsHit, status);
//                        } else {
//                            serverTextArea.append("no player has won yet, now sending the row column and amount of boats hit to player 2:" + '\n');
//                            sendMove(toPlayer2, row, column, boatsHit, status);
//                            serverTextArea.append("succesfully send the row, column and amount of boats hit to player 2" + '\n' + '\n');
//                        }




                        status = fromPlayer2.readInt();
                        row = fromPlayer2.readInt();
                        column = fromPlayer2.readInt();
                        boatsHit = fromPlayer2.readInt();

                        statuss = fromPlayer2.readInt();


                        serverTextArea.append("succesfully read the row, column and amount of boats hit from player2:" + '\n');
                        serverTextArea.append("row from player2: " + row+ '\n');
                        serverTextArea.append("column from player2: " + column+ '\n');
                        serverTextArea.append("boats hit from player2: " + boatsHit+ '\n' + '\n');

                        if (boatsHit == 5)
                        {
                            status = PLAYER1_WON;
                            sendMove(toPlayer1, status, row, column, boatsHit);
                            break;
                        }else
                        {
                            sendMove(toPlayer1,status, row, column, boatsHit);
                        }

                        toPlayer1.writeInt(statuss);

//                        serverTextArea.append("now checking if the first data send from client == player1_won or player2_won" + '\n');
//                        if (status == PLAYER1_WON || status == PLAYER2_WON) {
//                            sendMove(toPlayer1, row, column, boatsHit, status);
//                        } else {
//                            serverTextArea.append("no player has won yet, now sending the row column and amount of boats hit to player 1:" + '\n');
//                            sendMove(toPlayer1, row, column, boatsHit, status);
//                            serverTextArea.append("succesfully send the row, column and amount of boats hit to player 1" + '\n' + '\n');
//                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

            private void sendMove(DataOutputStream outputStream, int status, int row, int column, int boatshit)
            {
                try {
                    outputStream.writeInt(status);
                    outputStream.writeInt(row);
                    outputStream.writeInt(column);
                    outputStream.writeInt(boatshit);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

