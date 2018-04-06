package Admin_App;

public class Regular {
    public final int bicycle_id;
    public final String make;
    public final int production_date;
    public String bicycleStatus;

    public Regular(int bicycle_id, String make, int production_date, String bicycleStatus) {
        this.bicycle_id = bicycle_id;
        this.make = make;
        this.production_date = production_date;
        this.bicycleStatus = bicycleStatus;
    }



    public int getBicycle_id() {
        return bicycle_id;
    }

    public String getMake() {
        return make;
    }

    public int getProduction_date() {
        return production_date;
    }

    public String getBicycleStatus() {
        return bicycleStatus;
    }
}
