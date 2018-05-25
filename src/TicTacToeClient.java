

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TicTacToeClient extends JFrame implements Runnable, SeabattleDataStreamConstants {

    //wat er nog gemaakt moet worden:

    // - een getter voor welk valkje op geklikt is - gedaan door Anastasia Hellemons (getClickedButton)
    // - een methode die een willekeurig vakje kan veranderen
    // - een methode die het aangeklikte vakje naar de server stuurt - gedaan door Anastasia Hellemons (sendTile)
    // - een methode die kijkt of er een boot geraakt is(van jezelf)
    // - een methode die kijkt of er al ieand gewonnen heeft
    // gaan we doen als je een boot raakt dat je dan nog een keer mag shieten(anders hier ook een methode voor)
    // optioneel een knop voor een rematch
    //heel veel onzin staat hier nog in van tictactoe, die kan er allemaal uit




    private static ArrayList<ArrayList<JButton>> leftPlayFieldButtons;
    private static ArrayList<ArrayList<JButton>> rightPlayFieldButtons;

    private static ArrayList<JButton> boats = new ArrayList<>();
    private static int boatsPlaced = 0;
    private static int boatsHit = 0;

    private static Color huidigeAchtergrond;
    private static JLabel topVeldText = new JLabel();

    private static Color water = new Color(51, 190, 212);
    private static Color hoverWater = new Color(61,209, 232);

    private static int selectedRow;
    private static int selectedColumn;

    private static boolean noWinnerFound = true;
    private JLabel jlblStatus = new JLabel();

    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private ObjectInputStream objectFromServer;
    private ObjectOutputStream objectToServer;

    int player;

    private Panel topField;
    private Panel leftPlayField;
    private Panel centerArea;
    private Panel rightPlayField;

    private JButton clickedLeft = null;
    private JButton clickedRight = null;

    // Wait for the player to mark a cell
    private boolean waiting = true;

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
                //leftButton.setBorderPainted(false);


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


                    //kan als het goed is weg
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        clickedLeft = leftButton;
                        clickedRight = null;
                    }
                });

                final int finalLeftX = leftX;
                final int finalLeftY = leftY;

                leftButton.addActionListener(e ->
                {

                    selectedRow = finalLeftX;
                    selectedColumn = finalLeftY;
                    });

                //leftButton.setEnabled(false);
                leftPlayerGameField.add(leftButton);
                leftRow.add(leftButton);
            }
            leftPlayFieldButtons.add(leftRow);
        }

        JPanel rightPlayerGameField = new JPanel(new GridLayout(5,5));
        rightPlayField.add(rightPlayerGameField, BorderLayout.CENTER);

        rightPlayFieldButtons = new ArrayList<>();
        for(int rightY = 0; rightY < 5; rightY++)
        {
            ArrayList<JButton> rightRow = new ArrayList<>();
            for(int rightX = 0; rightX < 5; rightX++)
            {
                JButton rightButton = new JButton();
                rightButton.setBackground(water);
                //leftButton.setBorderPainted(false);

                rightButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        rightButton.setBackground(hoverWater);
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        rightButton.setBackground(water);
                    }
                });

                final int finalRightX = rightX;
                final int finalRightY = rightY;

                rightButton.addActionListener(e ->
                {
                    selectedRow = finalRightX;
                    selectedColumn = finalRightY;
                });

                rightPlayerGameField.add(rightButton);
                rightRow.add(rightButton);
            }
            rightPlayFieldButtons.add(rightRow);
        }

        topField.add(topVeldText);
        topVeldText.setText("dsfsdfsfd");
        topVeldText.setVisible(true);

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
            objectFromServer = new ObjectInputStream(socket.getInputStream());
            objectToServer = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            System.err.println(ex);
        }
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            player = fromServer.readInt();
            System.out.println(player);

            placeBoats();

            System.out.println(player);

            if (player == PLAYER1)
            {
                topVeldText.setText("its your turn to shoot");
            }else if (player == PLAYER2)
            {
                topVeldText.setText("wait for the other player to shoot");
            }

            while (noWinnerFound)
            {
                if (player == PLAYER1)
                {
                    waitForPlayerAction();
                    sendMoveServer();
                    receiveFromServer();
                }
                else if (player == PLAYER2)
                {
                    receiveFromServer();
                    waitForPlayerAction();
                    sendMoveServer();
                }
            }






        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Wait for the player to mark a cell */
    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }
//twee keer zelfde methode naam, dus deze gaat die nooit pakken
    public void getClickedButton() {
        if (clickedLeft != null) {
            getClickedButton("L", 0, 0);
        }
        else if (clickedRight != null){
            getClickedButton("R", 0, 0);
        }
        else
            System.out.println("there aren't any clicked buttons");
    }

    public void getClickedButton(String side, int indexList, int indexButton) {
        if (side.equals("L")) {
            if (clickedLeft.equals(leftPlayFieldButtons.get(indexList).get(indexButton)))
                System.out.println("the Clicked Button is on the left side of the screen on position " + indexList + "," + indexButton);
        }
        else if (side.equals("R")) {
            if (clickedRight.equals(rightPlayFieldButtons.get(indexList).get(indexButton)))
                System.out.println("the Clicked Button is on the right side of the screen on position " + indexList + "," + indexButton);
        }
        else if (rightPlayFieldButtons.get(indexList).size() > indexButton) {
            getClickedButton(side, indexList, indexButton + 1);
        }
        else if (rightPlayFieldButtons.size() > indexList) {
            getClickedButton(side, indexList + 1, 0);
        }
        else {
            System.out.println("the button clicked in not in this list");
        }
    }

    private void sendMoveServer() throws IOException {
        toServer.writeInt(selectedRow);//row
        toServer.writeInt(selectedColumn);//column
        toServer.writeInt(boatsHit);
    }

    /** Receive info from the server */
    private void receiveFromServer() throws IOException
    {
        int dataFromServer = fromServer.readInt();

        //if (dataFromServer == PLAYER1_READY)
        //{

        //}
        //else if (dataFromServer == PLAYER2_READY)
        //{

        //}
        //else
        if (dataFromServer == PLAYER1_BOATS_PLACED)
        {

        }
        else if (dataFromServer == PLAYER2_BOATS_PLACED)
        {

        }
        else if (dataFromServer == PLAYER1_WON)
        {

        }
        else if (dataFromServer == PLAYER2_WON)
        {

        }
        else {
            receiveMove();
            isHit();
        }
    }

    private int enemyRow;
    private int emenyColumn;

    private void receiveMove() throws IOException {
        enemyRow = fromServer.readInt();
        emenyColumn = fromServer.readInt();
    }

    private boolean isHit() {
        boolean isHit = false;
        try {
            receiveMove();
            JButton target = leftPlayFieldButtons.get(emenyColumn).get(enemyRow);
            if (boats.contains(target))
                isHit = true;
            else
                isHit = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isHit;
    }

    private void changeButton()
    {
        if (isHit() == true)
        {
            JButton target = leftPlayFieldButtons.get(emenyColumn).get(enemyRow);
            target.setBackground(Color.orange);
        }
        if (isHit() == false)
        {
            JButton target = leftPlayFieldButtons.get(emenyColumn).get(enemyRow);
            target.setBackground(Color.white);
        }
    }

        /** Handle mouse click on a cell */
        private class ClickListener extends MouseAdapter {
            public void mouseClicked(MouseEvent e) {
                // If cell is not occupied and the player has the turn
                //if ((token == ' ') && myTurn) {
                   // myTurn = false;
                   // rowSelected = row;
                   // columnSelected = column;
                    jlblStatus.setText("Waiting for the other player to move");
                    waiting = false; // Just completed a successful move
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
