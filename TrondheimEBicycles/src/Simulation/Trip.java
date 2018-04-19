package Simulation;

import Admin_App.DockingStation;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Trip extends Thread{
    /*
    private int fromDockStation;
    private int toDockStation;
    private int bicycleID = -1;
    private int userID;
    private int time;
    private double fromLatitude = -1;
    private double fromLongitude = -1;
    private double toLatitude = -1;
    private double toLongitude = -1;
    private double deltaLongitude = -1;
    private double deltaLatitude= -1;
    private final double DURATION = 30.0;
    private DockingStation dock;


    public Trip(int userID, int fromDockStation, int toDockStation){
        this.time = 0;
        this.userID = userID;
        this.fromDockStation = fromDockStation;
        this.toDockStation = toDockStation;
        dock = new DockingStation();
        double[] fromLocation = dock.getDockingStationLocation(fromDockStation);
        double[] toLocation = dock.getDockingStationLocation(toDockStation);
        if(fromLocation != null && toLocation != null){
            fromLatitude = fromLocation[0];
            fromLongitude = fromLocation[1];
            toLatitude = toLocation[0];
            toLongitude = toLocation[1];
        }
        deltaLatitude = toLatitude - fromLatitude;
        deltaLongitude = toLongitude - fromLongitude;
        deltaLatitude = deltaLatitude/DURATION;
        deltaLongitude = deltaLongitude/DURATION;

    }

    @Override
    public void run() {
        DatabaseConnection connection = new DatabaseConnection();
        DatabaseCleanup cleaner = new DatabaseCleanup();
        Connection con  = connection.getConnection();
        try{
            con.setAutoCommit(false);
            ArrayList<Integer> bikeID = dock.getBikeIDAtDockingStation(fromDockStation);
            //check if it exists more than one bike at the dock
            if(bikeID.size() >0){
                bicycleID = bikeID.get(0);
                String sql = "UPDATE Bicycle SET bicycleStatus='not in dock', latitude=?, longitude=? WHERE bicycle_id=?";
            }else{
                System.out.println("Customer is raging, there's no available bike at dockingstation");
                con.rollback();
                con.setAutoCommit(true);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        super.run();
    }
    */
}
