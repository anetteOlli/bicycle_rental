package Admin_App;

import java.sql.*;
import java.util.Random;
import DatabaseHandler.*;

public class Customer {
    private int custId;
    private final String custFirstName;
    private final String custLastName;
    private int custPhone;
    private String custEmail;
    private String custPassword;
    private int custCardNumber;
    private static Random random = new Random();

    public Customer(int custId, int custCardNumber, String custFirstName, String custLastName, int custPhone, String custEmail, String custPassword) {
        if(custFirstName == null || custLastName == null || custEmail == null || custPassword == null){
            throw new IllegalArgumentException("Alle feltene må oppgis");
        }
        this.custFirstName = custFirstName.trim();
        this.custLastName = custLastName.trim();
        this.custPhone = custPhone;
        this.custEmail = custEmail.trim();
        this.custPassword = custPassword.trim();
        this.custId = custId;
        this.custCardNumber = custCardNumber;
    }

    public String getName(){
        return custFirstName + " " + custLastName;
    }

    public int getCustPhone(){
        return custPhone;
    }

    public String getCustEmail(){
        return custEmail;
    }

   /* public int createNewId(){
        boolean numberfound = false;
        int randomnumber = 0;
        PreparedStatement selectSentence;
        while(!numberfound){
            try {
                randomnumber = random.nextInt(89999) + 10000;
                String selectSql = "SELECT * FROM Customer WHERE cust_id = " + randomnumber;
                selectSentence = Connection.prepareStatement(selectSql);
                ResultSet res = null;
                res = selectSentence.executeQuery();
                if(!res.isBeforeFirst()){
                    numberfound = true;
                    return randomnumber;
                }
            }catch (SQLException e){}

        }

    }*/

    //Customer ID skal finnes av en metode (som ikke er klar enda), tester om denne metoden funker ved å legge inn customerId manuelt
    public Customer registerNewCustomer(int customerid, int cardNumber, String firstname, String lastname, int phone, String email, String password) {
        PreparedStatement insertSentence = null;
        boolean ok = false;
        ResultSet res = null;
        DatabaseCleanup databasecleanup = new DatabaseCleanup();
        Connection connection = null;
        do {
            try {
                String insertSql = "INSERT INTO Customer values(?,?,?,?,?,?,?)";
                insertSentence = connection.prepareStatement(insertSql);
                insertSentence.setInt(1, customerid);
                insertSentence.setInt(2, cardNumber);
                insertSentence.setString(3, firstname);
                insertSentence.setString(4, lastname);
                insertSentence.setInt(5, phone);
                insertSentence.setString(6, email);
                insertSentence.setString(7, password);
                System.out.println(insertSentence);
                insertSentence.executeUpdate();
                ok = true;
            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                databasecleanup.closeResult(res);
                databasecleanup.closeSentence(insertSentence);
            }
        } while (!ok);
        return new Customer(customerid, cardNumber, firstname, lastname, phone, email, password);
    }
    public static void main(String[] args){
        Customer customer = null;
        customer.registerNewCustomer(1, 12, "Patrick", "Thorkildsen", 4146453, "patrick.thorkildsen@gmail.com", "passord123");
    }
}