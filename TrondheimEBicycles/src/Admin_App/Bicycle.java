package Admin_App;

import java.util.Date;

public class Bicycle {
    public int bicycle_id;
    public String make;
    public String modell;
    public String bicycleStatus;
    public Date registration_date;
    public int dock_id;
    public double price;

    public Bicycle(int bicycle_id, String make, String modell, String bicycleStatus, Date registration_date, int dock_id, double price) {
        this.bicycle_id = bicycle_id;
        this.make = make;
        this.modell = modell;
        this.bicycleStatus = bicycleStatus;
        this.registration_date = registration_date;
        this.dock_id = dock_id;
        this.price = price;
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

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public int getDock_id() {
        return dock_id;
    }

    public void setDock_id(int dock_id) {
        this.dock_id = dock_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return  bicycle_id +
                " - " + modell;

    }
}
