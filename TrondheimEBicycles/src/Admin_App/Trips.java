package Admin_App;

public class Trips {
    public int bicycle_id;
    public int trips;

    public Trips (int bicycle_id, int trips){
        this.bicycle_id=bicycle_id;
        this.trips=trips;
    }

    public int getBicycle_id() {
        return bicycle_id;
    }

    public int getTrips() {
        return trips;
    }
}
