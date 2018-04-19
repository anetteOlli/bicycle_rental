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
    private double DURATION = 30;
    private int time;





    public Trip(int customer, int fromDockStation, int toDockStation, int time){
        this.customer = customer;
        this.fromDockstation = fromDockStation;
        this.toDockStation = toDockStation;
        this.time = time;
    }

    @Override
    public void run() {
        TripPaymentDatabase database = new TripPaymentDatabase();
        DockingStation dockingStation = new DockingStation();
        bike = database.findAvailableBike(fromDockstation);
        BikeDatabase bikeDatabase = new BikeDatabase();
        TripPayment start = new TripPayment(customer, bike, fromDockstation);
        double fromLatitude= -1;
        double fromLongitude = -1;
        double toLatitude = -1;
        double toLongtitude = -1;
        double deltaLatitude = -1;
        double deltaLongitude = -1;
        if(database.startNewTrip(start)){
            double[] fromLocation = dockingStation.getDockingStationLocation(fromDockstation);
            double[] toLocation = dockingStation.getDockingStationLocation(toDockStation);
            if(fromLocation != null && toLocation != null && fromDockstation != toDockStation){
                fromLatitude = fromLocation[0];
                fromLongitude = fromLocation[1];
                toLatitude = toLocation[0];
                toLongtitude = toLocation[1];
                deltaLatitude = (toLatitude - fromLatitude)/DURATION;
                deltaLongitude = (toLongtitude - fromLongitude)/DURATION;


                //starts on moving bikecycle around
                while (time < DURATION) {
                    bikeDatabase.setLocation(bike, fromLatitude, fromLongitude);
                    System.out.println("latitude: " + fromLatitude+ ", longitude: "+ fromLongitude+ ", bikeID: " + bike);
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
                ReTripPayment end = new ReTripPayment(database.findTripIDfromCustomer(customer),toDockStation, 10);

            //if the location is missing or we are starting and going to the same dockingstation,
                //a default trip is set.
            }else{

            }
        }

    }

    public static void main(String[] args) {
        (new Trip(5540, 2,5,10)).start();
    }

}
