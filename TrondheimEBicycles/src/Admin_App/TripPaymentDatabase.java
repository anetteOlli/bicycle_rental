package Admin_App;
import java.sql.*;

import DatabaseHandler.*;

public class TripPaymentDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static final int ANTALL_TIMER = 1;
    private static final int TIMEPRIS = 150;//hentes fra sykkel

    public boolean startNewTrip(TripPayment newTripPayment){

        try{
            cleaner.setAutoCommit(con, false);

            String insert = "INSERT INTO TripPayment(trip_id, cust_id, bicycle_id, time_received, station_id_received) VALUES(DEFAULT, ?, ?, ?, ?);";
            PreparedStatement startNewTrip = connection.createPreparedStatement(con, insert);
            startNewTrip.setInt(1, newTripPayment.getCustomerID());
            startNewTrip.setInt(2, newTripPayment.getBikeID());
            startNewTrip.setTime(3, newTripPayment.getTime_received());
            startNewTrip.setInt(4, newTripPayment.getStation_id_received());


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
            String insert2 = "UPDATE TripPayment SET time_delivered=?, station_id_delivered=?, tripKM=?, sumPayment=? WHERE trip_id=?;";
            PreparedStatement endTrip = connection.createPreparedStatement(con, insert2);
            endTrip.setTime(1, newTripPayment.getTime_delivered());
            endTrip.setInt(2, newTripPayment.getStation_id_delivered());
            endTrip.setInt(3, newTripPayment.getTripKM());
            endTrip.setInt(4, newTripPayment.sumPayment());
            endTrip.setInt(5, newTripPayment.getTrip_id());




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

    public long getNoOfHours(TripPayment startTrip, ReTripPayment sluttTrip) {
        return sluttTrip.getTime_delivered().getHours() - startTrip.getTime_received().getHours();
    }

    public boolean isLate(TripPayment startTrip, ReTripPayment sluttTrip){
        if(getNoOfHours(startTrip, sluttTrip) > ANTALL_TIMER){
            return true;
        }
        else {
            return false;
        }
    }
    public int sumPayment(){
        return ANTALL_TIMER * TIMEPRIS;//skal hente pris fra bicycle
    }


    public int findTripID(TripPayment start){
        int trip_id = -1;
        String query = "SELECT trip_id FROM TripPayment WHERE cust_id =? AND bicycle_id = ? AND time_delivered IS NULL";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);

        try {
            sentence.setInt(1, start.getCustomerID());
            sentence.setInt(2, start.getBikeID());
            ResultSet rs = sentence.executeQuery();
            if (rs.next()) {
                trip_id = rs.getInt(1);
            }
            return trip_id;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return -1;
        }
        }

    public static void main(String[] args) {
        connection.getConnection();
        TripPaymentDatabase database = new TripPaymentDatabase();
        TripPayment start = new TripPayment(1, 123, 2);
        TripPayment start2 = new TripPayment(2, 123, 1);
        database.startNewTrip(start);
        database.startNewTrip(start2);
        ReTripPayment slutt2 = new ReTripPayment(database.findTripID(start2), 3, 30);
        ReTripPayment slutt = new ReTripPayment(database.findTripID(start), 1, 10);
        database.endTrip(slutt2);
        database.endTrip(slutt);
        System.out.println(database.getNoOfHours(start, slutt));

        System.out.println(database.isLate(start, slutt) + ", Pris: " + database.sumPayment());


        cleaner.closeConnection(con);

    }
}