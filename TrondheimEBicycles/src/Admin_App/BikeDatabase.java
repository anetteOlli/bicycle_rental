package Admin_App;

import java.sql.*;
import DatabaseHandler.*;

import javax.xml.bind.annotation.XmlType;

public class BikeDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    public boolean regNewBicycle(Bicycle newBicycle) {
        try {
            cleaner.setAutoCommit(con, false);

            String insert1 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, ?, ?, ?, ?, ?);";
            //String insert2 = "INSERT INTO Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs) VALUES('" + newBicycle.getBicycle_id() + "','" + newBicycle.getDock_id() + "','" + newBicycle.getPowerlevel() + "','" + newBicycle.getMake() + "','" + newBicycle.getModel() + "','" + newBicycle.getProduction_date() + "','" + newBicycle.getBicycleStatus() + "');";
            //RegNewBike = forbindelse.prepareStatement(insert2);
            PreparedStatement RegNewBicycle = connection.createPreparedStatement(con, insert1);
            //RegNewBicycle.setInt(1, newBicycle.bicycle_id);
            //RegNewBicycle.setInt(1, newBicycle.dock_id);
            RegNewBicycle.setInt(1, newBicycle.powerlevel);
            RegNewBicycle.setString(2, newBicycle.make);
            RegNewBicycle.setString(3, newBicycle.model);
            RegNewBicycle.setInt(4, newBicycle.production_date);
            RegNewBicycle.setString(5, newBicycle.bicycleStatus);
            //RegNewBicycle.executeUpdate();

            if (RegNewBicycle.executeUpdate() != 0) {

                cleaner.commit(con);
                return true;
            } else {
                cleaner.rollback(con);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleaner.setAutoCommit(con, true);
        }
        return false;
    }

    public boolean UpdateBicycle (BicycleUpdate newUpdate) {
        String updSetning1 = "UPDATE Bicycle SET dock_id = '" + newUpdate.getDock_id() + "', powerlevel = '" + newUpdate.getPowerlevel() + "', bicycleStatus = '" + newUpdate.getBicycleStatus() + "', totalKM = '" + newUpdate.getTotalKM() + "', trips = '" + newUpdate.getTrips() + "', nr_of_repairs = '" + newUpdate.getNr_of_repairs() + "' WHERE bicycle_id = '" + newUpdate.getBicycle_id() + "';";
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



   public static void main(String[] args) {
        BikeDatabase database = new BikeDatabase();
        connection.getConnection();
       Bicycle Quan = new Bicycle(1,  100, "DBS", "cargo", 11111111, "lost");
       database.regNewBicycle(Quan);
       /*BicycleUpdate Quan = new BicycleUpdate(8, 2, 50, "DBR", 50, 20, 5);
       database.UpdateBicycle(Quan);*/
      cleaner.closeConnection(con);
    }
}