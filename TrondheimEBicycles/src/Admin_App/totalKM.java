package Admin_App;

public class totalKM {
    public int bicycle_id;
    public int trip_id;

    public totalKM (int bicycle_id, int trip_id){
        this.bicycle_id = bicycle_id;
        this.trip_id = trip_id;
    }

    public int getBicycle_id(){
        return bicycle_id;
    }

    public int getTrip_id(){
        return trip_id;
    }

}
