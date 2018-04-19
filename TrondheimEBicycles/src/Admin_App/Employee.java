package Admin_App;

abstract class Employee {
    private int employeeId;
    private final String firstName;
    private final String lastName;
    private int phone;
    private String address;
    private String email;
    private String password;
    private boolean isHired;
    public boolean isAdmin;

    public Employee(int employeeId, String firstName, String lastName, int phone, String address, String email, String password){
        if(firstName == null || lastName == null || email == null || password == null){
            throw new IllegalArgumentException("All fields must be filled out!");
        }
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        isHired = true;
        isAdmin = false;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHired() {
        return isHired;
    }

    public void setHired(boolean hired) {
        isHired = hired;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getAddress(){
        return address;
    }
}

