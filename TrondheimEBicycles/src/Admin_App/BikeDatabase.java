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

            String insert1 = "INSERT INTO Bicycle(bicycle_id, powerlevel, make, model, production_date, bicycleStatus) VALUES(DEFAULT, 100, ?, ?, ?, ?);";
            PreparedStatement RegNewBicycle = connection.createPreparedStatement(con, insert1);
            RegNewBicycle.setString(1, newBicycle.make);
            RegNewBicycle.setString(2, newBicycle.model);
            RegNewBicycle.setInt(3, newBicycle.production_date);
            RegNewBicycle.setString(4, newBicycle.bicycleStatus);
            //RegNewBicycle.executeUpdate();
            for (int i = 0; i < 25; i++){
                    RegNewBicycle.addBatch();
                }

            int [] results = RegNewBicycle.executeBatch();
            cleaner.commit(con);
            cleaner.setAutoCommit(con, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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

            int [] results = RegNewFamily.executeBatch();
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

            int [] results = RegNewCargo.executeBatch();
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

            int [] results = RegNewRegular.executeBatch();
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

    public int UpdateKM (totalKM newKM){
        String KM = "UPDATE Bicycle SET totalKM=(Bicycle.totalKM + TripPayment.tripKM) WHERE (Bicycle.bicycle_id = TripPayment.bicycle_id)= 1 & trip_id = 1;";
        PreparedStatement Mileage = connection.createPreparedStatement(con, KM);
        try {
            Mileage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

   public static void main(String[] args) {
        BikeDatabase database = new BikeDatabase();
        connection.getConnection();

       //Bicycle test = new Bicycle(1, "DBS", "family", 19940815, "lost");
       Family test1 = new Family(1, "DBS", 19940815, "DBR");
       Cargo test2 = new Cargo(1, "DBS", 19940815, "DBR");
       Regular test3 = new Regular(1, "DBS", 19940815, "DBR");
       //database.regNewBicycle(test);
       database.regFamily(test1);
       database.regCargo(test2);
       database.regRegular(test3);
       //database.regFamily(Quan);
       //BicycleUpdate test4 = new BicycleUpdate(8, 2, 50, "DBR", 50, 20, 5);
       //database.UpdateBicycle(test4);

      cleaner.closeConnection(con);
    }
}