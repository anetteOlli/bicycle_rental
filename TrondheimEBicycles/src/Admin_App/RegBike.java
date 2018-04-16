package Admin_App;

public class RegBike {
    public int bicycle_id;
    public String make;
    public String modell;
    public String bicycleStatus;

    public RegBike(int bicycle_id, String make, String modell, String bicycleStatus) {
        this.bicycle_id = bicycle_id;
        this.make = make;
        this.modell = modell;
        this.bicycleStatus = bicycleStatus;
    }

    public int getBicycle_id() {
        return bicycle_id;
    }

    public void setBicycle_id(int bicycle_id) {
        this.bicycle_id = bicycle_id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public String getBicycleStatus() {
        return bicycleStatus;
    }

    public void setBicycleStatus(String bicycleStatus) {
        this.bicycleStatus = bicycleStatus;
    }

    @Override
    public String toString() {
        return bicycle_id +
                " | " + make +
                " | " + modell +
                " | " + bicycleStatus;
    }
}
