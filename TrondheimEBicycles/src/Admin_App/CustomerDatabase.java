package Admin_App;

import DatabaseHandler.*;

import java.sql.*;
import java.util.Random;
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;
import java.util.*;

public class CustomerDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();

    /**
     * @return Returns a random 4-digit integer that does not exist as a CustomerID in the database. This integer is to be used as a customer ID
     */
    public int findRandomId(){
        boolean exists = true;
        int randomNum = 0;
        while(exists){
            randomNum = random.nextInt(9000)+1000;
            try{
                String sentence = "SELECT * FROM Customer WHERE cust_id = '"+randomNum+"';";
                PreparedStatement randomID = connection.createPreparedStatement(con, sentence);
                ResultSet res = randomID.executeQuery();
                exists=false;
                while(res.next()){
                    exists = true;
                }
                if(exists){
                    return -1;
                }
                else{
                    return randomNum;
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
                return -2;
            }
        }
        return -3;
    }


    /**
     * @return Returns a random 5-letter/digit password. To be used as a starting-password or when a password is reset.
     */
    public String generateRandomPassword(){
        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String password = "";
        String[] chars = new String[5];
        for(int i = 0; i < 5; i++){
            chars[i] = Character.toString(characters.charAt(random.nextInt(characters.length())));
            password += chars[i];
        }
        return password;
    }


    /**
     * @param newCustomer - A customer object to be registered in the database
     * @return Registers the Customer object in the database, and creates a payment card for the customer at the same time. Also sends the customer a temporary password on the registered email address
     */
    /*Creates a new customer, and creates a payment card for that customer*/
    public boolean regNewCustomer(Customer newCustomer) {
        try {
            cleaner.setAutoCommit(con, false);
            String password = generateRandomPassword();
            String insertSql = "INSERT INTO Customer(cust_id, cardNumber, first_name, last_name, phone, email, password) VALUES(?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement RegNewCustomer = connection.createPreparedStatement(con, insertSql);
            int randCustId = findRandomId();
            PaymentCardDatabase paymentdatabase = new PaymentCardDatabase();
            connection.getConnection();
            PaymentCard paymentcard = new PaymentCard(paymentdatabase.findRandomId(), randCustId);
            paymentdatabase.regNewPaymentCard(paymentcard);
            RegNewCustomer.executeQuery("SET FOREIGN_KEY_CHECKS = 0"); //Customer and paymentcard are both dependant on eachother, that's why foreign key checks are temporarily disabled when we insert new values.
            RegNewCustomer.setInt(1, randCustId);
            RegNewCustomer.setInt(2, paymentcard.getCardNumber());
            RegNewCustomer.setString(3, newCustomer.getFirstName());
            RegNewCustomer.setString(4, newCustomer.getLastName());
            RegNewCustomer.setInt(5, newCustomer.getCustPhone());
            RegNewCustomer.setString(6, newCustomer.getCustEmail());
            RegNewCustomer.setString(7, PasswordStorage.createHash(password));
            if (RegNewCustomer.executeUpdate() != 0) {
                cleaner.commit(con);
                SendMail send = new SendMail(newCustomer.getCustEmail(), "Welcome to Trondheim Bicycle Rental, "+newCustomer.getFirstName(), "Thank you for registering your account with Trondheim Bicycle Rental! \n \n Use your email and this temporary password to log in: "+password+". Please change your password the first time you log in. \n \n Your card number is: "+newCustomer.getCustCardNumber());
                return true;
            } else {
                cleaner.rollback(con);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                cleaner.setAutoCommit(con, true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    /**
     * @param cust_id - Id of the customer to generate a new paymentcard for
     * @return Sets the card currently in use by the user as inactive,
     *         and creates a new, active card. The balance from the old card is also
     *         transferred to the new card.
     */
    public boolean getNewCard(int cust_id){
        try{
            cleaner.setAutoCommit(con, false);

            //Deletes old card
            PaymentCardDatabase database = new PaymentCardDatabase();
            connection.getConnection();
            String query = "SELECT balance FROM PaymentCard WHERE cust_id = ? AND active_status = 1";
            PreparedStatement sentence = connection.createPreparedStatement(con, query);
            sentence.setInt(1, cust_id);
            ResultSet res = sentence.executeQuery();
            int funds = -2;
            if(res.next()){
                funds = res.getInt("balance");
            }
            System.out.println(funds);
            database.deductFunds(cust_id, funds); //Deducts all the funds from the card that is set inactive
            database.setActiveStatus(cust_id, false);
            System.out.println(funds);
            //Creates new card
            PaymentCard paymentcard = new PaymentCard(database.findRandomId(), cust_id);
            if(database.regNewPaymentCard(paymentcard)){
                cleaner.commit(con);
                database.setBalance(cust_id, funds); //Adds the funds back to the new card
                String sentence1 = "UPDATE Customer SET cardNumber = "+paymentcard.getCardNumber()+" WHERE cust_id = " + cust_id;
                PreparedStatement statement1 = connection.createPreparedStatement(con, sentence1);
                statement1.executeUpdate();
                return true;
            }
            else{
                cleaner.rollback(con);
                return false;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public static void main(String[] args) {
        CustomerDatabase database = new CustomerDatabase();
        connection.getConnection();
        Customer patrick = new Customer(1, 24, "Patrick", "Thorkildsen", 41146453, "patrick.thorkildsen@gmail.com", "Passord123");
        Customer quan = new Customer(1, 20, "Quan", "Mann", 47867632, "Quan@gmail.com", "Quanpassord123");
        database.regNewCustomer(patrick);
        database.regNewCustomer(quan);
        //SendMail send = new SendMail("patrick.thorkildsen@gmail.com", "TEstSubject", "Hello");
        //database.getNewCard(8019);
        try {
            if (PasswordStorage.verifyPassword("hvut2", "sha1:64000:18:faGrbsAEpP12xOUNTqsuukVV+hqJ3zxF:/nXAieiutaJSehpeHHo5nZEd")) {
                System.out.println("Riktig hash!");
            }else{
                System.out.println("Passordet og hashen matcher ikke");
            }
            cleaner.closeConnection(con);
        }catch(PasswordStorage.CannotPerformOperationException e){
            System.out.println(e.getMessage());
        }catch(PasswordStorage.InvalidHashException a){
            System.out.println(a.getMessage());
        }
    }
}