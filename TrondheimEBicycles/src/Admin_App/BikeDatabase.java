package Admin_App;
import java.sql.*;

public class BikeDatabase {
    private Connection forbindelse;
    private Statement setning;
    private String databaseDriver;
    private String databaseNavn;

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
            /*if(Bicycle_idExists(newBicycle.getBicycle_id())) {

                return false;
            } else {*/
        try {
            forbindelse.setAutoCommit(false);

            //String insert1 = "INSERT INTO Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            String insert2 = "INSERT INTO Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs) VALUES('" + newBicycle.getBicycle_id() + "','" + newBicycle.getDock_id() + "','" + newBicycle.getPowerlevel() + "','" + newBicycle.getMake() + "','" + newBicycle.getModel() + "','" + newBicycle.getProduction_date() + "','" + newBicycle.getBicycleStatus() + "','" + newBicycle.getTotalKM() + "','" + newBicycle.getTrips() + "','" + newBicycle.getNr_of_repairs() + "');";
            RegNewBike = forbindelse.prepareStatement(insert2);


            if (RegNewBike.executeUpdate() != 0) {

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

          /*  private boolean Bicycle_idExists ( int bicycle_id){
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
            }*/

          /*  public boolean UpdateBicycle (Bicycle newBicycle){
                String updSetning = "UPDATE Bicycle WHERE bicycle_id = Bicycle.bicycle_id;";
                try {
                    return (setning.executeUpdate(updSetning) != 0); //redundant?

                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }*/
    }

    public static void main(String[] args) {
        BikeDatabase database = new BikeDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/quannt?user=quannt&password=roBv7vFp");
        Bicycle Quan = new Bicycle(123, 1, 10, "DBS", "cargo", 11111111, "lost", 10, 2, 2);
        database.regNewBicycle(Quan);
        database.lukkForbindelse();
    }
}