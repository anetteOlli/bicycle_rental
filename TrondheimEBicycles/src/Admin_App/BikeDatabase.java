package Admin_App;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import DatabaseHandler.*;

import javax.swing.*;

public class BikeDatabase {

    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();


    public ArrayList regBicycle(String make, String modell, String bicycleStatus, int nr) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date registration_date = new java.sql.Date(calendar.getTime().getTime());

        ArrayList<Bicycle> bicycles = new ArrayList<>();
        try {
            cleaner.setAutoCommit(con, false);

            String mySQL = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, ?, ?, ?);";
            PreparedStatement regBicycle = connection.createPreparedStatement(con, mySQL);
            regBicycle.setString(1, make);
            regBicycle.setString(2, modell);
            regBicycle.setDate(3, registration_date);
            regBicycle.setString(4, bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < nr; i++){
                regBicycle.addBatch();
            }


            regBicycle.executeBatch();


            /*ResultSet res;
            while(res.next()){
                Bicycle bike = new Bicycle(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("production_date"), res.getInt("dock_id"));
                bicycles.add(bike);
            }*/
            cleaner.commit(con);
            cleaner.setAutoCommit(con, true);
        } catch (SQLException e) {
            e.printStackTrace();
        } return bicycles;
    }




    public boolean UpdateBicycle (String bicycleStatus, int bicycle_id) {
        String updSetning = "UPDATE Bicycle SET bicycleStatus=? WHERE bicycle_id=?";
        PreparedStatement Update = connection.createPreparedStatement(con, updSetning);


        try {
            Update.setString(1, bicycleStatus);
            Update.setInt(2, bicycle_id);

            Update.executeUpdate();
        } catch (SQLException e) {
            return false;

        }
        return true;
    }

    public void UpdateKM (int bicycle_id){
        String KM = "UPDATE Bicycle SET totalKM = (SELECT SUM(tripKM) FROM TripPayment WHERE bicycle_id = ?) WHERE bicycle_id = ?;";
        PreparedStatement Mileage = connection.createPreparedStatement(con, KM);
        try {
        Mileage.setInt(1, bicycle_id);
        Mileage.setInt(2, bicycle_id);

        Mileage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int CheckTrip(int bicycle_id){
        int checked = -1;
        String checkT = "SELECT COUNT(trip_id) FROM TripPayment WHERE bicycle_id=?";
        PreparedStatement TripCheck = connection.createPreparedStatement(con, checkT);
        try{
            TripCheck.setInt(1, bicycle_id);
            ResultSet result = TripCheck.executeQuery();
            if (result.next()){
                checked = result.getInt(1);
            }
            if (cleaner.closeSentence(TripCheck) && (cleaner.closeResult(result)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checked;
    }

    public void RegRepairs (int bicycle_id){
        String RegNrReps = "Update Bicycle SET nr_of_repairs = (SELECT COUNT(repair_id) FROM Repair) WHERE bicycle_id = ?;";
        PreparedStatement Reps = connection.createPreparedStatement(con, RegNrReps);
        try {
            Reps.setInt(2, bicycle_id);
            Reps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void RegTrips (int bicycle_id){
        String trippe = "Update Bicycle SET trips=(SELECT COUNT(trip_id) FROM TripPayment) WHERE bicycle_id=?;";
        PreparedStatement trip = connection.createPreparedStatement(con, trippe);
        try{
            trip.setInt(1, bicycle_id);
            trip.executeUpdate();
        } catch (SQLException e) {
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
       cleaner.closeConnection(con);
    }
}