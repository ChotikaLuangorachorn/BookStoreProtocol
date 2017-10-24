import javafx.fxml.Initializable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController{
    public static void main(String[] args) {
        Books books = new Books();
        books.selectToDB();
        new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(4929);
                while (true){
                    Socket socket = serverSocket.accept();
                    new Thread(new HandleClient(socket, books)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }).start();

    }
    static class HandleClient implements Runnable {
        private Socket socket;
        private DataInputStream inputFromClient;
        private DataOutputStream outputToClient;
        private Books books;

        public HandleClient(Socket socket, Books books) {
            this.socket = socket;
            this.books = books;
        }

        @Override
        public void run() {
            try {
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());
                while (true) {
                        String msg = inputFromClient.readUTF();
                        System.out.println("input--> " + msg);
                        if (msg.split(",",2)[0].equals("search")) {
                            searchBook(msg);
                         }
                         else if(msg.split(",",2)[0].equals("add")){
                            addBook(msg);
                        }
                        outputToClient.writeUTF("");
                }
            } catch (IOException e) {
                System.out.println("Client Close ...");
            }

        }
        public void searchBook(String msg) throws IOException {
            String type = "";
            String search = "";
            String[] input = msg.split(",", 3);
            type = input[1];
            search = input[2].toLowerCase();
            System.out.println("type: " + type + " search: " + search);

            String text = "";
            int countBook = 0;
            if (search.equals("")) {
                for (Book b : books.getBooks()) {
                    text += "Name: " + b.getName() + "\n   Amount: " + b.getAmount() + ", Price: " + b.getPrice() + " baht.\n----------\n";
                    countBook += 1;
                }
            } else if (type.equals("Name")) {
                for (Book b : books.getBooks()) {
                    if ((b.getName().toLowerCase()).contains(search)) {
                        text += "Name: " + b.getName() + "\n   Amount: " + b.getAmount() + ", Price: " + b.getPrice() + " baht.\n----------\n";
                        countBook += 1;
                    }
                }
            } else if (type.equals("Amount")) {
                for (Book b : books.getBooks()) {
                    if ((b.getAmount() + "").equals(search)) {
                        text += "Name: " + b.getName() + "\n   Amount: " + b.getAmount() + ", Price: " + b.getPrice() + " baht.\n----------\n";
                        countBook += 1;
                    }
                }
            } else if (type.equals("Price")) {
                for (Book b : books.getBooks()) {
                    if (b.getPrice() == Float.parseFloat(search)) {
                        text += "Name: " + b.getName() + "\n   Amount: " + b.getAmount() + ", Price: " + b.getPrice() + " baht.\n----------\n";
                        countBook += 1;
                    }
                }
            }
            outputToClient.writeUTF(countBook + "");
            outputToClient.writeUTF(text);
        }
        public void addBook(String msg) throws IOException {

            String[] input = msg.split(",", 4);
            String name = input[1].toLowerCase();
            System.out.println(input[2]);
            int amount = Integer.parseInt(input[2]);
            float price = Float.parseFloat(input[3]);
            if (! name.equals("") && amount >=0 && price >=0){
                System.out.println(name+","+amount+","+price);
                boolean checkDuplicate = false;
                for (Book b: books.getBooks()){
                    if (name.equals(b.getName().toLowerCase())){
                        checkDuplicate = true;
                        break;
                    }
                }
                if (checkDuplicate){
                    outputToClient.writeUTF("duplicate");
                }
                else{
                    books.insertToDB(new Book(name,amount,price));
                    outputToClient.writeUTF("success");
                }
            }
            else{
                outputToClient.writeUTF("error");
            }

        }
    }
}
