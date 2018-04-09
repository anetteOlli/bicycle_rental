package Admin_App;

public class Nr_of_repairs {
    public int bicycle_id;
    public int nr_of_repairs;

    public Nr_of_repairs(int bicycle_id, int nr_of_repairs){
        this.bicycle_id = bicycle_id;
        this.nr_of_repairs = nr_of_repairs;
    }

    public int getBicycle_id() {
        return bicycle_id;
    }

    public int getNr_of_repairs() {
        return nr_of_repairs;
    }
}
