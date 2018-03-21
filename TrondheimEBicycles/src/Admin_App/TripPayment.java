package Admin_App;

import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TripPayment {
        private int tripID; // autoincrement
        private int customerID;
        private int bikeID;
        private LocalTime time_received;
        private LocalTime time_delivered;
        private int station_id_received;
        private int station_id_delivered;
        private int tripKM; //antall km turen varte, hentet fra gps simulator
        private int sumPayment;
        private static final int ANTALL_TIMER = 3;
        private static final int TIMEPRIS = 150;

        //Når sykkel hentes(før)
        public TripPayment(int bikeId, int customerID, int station_id_received) {
            this.bikeID = bikeId;
            this.customerID = customerID;
            this.station_id_received = station_id_received;
            this.time_received = LocalTime.now();
        }

        //Når sykkel leveres(etter)
        public TripPayment(int bikeID, int customerID, LocalTime time_received, int station_id_received, int station_id_delivered, int tripKM){
            this.bikeID = bikeID;
            this.customerID = customerID;
            this.time_received = time_received;
            this.time_delivered = LocalTime.now();
            this.station_id_received = station_id_received;
            this.station_id_delivered = station_id_delivered;
            this.tripKM = tripKM;
        }

    public int getTripID(){
            return tripID;
     }

    public LocalTime getTime_received() {
        return time_received;
    }

    public LocalTime getTime_delivered() {
        return time_delivered;
    }

    public int getTripKM() {
        return tripKM;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getBikeID() {
        return bikeID;
    }

    public int getStation_id_received() {
        return station_id_received;
    }

    public int getStation_id_delivered() {
        return station_id_delivered;
    }

    public int getSumPayment() {
        return sumPayment;
    }

    public int sumPayment() {
        return TIMEPRIS * ANTALL_TIMER; // hente timepris fra bicycle
    }

    public int getTripHours(){
            return ANTALL_TIMER;
    }


   /* public long findTripTime(){
        return long noOfHours = ChronoUnit.HOURS.between(time_received, LocalTime.now());
    }

    public boolean sendClaim() {
        if (noOfHours > tripHours) {
            return true;
        }else{
            return false;
        }
    }*/

    public static void main(String[] args){
        System.out.println("Hei");
        TripPayment tripPayment = new TripPayment(1, 2, 3);
        System.out.println(tripPayment.time_received);
        tripPayment = new TripPayment(tripPayment.bikeID,tripPayment.customerID, tripPayment.time_received, tripPayment.station_id_received, 4, 50);
        System.out.println(tripPayment.bikeID + " ," + tripPayment.customerID + " ," + tripPayment.time_received + " ," + tripPayment.time_delivered + " ," + tripPayment.station_id_received + " ," + tripPayment.station_id_delivered + " ," + tripPayment.tripKM);
    }

}