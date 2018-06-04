package unnecessaryClasses;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private static ArrayList<ArrayList<JButton>> leftPlayFieldButtons;
    private static ArrayList<ArrayList<JButton>> rightPlayFieldButtons;

    private static boolean isStartPressed = false;
    private static boolean areBoatsPlaced = false;
    private static int boatsPlaced = 0;

    private static Color currentBackground;
    private static JLabel topFieldText = new JLabel();

    private static Color water = new Color(51, 190, 212);
    private static Color hoverWater = new Color(61,209, 232);

    public static void main(String[] args) {

        //the outer frame is created here
        JFrame frame = new JFrame("Gui oefenen Timo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 400));
        frame.setExtendedState(MAXIMIZED_BOTH);

        //this is hte panel that is going to contain all other panels
        JPanel mainPanel = new JPanel();

        //these are the 3 panels which are going to be placed on the mainPanel
        Panel topField = new Panel();
        Panel leftPlayField = new Panel();
        Panel centerArea = new Panel();
        Panel rightPlayField = new Panel();


        //the 3 panels are set as borderlayout here, so that there can be added more panels
        topField.setLayout(new FlowLayout());
        leftPlayField.setLayout(new BorderLayout());
        centerArea.setLayout(new BorderLayout());
        rightPlayField.setLayout(new BorderLayout());

        mainPanel.add(topField);
        mainPanel.add(leftPlayField);
        mainPanel.add(centerArea);
        mainPanel.add(rightPlayField);

        //thanks to an gridBagLayout it is possible to change all sizes according to your wishes,
        // this allows you to decide exactly where to place which panel and what size.
        //you can also simply add an panel without changing too much.
        GridBagLayout gridBagLayout = new GridBagLayout();
        makeConstraints(gridBagLayout, topField, 1, 1, 0, 0, 1.0, 0.1);
        makeConstraints(gridBagLayout, leftPlayField, 1, 1, 0, 1, 5.0, 10.0);
        makeConstraints(gridBagLayout, centerArea, 1, 1, 1, 1, 1.0, 10.0);
        makeConstraints(gridBagLayout, rightPlayField, 1, 1, 2, 1, 5.0, 10.0);

        centerArea.add(new JLabel("middenVeld"));


        //here are 2 codes to create the playfield, these are recycled from Johan's practisetest of lightsout in period 1.2
        //we must look to see if we must change this or creat an separate method.

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
                final int finalRightY = leftY;


                Icon testImage = new ImageIcon("ToetsStof.png");

                leftButton.addActionListener(e ->
                {
                    if (isStartPressed)
                    {
                        if (boatsPlaced < 5) {
                            leftButton.setBackground(Color.red);
                            currentBackground = leftButton.getBackground();
                            leftButton.setEnabled(false);
                            boatsPlaced++;
                            if (boatsPlaced == 5) {
                                areBoatsPlaced = true;
                                isStartPressed = false;
                                topFieldText.setText("Let the games begin!!");
                                leftPlayField.setEnabled(false);
                            }
                        }
                    }

                    System.out.println("position of the button:    x " + (finalLeftX + 1)  +  "  Y  " + (finalRightY + 1));
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
                    System.out.println("Position of the button:    x " + (finalRightX + 1)  +  "  Y  " + (finalRightY + 1));
                });

                rightPlayerGameField.add(rightButton);
                rightRow.add(rightButton);
            }
            rightPlayFieldButtons.add(rightRow);
        }

        //here comes the code after the field is createsd

        //first there comes conection stuf and then seabattle gets started

        topField.add(topFieldText);
        topFieldText.setVisible(false);
        JButton startButton = new JButton("start game");
        topField.add(startButton);
        startButton.addActionListener(e ->
        {
            startButton.setVisible(false);
            topFieldText.setText("U mag nu 5 schepen neer zetten");
            topFieldText.setVisible(true);
            isStartPressed = true;
        });






        mainPanel.setLayout(gridBagLayout);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
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

    //this is the method to create the gridBagConstraints(how you can decide where each field is placed)
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
}
