package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class BikeDatabase {
    Bikes bikes = new Bikes();
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    Statement stm;


    public boolean regFamily(Bicycle newBicycle, int nr) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date registration_date = new java.sql.Date(calendar.getTime().getTime());
        try {
            cleaner.setAutoCommit(con, false);

            String insert2 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, 'family', ?, ?);";
            PreparedStatement RegNewFamily = connection.createPreparedStatement(con, insert2);
            RegNewFamily.setString(1, newBicycle.make);
            RegNewFamily.setDate(2, registration_date);
            RegNewFamily.setString(3, newBicycle.bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < nr; i++){
                RegNewFamily.addBatch();

            }

            RegNewFamily.executeBatch();
            cleaner.commit(con);
            cleaner.setAutoCommit(con, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean regCargo(Bicycle newBicycle, int nr) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date registration_date = new java.sql.Date(calendar.getTime().getTime());
        try {
            cleaner.setAutoCommit(con, false);

            String insert3 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, 'cargo', ?, ?);";
            PreparedStatement RegNewCargo = connection.createPreparedStatement(con, insert3);
            RegNewCargo.setString(1, newBicycle.make);
            RegNewCargo.setDate(2, registration_date);
            RegNewCargo.setString(3, newBicycle.bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < nr; i++){
                RegNewCargo.addBatch();

            }

            RegNewCargo.executeBatch();
            cleaner.commit(con);
            cleaner.setAutoCommit(con, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean regRegular(Bicycle newBicycle, int nr) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date registration_date = new java.sql.Date(calendar.getTime().getTime());
        try {
            cleaner.setAutoCommit(con, false);

            String insert4 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, 'regular', ?, ?);";
            PreparedStatement RegNewRegular = connection.createPreparedStatement(con, insert4);
            RegNewRegular.setString(1, newBicycle.make);
            RegNewRegular.setDate(2, registration_date);
            RegNewRegular.setString(3, newBicycle.bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < nr; i++){
                RegNewRegular.addBatch();

            }

            RegNewRegular.executeBatch();
            cleaner.commit(con);
            cleaner.setAutoCommit(con, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean UpdateBicycle (int dock_id, String bicycleStatus, int bicycle_id) {
        String updSetning = "UPDATE Bicycle SET dock_id=?, bicycleStatus=? WHERE bicycle_id=?";
        PreparedStatement Update = connection.createPreparedStatement(con, updSetning);


        try {
            Update.setInt(1, dock_id);
            Update.setString(2, bicycleStatus);
            Update.setInt(3, bicycle_id);

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





    public static void main(String[] args) {
        BikeDatabase database = new BikeDatabase();
        Bikes bike = new Bikes();
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