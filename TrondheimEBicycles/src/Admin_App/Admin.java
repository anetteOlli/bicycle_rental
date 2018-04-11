package Admin_App;

public class Admin extends Employee {
    public Admin(int employeeId, String firstName, String lastName, int phone, String address, String email, String password) {
        super(employeeId, firstName, lastName, phone, address, email, password);
        isAdmin = true;
    }

    @Override
    public String toString() {
        return getFirstName() +" " + getLastName();
    }
}
