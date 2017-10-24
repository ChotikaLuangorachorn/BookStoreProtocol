import java.sql.*;
import java.util.ArrayList;

public class Books {
    private ArrayList<Book> books;
    private String url = "bookstore.db";

    public Books(){
        books = new ArrayList<Book>();
    }
    public Connection connectionDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String dbURL = "jdbc:sqlite:" + url;
        Connection conn = DriverManager.getConnection(dbURL);
        return conn;
    }
    public void selectToDB(){
        try{
            Connection conn = connectionDB();
            if (conn != null) {
                System.out.println("Connected to the database...");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                Statement statement = conn.createStatement();
                String querySelect = "select * from books";
                ResultSet result =  statement.executeQuery(querySelect);
                while (result.next()){
                    String name = result.getString(1);
                    int amount = Integer.parseInt(result.getString(2));
                    float price = Float.parseFloat(result.getString(3));
                    System.out.println(name+" "+amount+" "+price);

                    books.add(new Book(name, amount, price));
                }
                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertToDB(Book book) {
        try{
            Connection conn = connectionDB();
            if (conn != null) {
                System.out.println("Connected to the database...");
                Statement statement = conn.createStatement();
                try{
                    String name = (book.getName()).replace("'","\''");
                    int amount = book.getAmount();
                    float price = book.getPrice();
                    String queryInsert = "insert into books "+"values('" + name+"','"+amount+"','"+price+"')";
                    statement.executeUpdate(queryInsert);
                    System.out.println("adding book to DB...");
                    books.add(book);
                }catch (SQLException ex){
                    System.out.println("adding errer");}
                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

}
