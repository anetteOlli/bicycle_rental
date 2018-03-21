package Admin_App;
import java.sql.*;

public class BikeDatabase {
    private Connection forbindelse;
    private Statement setning;
    private String databaseDriver;
    private String databaseNavn;
    //Bicycle bicycle = new Bicycle();

        public BikeDatabase(String databaseDriver, String databaseNavn) {
            this.databaseDriver = databaseDriver;
            this.databaseNavn = databaseNavn;
            startForbindelse();
        }

        private void startForbindelse() {
            try {
                Class.forName(databaseDriver);
                forbindelse = DriverManager.getConnection(databaseNavn);
                setning = forbindelse.createStatement();
            } catch (ClassNotFoundException classEx) {
                System.out.println(classEx.getMessage());
            } catch (SQLException sqlEx) {
                System.out.println(sqlEx.getMessage());
            } catch (Exception e) {
                System.out.println(3);
            }
        }


        public void lukkForbindelse() {
            try {
                setning.close();
                forbindelse.close();
            } catch (SQLException sqlEx) {
                System.out.println("Error1");
            } catch (Exception e) {
                System.out.println("Error2");
            }
        }





    public boolean regNewBicycle(Bicycle newBicycle) {
        PreparedStatement RegNewBike;
        PreparedStatement RegNewBicycle;
        //Bicycle bicycle = new Bicycle;
            /*if(Bicycle_idExists(newBicycle.getBicycle_id())) {

                return false;
            } else {*/
        try {
            forbindelse.setAutoCommit(false);
            //            String insert2 = "INSERT INTO Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs) VALUES('" + newBicycle.getBicycle_id() + "','" + newBicycle.getDock_id() + "','" + newBicycle.getPowerlevel() + "','" + newBicycle.getMake() + "','" + newBicycle.getModel() + "','" + newBicycle.getProduction_date() + "','" + newBicycle.getBicycleStatus() + "','" + newBicycle.getTotalKM() + "','" + newBicycle.getTrips() + "','" + newBicycle.getNr_of_repairs() + "');";

            String insert1 = "INSERT INTO Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            String insert2 = "INSERT INTO Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs) VALUES('" + newBicycle.getBicycle_id() + "','" + newBicycle.getDock_id() + "','" + newBicycle.getPowerlevel() + "','" + newBicycle.getMake() + "','" + newBicycle.getModel() + "','" + newBicycle.getProduction_date() + "','" + newBicycle.getBicycleStatus() + "');";
            RegNewBike = forbindelse.prepareStatement(insert2);
            RegNewBicycle = forbindelse.prepareStatement(insert1);
            RegNewBicycle.setInt(1, newBicycle.bicycle_id);
            RegNewBicycle.setInt(2, newBicycle.dock_id);
            RegNewBicycle.setInt(3, newBicycle.powerlevel);
            RegNewBicycle.setString(4, newBicycle.make);
            RegNewBicycle.setString(5, newBicycle.model);
            RegNewBicycle.setInt(6, newBicycle.production_date);
            RegNewBicycle.setString(7, newBicycle.bicycleStatus);
            RegNewBicycle.setInt(8, newBicycle.totalKM);
            RegNewBicycle.setInt(9, newBicycle.trips);
            RegNewBicycle.setInt(10, newBicycle.nr_of_repairs);

            if (RegNewBicycle.executeUpdate() != 0) {

                forbindelse.commit();
                return true;
            } else {
                forbindelse.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                forbindelse.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //}
        return false;
        //}

           /* private boolean Bicycle_idExists (int bicycle_id){
                try {
                    ResultSet resultSet = setning.executeQuery("SELECT * FROM Bicycle WHERE bicycle_id = '" + bicycle_id + "';");
                    boolean eksisterer = false;
                    while (resultSet.next()) {
                        eksisterer = true;
                    }
                    if (eksisterer) { //redundant??
                        return true;
                    } else {
                        return false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } */
        }

        public boolean UpdateBicycle (BicycleUpdate newUpdate) {

            String updSetning = "UPDATE Bicycle SET Dock_id = '" + newUpdate.getDock_id() + "', powerlevel = '" + newUpdate.getPowerlevel() + "', bicycleStatus '" + newUpdate.getBicycleStatus() + "', totalKM = '" + newUpdate.getTotalKM() + "', trips = '" + newUpdate.getTrips() + "', nr_of_repairs = '" + newUpdate.getNr_of_repairs() + "' WHERE bicycle_id = '" + newUpdate.getBicycle_id() + "';";
            PreparedStatement Update;
            try {
                /*Update = forbindelse.prepareStatement(updSetning);
                Update.setInt(1, newUpdate.dock_id);
                Update.setInt(2, newUpdate.powerlevel);
                Update.setString(3, newUpdate.bicycleStatus);
                Update.setInt(4, newUpdate.totalKM);
                Update.setInt(5, newUpdate.trips);
                Update.setInt(6, newUpdate.nr_of_repairs);*/

                setning.executeUpdate(updSetning);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } return false;
        }


   public static void main(String[] args) {
        BikeDatabase database = new BikeDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/quannt?user=quannt&password=roBv7vFp");
        //Bicycle Quan = new Bicycle(981, 1, 10, "DROP TABLE Test;", "cargo", 11111111, "lost", 10, 2, 2);
        //database.regNewBicycle(Quan);
       BicycleUpdate Quan = new BicycleUpdate(981, 2, 50, "DBR", 50, 20, 5);
       database.UpdateBicycle(Quan);
       database.lukkForbindelse();
    }
}