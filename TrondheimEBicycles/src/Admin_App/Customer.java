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
    private static Random random = new Random();

    public Customer(int custId, String custFirstName, String custLastName, int custPhone, String custEmail, String custPassword) {
        if(custFirstName == null || custLastName == null || custPhone == null || custEmail == null || custPassword == null){
            throw new IllegalArgumentException("Alle feltene må oppgis");
        }
        this.custFirstName = custFirstName.trim();
        this.custLastName = custLastName.trim();
        this.custPhone = custPhone;
        this.custEmail = custEmail.trim();
        this.custPassword = custPassword.trim();
        this.custId = createNewId();
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

    public int createNewId(){
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
            }catch (SQLException e)

        }

    }

    public Customer registerNewCustomer(String firstname, String lastname, int phone, String email, String password){
        PreparedStatement insertSentence;
        boolean ok = false;
        ResultSet res = null;
        do{
            try{

            }
        }while(!ok);


    }
}