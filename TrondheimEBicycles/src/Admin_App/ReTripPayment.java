package Admin_App;

import java.sql.Time;
import java.time.LocalTime;

public class ReTripPayment {
    private int trip_id;
    private LocalTime time_delivered;
    private int station_id_delivered;
    private int tripKM; //antall km turen varte, hentet fra gps simulator
    private static final int ANTALL_TIMER = 3;
    private static final int TIMEPRIS = 150;

    //Når sykkel leveres(etter)
    public ReTripPayment(int trip_id, int station_id_delivered, int tripKM){
        this.trip_id = trip_id;
        this.time_delivered = LocalTime.now();
        this.station_id_delivered = station_id_delivered;
        this.tripKM = tripKM;
    }
    public int getTrip_id(){
        return trip_id;
    }

    public Time getTime_delivered() {
        return java.sql.Time.valueOf(time_delivered);
    }

    public int getTripKM() {
        return tripKM;
    }


    public int getStation_id_delivered() {
        return station_id_delivered;
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