import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Opdracht31I2_Server extends Application {
    @Override
// Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server");// Set the stage title
        primaryStage.setScene(scene);// Place the scene in the stage
        primaryStage.show();// Display the stage

            new Thread(() -> {
                try {
                    // Create a server socket
                    ServerSocket serverSocket = new ServerSocket(8003);
                    Platform.runLater(() ->
                            ta.appendText("Server started at " + new Date() + '\n'));
                    // Listen for a connection request
                    while (true) {
                    Socket socket = serverSocket.accept();

                    // Create data input and output streams
                    DataInputStream inputFromClient = new DataInputStream(
                            socket.getInputStream());
                    DataOutputStream outputToClient = new DataOutputStream(
                            socket.getOutputStream());

                        // Receive radius from the client
                        double height = inputFromClient.readDouble();
                        double weight = inputFromClient.readDouble();

                        // Compute area
                        double bmi = weight / (height * height);

                        // Send area back to the client
                        outputToClient.writeDouble(bmi);

                        Platform.runLater(() -> {
                            ta.appendText("height received from client: "
                                    + height + '\n');
                            ta.appendText("weight received from client: "
                                    + weight + '\n');
                            ta.appendText("BMI is: " + bmi + '\n');
                        });
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
        }
