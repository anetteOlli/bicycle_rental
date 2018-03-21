package Admin_App;

import java.time.LocalTime;

public class TripPayment {
        private int tripID;
        private LocalTime time_received;
        private LocalTime time_delivered;
        private int tripKM;
        private int tripHours;

        public TripPayment(LocalTime time_received, LocalTime time_delivered, int tripKM){
            this.time_received = time_received;
            this.time_delivered = time_delivered;
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

    public int sumPayment() {
        return bicycle.getPrice() * getTripHours();
    }

    public int getTripHours(){
            return tripHours;
    }

    public void setTripHours(int tripHours) {
        this.tripHours = tripHours;
    }
}
