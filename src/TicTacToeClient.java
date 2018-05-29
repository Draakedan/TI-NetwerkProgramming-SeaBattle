

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TicTacToeClient extends JFrame implements Runnable, SeabattleDataStreamConstants {

    private static ArrayList<ArrayList<JButton>> leftPlayFieldButtons;
    private static ArrayList<ArrayList<JButton>> rightPlayFieldButtons;

    private static ArrayList<JButton> boats = new ArrayList<>();
    private static int boatsPlaced = 0;
    private static int boatsHit = 0;

    private static boolean buttonclicked = false;
    private static boolean isStartPressed = false;
    private static boolean areBoatsPlaced = false;

    private static Color huidigeAchtergrond;
    private static JLabel topVeldText = new JLabel();

    private static Color water = new Color(51, 190, 212);
    private static Color hoverWater = new Color(61,209, 232);

    private static int selectedRow = 0;
    private static int selectedColumn = 0;

    private static boolean noWinnerFound = true;

    private DataInputStream fromServer;
    private DataOutputStream toServer;

    int player;

    private Panel topField;
    private Panel leftPlayField;
    private Panel centerArea;
    private Panel rightPlayField;

    private String host = "localhost";

    public TicTacToeClient(){
        JFrame frame = new JFrame("Gui oefenen Timo");
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
                        huidigeAchtergrond = leftButton.getBackground();
                        if (leftButton.getBackground().equals(water)) {
                            leftButton.setBackground(hoverWater);
                        }else{
                            leftButton.setBackground(Color.black);
                        }
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        leftButton.setBackground(huidigeAchtergrond);

                    }
                });

                final int finalLeftX = leftX;
                final int finalLeftY = leftY;

                leftButton.addActionListener(e ->
                {
                    if (isStartPressed)
                    {
                        if (boatsPlaced < 5) {
                            leftButton.setBackground(Color.red);
                            huidigeAchtergrond = leftButton.getBackground();
                            leftButton.setEnabled(false);
                            boatsPlaced++;
                            boats.add(leftButton);
                            if (boatsPlaced == 5) {
                                areBoatsPlaced = true;
                                isStartPressed = false;
                                topVeldText.setText("Wacht tot de andere speler zijn boten heeft geplaatst");
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
                        huidigeAchtergrond = rightButton.getBackground();
                        if (rightButton.getBackground().equals(water)) {
                            rightButton.setBackground(hoverWater);
                        }else{
                            rightButton.setBackground(Color.black);
                        }

                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        rightButton.setBackground(huidigeAchtergrond);

                    }
                });

                final int finalRightX = rightX;
                final int finalRightY = rightY;

                rightButton.addActionListener(e ->
                {
                    selectedRow = finalRightX;
                    selectedColumn = finalRightY;
                    buttonclicked = true;
                    //rightButton.setEnabled(false);
                });


                rightPlayerGameField.add(rightButton);
                rightRow.add(rightButton);
            }
            rightPlayFieldButtons.add(rightRow);
        }

        topField.add(topVeldText);
        topVeldText.setVisible(false);
        JButton startButton = new JButton("start game");
        topField.add(startButton);
        startButton.addActionListener(e ->
        {
            startButton.setVisible(false);
            topVeldText.setText("U mag nu 5 schepen neer zetten");
            topVeldText.setVisible(true);
            isStartPressed = true;
        });

        mainPanel.setLayout(gridBagLayout);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);


        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket;
            socket = new Socket(host, 8000);
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
        try {

            player = fromServer.readInt();
            System.out.println("I am player:" + player);

            int canTheGameStart = fromServer.readInt();
            if (canTheGameStart == START_GAME) {
                while (noWinnerFound) {
                    System.out.println("we are now in the while true loop, where player1 and player 2 make their moves");

                    if (player == PLAYER1) {
                        topVeldText.setText("its your turn to shoot");
                        System.out.println("in de noWinnerFound gaat nu de waitforPlayerAction from player 1");
                        rightPlayField.setEnabled(true);
                        waitForPlayerAction();
                        rightPlayField.setEnabled(false);
                        System.out.println("in de noWinnerFound gaat nu de sendMoveServer from player 1");
                        sendMoveServer();
                        topVeldText.setText("wait for the other player to shoot");
                        System.out.println("in de noWinnerFound gaat nu receiveFromServer from player 1");
                        receiveFromServer();
                    } else if (player == PLAYER2) {
                        System.out.println("in de noWinnerFound gaat nu receiveFromServer from player 2");
                        receiveFromServer();
                        topVeldText.setText("its your turn to shoot");
                        System.out.println("in de noWinnerFound gaat nu de waitforPlayerAction from player 2");
                        rightPlayField.setEnabled(true);
                        waitForPlayerAction();
                        rightPlayField.setEnabled(false);

                        System.out.println("in de noWinnerFound gaat nu de sendMoveServer from player 2");
                        sendMoveServer();
                        topVeldText.setText("wait for the other player to shoot");
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
        System.out.println("we are now waiting for the player to press a button");

        while (buttonclicked == false) {
            Thread.sleep(100);
        }
        buttonclicked = false;
    }

    private void sendMoveServer() throws IOException {
        toServer.writeInt(selectedRow);//row
        toServer.writeInt(selectedColumn);//column
        toServer.writeInt(boatsHit);
        System.out.println("the move send to the server is:");
        System.out.println("row: " + selectedRow);
        System.out.println("column: " + selectedColumn);
        System.out.println("boats hit:" + boatsHit);
    }

    private void receiveFromServer() throws IOException
    {
//        int dataFromServer = fromServer.readInt();
//        System.out.println("the int received from the server is:" + dataFromServer);
//
//        if (dataFromServer == PLAYER1_WON)
//        {
//            if (player == PLAYER1)
//            topVeldText.setText("U heeft gewonnen!!!!");
//            if (player == PLAYER2)
//                topVeldText.setText("Helaas de andere speler heeft gewonnen :(");
//        }
//        else if (dataFromServer == PLAYER2_WON)
//        {
//            if (player == PLAYER1)
//                topVeldText.setText("Helaas de andere speler heeft gewonnen");
//            if (player == PLAYER2)
//                topVeldText.setText("U heeft gewonnen!!!!");
//
//        }
//        else {
            changeButton();
        //}
    }

    private int enemyRow;
    private int enemyColumn;

    private void receiveMove() throws IOException {
        enemyRow = fromServer.readInt();
        enemyColumn = fromServer.readInt();
        System.out.println("the move received from the server is:");
        System.out.println("row: " + enemyRow);
        System.out.println("column: " + enemyColumn);
    }

    private boolean isHit() {
        System.out.println("er word nu gekeken of er een boot geraakt is");
        boolean isHit = false;
        try {
            receiveMove();
            JButton target = leftPlayFieldButtons.get(enemyColumn).get(enemyRow);
            if (boats.contains(target)) {
                isHit = true;
                System.out.println("er is een boot geraakt");
            }
            else {
                isHit = false;
                System.out.println("er is geen boot geraakt");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isHit;
    }

    private void changeButton()
    {
        System.out.println("als er een button geraakt is word de knop van kleur veranderd");
        if (isHit() == true)
        {
            JButton target = leftPlayFieldButtons.get(enemyColumn).get(enemyRow);
            target.setBackground(Color.orange);
        }
        if (isHit() == false)
        {
            JButton target = leftPlayFieldButtons.get(enemyColumn).get(enemyRow);
            target.setBackground(Color.white);
        }
    }

    private void placeBoats()
    {
        topVeldText.setText("you can now place 5 boats");
        while (boatsPlaced < 5) {
            JButton button = leftPlayFieldButtons.get(selectedColumn).get(selectedRow);
            button.setBackground(Color.red);
            huidigeAchtergrond = button.getBackground();
            button.setEnabled(false);
            boatsPlaced++;
            if (boatsPlaced == 5) {
                boats.add(button);
                topVeldText.setText("Let the games begin!!");
                leftPlayField.setEnabled(false);
                try {
                    if (player == PLAYER1) {
                        toServer.writeInt(PLAYER1_BOATS_PLACED);
                    }else if (player == PLAYER2)
                    {
                        toServer.writeInt(PLAYER2_BOATS_PLACED);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
    TicTacToeClient ticTacToeClient = new TicTacToeClient();
    }
}
