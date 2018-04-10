package Admin_App;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Bicycle {
    Calendar calendar = Calendar.getInstance();
    java.sql.Date registration_date = new java.sql.Date(calendar.getTime().getTime());
    public final String make;
    public String bicycleStatus;

    public Bicycle(String make, String bicycleStatus) {
        this.make = make;
        this.bicycleStatus = bicycleStatus;
    }


    public String getMake() {
        return make;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public String getBicycleStatus() {
        return bicycleStatus;
    }
}
