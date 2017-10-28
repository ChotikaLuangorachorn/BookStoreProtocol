// Chotika Luangorachorn 5810404928 sec 1
package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Client implements Initializable{

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
    private int portNum = 4928;
    private String hostName = "127.0.0.1";

    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> types = FXCollections.observableArrayList("Name","Amount","Price");
        searchType.setValue("Name");
        searchType.setItems(types);
        try {
            socket = new Socket(hostName, portNum);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            toServer.writeUTF("SEARCH,,");
            toServer.flush();
            bookArea.clear();
            String codeStatus = fromServer.readUTF();
            countBook.setText(fromServer.readUTF()+" book");
            bookArea.appendText(fromServer.readUTF());
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't connect to server.");
        }

    }
    public void search(ActionEvent event) throws IOException {
        socket = new Socket(hostName, portNum);
        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());
        System.out.println("Searching ... ");
        toServer.writeUTF("SEARCH,"+searchType.getValue().toString()+","+searchField.getText());
        toServer.flush();
        bookArea.clear();
        String codeStatus = fromServer.readUTF();
        System.out.println("response status: "+ codeStatus);
        countBook.setText(fromServer.readUTF()+" book");
        bookArea.appendText(fromServer.readUTF());
        socket.close();
        }
    public void addBook(ActionEvent event) throws IOException {
        socket = new Socket(hostName, portNum);
        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());
        System.out.println("Adding book ...");
        String name = nameBookText.getText();
        String amount = amountBookText.getText();
        String price = priceBookText.getText();
        toServer.writeUTF("ADD,"+ name +","+amount+","+price);
        toServer.flush();
        String codeStatus = fromServer.readUTF();
        System.out.println("response status: "+ codeStatus);
        if (codeStatus.contains("406")){
            addStatus.setText("duplicate");
        }else if (codeStatus.contains("400")){
            addStatus.setText("can't add to store.");
        }else if (codeStatus.contains("201")){
            addStatus.setText(name + " - add to store success.");
            nameBookText.setText("");
            amountBookText.setText("0");
            priceBookText.setText("0.00");
            search(new ActionEvent());
        }
        socket.close();

    }
    public void showAll(ActionEvent evnet) throws IOException {
        this.searchType.setValue("Name");
        this.searchField.setText("");
        this.search(new ActionEvent());
    }

}
