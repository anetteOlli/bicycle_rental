package Admin_App;

public class Technician extends Employee{
    public Technician(int employeeId, String firstName, String lastName, int phone, String address, String email, String password) {
        super(employeeId, firstName, lastName, phone, address, email, password);
    }

    @Override
    public String toString(){
        return getFirstName() + " " + getLastName();
    }
}
