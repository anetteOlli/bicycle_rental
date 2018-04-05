package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;


import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the transactions done to both Dock and DockingStation
 */
public class DockingStation {
    //todo
    /*
    review the database integrity levels, most likely serialize is a overkill for this
    and will slow the database down
     */

    /**
     *  Method used for registerDock at the dockingstation, when a dock is registered it's availability is set to true
     * @param dockingStationID the ID for the dockingstation.
     * @return boolean indicating if the method was succesful or not.
     */
    private boolean registerDock(int dockingStationID) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        //tries to turn off autocommit
        System.out.println("er i metoden");
        if(cleaner.setAutoCommit(con, false)){
            System.out.println("reg DOck: kom seg hit 0");
            //tries to set connection to serialize

            System.out.println("reg DOck: kom seg hit 1");
            //the code below will check that there is room for more docks at the given dockingstation
            int oldNumberOfDocks = findNumbersOfDocks(dockingStationID);
            int newNumberOfDocks = oldNumberOfDocks +1;
            //checks that the newDockID doesn't exceeds the dockingstation's capacity
            if(newNumberOfDocks <= findDockingStationCapacity(dockingStationID)){
                  String mysql ="INSERT INTO Dock (dock_id, station_id, isAvaialbe) VALUES (DEFAULT,?, true)";
                  PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
                  System.out.println("reg Dock: kom seg hit 2");
                  try{
                      sentence.setInt(1,dockingStationID);
                      System.out.println("kom seg hit 3");
                      sentence.execute();
                      System.out.println("kom seg hit 4");
                      if(cleaner.closeSentence(sentence)) {

                      }
                  } catch (SQLException e) {
                      e.printStackTrace();
                      return false;
                  }
            }else{
                //there's no room for additional docks
                return false;
            }
            if (cleaner.setAutoCommit(con, true) && cleaner.closeConnection(con)){

            }
        }
        return true;
    }


    /**
     * This method counts the amount of docks registered at a dockinsstation
     * @param dockingstationID the ID of the dockingstation
     * @return int number of docks registered at the dockingsstation.
     */
    public int findNumbersOfDocks(int dockingstationID) {
        int res = -1;
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        String mysql = "SELECT COUNT(dock_id) FROM Dock where station_id = ?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try {
            sentence.setInt(1, dockingstationID);
            ResultSet result = sentence.executeQuery();
            if (result.next()) {
                res = result.getInt(1);
            }
            if (cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){
                return res;
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }


    /**
     *  Method returns the capacity of a given dockingsstation
     * @param dockingStationID
     * @return integer, the total number of docks a given dockingsstation can hold.
     * This includes docks already registered at the dock and unoccupied dock slots
     */
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
            if (cleaner.closeResult(result) && cleaner.closeSentence(sentence) &&cleaner.closeConnection(con)){
                return res;
            }

            return -1;
        }catch (SQLException e){
            return -1;
        }
    }

    /**
     * DEPRECATED.... TO BE REMOVED UNLESS SOMEBODY USES THIS
     * @return
     */
    public int largestDockingStationsID(){
        int res = -1;
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String mysql = "SELECT MAX(station_id) FROM DockingStation";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            ResultSet result = sentence.executeQuery();
            if (result.next()){
                res = result.getInt(1);
            }
            if (cleaner.closeResult(result)&& cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                return res;
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * method counts the amount of registered docks in the databasesystem and returns the number of dockingsstations
     * @return integer
     */
    public int findNumberOfDockingStations(){
        int res = -1;
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String mysql = "SELECT COUNT(*) FROM DockingStation";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            ResultSet result = sentence.executeQuery();
            if (result.next()){
                res = result.getInt(1);
            }
            if (cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){
                return res;
            }
            return res;
        } catch (SQLException e) {
            return -1;
        }

    }


    /**
     *  this register method register a docking station
     * @param name of the dock
     * @param capacity of the dock
     * @param longitude longitude in degrees of the location of the dock
     * @param latitude in degrees of the location of the dock
     */
    public void registerDockStation(String name, int capacity, double longitude, double latitude){
        //converts the name to lowercase and trims out whitespace from the name
        //this is done to make searching for dockingStationID easier.
        String lowName = name.toLowerCase().trim();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        //tries to turn off autocommit
        System.out.println("DockStation: er i metoden");
        if(cleaner.setAutoCommit(con, false)){
            System.out.println(" DockStation: kom seg hit 0");

            System.out.println("DockStation: kom seg hit 1");
            //retrieves the largest number of dock_id already in existence
            //int oldDockingStationID = largestDockingStationsID();
            //newDockingStationID = oldDockingStationID +1;
            String mysql ="INSERT INTO DockingStation (name, active_status, capacity, longitude, latitude) VALUES (?, 1, ?, ?, ?)";
            //first value: station_id, second value: name, third value: active status, forth value: capacity
            PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
            System.out.println("DockStation: kom seg hit 2");
            try{
                //sentence.setInt(1, newDockingStationID);
                sentence.setString(1,lowName.trim());
                sentence.setInt(2, capacity);
                sentence.setDouble(3, longitude);
                sentence.setDouble(4,latitude);
                System.out.println("reg DockStation: kom seg hit 3");
                sentence.execute();
                System.out.println("reg DockStation: kom seg hit 4");
                if(cleaner.closeSentence(sentence)) {

                }
                } catch (SQLException e) {
                    e.printStackTrace();
            }

            if (cleaner.setAutoCommit(con, true) && cleaner.closeConnection(con)){
            }
        }
    }

    /**
     * searches the dockingstation table and finds the ID of a given dockingstation
     * @param name of the dockingstation
     * @return ArrayList consisting of dockStationID integers
     */
    public ArrayList findDockStationID(String name){
        String lowName = name.toLowerCase().trim();
        ArrayList<Integer> idNumbers = new ArrayList<Integer>();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        String mysql ="SELECT station_id FROM DockingStation WHERE name = ?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setString(1, lowName);
            ResultSet result = sentence.executeQuery();
            while (result.next()){
                idNumbers.add(result.getInt(1));
            }
            if (cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
            return idNumbers;
            }
            return new ArrayList<Integer>();
        } catch (SQLException e) {
             return new ArrayList<Integer>();
        }
    }

    /**
     * Method changes the name of one or several dockingstation
     * is not depending on lower/uppercase spelling of the names
     * @param oldName the name to be changed
     * @param newName the new name that will replace the old name
     * @return
     */
    public boolean editExistingDockStation(String oldName, String newName) {
        String oldnameLow = oldName.toLowerCase().trim();
        String newNameLow = newName.toLowerCase().trim();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        String mysql = "UPDATE DockingStation SET name=? WHERE name=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setString(1, newNameLow);
            sentence.setString(2, oldnameLow);
            sentence.execute();

        }catch (SQLException e){
            return false;
        }
        if (cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){
            if(findDockStationID(newNameLow).size()>0) {
                return true;
            }
            return false;
        }
        //this will be run if the close connection part fails
        return false;
    }

    /**
     *  This method is used for setting the activeStatus for the dockingStation
     * @param name name of the dockingStation
     * @param activeStatus is 0 for inactive and 1 for active
     * @return true if it successfully updates the dockingsstation.
     */
    public boolean editExistingDockingStation(String name, int activeStatus){
        String lowName = name.toLowerCase().trim();
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql = "UPDATE DockingStation SET active_status=? WHERE name=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setInt(1, activeStatus);
            sentence.setString(2,lowName);
            sentence.execute();
        }catch (SQLException e){
            return false;
        }
        if (cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
            return true;
        }
        //something went wrong when closing the resources
        return false;
    }

    /**
     * Method finds the name of a given dockinstation by given dockingstationID
     * @param dockingStationID the id of a given dockingstation
     * @return String dockingstationName
     */
    public String getName(int dockingStationID){
        String dock;
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql = "SELECT name FROM DockingStation WHERE station_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setInt(1, dockingStationID);
            ResultSet result = sentence.executeQuery();
            if(result.next()){
                dock = result.getString(1);
                if(cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                    return dock;
                }
            }
        }catch (SQLException e){
            return null;
        }
        return null;

    }

    /**
     * This methods set the active status of a specific dock at the dockingsstation.
     * It will return true if it was a success or false if it failed.
     * Failure can happen due to the dockID given not existing.
     * @param active true if the dock needs to be set as active, false if the dock is going to be set as false
     * @param DockId dockID of the dock to be changed
     */
    public boolean setDockAvailable(boolean active, int DockId) {
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql1 = "SELECT COUNT(*) FROM Dock WHERE dock_id=?";
        String mysql2 = "UPDATE Dock SET isAvailable=? WHERE dock_id=?";
        Boolean success = false;
        if (cleaner.setAutoCommit(con, false)) try {
            PreparedStatement sentence = connection.createPreparedStatement(con, mysql1);
            sentence.setInt(1, DockId);
            ResultSet result = sentence.executeQuery();
            //if this query returns a row, it means that the dockID exists, and the update sentence can be used
            //if not the update sentence should not be used, and a rollback should be performed
            if (result.next()) {
                PreparedStatement sentence2 = connection.createPreparedStatement(con, mysql2);
                sentence2.setBoolean(1, active);
                sentence2.setInt(2, DockId);
                sentence2.execute();
                if (cleaner.commit(con) && cleaner.setAutoCommit(con, true)) {
                    success = true;
                }
            } else {
                if (cleaner.rollback(con) && cleaner.setAutoCommit(con, true)) {

                }
            }
            if (cleaner.closeResult(result) && cleaner.closeConnection(con)) {

            }

        } catch (SQLException e) {
            return false;
        }
        return success;
    }

    /**
     * This methods set the active status of a specific dock at the dockingsstation.
     * It will return true if it was a success or false if it failed.
     * Failure can happen due to the dockID given not existing.
     * @param active true if the dock needs to be set as active, false if the dock is going to be set as false
     * @param DockinstationID DockingStationID of the dockingstation that the method will alter
     * @return boolean success of the method
     */
    public boolean setDockingStationActive(boolean active, int DockinstationID){
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql1 = "SELECT COUNT(*) FROM DockingStation WHERE station_id=?";
        String mysql2 = "UPDATE DockingStation SET active_status=? WHERE station_id=?";
        Boolean success = false;
        if (cleaner.setAutoCommit(con, false)) try {
            PreparedStatement sentence = connection.createPreparedStatement(con, mysql1);
            sentence.setInt(1, DockinstationID);
            ResultSet result = sentence.executeQuery();
            //if this query returns a row, it means that the dockID exists, and the update sentence can be used
            //if not the update sentence should not be used, and a rollback should be performed
            if (result.next()) {
                PreparedStatement sentence2 = connection.createPreparedStatement(con, mysql2);
                sentence2.setBoolean(1, active);
                sentence2.setInt(2, DockinstationID);
                sentence2.execute();
                if (cleaner.commit(con) && cleaner.setAutoCommit(con, true)) {
                    success = true;
                }
            } else {
                if (cleaner.rollback(con) && cleaner.setAutoCommit(con, true)) {

                }
            }
            if (cleaner.closeResult(result) && cleaner.closeConnection(con)) {

            }

        } catch (SQLException e) {
            return false;
        }
        return success;
    }


    /**
     * Method lists out all docking stations id registered in the system
     * @return
     */
    public ArrayList getDockingsStationIDs(){
        String mysql = "SELECT station_id FROM DockingStation";
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        try{
            PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
            ResultSet result = sentence.executeQuery();
            ArrayList<Integer> dockingStationIDs = new ArrayList<Integer>();
            while(result.next()){
                dockingStationIDs.add(result.getInt(1));
            }
            if(cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){
                return dockingStationIDs;
            }
        }catch (SQLException e){
            return null;

        }
        //if the methods gets here something went wrong
        return null;
    }




    public static void main(String[] args) {
        DockingStation ds = new DockingStation();
        System.out.println(ds.largestDockingStationsID());



        
    }

}
