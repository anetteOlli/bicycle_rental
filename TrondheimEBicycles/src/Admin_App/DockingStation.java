package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DockingStation {

    private void registerDock(int dockingStationID) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
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
                    PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
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
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        String mysql = "SELECT MAX(dock_id) FROM Dock where station_id = ?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
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
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        String mysql = "SELECT capacity FROM DockingStation where station_id = ?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
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
        DatabaseConnection conection = new DatabaseConnection();
        Connection con = conection.getConnection();
        String mysql = "SELECT MAX(station_id) FROM DockingStation";
        PreparedStatement sentence = conection.createPreparedStatement(con, mysql);
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



    public void registerDockStation(String name, int capacity){

    }
    public void editExistingDockStation(String oldName, String newName){

    }
    public void editExistingDockStation(String name, int activeStatus){

    }

    public void viewDockingStation(String name){

    }

    public static void main(String[] args) {
        DockingStation ds = new DockingStation();
        System.out.println(ds.findNumbersOfDockingStations());
        
    }

}
