import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Opdracht31I2_Client extends Application {
// IO streams
DataOutputStream toServer = null;
DataInputStream fromServer = null;

        @Override
// Override the start method in the Application class
        public void start(Stage primaryStage) {
            // Panel p to hold the label and text field
        BorderPane HeightTextField = new BorderPane();
        HeightTextField.setPadding(new Insets(5, 5, 5, 5));
        HeightTextField.setStyle("-fx-border-color: green");
        HeightTextField.setLeft(new Label("Enter your Height in meter: "));

        TextField htf = new TextField();
        htf.setAlignment(Pos.BOTTOM_RIGHT);
        HeightTextField.setCenter(htf);
        BorderPane mainPane = new BorderPane();

            BorderPane weightTextField = new BorderPane();
            weightTextField.setPadding(new Insets(5, 5, 5, 5));
            weightTextField.setStyle("-fx-border-color: green");
            weightTextField.setLeft(new Label("Enter your Weight in kG: "));

            TextField wtf = new TextField();
            wtf.setAlignment(Pos.BOTTOM_RIGHT);
            weightTextField.setCenter(wtf);
            GridPane text = new GridPane();

        // Text area to display contents
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(text);
        text.add(weightTextField, 1,0);
        text.add(HeightTextField,0,0);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client");// Set the stage title
        primaryStage.setScene(scene);
        // Place the scene in the stage
        primaryStage.show();// Display the stage

        wtf.setOnAction(e -> {
            try {
                // Get the radius from the text field
                double Height = Double.parseDouble(htf.getText().trim());
                double Width = Double.parseDouble(wtf.getText().trim());
                // Send the radius to the server
                toServer.writeDouble(Height);
                toServer.writeDouble(Width);
                toServer.flush();
                // Get area from the server
                double area = fromServer.readDouble();
                // Display to the text area
                ta.appendText("BMI is " + "\n");
                ta.appendText("value received from the server is "
                        + area + '\n');
                }
                catch (IOException ex) {
                System.err.println(ex);
                }
            });

        try {
            // Create a socket to connect to the server
            //Socket socket = new Socket("localhost", 8000);
            Socket socket = new Socket("2001:610:1a0:1300:a4cd:d583:833f:ea3d", 8000);
            // Socket socket = new Socket("drake.Armstrong.edu", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
            }
            catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
            }
        }
}