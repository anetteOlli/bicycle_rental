package Admin_App;
import java.sql.*;
import DatabaseHandler.*;

public class TripPaymentDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private Connection forbindelse;
    private Statement setning;
    private String databaseDriver;
    private String databaseNavn;

    /*public TripPaymentDatabase(String databaseDriver, String databaseNavn) {
        this.databaseDriver = databaseDriver;
        this.databaseNavn = databaseNavn;
        startForbindelse();*/

    /*private void startForbindelse() {
        try {
            Class.forName(databaseDriver);
            forbindelse = DriverManager.getConnection(databaseNavn);
            setning = forbindelse.createStatement();
        } catch (ClassNotFoundException classEx) {
            System.out.println("1" + classEx.getMessage());
        } catch (SQLException sqlEx) {
            System.out.println("2" + sqlEx.getMessage());
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
*/
    public boolean startNewTrip(TripPayment newTripPayment){

        try{
            cleaner.setAutoCommit(con, false);

            String insert = "INSERT INTO TripPayment(trip_id, cust_id, bicycle_id, time_received, station_id_received) VALUES(DEFAULT, ?, ?, ?, ?);";
           PreparedStatement startNewTrip = connection.createPreparedStatement(con, insert);
            startNewTrip.setInt(1, newTripPayment.getCustomerID());
            startNewTrip.setInt(2, newTripPayment.getBikeID());
            startNewTrip.setTime(3, newTripPayment.getTime_received());
            startNewTrip.setInt(4, newTripPayment.getStation_id_received());
            //startNewTrip.executeUpdate();


            if (startNewTrip.executeUpdate() != 0) {
                cleaner.commit(con);
                return true;
            } else {
                cleaner.rollback(con);
                return false;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean endTrip(ReTripPayment newTripPayment){
        try{
            cleaner.setAutoCommit(con, false);
           // public TripPayment(int bikeID, int customerID, LocalTime time_received, int station_id_received, int station_id_delivered, int tripKM)
            String insert2 = "UPDATE TripPayment SET time_delivered=?, station_id_delivered=?, tripKM=? WHERE trip_id=?;";
            PreparedStatement endTrip = connection.createPreparedStatement(con, insert2);
            endTrip.setTime(1, newTripPayment.getTime_delivered());
            endTrip.setInt(2, newTripPayment.getStation_id_delivered());
            endTrip.setInt(3, newTripPayment.getTripKM());
            endTrip.setInt(4, newTripPayment.getTrip_id());



            if (endTrip.executeUpdate() != 0) {
                cleaner.commit(con);
                return true;
            } else {
                cleaner.rollback(con);
                return false;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }

    }
    public static void main(String[] args) {
        connection.getConnection();
        TripPaymentDatabase database = new TripPaymentDatabase();
        //TripPayment start = new TripPayment(123, 1, 3);
       // database.startNewTrip(start);
        ReTripPayment slutt = new ReTripPayment(3, 2, 10);
        database.endTrip(slutt);
        cleaner.closeConnection(con);

    }
}