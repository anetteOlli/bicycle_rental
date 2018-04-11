package Admin_App;

import DatabaseHandler.*;

import java.sql.*;
import java.util.Random;

public class CustomerDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();

    /*Finds a random 4-digit number that does not already exist in the customer table.
    * This number is used as the customer ID*/
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

    /*Creates a new customer, and creates a payment card for that customer*/
    public boolean regNewCustomer(Customer newCustomer) {
        try {
            cleaner.setAutoCommit(con, false);
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
            RegNewCustomer.setString(7, PasswordStorage.createHash(newCustomer.getPassword()));
            if (RegNewCustomer.executeUpdate() != 0) {
                cleaner.commit(con);
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

    /*This method sets the card currently in use by the user as inactive,
    and creates a new, active card. The balance from the old card is also
    transferred to the new card.*/
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

    //Customers should not be deleted
  /*  public boolean deleteCustomer(String email, String password){
        try{
            cleaner.setAutoCommit(con, false);
            String deleteSqlCard = "DELETE PaymentCard FROM PaymentCard AS p INNER JOIN Customer AS c ON p.cust_id = c.cust_id WHERE c.email = ? AND c.password = ?";
            String deleteSql = "DELETE FROM Customer WHERE email = ? AND password = ?";
            PreparedStatement deleteCustomer = connection.createPreparedStatement(con, deleteSql);
            deleteCustomer.setString(1, email);
            deleteCustomer.setString(2, password);
            PreparedStatement deleteCard = connection.createPreparedStatement(con, deleteSqlCard);
            deleteCard.setString(1, email);
            deleteCard.setString(2, password);
            deleteCard.executeUpdate();
            if(deleteCustomer.executeUpdate() != 0){
                cleaner.commit(con);
                return true;
            }
            else{
                cleaner.rollback(con);
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }*/

    public static void main(String[] args) {
        CustomerDatabase database = new CustomerDatabase();
        connection.getConnection();
        Customer patrick = new Customer(1, 24, "Patrick", "Thorkildsen", 41146453, "patrick.thorkildsen@gmail.com", "Passord123");
        Customer quan = new Customer(1, 20, "Quan", "Mann", 47867632, "Quan@gmail.com", "Quanpassord123");
        database.regNewCustomer(patrick);
        database.regNewCustomer(quan);
        //database.getNewCard(8019);
        try {
            if (PasswordStorage.verifyPassword("Passord123", "sha1:64000:18:S9JgR8ArOq1rDpyc3AYRdp4RJIARdBV1:R6DD8NPMb/26QxD49RTUwiXB")) {
                System.out.println("Riktig hash!");
            }
            cleaner.closeConnection(con);
        }catch(PasswordStorage.CannotPerformOperationException e){
            System.out.println(e.getMessage());
        }catch(PasswordStorage.InvalidHashException a){
            System.out.println(a.getMessage());
        }
    }
}