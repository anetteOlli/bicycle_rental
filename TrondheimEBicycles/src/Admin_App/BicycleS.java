package Admin_App;

import java.util.Date;

public class BicycleS {
    public int bicycle_id;
    public String make;
    public String modell;
    public String bicycleStatus;
    public Date registration_date;
    public int dock_id;
    public double total_km;
    public int total_rep;
    public int total_trip;
    public int powerlevel;
    public double price;
    public double priceBicycle;
    public int dockingstation;
    public String dsName;

    public BicycleS(int bicycle_id, String make, String modell, String bicycleStatus, Date registration_date, int dock_id, double total_km, int total_rep, int total_trip, int powerlevel, double price, double priceBicycle, int dockingstation, String dsName) {
        this.bicycle_id = bicycle_id;
        this.make = make;
        this.modell = modell;
        this.bicycleStatus = bicycleStatus;
        this.registration_date = registration_date;
        this.dock_id = dock_id;
        this.total_km = total_km;
        this.total_rep = total_rep;
        this.total_trip = total_trip;
        this.powerlevel = powerlevel;
        this.price = price;
        this.priceBicycle = priceBicycle;
        this.dockingstation = dockingstation;
        this.dsName = dsName;
    }

    public int getBicycle_id() {
        return bicycle_id;
    }

    public String getMake() {
        return make;
    }

    public String getModell() {
        return modell;
    }

    public String getBicycleStatus() {
        return bicycleStatus;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public int getDock_id() {
        return dock_id;
    }

    public double getTotal_km() {
        return total_km;
    }

    public int getTotal_rep() {
        return total_rep;
    }

    public int getTotal_trip() {
        return total_trip;
    }

    public int getPowerlevel() {
        return powerlevel;
    }

    public double getPrice() {
        return price;
    }

    public double getPriceBicycle() {
        return priceBicycle;
    }

    public int getDockingstation() {
        return dockingstation;
    }

    public String getDsName() {
        return dsName;
    }

    @Override
    public String toString() {
        return  bicycle_id + " - " + modell;
    }
}
