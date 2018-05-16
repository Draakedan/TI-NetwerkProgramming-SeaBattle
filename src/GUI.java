import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private static ArrayList<ArrayList<JButton>> leftPlayFieldButtons;
    private static ArrayList<ArrayList<JButton>> rightPlayFieldButtons;

    public static void main(String[] args) {

        //hier word het buitenste frame gemaakt
        JFrame frame = new JFrame("Gui oefenen Timo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 400));
        frame.setExtendedState(MAXIMIZED_BOTH);

        //dit is het paneel waar alle andere layouts/panelen op komen
        JPanel mainPanel = new JPanel();

        //dit zijn de 3 panelen die op het mainPanel komen
        Panel leftPlayField = new Panel();
        Panel centerArea = new Panel();
        Panel rightPlayField = new Panel();


        //hier worden de 3 panelen als borderlayout gezet, zodat er meerdere dingen aan het paneel toegevoegd kunnen worden
        leftPlayField.setLayout(new BorderLayout());
        centerArea.setLayout(new BorderLayout());
        rightPlayField.setLayout(new BorderLayout());


        //door middel van een gridBagLayout, kan je alle grotes aanpassen naar eigen wens,
        //dit zorgt er voor dat je precies kan bepalen waar elk paneel komt te staan, en welke grote
        //ook kan je makkelijk een paneel toevoegen zonder veel te hoeven veranderen
        GridBagLayout gridBagLayout = new GridBagLayout();
        makeConstraints(gridBagLayout, leftPlayField, 1, 1, 0, 0, 5.0, 1.0);
        makeConstraints(gridBagLayout, centerArea, 1, 1, 1, 0, 1.0, 1.0);
        makeConstraints(gridBagLayout, rightPlayField, 1, 1, 2, 0, 5.0, 1.0);


        //in het centerArea staat voorlopig een button met middelveld, dit is omdat nog niet duildelijk is wat we
        //met het middenstuk willen doen
        centerArea.add(new JButton("middenVeld"), BorderLayout.CENTER);


//hier staan 2 codes om het speelveld te maken, deze komen uit johans oefentoest van lightsout van periode 2
        //we moeten kijken of we een aparte methode gaan schrijven om dit toe te voegen of het anders doen

        JPanel leftPlayerGameField = new JPanel(new GridLayout(10,10));
        leftPlayField.add(leftPlayerGameField, BorderLayout.CENTER);

        leftPlayFieldButtons = new ArrayList<>();
        for(int leftY = 0; leftY < 10; leftY++)
        {
            ArrayList<JButton> leftRow = new ArrayList<>();
            for(int leftX = 0; leftX < 10; leftX++)
            {
                JButton leftButton = new JButton();

                final int finalX = leftX;
                final int finalY = leftY;

                leftButton.addActionListener(e ->
                {

                });

                leftPlayerGameField.add(leftButton);
                leftRow.add(leftButton);
            }
            leftPlayFieldButtons.add(leftRow);
        }


        JPanel rightPlayerGameField = new JPanel(new GridLayout(10,10));
        rightPlayField.add(rightPlayerGameField, BorderLayout.CENTER);

        rightPlayFieldButtons = new ArrayList<>();
        for(int rightY = 0; rightY < 10; rightY++)
        {
            ArrayList<JButton> rightRow = new ArrayList<>();
            for(int rightX = 0; rightX < 10; rightX++)
            {
                JButton rightButton = new JButton();

                final int finalX2 = rightX;
                final int finalY2 = rightY;

                rightButton.addActionListener(e ->
                {

                });

                rightPlayerGameField.add(rightButton);
                rightRow.add(rightButton);
            }
            rightPlayFieldButtons.add(rightRow);
        }

        //hier word alles toegevoegd en visible gezet
        mainPanel.add(leftPlayField);
        mainPanel.add(centerArea);
        mainPanel.add(rightPlayField);

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

}
