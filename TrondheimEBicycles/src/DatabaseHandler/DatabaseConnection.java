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
public class DatabaseConnection {
    private DatabasePasswordKeep credentials = new DatabasePasswordKeep();
    private String[] information = credentials.getCredentials();
    private String username = information[0];
    private String password = information[1];
    private String connectionURL = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/" + username + "?user=" + username + "&password=" + password;
    private String databasedriver = "com.mysql.jdbc.Driver";
    //private Connection con = null;

    /*
    public DatabaseConnection() {
        con = createConnection();

    }
    */

    private Connection createConnection() {
        try {
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


    //anette bruker ikke denne metoden, er det noen som i det hele tatt bruker denne?
    public PreparedStatement createPreparedStatement(Connection con, String sentence) {
        try {
            PreparedStatement statement = con.prepareStatement(sentence);
            return statement;
        } catch (SQLException e) {
            return null;
        }
    }


    private void registerDock(int dockingStationID) {
        Connection con = createConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        //tries to turn off autocommit
        System.out.println("er i metoden");
        if(cleaner.setAutoCommit(con, false)){
            System.out.println("kom seg hit 0");
            //tries to set connection to serialize
            if(cleaner.setSerializable(con)){
                System.out.println("kom seg hit 1");
                //retrieves the largest number of dock_id already in existence
                int oldDockID = findNumbersOfDocks(dockingStationID);
                int newDockID = oldDockID +1;
                //checks that the newDockID doesn't exceeds the dockingstation's capacity
                if(newDockID <= findDockingStationCapacity(dockingStationID)){


                    String mysql ="INSERT INTO Dock VALUES (?,?, 1)";
                    PreparedStatement sentence = createPreparedStatement(con, mysql);
                    System.out.println("kom seg hit 2");
                    try{
                        sentence.setInt(1, newDockID);
                        sentence.setInt(2,dockingStationID);
                        System.out.println("kom seg hit 3");
                        sentence.execute();
                        System.out.println("kom seg hit 4");
                        if(cleaner.commit(con))
                            if (cleaner.closeSentence(sentence)) {
                            }


                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    //
                if (cleaner.setAutoCommit(con, true))if (cleaner.closeConnection(con)){

                }
                }

        }


    }

    public int findNumbersOfDocks(int dockingstationID) {
        int res = -1;
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = createConnection();
        String mysql = "SELECT MAX(dock_id) FROM Dock where station_id = ?";
        PreparedStatement sentence = createPreparedStatement(con, mysql);
        try {
            sentence.setInt(1, dockingstationID);
            ResultSet result = sentence.executeQuery();
            if (result.next()) {
                res = result.getInt(1);
            }
            if (cleaner.closeResult(result)) if (cleaner.closeSentence(sentence)) {
                if (cleaner.closeConnection(con)) return res;
            }
            return res;
        } catch (SQLException e) {
            return -1;
        }
    }
    public int findDockingStationCapacity(int dockingStationID){
        int res = 0;
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = createConnection();
        String mysql = "SELECT capacity FROM DockingStation where station_id = ?";
        PreparedStatement sentence = createPreparedStatement(con, mysql);
        try{
            sentence.setInt(1, dockingStationID);
            ResultSet result = sentence.executeQuery();
            if (result.next()) {
                res = result.getInt(1);
            }
            if (cleaner.closeResult(result)) if (cleaner.closeSentence(sentence)) {
                if (cleaner.closeConnection(con)) return res;
            }
            return res;

        }catch (SQLException e){
            return -1;
        }
    }

    public int findNumbersOfDockingStations(){
        int res = -1;
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = createConnection();
        String mysql = "SELECT MAX(station_id) FROM DockingStation";
        PreparedStatement sentence = createPreparedStatement(con, mysql);
        try{
            ResultSet result = sentence.executeQuery();
            if (result.next()){
                res = result.getInt(1);
            }
            if (cleaner.closeResult(result)) if (cleaner.closeSentence(sentence)) {
                if (cleaner.closeConnection(con)) return res;
            }
            return res;
        } catch (SQLException e) {
            return -1;
        }
    }
    public void registerDockingStation(String name, int capacity){

    }

    public static void main(String[] args) {
        DatabaseConnection con = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        /*
        for (int i = 0; i < 5; i++){
            con.registerDock(1);
            System.out.println(con.findNumbersOfDocks(1));
        }
        */
        System.out.println(con.findNumbersOfDockingStations());



    }
}





