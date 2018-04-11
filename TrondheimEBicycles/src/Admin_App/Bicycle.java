package Admin_App;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Bicycle {
    public final String make;
    public String bicycleStatus;

    public Bicycle(String make, String bicycleStatus) {
        this.make = make;
        this.bicycleStatus = bicycleStatus;
    }


    public String getMake() {
        return make;
    }

    public String getBicycleStatus() {
        return bicycleStatus;
    }
}
