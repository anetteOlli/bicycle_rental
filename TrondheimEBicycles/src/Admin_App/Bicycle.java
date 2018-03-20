package Admin_App;

public class Bicycle {
    private final int bicycle_id;
    private int dock_id;
    private int powerlevel;
    private final String make;
    private final String model;
    private final int production_date;
    private String bicycleStatus;
    private int totalKM;
    private int trips;
    private int nr_of_repairs;

    public Bicycle(int bicycle_id, int dock_id, int powerlevel, String make, String model, int production_date, String bicycleStatus, int totalKM, int trips, int nr_of_repairs) {
        this.bicycle_id = bicycle_id;
        this.dock_id = dock_id;
        this.powerlevel = powerlevel;
        this.make = make;
        this.model = model;
        this.production_date = production_date;
        this.bicycleStatus = bicycleStatus;
        this.totalKM = totalKM;
        this.nr_of_repairs = nr_of_repairs;
    }

    public int getBicycle_id() {
        return bicycle_id;
    }

    public int getDock_id() {
        return dock_id;
    }

    public int getPowerlevel() {
        return powerlevel;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getProduction_date() {
        return production_date;
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

   /* public String toString() {
        return "bicycle id: " + bicycle_id + "dock id: " + dock_id + "powerlevel: " + powerlevel
    }*/
}