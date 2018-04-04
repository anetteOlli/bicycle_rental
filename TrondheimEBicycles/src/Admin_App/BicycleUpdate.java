package Admin_App;

public class BicycleUpdate {
    public final int bicycle_id;
    public int dock_id;
    public int powerlevel;
    public String bicycleStatus;
    public int totalKM;
    public int trips;
    public int nr_of_repairs;


    public BicycleUpdate(int bicycle_id,int dock_id, int powerlevel, String bicycleStatus, int totalKM, int trips, int nr_of_repairs) {
        this.bicycle_id = bicycle_id;
        this.dock_id = dock_id;
        this.powerlevel = powerlevel;
        this.bicycleStatus = bicycleStatus;
        this.totalKM = totalKM;
        this.trips = trips;
        this.nr_of_repairs = nr_of_repairs;
    }

    public int getBicycle_id(){
        return bicycle_id;
    }

    public int getDock_id() {
        return dock_id;
    }

    public int getPowerlevel() {
        return powerlevel;
    }

    public String getBicycleStatus() {
        return bicycleStatus;
    }

    public int getTotalKM() {
        return totalKM;
    }

    public int getTrips() {
        return trips;
    }

    public int getNr_of_repairs() {
        return nr_of_repairs;
    }
}
