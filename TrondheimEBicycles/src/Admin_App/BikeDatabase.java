package Admin_App;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import DatabaseHandler.*;
import GUI.RegBicycle;

import javax.swing.*;

public class BikeDatabase {

    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();



    public void regBicycle(String make, String modell, String bicycleStatus, int price_of_bike, int nr) {
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
            regBicycle.setInt(5, price_of_bike);
            //RegNewBicycle.executeUpdate();
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




    public void RegBikeConfirm () {
        ArrayList<Bicycle> bicycles = new ArrayList<>();
        DefaultListModel<Bicycle> model = new DefaultListModel<>();
        try {
            RegBicycle regBicycle = new RegBicycle();
            String mySQL = "SELECT * FROM Bicycle ORDER BY (bicycle_id) DESC LIMIT ?";
            PreparedStatement SQL = connection.createPreparedStatement(con, mySQL);
            System.out.println("hei ho" + regBicycle.getCurrentValue());
            SQL.setInt(1, regBicycle.getCurrentValue());
            ResultSet res = SQL.executeQuery();
            while (res.next()) {
                Bicycle bike = new Bicycle(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("registration_date"), res.getInt("dock_id"));
                model.addElement(bike);
            }
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