package DatabaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;


/**
 * This class handles communication with the database
 * It reads username and password from a file and establishes a connection
 * with the database
 * author: anette
 */
public class DatabaseConnection{
    private DatabasePasswordKeep credentials = new DatabasePasswordKeep();
    private String[] information = credentials.getCredentials();
    private String username = information[0];
    private String password = information[1];
    private String connectionURL = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/" + username + "?user=" + username + "&password=" + password;
    private String databasedriver = "com.mysql.jdbc.Driver";
    private Connection con = null;

    public DatabaseConnection(){
        con = createConnection();

    }

    public Connection createConnection(){
        try{
            Connection con = DriverManager.getConnection(connectionURL);
            Class.forName(databasedriver);
            return con;

        } catch (SQLException e) {
            System.out.println("error with databaseconstructor 1");
        } catch (ClassNotFoundException ex) {
            System.out.println("error with databaseconstructor 2");
        }
        return null;
    }
    public Connection getConnection(){
        return con;
    }

    public PreparedStatement createPreparedStatement(Connection con, String sentence){
        try {
            PreparedStatement statement = con.prepareStatement(sentence);
            return statement;
        }catch (SQLException e){
            return null;
        }

    }

    public static void main(String[] args) {
        DatabaseConnection con = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection connector = con.createConnection();
        cleaner.closeConnection(connector);


    }




}





