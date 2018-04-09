package Admin_App;

public class Regular {
    public final String make;
    public final int production_date;
    public String bicycleStatus;

    public Regular(String make, int production_date, String bicycleStatus) {
        this.make = make;
        this.production_date = production_date;
        this.bicycleStatus = bicycleStatus;
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
