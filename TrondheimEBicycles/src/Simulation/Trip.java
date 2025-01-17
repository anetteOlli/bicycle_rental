package Simulation;

import Admin_App.*;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Trip extends Thread{
    private int customer;
    private int fromDockstation;
    private int toDockStation;
    private int bike = -1;
    private double duration;
    private int time = 0;





    public Trip(int customer, int fromDockStation, int toDockStation, double duration){
        this.customer = customer;
        this.fromDockstation = fromDockStation;
        this.toDockStation = toDockStation;
        this.duration = duration;
    }
    public Trip(int customer, int fromDockstation, int toDockStation, double duration, int bikeID){
        this.customer = customer;
        this.fromDockstation = fromDockstation;
        this.toDockStation = toDockStation;
        this.duration = duration;
        this.bike = bikeID;
    }

    @Override
    public void run() {
        TripPaymentDatabase database = new TripPaymentDatabase();
        DockingStation dockingStation = new DockingStation();
        if(bike <= 0){
            bike = database.findAvailableBike(fromDockstation);
        }
        if(bike > 0){
            BikeDatabase bikeDatabase = new BikeDatabase();
            TripPayment start = new TripPayment(customer, bike, fromDockstation);
            double fromLatitude;
            double fromLongitude;
            double toLatitude;
            double toLongtitude;
            double deltaLatitude;
            double deltaLongitude;
            if(database.startNewTrip(start) != 0) {
                double[] fromLocation = dockingStation.getDockingStationLocation(fromDockstation);
                double[] toLocation = dockingStation.getDockingStationLocation(toDockStation);
                if (fromLocation != null && toLocation != null && fromDockstation != toDockStation) {
                    fromLatitude = fromLocation[0];
                    fromLongitude = fromLocation[1];
                    toLatitude = toLocation[0];
                    toLongtitude = toLocation[1];
                    deltaLatitude = (toLatitude - fromLatitude) / duration;
                    deltaLongitude = (toLongtitude - fromLongitude) / duration;


                    //starts on moving bikecycle around
                    while (time <= duration) {
                        bikeDatabase.setLocation(bike, fromLatitude, fromLongitude);
                        System.out.println("latitude: " + fromLatitude + ", longitude: " + fromLongitude + ", bikeID: " + bike);
                        fromLatitude += deltaLatitude;
                        fromLongitude += deltaLongitude;
                        time++;
                        try {
                            sleep(9000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //done cycling over to the other station
                    ReTripPayment end = new ReTripPayment(database.findTripIDfromCustomer(customer), toDockStation, 10);
                    database.endTrip(end);

                    //if the location is missing or we are starting and going to the same dockingstation,
                    //a default trip is set.
                } else {

                }
            }
        }

    }

    public static void main(String[] args) {
      // (new Trip(5025, 4,1,5,6)).start();
        //for(int i = 1; i < 340; i++){
        int i = 1;
            (new Trip(3376, 1,8,5, 4)).start();
            i++;
            (new Trip(1232, 2,7,5, 5)).start();
            i++;
            (new Trip(1580, 3,6,5, 8)).start();
           /* i++;
            (new Trip(3179, 4,5,5, i)).start();
            i++;
            (new Trip(3593, 5,4,5, i)).start();
            i++;
            (new Trip(5574, 6,3,5, i)).start();
            i++;
            (new Trip(5881, 7,2,5, i)).start();
            i++;
            (new Trip(6947, 8,1,5, i)).start();*/
       //}
    }

}
