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
//        View view = new View();
//        Person test = new Person();
//        try {
//            Scanner sc = new Scanner(System.in);
//            Socket socket = new Socket("127.0.0.1", 4928);
            System.out.println("Client Open ...");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("app.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("App");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();


//            System.out.println("Enter any number: ");
//            String info = view.getName()+","+view.getPassword();
//            PrintStream ps = new PrintStream(socket.getOutputStream());
//            ps.println(info);
//
//            Scanner sc1 = new Scanner(socket.getInputStream());
//            String temp = sc1.next();
//            System.out.println(temp);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
//    static class Person{
//        public String name;
//        public int point;
//        public Person(){
//            name = "Test";
//            point = 120;
//        }
//
//    }
}
