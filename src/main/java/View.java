
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class View implements Initializable{

    @FXML
    private TextField searchField, nameBookText, amountBookText, priceBookText;
    @FXML
    private TextArea bookArea;
    @FXML
    private ComboBox searchType;
    @FXML
    private Label countBook, addStatus;

    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private Socket socket;

    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> types = FXCollections.observableArrayList("Name","Amount","Price");
        searchType.setValue("Name");
        searchType.setItems(types);
        try {
            socket = new Socket("127.0.0.1", 4929);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            toServer.writeUTF("search,,");
            toServer.flush();
            bookArea.clear();
            countBook.setText(fromServer.readUTF()+" book");
            bookArea.appendText(fromServer.readUTF());
            socket.close();
        } catch (IOException e) {
            System.out.println("Can't connect to Server");
        }

    }
    public void search(ActionEvent event) throws IOException {
        socket = new Socket("127.0.0.1", 4929);
        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());
        System.out.println("Searching ... ");
        toServer.writeUTF("search,"+searchType.getValue().toString()+","+searchField.getText());
        toServer.flush();
        bookArea.clear();
        countBook.setText(fromServer.readUTF()+" book");
        bookArea.appendText(fromServer.readUTF());
        socket.close();
        }
    public void addBook(ActionEvent event) throws IOException {
        socket = new Socket("127.0.0.1", 4929);
        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());
        System.out.println("Adding book ...");
        String name = nameBookText.getText();
        String amount = amountBookText.getText();
        String price = priceBookText.getText();
        toServer.writeUTF("add,"+ name +","+amount+","+price);
        toServer.flush();
        String msgFromServer = fromServer.readUTF();
        if (msgFromServer.equals("duplicate")){
            addStatus.setText(name + " - duplicate");
        }else if (msgFromServer.equals("error")){
            addStatus.setText(name + " - can't add to store.");
        }else if (msgFromServer.equals("success")){
            addStatus.setText(name + " - add to store success.");
        }
        socket.close();



    }


}
