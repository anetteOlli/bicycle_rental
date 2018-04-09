package Admin_App;

import java.sql.*;
import DatabaseHandler.*;

public class BikeDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    Statement stm;


    public boolean regFamily(Family newFamily) {
        try {
            cleaner.setAutoCommit(con, false);

            String insert2 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, 'family', ?, ?);";
            PreparedStatement RegNewFamily = connection.createPreparedStatement(con, insert2);
            RegNewFamily.setString(1, newFamily.make);
            RegNewFamily.setInt(2, newFamily.production_date);
            RegNewFamily.setString(3, newFamily.bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < 25; i++){
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

    public boolean regCargo(Cargo newCargo) {
        try {
            cleaner.setAutoCommit(con, false);

            String insert3 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, 'cargo', ?, ?);";
            PreparedStatement RegNewCargo = connection.createPreparedStatement(con, insert3);
            RegNewCargo.setString(1, newCargo.make);
            RegNewCargo.setInt(2, newCargo.production_date);
            RegNewCargo.setString(3, newCargo.bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < 25; i++){
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

    public boolean regRegular(Regular newRegular) {
        try {
            cleaner.setAutoCommit(con, false);

            String insert4 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, 'regular', ?, ?);";
            PreparedStatement RegNewRegular = connection.createPreparedStatement(con, insert4);
            RegNewRegular.setString(1, newRegular.make);
            RegNewRegular.setInt(2, newRegular.production_date);
            RegNewRegular.setString(3, newRegular.bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < 25; i++){
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

    public void UpdateKM (totalKM newKM){
        String KM = "UPDATE Bicycle SET totalKM = (SELECT tripKM FROM TripPayment WHERE bicycle_id = ?) + totalKM WHERE (SELECT trip_id FROM TripPayment) = ? AND bicycle_id = ?;";
        PreparedStatement Mileage = connection.createPreparedStatement(con, KM);
        try {
        Mileage.setInt(1, newKM.bicycle_id);
        Mileage.setInt(2, newKM.trip_id);
        Mileage.setInt(3, newKM.bicycle_id);

        Mileage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CheckRep(int bicycle_id){
        String checkR = "SELECT COUNT bicycle_id FROM Repairs WHERE bicycle_id=?;";
        PreparedStatement RepCheck = connection.createPreparedStatement(con, checkR);
        try{
            RepCheck.setInt(1, bicycle_id);
            ResultSet RC = stm.executeQuery(checkR);
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

    public void RegRepairs (Nr_of_repairs repairs){
        String RegNrReps = "Update Bicycle SET nr_of_repairs = ? WHERE bicycle_id = ?;";
        PreparedStatement Reps = connection.createPreparedStatement(con, RegNrReps);
        try {
            Reps.setInt(1, repairs.nr_of_repairs);
            Reps.setInt(2, repairs.bicycle_id);
            Reps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void RegTrips (Trips trips){
        String trippe = "Update Bicycle SET trips=? WHERE bicycle_id=?;";
        PreparedStatement trip = connection.createPreparedStatement(con, trippe);
        try{
            trip.setInt(1, trips.trips);
            trip.setInt(2, trips.bicycle_id);
            trip.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


   public static void main(String[] args) {
        BikeDatabase database = new BikeDatabase();
        connection.getConnection();

       //Family test1 = new Family("DBS", 19940815, "DBR");
       //Cargo test2 = new Cargo("DBS", 19940815, "DBR");
       //Regular test3 = new Regular("DBS", 19940815, "DBR");
       //totalKM test4 = new totalKM(3,1);
       //BicycleUpdate test5 = new BicycleUpdate(8, 2, 50, "DBR", 50, 20, 5);
       //Nr_of_repairs test6 = new Nr_of_repairs(1, 20);
       //Trips test7 = new Trips(1,3);
       //database.regFamily(test1);
       //database.regCargo(test2);
       //database.regRegular(test3);
       //database.UpdateKM(test4);
       //database.UpdateBicycle(test5);
       //database.RegRepairs(test6);
       //database.RegTrips(test7);
       System.out.println("Nr of trips for bicycle " + database.CheckTrip(1));
       cleaner.closeConnection(con);
    }
}