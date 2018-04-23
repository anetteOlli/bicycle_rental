package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TripPaymentDatabase {
    private static final double ANTALL_TIMER = 1;
    private static final double DEPOSIT = 150;
    private boolean space;
    private boolean paid;
    private boolean sufficientBalance = false;
    PaymentCardDatabase paymentCardDatabase = new PaymentCardDatabase();

    //Method that starts a new trip
    public int startNewTrip(TripPayment newTripPayment) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        if(paymentCardDatabase.checkBalance(newTripPayment.getCustomerID())>sumPayment(newTripPayment)){
            sufficientBalance = true;
        }


        if (sufficientBalance) {
            if (findAvailableBike(newTripPayment.getStation_id_received()) > 0) {
                if (setDockAvailable(newTripPayment)) {
                    if (setBicycleStatusUnavailable(newTripPayment)) {
                        String insert = "INSERT INTO TripPayment(trip_id, cust_id, bicycle_id, time_received, station_id_received, sumPayment) VALUES(DEFAULT, ?, ?, ?, ?, ?);";
                        PreparedStatement startNewTrip = connection.createPreparedStatement(con, insert);
                        try {
                            cleaner.setAutoCommit(con, false);

                            startNewTrip.setInt(1, newTripPayment.getCustomerID());
                            startNewTrip.setInt(2, newTripPayment.getBikeID());
                            startNewTrip.setTimestamp(3, newTripPayment.getTime_received());
                            startNewTrip.setInt(4, newTripPayment.getStation_id_received());
                            startNewTrip.setDouble(5, sumPayment(newTripPayment));

                            if (startNewTrip.executeUpdate() != 0) {
                                paid = paymentCardDatabase.deductFunds(newTripPayment.getCustomerID(), sumPayment(newTripPayment) + DEPOSIT);
                                if (paid) {
                                    cleaner.commit(con);
                                    startNewTrip.close();
                                    con.close();
                                    return newTripPayment.getBikeID();
                                } else {
                                    startNewTrip.close();
                                    con.close();
                                    return -1;
                                }
                            } else {
                                cleaner.rollback(con);
                                startNewTrip.close();
                                con.close();
                                return -1;
                            }
                        } catch (SQLException e) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    // finds an available bie from the dockingstation
    public int findAvailableBike(int dockingstation) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        int bicycle_id = -2;
        String query = "SELECT bicycle_id FROM Bicycle b JOIN Dock d ON b.dock_id=d.dock_id JOIN DockingStation ds ON d.station_id=ds.station_id WHERE ds.station_id = ? AND b.bicycleStatus = 'in dock';";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);
        try {
            sentence.setInt(1, dockingstation);
            ResultSet rs = sentence.executeQuery();
            if (rs.next()) {
                bicycle_id = rs.getInt(1);
            }
            return bicycle_id;
        } catch (SQLException e) {
            return -1;
        }
    }

    // this would be used in the userapp, to give the user information about which dock they are supposed get the bicycle from
    public int findDockID(int bicycle_id) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        int dock_id = -1;
        String query = "SELECT dock_id FROM Bicycle WHERE bicycle_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);

        try {
            sentence.setInt(1, bicycle_id);
            ResultSet rs = sentence.executeQuery();
            if (rs.next()) {
                dock_id = rs.getInt(1);
            }
            return dock_id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    //changes isAvailable to 1 when a bicycle is taken out of the dock
    public boolean setDockAvailable(TripPayment newTripPayment) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String update = "UPDATE Dock SET isAvailable=1 WHERE dock_id=(SELECT dock_id FROM Bicycle WHERE bicycle_id=?);";
        PreparedStatement updateBicycleStatus = connection.createPreparedStatement(con, update);
        System.out.println("lagd preparestatement for setdockavailable");
        try {
            updateBicycleStatus.setInt(1, newTripPayment.getBikeID());
            System.out.println("setDockAvailable--- BikeID: " + newTripPayment.getBikeID());
            if (updateBicycleStatus.executeUpdate() != 0) {
                System.out.println("setdockavailable godkjent");
                return true;
            } else {
                System.out.println("setdockavailable rollback");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL-feil i setdockavailable" + e.getMessage());
            return false;
        }
    }

    //Sets the bicycleStatus to 'not in dock' when a trip starts
    public boolean setBicycleStatusUnavailable(TripPayment newTripPayment) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String update = "UPDATE Bicycle SET bicycleStatus='not in dock', dock_id = NULL WHERE bicycle_id=?;";
        PreparedStatement updateBicycleStatus = connection.createPreparedStatement(con, update);
        try {

            updateBicycleStatus.setInt(1, newTripPayment.getBikeID());
            if (updateBicycleStatus.executeUpdate() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    // method to end a trip
    public boolean endTrip(ReTripPayment newReTripPayment) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        space = checkSpace(newReTripPayment.getStation_id_delivered());
        if (space) {
            BikeDatabase bike = new BikeDatabase();
            String insert2 = "UPDATE TripPayment SET time_delivered=?, station_id_delivered=?, tripKM=? WHERE trip_id=?;";
            PreparedStatement endTrip = connection.createPreparedStatement(con, insert2);

            try {

                endTrip.setTimestamp(1, newReTripPayment.getTime_delivered());
                endTrip.setInt(2, newReTripPayment.getStation_id_delivered());
                endTrip.setInt(3, newReTripPayment.getTripKM());
                endTrip.setInt(4, newReTripPayment.getTrip_id());

                if (endTrip.executeUpdate() != 0) {
                    setBicycleStatusAvailable(newReTripPayment);
                    assignBicycletoDock(newReTripPayment);
                    setDockUnavailable(newReTripPayment);
                    bike.UpdateKM(getBikeID(newReTripPayment));
                    bike.RegTrips(getBikeID(newReTripPayment));
                    paymentCardDatabase.addFunds(getCustID(newReTripPayment), DEPOSIT);
                    endTrip.close();
                    con.close();
                    return true;
                } else {
                    endTrip.close();
                    con.close();
                    return false;
                }
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    // gets which bicycle is used in the trip, this is used in endTrip()
    public int getBikeID(ReTripPayment newReTripPayment){
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        int bikeID;
        String query = "SELECT bicycle_id FROM TripPayment WHERE trip_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);
        try{
            sentence.setInt(1, newReTripPayment.getTrip_id());
            ResultSet rs = sentence.executeQuery();
            if(rs.next()) {
                bikeID = rs.getInt(1);
                return bikeID;
            }
        }catch(SQLException e){
            e.getMessage();
        }
        return -1;
    }

    //gets the customer that is used in a trip, used in endTrip()
    public int getCustID(ReTripPayment newReTripPayment){
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        int custID;
        String query = "SELECT cust_id FROM TripPayment WHERE trip_id=?";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);
        try{
            sentence.setInt(1, newReTripPayment.getTrip_id());
            ResultSet rs = sentence.executeQuery();
            if(rs.next()) {
                custID = rs.getInt(1);
                return custID;
            }
        }catch(SQLException e){
            e.getMessage();
        }
        return -1;
    }

    //sets a dock_id to bicycle when the trip is over, used in endTrip()
    public boolean assignBicycletoDock(ReTripPayment newReTripPayment) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String update = "UPDATE Bicycle SET dock_id=(SELECT MIN(d.dock_id) FROM Dock d JOIN DockingStation ds ON d.station_id=ds.station_id WHERE ds.station_id=? AND d.isAvailable=1) WHERE bicycle_id=(SELECT bicycle_id FROM TripPayment WHERE trip_id=?);";
        PreparedStatement sentence = connection.createPreparedStatement(con, update);
        try {
            cleaner.setAutoCommit(con, false);
            sentence.setInt(1, newReTripPayment.getStation_id_delivered());
            sentence.setInt(2, newReTripPayment.getTrip_id());
            if (sentence.executeUpdate() != 0) {
                cleaner.commit(con);
                return true;
            } else {
                cleaner.rollback(con);
                return false;
            }

        } catch (SQLException e) {
            return false;

        }
    }

    //checks if there is enough space in the dockingstation, used in endTrip()
    public boolean checkSpace(int dockingstation) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        int plasser;
        String query = "SELECT COUNT(*) FROM Dock d JOIN DockingStation ds ON d.station_id=ds.station_id WHERE d.isAvailable=TRUE && d.station_id=?;";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);
        try {
            sentence.setInt(1, dockingstation);
            ResultSet rs = sentence.executeQuery();
            if (rs.next()) {
                plasser = rs.getInt(1);
                if (plasser > 0) {
                    return true;
                }
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    //sets bicycleStatus to 'in dock' when its placed in a dock
    public boolean setBicycleStatusAvailable(ReTripPayment newReTripPayment) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String update = "UPDATE Bicycle SET bicycleStatus='in dock' WHERE bicycle_id=(SELECT DISTINCT bicycle_id FROM TripPayment WHERE trip_id=?);";
        PreparedStatement updateBicycleStatus = connection.createPreparedStatement(con, update);
        try {

            updateBicycleStatus.setInt(1, newReTripPayment.getTrip_id());
            updateBicycleStatus.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    public long getNoOfHours(TripPayment startTrip, ReTripPayment sluttTrip) {
        return sluttTrip.getTime_delivered().getHours() - startTrip.getTime_received().getHours();
    }

    public boolean isLate(TripPayment startTrip, ReTripPayment sluttTrip) {
        if (getNoOfHours(startTrip, sluttTrip) > ANTALL_TIMER) {
            return true;
        } else {
            return false;
        }
    }

    // calculates the price of the trip
    public double sumPayment(TripPayment newTripPayment) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        double price = -1;
        String query = "SELECT price FROM Model WHERE model=(SELECT model FROM Bicycle WHERE bicycle_id=?)";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);
        try {
            sentence.setInt(1, newTripPayment.getBikeID());
            ResultSet rs = sentence.executeQuery();
            if (rs.next()) {
                price = rs.getInt(1);
            }
            return price * ANTALL_TIMER;
        } catch (SQLException e) {
            return -1;
        }
    }


    public int findTripID(TripPayment start) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
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
        } catch (SQLException e) {
            return -1;
        }
    }


    public int findTripIDfromCustomer(int cust_id) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        int trip_id = -1;
        String query = "SELECT trip_id FROM TripPayment WHERE cust_id =? AND time_delivered IS NULL";
        PreparedStatement sentence = connection.createPreparedStatement(con, query);

        try {
            sentence.setInt(1, cust_id);
            ResultSet rs = sentence.executeQuery();
            if (rs.next()) {
                trip_id = rs.getInt(1);
            }
            return trip_id;
        } catch (SQLException e) {
            return -1;
        }
    }

    public boolean setDockUnavailable(ReTripPayment newReTripPayment) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String update = "UPDATE Dock SET isAvailable=0 WHERE dock_id = (SELECT b.dock_id FROM Bicycle b JOIN TripPayment t ON b.bicycle_id=t.bicycle_id WHERE t.trip_id=?);";
        PreparedStatement updateDockStatus = connection.createPreparedStatement(con, update);
        try {
            cleaner.setAutoCommit(con, false);
            updateDockStatus.setInt(1, newReTripPayment.getTrip_id());
            if (updateDockStatus.executeUpdate() != 0) {
                cleaner.commit(con);
                return true;
            } else {
                cleaner.rollback(con);
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    //if there is some wrong data in the database, fixDocks() and fixBicycles() can clean it up a little bit, rather than having to do it manually
    public boolean fixDocks(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        try{
            for(int i=1; i<1000; i++) {
                String update = "UPDATE Dock SET isAvailable=1 WHERE dock_id=" + i+";";
                PreparedStatement sentence = connection.createPreparedStatement(con, update);
                sentence.executeUpdate();
            }
            for(int i=1; i<1000; i++) {
                String update = "UPDATE Dock SET isAvailable=0 WHERE dock_id=(SELECT dock_id FROM Bicycle WHERE bicycle_id = ? AND bicycleStatus='in dock')";
                PreparedStatement sentence = connection.createPreparedStatement(con, update);
                sentence.setInt(1, i);
                sentence.executeUpdate();
            }

        }catch (SQLException e){
            e.getMessage();
        }
        return false;
    }

    public boolean fixBicycles(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        try{
            for(int i=1; i<26; i++) {
                String update = "UPDATE Bicycle SET dock_id=NULL WHERE bicycleStatus != 'in dock' AND bicycle_id=?";
                PreparedStatement sentence = connection.createPreparedStatement(con, update);
                sentence.setInt(1, i);
                sentence.executeUpdate();
            }

        }catch (SQLException e){
            e.getMessage();
        }
        return false;
    }



    public static void main(String[] args) {
        TripPaymentDatabase database = new TripPaymentDatabase();
        int customer = 1331;
        int startStation = 1;
        int bike = database.findAvailableBike(startStation);
        int sluttStation = 2;
        //TripPayment start = new TripPayment(customer, bike, startStation);
        //database.startNewTrip(start);
        //if (database.startNewTrip(start)) {
            ReTripPayment slutt = new ReTripPayment(database.findTripIDfromCustomer(customer), sluttStation, 10);
            database.endTrip(slutt);
        //}
        //database.fixDocks();
        //database.fixBicycles();


    }
}