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
    review the database integrity levels,
     */

    /**
     * Method tested and verified 2018.04.08
     * Method will not register more docks, than the dockingStation has a capacity for
     *  Method used for registerDock at the dockingstation, when a dock is registered it's availability is set to true
     * @param dockingStationID the ID for the dockingstation.
     * @param numberOfDocks the capacity of the dockingstation. This number will not overrule the given capacity of the dockingstation
     * @return boolean indicating if the method was succesful or not.
     */
    private boolean registerDock(int dockingStationID, int numberOfDocks) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        //tries to turn off autocommit
        if(cleaner.setAutoCommit(con, false)){
            //the code below will check that there is room for more docks at the given dockingstation
            int oldNumberOfDocks = getNumbersOfDocks(dockingStationID);
            int newNumberOfDocks = oldNumberOfDocks +1;
            //checks that the newDockID doesn't exceeds the dockingstation's capacity
            if(newNumberOfDocks <= getDockingStationCapacity(dockingStationID)){
                //the dock is initially set to available, as it is assumed that any dock registered to the system
                //will be available
                  String mysql ="INSERT INTO Dock (dock_id, station_id, isAvailable) VALUES (DEFAULT,?, true)";
                  PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
                  try{
                      sentence.setInt(1,dockingStationID);
                      for(int i = 0; i <= numberOfDocks; i++){
                          sentence.execute();
                      }
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
     * Method tested and verified 2018.04.08
     * This method counts the amount of docks registered at a dockinsstation
     * @param dockingstationID the ID of the dockingstation
     * @return int number of docks registered at the dockingsstation.
     */
    public int getNumbersOfDocks(int dockingstationID) {
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
     * Method tested 2018.04.08
     *  Method returns the capacity of a given dockingsstation
     * @param dockingStationID
     * @return integer, the total number of docks a given dockingsstation can hold.
     * This includes docks already registered at the dock and unoccupied dock slots
     */
    public int getDockingStationCapacity(int dockingStationID){
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

    /** method tested 2018.04.08
     * method counts the amount of registered docks in the databasesystem and returns the number of dockingsstations
     * @return integer
     */
    public int getNumberOfDockingStations(){
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
     * Method tested 2018.04.08
     *  this register method register a docking station
     * @param name of the dock
     * @param capacity of the dock
     * @param longitude longitude in degrees of the location of the dock
     * @param latitude in degrees of the location of the dock
     */
    public int registerDockStation(String name, int capacity, double longitude, double latitude){
        //converts the name to lowercase and trims out whitespace from the name
        //this is done to make searching for dockingStationID easier.
        int dockStationID= -1;
        String lowName = name.toLowerCase().trim();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        if(cleaner.setAutoCommit(con, false)) {
            String mysql = "INSERT INTO DockingStation (name, active_status, capacity, longitude, latitude, powerUsage) VALUES (?, TRUE, ?, ?, ?, 0)";
            try {
                PreparedStatement sentence = con.prepareStatement(mysql, PreparedStatement.RETURN_GENERATED_KEYS);
                sentence.setString(1, lowName.trim());
                sentence.setInt(2, capacity);
                sentence.setDouble(3, longitude);
                sentence.setDouble(4, latitude);
                System.out.println("reg DockStation: kom seg hit 3");
                sentence.executeUpdate();
                ResultSet rs = sentence.getGeneratedKeys();
                if (rs.next()) {
                    dockStationID = rs.getInt(1);
                }
                System.out.println("reg DockStation: kom seg hit 4");
                if(dockStationID > 0){
                    registerDock(dockStationID, capacity);
                }
                if(cleaner.commit(con)){
                    if(cleaner.closeSentence(sentence) && cleaner.closeResult(rs) && cleaner.closeConnection(con)){
                        return dockStationID;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /** Method tested 2018.04.08
     * searches the dockingstation table and finds the ID of a given dockingstation
     * @param name of the dockingstation
     * @return ArrayList consisting of dockStationID integers
     */
    public ArrayList getSpecificDockStationID(String name){
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
     * Method tested 2018.04.08
     * Method changes name of a given dockingstation.
     * @param dockingStationID The ID of the dockingstation that should get a name change
     * @param newName the new name, the name will be stored in lowcase
     * @return true if the method is successful, or false if it fails
     */
    public boolean setName(int dockingStationID, String newName) {
        String newNameLow = newName.toLowerCase().trim();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        String mysqlUpdate = "UPDATE DockingStation SET name=? WHERE station_id=?";
        String mysqlSelect = "SELECT name FROM DockingStation WHERE station_id= ?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysqlUpdate);
        PreparedStatement sentence2 = connection.createPreparedStatement(con, mysqlSelect);
        boolean result = false;
        if(cleaner.setAutoCommit(con, false)){
            try{
                sentence.setString(1, newNameLow);
                sentence.setInt(2, dockingStationID);
                sentence.execute();
                sentence2.setInt(1, dockingStationID);
                ResultSet resultSet = sentence2.executeQuery();
                if(resultSet.next()){
                    String resultName = resultSet.getString(1);
                    if(resultName.equals(newNameLow)){
                        if(cleaner.commit(con)&& cleaner.setAutoCommit(con, true)){
                            result= true;
                        }
                    }else{
                        if(cleaner.rollback(con) && cleaner.setAutoCommit(con, true)){

                        }
                    }
                }
                if(cleaner.closeResult(resultSet) && cleaner.closeSentence(sentence) && cleaner.closeSentence(sentence2) && cleaner.closeSentence(sentence2)){
                    return result;
                }

            }catch (SQLException e){
                return false;
            }
            }
            return false;
        }

    /*
     *  This method is used for setting the activeStatus for the dockingStation
     * @param name name of the dockingStation
     * @param activeStatus is 0 for inactive and 1 for active
     * @return true if it successfully updates the dockingsstation.
     */

    //DEPRECATED, BETTER METHOD IMPLEMENTED
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
     * Method tested and verified 2018.04.08
     * Method finds the name of a given dockinstation by given dockingstationID
     * @param dockingStationID the id of a given dockingstation
     * @return String dockingstationName, null if the given dockingStationID doesn't exists
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
     * Method tested 2018.04.08
     * This method returns an int depending on what the activity_status is on the given dockingStation
     * @param dockingStationID
     * @return -1 if the dockingstation does not exist, or if the method encountered a problem
     * @return 0 if the dockingstation is inactive
     * @return 1 if the dockingstation is active
     */
    public int getDockingStationStatus(int dockingStationID){
        boolean status;
        int statusResult = 0;
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql = "SELECT active_status FROM DockingStation WHERE station_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setInt(1, dockingStationID);
            ResultSet result = sentence.executeQuery();
            if(result.next()){
                status = result.getBoolean(1);
                if(status){
                    statusResult = +1;
                }else{
                    statusResult = 0;
                }
                if(cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {

                }
            }else{
                statusResult = -1;
            }
        }catch (SQLException e){
            return -1;
        }
        return  statusResult;
    }

    /**
     * method tested 2018.04.08
     *
     * @param dockingStationID
     * @return powerUsage as double
     * @return 0 if the powerUsage is not set in the database
     * @return -1 in case of database errors
     */
    public double getPowerUsage(int dockingStationID){
        double powerUsage = 0;
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql = "SELECT powerUsage FROM DockingStation WHERE station_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setDouble(1, dockingStationID);
            ResultSet resultSet = sentence.executeQuery();
            if(resultSet.next()){
                powerUsage = resultSet.getDouble(1);
            }
            if(cleaner.closeResult(resultSet) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){

            }
            return powerUsage;
        }catch (SQLException e){
            return -1;
        }
    }

    /**
     * Method tested 2018.04.08
     * finds the number of bikes that is at a given dockinstation
     * @param dockingStationID
     * @return bikenumber
     * @return -1 in case of databaseerrors
     */
    public int getNumberOfBikesCheckedIn(int dockingStationID){
        int bike = 0;
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql = "SELECT COUNT(*) FROM Bicycle NATURAL JOIN Dock WHERE station_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setInt(1,dockingStationID);
            ResultSet resultSet = sentence.executeQuery();
            if(resultSet.next()){
                bike = resultSet.getInt(1);
            }
            if(cleaner.closeResult(resultSet) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){

            }
            return bike;

        }catch (SQLException e) {
            return -1;
        }
    }

    /**
     *
     * @param dockingstationID
     * @return
     */
    public ArrayList getBikeIDAtDockingStation(int dockingstationID){
        ArrayList<Integer> bike = new ArrayList<Integer>();
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        String mysql = "SELECT bicycle_id FROM Bicycle NATURAL JOIN Dock WHERE station_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try{
            sentence.setInt(1, dockingstationID);
            ResultSet resultSet = sentence.executeQuery();
            while(resultSet.next()){
                bike.add(resultSet.getInt(1));
            }
            if(cleaner.closeResult(resultSet) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){

            }
            return bike;
        }catch (SQLException e){
            return null;
        }
    }



    /*
     * This methods set the active status of a specific dock at the dockingsstation.
     * It will return true if it was a success or false if it failed.
     * Failure can happen due to the dockID given not existing.
     * @param active true if the dock needs to be set as active, false if the dock is going to be set as false
     * @param DockId dockID of the dock to be changed
     *
     */

    //should this method become DEPRECATED?? Method should only be used by bicycle class??
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
     *  Method set wheter or not a given dockingstation should be active or not
     * @param active true for the dockingstation should be active, and false if the dockingstation is set to inactive
     * @param dockingStationID
     * @return true if the method is successful. Method returns false on fails and if the dockingstation doesn't exists.
     * Method tested and verified 2018.04.08
     */
    public boolean setDockingStationActivity(boolean active, int dockingStationID){
        String mysqlupdate = "UPDATE DockingStation SET active_status=? WHERE station_id=?";
        String mysqlconfirm = "SELECT active_status FROM DockingStation WHERE station_id =?";
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        boolean results = false;
        if (cleaner.setAutoCommit(con, false)) {
            try {

                PreparedStatement sentence = connection.createPreparedStatement(con, mysqlupdate);
                sentence.setBoolean(1, active);
                sentence.setInt(2,dockingStationID);
                sentence.execute();
                PreparedStatement sentence2 = connection.createPreparedStatement(con, mysqlconfirm);
                sentence2.setInt(1, dockingStationID);
                ResultSet result = sentence2.executeQuery();
                if (result.next()) {
                    boolean resultActivity = result.getBoolean(1);
                    if (resultActivity == active) {
                        if (cleaner.commit(con) && cleaner.setAutoCommit(con, true)) {
                            results = true;
                        }
                        //if the select sentence doesn't give correct longitude and latitude, this block will run
                        //it's intention is to perform a rollback and the method should return false
                    } else {
                        if (cleaner.rollback(con) && cleaner.setAutoCommit(con, true)) {
                        }
                        results = false;
                    }
                    //if there is no results, then the dockingstation ID supplied not existed
                } else {

                }
                if (cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeSentence(sentence2) && cleaner.closeConnection(con)) {
                    return results;
                }
            } catch (SQLException e) {
                return false;
            }

        }
        //if we somehow ended up here, then something went wrong and the method returns false
        return false;
    }


    /**
     * Method lists out all docking stations id registered in the system
     * Note: method differs from getSpecificDockStationID() in that this methods list ALL dockingstations in the system
     * while getSpecificDockStationID() only lists id for a given dockingstation name
     * Method tested and verified 2018.04.08 (yy.mm.dd)
     * @return ArrayList consisting of Integers with dockingstationIDs, if no dockingstations exists it returns null.
     */
    public ArrayList getAllDockingStationIDs(){
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

    /**
     * Method tested and verified 2018.04.08
     * Method almost identical to getAllDockingStationIDs(), however this method only lists those dockingstations that are active
     * @return ArrayList consisting of integers of the dockingStationIDs that are active, or null if none exists
     */
    public ArrayList getActiveDockingStationsIDs(){
    String mysql = "SELECT station_id FROM DockingStation WHERE active_status=TRUE";
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

    /**
     * method changes the dockingstation coordinates. The method will return true if successful or false if not.
     * The method takes into consideration if the dockingstationID supplied does not exists and returns a false in those cases
     * Method has been tested and verified 2018.04.08 (yy.mm.dd)
     * @param dockingStationID the station id used
     * @param longitude in degrees
     * @param latitude in degrees
     * @return success of the method
     */
    public boolean setDockingStationMapCoordinates(int dockingStationID, int longitude, int latitude) {
        String mysqlupdate = "UPDATE DockingStation SET longitude=?, latitude=? WHERE station_id=?";
        String mysqlconfirm = "SELECT longitude, latitude FROM DockingStation WHERE station_id =?";
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        boolean results = false;
        if (cleaner.setAutoCommit(con, false)) {
            try {

                PreparedStatement sentence = connection.createPreparedStatement(con, mysqlupdate);
                sentence.setInt(1, longitude);
                sentence.setInt(2, latitude);
                sentence.setInt(3, dockingStationID);
                sentence.execute();
                PreparedStatement sentence2 = connection.createPreparedStatement(con, mysqlconfirm);
                sentence2.setInt(1, dockingStationID);
                ResultSet result = sentence2.executeQuery();
                if (result.next()) {
                    int resultLongitude = result.getInt(1);
                    int resultLatitude = result.getInt(2);
                    if (latitude == resultLatitude && longitude == resultLongitude) {
                        if (cleaner.commit(con) && cleaner.setAutoCommit(con, true)) {
                            results = true;
                        }
                        //if the select sentence doesn't give correct longitude and latitude, this block will run
                        //it's intention is to perform a rollback and the method should return false
                    } else {
                        if (cleaner.rollback(con) && cleaner.setAutoCommit(con, true)) {
                        }
                        results = false;
                    }
                    //if there is no results, then the dockingstation ID supplied not existed
                } else {

                }
                if (cleaner.closeResult(result) && cleaner.closeSentence(sentence) && cleaner.closeSentence(sentence2) && cleaner.closeConnection(con)) {
                    return results;
                }
            } catch (SQLException e) {
                return false;
            }

        }
        //if we somehow ended up here, then something went wrong and the method returns false
        return false;
    }

    /**
     * method gets a specific dockingstation location in longitude and latitude in degrees
     *
     * @param dockStnID dockingstation specified
     * @return index 0: longitude, index 1: latitude
     */
    public double[] getDockingStationLocation(int dockStnID){
        double[] result = new double[2];
        String sql = "SELECT longitude, latitude FROM DockingStation WHERE station_id = ?";
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        PreparedStatement sentence = connection.createPreparedStatement(con, sql);
        try{
            sentence.setInt(1,dockStnID);
            ResultSet resultSet = sentence.executeQuery();
            if(resultSet.next()){
                result[0] = resultSet.getDouble(1);
                result[1] = resultSet.getDouble(2);
            }
            if(cleaner.closeResult(resultSet) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){
                return result;
            }else{
                return null;
            }
        }catch (SQLException e){
            return null;
        }

    }

    /**
     * method collects all activeDockingstations and places then into a table
     * @return double[]][], double[row][0] = dockingStationID as a double
     * double[row][1] = latitude, double[row][2] = longitude
     */
    public double[][] getAllDockingStationLocation(){
        //starts off with a table with space for 5 dockingstations.
        //we know that horizontally we will only have 3 columns
        double[][] result = new double[5][3];
        int noDockStation = 0;
        String sql = "SELECT station_id, latitude, longitude FROM DockingStation WHERE active_status=TRUE";
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        PreparedStatement sentence = connection.createPreparedStatement(con, sql);
        try{
            ResultSet resultSet = sentence.executeQuery();
            while(resultSet.next()) {
                if (noDockStation < result.length) {
                    result[noDockStation][0] = (double) resultSet.getInt(1);
                    result[noDockStation][1] = resultSet.getDouble(2);
                    result[noDockStation][2] = resultSet.getDouble(3);
                    noDockStation++;
                } else {
                    //generate new result-tabel with space for 5 new rows
                    //and copy content over to it
                    double[][] tempResult = new double[result.length +5][3];
                    for (int row = 0; row < result.length; row++) {
                        for (int column = 0; column < result[row].length; column++) {
                            tempResult[row][column] = result[row][column];
                        }
                    }
                    result = tempResult;
                    result[noDockStation][0] = (double) resultSet.getInt(1);
                    result[noDockStation][1] = resultSet.getDouble(2);
                    result[noDockStation][2] = resultSet.getDouble(3);
                    noDockStation++;
                }
            }
            //remove rows that does not contain data:
            double[][] temp = new double[noDockStation][3];
            for(int row = 0; row <noDockStation; row++){
                for(int column = 0;column < result[row].length; column++){
                    temp[row][column] = result[row][column];
                }
            }
            result = temp;
            if(cleaner.closeResult(resultSet) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                return result;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Method changes powerusage for a givendockingstation
     * @param dockingstationID
     * @param powerUsage
     */
    public void setPowerUsage(int dockingstationID, double powerUsage){
        String sql = "UPDATE  DockingStation SET powerUsage=? WHERE station_id=?";
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        PreparedStatement sentence = connection.createPreparedStatement(con, sql);
        try{
            sentence.setDouble(1, powerUsage);
            sentence.setInt(2, dockingstationID);
            sentence.executeUpdate();
            con.close();
            sentence.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }





    public static void main(String[] args) {
        DockingStation ds = new DockingStation();
        System.out.println(ds.largestDockingStationsID());
        System.out.println(ds.setDockingStationMapCoordinates(1, 24, 76));
        System.out.println(ds.setDockingStationMapCoordinates(23, 24, 56));
        ArrayList<Integer> allDockingstationID = ds.getAllDockingStationIDs();
        System.out.println(allDockingstationID.size());
        System.out.println("setDockingStationActivity: " +ds.setDockingStationActivity(true, 2));
        System.out.println("getName(): " + ds.getName(2));

        for(int i = 0; i < allDockingstationID.size(); i++ ){
            System.out.println(allDockingstationID.get(i));
        }
        ArrayList<Integer> activeDockingStationID = ds.getActiveDockingStationsIDs();
        for(int i = 0; i <activeDockingStationID.size(); i++){
            System.out.println("activeDockingStationID: " +i + " :" + activeDockingStationID.get(i));
        }
        System.out.println("setName: " + ds.setName(1, "olaug"));
        ArrayList<Integer> olaug = ds.getSpecificDockStationID("olaug");
        for(int i = 0; i < olaug.size(); i++){
            System.out.println("getSpecificDockStationID: " + olaug.get(i));
        }
        System.out.println("registrert dock: "+ds.registerDockStation("munkegata", 15, 13,54));
        System.out.println("registrert dock: "+ds.registerDockStation(" ", 15, 13,54));
        System.out.println("kapasitet: " + ds.getDockingStationCapacity(1));
        System.out.println("antall dockingstationer: " + ds.getNumberOfDockingStations());
        System.out.println("begynner å registrere docks");
        boolean plass;
        do{
            plass = ds.registerDock(1, 15);
        }while(plass);

        System.out.println("getNumberOfDocks(1): " + ds.getNumbersOfDocks(1));
        System.out.println("getDockingStationStatus(1) og (4)" + ds.getDockingStationStatus(1) + " " + ds.getDockingStationStatus(4));
        System.out.println("getPowerUsage(3): "+ ds.getPowerUsage(1));
        System.out.println("getNumberOfBikesCheckedIn(1): " + ds.getNumberOfBikesCheckedIn(1));
        ArrayList<Integer> bikes = ds.getBikeIDAtDockingStation(1);
        for(int i = 0;i < bikes.size(); i++){
            System.out.println("bikeID: " + bikes.get(i));
        }
        double[][] location = ds.getAllDockingStationLocation();
        for(int i = 0; i < location.length; i++){
            System.out.println();
            for(int column = 0; column < location[i].length; column++){
                System.out.print(" " + location[i][column]);
            }
        }
    }


}
