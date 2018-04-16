package Admin_App;

import java.sql.Time;
import java.time.LocalTime;

public class TripPayment {
        private int tripID; // autoincrement
        private int customerID;
        private int bikeID;
        private LocalTime time_received;
        private int station_id_received;
        private static final int ANTALL_TIMER = 3;
        private static final int TIMEPRIS = 150;

        //Når sykkel hentes(før)
        public TripPayment(int customerID, int bikeID, int station_id_received) {
            this.bikeID = bikeID;
            this.customerID = customerID;
            this.station_id_received = station_id_received;
            this.time_received = LocalTime.now();
        }


    public int getTripID(){
            return tripID;
     }

    public Time getTime_received() {
        return java.sql.Time.valueOf(time_received);
    }

    public Time getTime_delivered() {
        return java.sql.Time.valueOf(time_delivered);
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
    }

    public static void main(String[] args){
        System.out.println("Hei");
        TripPayment tripPayment = new TripPayment(1, 2, 3);
        System.out.println(tripPayment.time_received);
        tripPayment = new TripPayment(tripPayment.bikeID,tripPayment.customerID, tripPayment.time_received, tripPayment.station_id_received, 4, 50);
        System.out.println(tripPayment.bikeID + " ," + tripPayment.customerID + " ," + tripPayment.time_received + " ," + tripPayment.time_delivered + " ," + tripPayment.station_id_received + " ," + tripPayment.station_id_delivered + " ," + tripPayment.tripKM);
    }
*/
}