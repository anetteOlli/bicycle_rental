package Admin_App;

import java.util.Random;

public class Customer {
    private int custId;
    private final String custFirstName;
    private final String custLastName;
    private int custPhone;
    private String custEmail;
    private String custPassword;
    private int custCardNumber;
    private String address;
    private static Random random = new Random();

    public Customer(int custId, int custCardNumber, String custFirstName, String custLastName, int custPhone, String custEmail, String custPassword) {
        if(custFirstName == null || custLastName == null || custEmail == null || custPassword == null){
            throw new IllegalArgumentException("All fields must be filled out!");
        }
        this.custFirstName = custFirstName.trim();
        this.custLastName = custLastName.trim();
        this.custPhone = custPhone;
        this.custEmail = custEmail.trim();
        this.custPassword = custPassword.trim();
        this.custId = custId;
        this.custCardNumber = custCardNumber;
        this.address = address;
    }

    public int getCustId(){
        return custId;
    }

    public int getCustCardNumber(){
        return custCardNumber;
    }

    public String getFirstName(){
        return custFirstName;
    }

    public String getLastName(){
        return custLastName;
    }

    public int getCustPhone(){
        return custPhone;
    }

    public String getCustEmail(){
        return custEmail;
    }

    public String getPassword(){
        return custPassword;
    }

    public void setCustCardNumber(int newNumber){
        custCardNumber = newNumber;
    }

    @Override
    public String toString(){
        return getFirstName() + " " + getLastName();
    }
}