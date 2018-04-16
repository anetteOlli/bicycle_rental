package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.*;
import java.util.Calendar;

public class BikeDatabase {
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

    public boolean UpdateBicycle (BicycleUpdate newUpdate) {
        String updSetning = "UPDATE Bicycle SET dock_id=?, powerlevel=?, bicycleStatus=?, totalKM=?, trips=?, nr_of_repairs=? WHERE bicycle_id=?";
        PreparedStatement Update = connection.createPreparedStatement(con, updSetning);


        try {
            Update.setInt(1, newUpdate.dock_id);
            Update.setInt(2, newUpdate.powerlevel);
            Update.setString(3, newUpdate.bicycleStatus);
            Update.setInt(4, newUpdate.totalKM);
            Update.setInt(5, newUpdate.trips);
            Update.setInt(6, newUpdate.nr_of_repairs);
            Update.setInt(7, newUpdate.bicycle_id);

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


       Bicycle test3 = new Bicycle("DBS", "not employed");
       //BicycleUpdate test5 = new BicycleUpdate(8, 2, 50, "DBR", 50, 20, 5);
       //Nr_of_repairs test6 = new Nr_of_repairs(1, 20);
       database.regFamily(test3, 11);
       //database.regCargo(test3);
       database.regRegular(test3, 10);
       //database.UpdateKM(1);
       //database.UpdateBicycle(test5);
       //database.RegRepairs(test6);
       //database.RegTrips(1);
       System.out.println("Nr of trips for bicycle " + database.CheckTrip(1));
       cleaner.closeConnection(con);
    }
}