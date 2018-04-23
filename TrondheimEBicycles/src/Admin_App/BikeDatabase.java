package Admin_App;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import DatabaseHandler.*;
import GUI.EditBike1;
import GUI.RegBicycle;

import javax.swing.*;

public class BikeDatabase {

    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();



    public void regBicycle(String make, String modell, String bicycleStatus, double price_of_bike, int nr) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date registration_date = new java.sql.Date(calendar.getTime().getTime());

        try {
            cleaner.setAutoCommit(con, false);

            String mySQL = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, registration_date, bicycleStatus, price_of_bike) VALUES(DEFAULT, 100, ?, ?, ?, ?,?);";
            PreparedStatement regBicycle = connection.createPreparedStatement(con, mySQL);
            regBicycle.setString(1, make);
            regBicycle.setString(2, modell);
            regBicycle.setDate(3, registration_date);
            regBicycle.setString(4, bicycleStatus);
            regBicycle.setDouble(5, price_of_bike);
            for (int i = 0; i < nr; i++){
                regBicycle.addBatch();
            }

            regBicycle.executeBatch();

            cleaner.commit(con);
            cleaner.setAutoCommit(con, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateKM (int nr){
        String KM = "UPDATE Bicycle SET totalKM = (SELECT SUM(tripKM) FROM TripPayment WHERE bicycle_id = ?) WHERE bicycle_id = ?;";
        PreparedStatement Mileage = connection.createPreparedStatement(con, KM);
        try {
        Mileage.setInt(1, nr);
        Mileage.setInt(2, nr);
        for (int i = 0; i < nr; i++) {
            Mileage.addBatch();
        }
        Mileage.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public ArrayList editBike(String sort) {
        try {

            String getInfo = "SELECT bicycle_id, make, model, bicycleStatus, registration_date, dock_id  FROM Bicycle ORDER BY ?;";
            PreparedStatement names = connection.createPreparedStatement(con, getInfo);
            names.setString(1, sort);
            ArrayList<BicycleE> bicycleE = new ArrayList<>();
            ResultSet res = names.executeQuery();
            while (res.next()) {
                BicycleE bike = new BicycleE(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("registration_date"), res.getInt("dock_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }




    public void RegRepairs (int nr){
        String RegNrReps = "Update Bicycle SET nr_of_repairs = (SELECT COUNT(repair_id) FROM Repair) WHERE bicycle_id = ?;";
        PreparedStatement Reps = connection.createPreparedStatement(con, RegNrReps);
        try {
            Reps.setInt(1, nr);
            for (int i = 0; i < nr; i++) {
                Reps.addBatch();
            }
            Reps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void RegTrips (int nr) {
        String trippe = "Update Bicycle SET trips=(SELECT COUNT(trip_id) FROM TripPayment) WHERE bicycle_id=?;";
        PreparedStatement trip = connection.createPreparedStatement(con, trippe);
        try {
            trip.setInt(1, nr);
            for (int i = 0; i < nr; i++) {
                trip.addBatch();
            }
            } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * method collects all active Bicycles and places then into a table
     * @return double[]][], double[row][0] = bicycleID as a double
     * double[row][1] = latitude, double[row][2] = longitude
     */
    public double[][] getAllBicycleLovations(){
        //starts off with a table with space for 5 bicycles.
        //we know that horizontally we will only have 3 columns
        double[][] result = new double[5][3];
        int noDockStation = 0;
        String sql = "SELECT bicycle_id, latitude, longitude FROM Bicycle";
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        PreparedStatement sentence = connection.createPreparedStatement(con, sql);
        try{
            ResultSet rs = sentence.executeQuery();
            while(rs.next()) {
                if (noDockStation < result.length) {
                    result[noDockStation][0] = (double) rs.getInt(1);
                    result[noDockStation][1] = rs.getDouble(2);
                    result[noDockStation][2] = rs.getDouble(3);
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
                    result[noDockStation][0] = (double) rs.getInt(1);
                    result[noDockStation][1] = rs.getDouble(2);
                    result[noDockStation][2] = rs.getDouble(3);
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
            if(cleaner.closeResult(rs) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                return result;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public void changePrice (int price, String model){
        String sql = "UPDATE Model SET price=? WHERE model=?;";
        PreparedStatement trip = connection.createPreparedStatement(con, sql);
        try{
            trip.setInt(1, price);
            trip.setString(2, model);
            trip.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * method collects the latitude and longitude of a specific bike
     * @param bikeID
     * @return a double[], index 0 is latitude. Index 1 is longitude
     * length of double[] == 1
     */
    public double[] getLocationSpecificBike(int bikeID){
        double[] result = new double[2];
        String sql = "SELECT latitude, longitude FROM Bicycle WHERE bicycle_id=?";
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        PreparedStatement sentence = connection.createPreparedStatement(con, sql);
        try{
            sentence.setInt(1,bikeID);
            ResultSet resultSet = sentence.executeQuery();
            if(resultSet.next()){
                result[0] = resultSet.getDouble(1);
                result[1] = resultSet.getDouble(2);
            }
            if(cleaner.closeResult(resultSet) && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)){
                return  result;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * method to change a bike's location
     * @param bikeID the id of a given bike
     * @param latitude latitude coordinates as a double
     * @param longitude longitude coordinates as a double
     */
    public void setLocation(int bikeID, double latitude, double longitude){
        String sql = "UPDATE Bicycle SET longitude=?, latitude=? WHERE bicycle_id=?";
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con = connection.getConnection();
        try{
            PreparedStatement sentence = connection.createPreparedStatement(con, sql);
            sentence.setDouble(1, latitude);
            sentence.setDouble(2, longitude);
            sentence.setInt(3, bikeID);
            sentence.executeUpdate();
            sentence.close();
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }





    public static void main(String[] args) {
        BikeDatabase database = new BikeDatabase();

        connection.getConnection();


       //Bicycle test3 = new Bicycle("DBS", "not employed");
       //BicycleUpdate test5 = new BicycleUpdate(8, 2, 50, "DBR", 50, 20, 5);
       //Nr_of_repairs test6 = new Nr_of_repairs(1, 20);
       //database.regFamily(test3, 11);
       //database.regCargo(test3);
       //database.regRegular(test3, 10);
       //database.UpdateKM(1);
       //database.UpdateBicycle(1, "in dock", 1);
       //database.RegRepairs(test6);
       //database.RegTrips(1);
       //System.out.println("Nr of trips for bicycle " + database.CheckTrip(1));
       //cleaner.closeConnection(con);
    }
}