

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class beginClient extends JFrame implements Runnable, SeabattleDataStreamConstants {

    private static int testCounter = 0;

    private static ArrayList<ArrayList<JButton>> leftPlayFieldButtons;
    private static ArrayList<ArrayList<JButton>> rightPlayFieldButtons;

    private static ArrayList<JButton> boats = new ArrayList<>();
    private static int boatsPlaced = 0;
    private static int boatsHit = 0;
    private static int boatsHitFromEnemy = 0;
    private static int currrentBoatsHitFromEnemy = 0;

    private static boolean buttonclicked = false;
    private static boolean isStartPressed = false;

    private static Color currentBackground;
    private static JLabel topFieldText = new JLabel();

    private static Color water = new Color(51, 190, 212);
    private static Color hoverWater = new Color(61,209, 232);
    private static Color boatMisWater = new Color(73, 99, 255);
    private static Color boathitColor = new Color(232, 1, 0);


    private static int selectedRow = 0;
    private static int selectedColumn = 0;
    private static int status = 0;


    private static boolean noWinnerFound = true;

    private DataInputStream fromServer;
    private DataOutputStream toServer;

    int player;

    private Panel topField;
    private Panel leftPlayField;
    private Panel centerArea;
    private Panel rightPlayField;

    private String host = "localhost";

    public beginClient(){
        JFrame frame = new JFrame("Network SeaBattle");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 400));
        frame.setExtendedState(MAXIMIZED_BOTH);

        //dit is het paneel waar alle andere layouts/panelen op komen
        JPanel mainPanel = new JPanel();

        //dit zijn de 3 panelen die op het mainPanel komen
        topField = new Panel();
        leftPlayField = new Panel();
        centerArea = new Panel();
        rightPlayField = new Panel();


        //hier worden de 3 panelen als borderlayout gezet, zodat er meerdere dingen aan het paneel toegevoegd kunnen worden
        topField.setLayout(new FlowLayout());
        leftPlayField.setLayout(new BorderLayout());
        centerArea.setLayout(new BorderLayout());
        rightPlayField.setLayout(new BorderLayout());

        mainPanel.add(topField);
        mainPanel.add(leftPlayField);
        mainPanel.add(centerArea);
        mainPanel.add(rightPlayField);


        //door middel van een gridBagLayout, kan je alle grotes aanpassen naar eigen wens,
        //dit zorgt er voor dat je precies kan bepalen waar elk paneel komt te staan, en welke grote
        //ook kan je makkelijk een paneel toevoegen zonder veel te hoeven veranderen
        GridBagLayout gridBagLayout = new GridBagLayout();
        makeConstraints(gridBagLayout, topField, 1, 1, 0, 0, 1.0, 0.1);
        makeConstraints(gridBagLayout, leftPlayField, 1, 1, 0, 1, 5.0, 10.0);
        makeConstraints(gridBagLayout, centerArea, 1, 1, 1, 1, 1.0, 10.0);
        makeConstraints(gridBagLayout, rightPlayField, 1, 1, 2, 1, 5.0, 10.0);

        centerArea.add(new JLabel("middenVeld"));


//hier staan 2 codes om het speelveld te maken, deze komen uit johans oefentoest van lightsout van periode 2
        //we moeten kijken of we een aparte methode gaan schrijven om dit toe te voegen of het anders doen

        JPanel leftPlayerGameField = new JPanel(new GridLayout(5,5));
        leftPlayField.add(leftPlayerGameField, BorderLayout.CENTER);

        leftPlayFieldButtons = new ArrayList<>();
        for(int leftY = 0; leftY < 5; leftY++)
        {
            ArrayList<JButton> leftRow = new ArrayList<>();
            for(int leftX = 0; leftX < 5; leftX++)
            {
                JButton leftButton = new JButton();
                leftButton.setBackground(water);

                leftButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        currentBackground = leftButton.getBackground();
                        if (leftButton.getBackground().equals(water)) {
                            leftButton.setBackground(hoverWater);
                        }else{
                            leftButton.setBackground(Color.black);
                        }
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        leftButton.setBackground(currentBackground);

                    }
                });

                final int finalLeftX = leftX;
                final int finalLeftY = leftY;
                

                leftButton.addActionListener(e ->
                {
                    if (isStartPressed)
                    {
                        if (boatsPlaced < 5) {
                            leftButton.setBackground(Color.YELLOW);
                            currentBackground = leftButton.getBackground();
                            leftButton.setEnabled(false);
                            boatsPlaced++;
                            boats.add(leftButton);
                            if (boatsPlaced == 5) {
                                isStartPressed = false;
                                topFieldText.setText("wait for the other player to place his boats");
                                if (player == PLAYER1) {
                                    try {
                                        toServer.writeInt(PLAYER1_BOATS_PLACED);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }else if (player == PLAYER2)
                                {
                                    try {
                                        toServer.writeInt(PLAYER2_BOATS_PLACED);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });


                leftPlayerGameField.add(leftButton);
                leftRow.add(leftButton);
            }
            leftPlayFieldButtons.add(leftRow);
        }

        JPanel rightPlayerGameField = new JPanel(new GridLayout(5,5));
        rightPlayField.add(rightPlayerGameField, BorderLayout.CENTER);
        rightPlayField.setEnabled(false);

        rightPlayFieldButtons = new ArrayList<>();
        for(int rightY = 0; rightY < 5; rightY++)
        {
            ArrayList<JButton> rightRow = new ArrayList<>();
            for(int rightX = 0; rightX < 5; rightX++)
            {
                JButton rightButton = new JButton();
                rightButton.setBackground(water);

                rightButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        currentBackground = rightButton.getBackground();
                        if (rightButton.getBackground().equals(water)) {
                            rightButton.setBackground(hoverWater);
                        }else{
                            rightButton.setBackground(Color.black);
                        }

                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        rightButton.setBackground(currentBackground);
                    }
                });

                final int finalRightX = rightX;
                final int finalRightY = rightY;

                rightButton.addActionListener(e ->
                {
                    selectedRow = finalRightX;
                    selectedColumn = finalRightY;
                    buttonclicked = true;
                    rightButton.setBackground(Color.white);
                    rightButton.setEnabled(false);
                    currentBackground = rightButton.getBackground();
                    //rightButton.setEnabled(false);
                });


                rightPlayerGameField.add(rightButton);
                rightRow.add(rightButton);
            }
            rightPlayFieldButtons.add(rightRow);
        }

        topField.add(topFieldText);
        topFieldText.setVisible(false);
        JButton startButton = new JButton("start game");
        topField.add(startButton);
        startButton.addActionListener(e ->
        {
            startButton.setVisible(false);
            topFieldText.setText("You may place 5 boats on the left field");
            topFieldText.setVisible(true);
            isStartPressed = true;
        });

        mainPanel.setLayout(gridBagLayout);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        System.out.println("now connecting to the server... ");
        System.out.println("");
        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket;
            socket = new Socket(host, 8000);
            //socket = new Socket("2001:610:1a0:1300:99f2:46f4:e0b1:d5d1", 8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        System.out.println("now entering the run() method");
        try {

            player = fromServer.readInt();
            System.out.println("the server send me what player i am, and i am player:" + player);
            int canTheGameStart = fromServer.readInt();
            if (canTheGameStart == START_GAME) {
                System.out.println("both players have succesfully placed their boats:");
                status = CONTINUE;
                while (noWinnerFound) {
                    System.out.println("we have now entered the while there is no winner found loop");
                    System.out.println("the amount of boats that are hit is: " + boatsHit);

                    if (player == PLAYER1) {
                        isThereAWinner();
                        if (status == PLAYER1_WON){
                            topFieldText.setText("You have lost...");
                            rightPlayField.setEnabled(false);
                            break;
                        } else if (status == PLAYER2_WON) {

                            topFieldText.setText("You have won!!");
                            rightPlayField.setEnabled(false);
                            break;
                        }
                        if (boatsHit == 5) {
                            status = PLAYER1_WON;
                            break;
                        }
                                            topFieldText.setText("its your turn to shoot");
                                            rightPlayField.setEnabled(true);
                        waitForPlayerAction();
                                            rightPlayField.setEnabled(false);
                                            revieveStatus();
                        sendMoveServer();
                                            topFieldText.setText("wait for the other player to shoot");
                                            revieveStatus();
                        receiveFromServer();
                        revieveStatus();
                        isThereAWinner();
                        if (status == PLAYER1_WON){
                            topFieldText.setText("You have lost...");
                            rightPlayField.setEnabled(false);
                            break;
                        } else if (status == PLAYER2_WON) {

                            topFieldText.setText("You have won!!");
                            rightPlayField.setEnabled(false);
                            break;
                        }

                    } else if (player == PLAYER2) {
                        isThereAWinner();
                        if (status == PLAYER1_WON){
                            topFieldText.setText("You have won!!");
                            rightPlayField.setEnabled(false);
                            break;
                        } else if (status == PLAYER2_WON){
                            topFieldText.setText("You have lost...");
                            rightPlayField.setEnabled(false);
                            break;
                        }
                        if (boatsHit == 5) {
                            status = PLAYER2_WON;
                            break;
                        }
                                            topFieldText.setText("wait for the other player to shoot");
                                            rightPlayField.setEnabled(false);
                                            revieveStatus();
                        receiveFromServer();
                        revieveStatus();
                        isThereAWinner();
                                            topFieldText.setText("its your turn to shoot");
                                            rightPlayField.setEnabled(true);
                        waitForPlayerAction();
                        sendMoveServer();
                        revieveStatus();
                                            topFieldText.setText("wait for the other player to shoot");
                                            rightPlayField.setEnabled(false);
                        if (status == PLAYER1_WON){
                            topFieldText.setText("You have won!!");
                            rightPlayField.setEnabled(false);
                            break;
                        } else if (status == PLAYER2_WON){
                            topFieldText.setText("You have lost...");
                            rightPlayField.setEnabled(false);
                            break;
                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForPlayerAction() throws InterruptedException {
        System.out.println("now entering waitForPlayerAction() loop");
        System.out.println("while buttonclicked = false we stay in here");
        while (buttonclicked == false) {
            Thread.sleep(100);
        }
        System.out.println("now exiting waitForPlayerAction() loop");
        System.out.println("there is a button pressed, buttonclicked = false agan");
        System.out.println("");
        buttonclicked = false;
    }

    private void sendStatus() throws IOException {
        toServer.writeInt(status);
    }

    private void revieveStatus() throws IOException {
        status = fromServer.readInt();
        if (status == PLAYER1_WON)
            topFieldText.setText("Player 1 has won");
        else if (status == PLAYER2_WON)
            topFieldText.setText("Player 2 has won");
    }

    private void sendMoveServer() throws IOException {
        System.out.println("now entering sendMoveServer()");
        System.out.println("status: "+ status);
        toServer.writeInt(selectedRow);//row
        toServer.writeInt(selectedColumn);//column
        toServer.writeInt(boatsHit);
        toServer.writeInt(status);
        System.out.println("row send to the server: " + selectedRow);
        System.out.println("column send to the server: " + selectedColumn);
        System.out.println("amount of boats that has been hit send to the server:" + boatsHit);
        System.out.println("now exited sendMoveServer()");
        System.out.println("");
    }

    private void receiveFromServer() throws IOException
    {
        System.out.println("now entering receiveFromServer()");
            changeButton();
        System.out.println("now exiting receiveFromServer");

    }

    private int enemyRow;
    private int enemyColumn;

    private void receiveMove() throws IOException {
        System.out.println("now entering receiveMove()");
        enemyRow = fromServer.readInt();
        enemyColumn = fromServer.readInt();
        boatsHitFromEnemy = fromServer.readInt();
        status = fromServer.readInt();
        System.out.println("row read from the server: " + enemyRow);
        System.out.println("column: read from the server" + enemyColumn);
        System.out.println("boats hit from the enemy read from the server:" + boatsHitFromEnemy);
    }

    private boolean isHit() {
        boolean isHit = false;
        try {
            receiveMove();
            JButton target = leftPlayFieldButtons.get(enemyColumn).get(enemyRow);
            if (boats.contains(target)) {
                isHit = true;
            }
            else {
                isHit = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isHit;
    }


    private void changeButton()
    {
        System.out.println("now entering changeButton()");
        System.out.println("we are checking if isHit() is true of false");

        boolean isThereABoatHit = isHit();
        JButton button = rightPlayFieldButtons.get(selectedColumn).get(selectedRow);
        JButton target = leftPlayFieldButtons.get(enemyColumn).get(enemyRow);
        if (isThereABoatHit == true)
        {
            System.out.println("isHit() == true");
            boatsHit++;
            if (testCounter == 0 && player == PLAYER2)
            {
                target.setBackground(boathitColor);
            }else {
                //button.setBackground(boathitColor);
                target.setBackground(boathitColor);
            }
        }
        if (isThereABoatHit == false) {
            System.out.println("isHit() == false");
            if (testCounter == 0 && player == PLAYER2) {
                target.setBackground(boatMisWater);
            } else {
                button.setBackground(boatMisWater);
                target.setBackground(boatMisWater);
            }
        }
        if (currrentBoatsHitFromEnemy != boatsHitFromEnemy)
        {
            button.setBackground(boathitColor);
        }else if (currrentBoatsHitFromEnemy == boatsHitFromEnemy)
        {
            if (testCounter != 0 && player == PLAYER2)
            button.setBackground(boatMisWater);
            if (testCounter !=0 && player == PLAYER1)
                button.setBackground(boatMisWater);
        }

        currrentBoatsHitFromEnemy = boatsHitFromEnemy;
        System.out.println("now exiting changeButton()");
        System.out.println("");
        testCounter++;
    }

    private void isThereAWinner() {
        System.out.println("now entering isThereAWinner()");
        if (boatsHitFromEnemy >= 5) {
            if (player == PLAYER1) {
                System.out.println("boatsHitFromEnemy == 5");
                topFieldText.setText("You have won the game!");
                //toServer.writeInt(PLAYER2_WON);
                status = PLAYER2_WON;

            } else if (player == PLAYER2) {
                topFieldText.setText("Your opponent won the game :(");
                //toServer.writeInt(PLAYER1_WON);
                status = PLAYER1_WON;
            }
            noWinnerFound = false;
        }
            if (boatsHit >= 5) {
                if (player == PLAYER1) {
                    topFieldText.setText("Player1 has won the game :(");
                    //toServer.writeInt(PLAYER1_WON);
                    status = PLAYER1_WON;

                } else if (player == PLAYER2) {
                    topFieldText.setText("player2 has won the game");

                    //toServer.writeInt(PLAYER2_WON);
                    status = PLAYER2_WON;

                }
                noWinnerFound = false;
            }
        }

    /**
     * Generate constraints for Swing components
     * @param gridBagLayout     a gridbaglayout that used to embed Swing component
     * @param panel           *wrong now*  a Swing component intended to be embedded in gbl
     * @param width             desired component width
     * @param height            desired component height
     * @param x_axis            desired location in x-axis
     * @param y_axis            desired location in y-axis
     * @param weightx           desired weight in terms of x-axis
     * @param weighty           desired weight in terms of y-axis
     */

    //dit is de methode om de gridbagConstraints te maken(hoe je kan bepalen waar welk veld komt)
    public static void makeConstraints(GridBagLayout gridBagLayout, Panel panel, int width, int height, int x_axis, int y_axis,
                                       double weightx, double weighty) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.gridx = x_axis;
        constraints.gridy = y_axis;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        gridBagLayout.setConstraints(panel, constraints);
    }

    /** This main method enables the applet to run as an application */
    public static void main(String[] args) {
    beginClient ticTacToeClient = new beginClient();
    }
}
