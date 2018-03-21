package Admin_App;
import java.sql.*;


public class TripPaymentDatabase {
    private Connection forbindelse;
    private Statement setning;
    private String databaseDriver;
    private String databaseNavn;

    public TripPaymentDatabase(String databaseDriver, String databaseNavn) {
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

    public boolean startNewTrip(TripPayment newTripPayment){
        PreparedStatement startNewTrip;

        try{
            forbindelse.setAutoCommit(false);

            String insert = "INSERT INTO TripPayment(trip_id, cust_id, bicycle_id, time_received, station_id_received) VALUES('" + newTripPayment.getBikeID() + "','" + newTripPayment.getCustomerID() + "','" + newTripPayment.getTime_received() + "','" + newTripPayment.getStation_id_received() + "');";
            startNewTrip = forbindelse.prepareStatement(insert);
        }

    }

}
