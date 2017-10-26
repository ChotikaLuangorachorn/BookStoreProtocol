// Chotika Luangorachorn 5810404928 sec 1
package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Client Open ...");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/app.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("App");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
