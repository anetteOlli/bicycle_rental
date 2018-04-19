package Admin_App;

import DatabaseHandler.*;

import java.sql.*;
import java.util.Random;

public class PaymentCardDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();

    /*Finds a random 6-digit number that does not already exist in the PaymentCard
     * table. This random number is used as the PaymentCard ID*/
    public int findRandomId(){
        boolean exists = true;
        int randomNum = 0;
        while(exists){
            randomNum = random.nextInt(900000)+100000;
            try{
                String sentence = "SELECT * FROM PaymentCard WHERE cardNumber = '"+randomNum+"';";
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

    /*Creates a new Payment Card. This method is further called in the
     * regNewCustomer() method*/
    public boolean regNewPaymentCard(PaymentCard newPaymentCard){
        int active;
        try{
            cleaner.setAutoCommit(con, false);
            if(newPaymentCard.isActive()){
                active = 1;
            }
            else{
                active = 0;
            }
            String insertSql = "INSERT INTO PaymentCard(cardNumber, cust_id, balance, active_status) VALUES(?, ?, ?, ?);";
            PreparedStatement RegNewCard = connection.createPreparedStatement(con, insertSql);
            RegNewCard.executeQuery("SET FOREIGN_KEY_CHECKS = 0"); //Customer and paymentcard are both dependant on eachother, that's why foreign key checks are temporarily disabled when we insert new values.
            RegNewCard.setInt(1, newPaymentCard.getCardNumber());
            RegNewCard.setInt(2, newPaymentCard.getCustId());
            RegNewCard.setDouble(3, newPaymentCard.getBalance());
            RegNewCard.setInt(4, active);
            if(RegNewCard.executeUpdate() != 0){
                cleaner.commit(con);
                return true;
            } else{
                cleaner.rollback(con);
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                cleaner.setAutoCommit(con, true);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    /*We do not allow deletion of customer cards or customers from the database.
     * This method sets the payment card associated with a customer as inactive
     * An inactive paymentcard can not be used (can't add or deduct funds)*/
    public boolean setActiveStatus(int cust_id, boolean status) {
        try {
            int status1;
            if (status == true) {
                status1 = 1;
            } else {
                status1 = 0;
            }
            cleaner.setAutoCommit(con, false);
            String updateSql = "UPDATE PaymentCard SET active_status = ? WHERE cust_id = ?";
            PreparedStatement update = connection.createPreparedStatement(con, updateSql);
            update.setInt(1, status1);
            update.setInt(2, cust_id);
            if (update.executeUpdate() != 0) {
                cleaner.commit(con);
                return true;
            } else {
                cleaner.rollback(con);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }finally {
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /* Sets the balance of a payment card associated with a customer to a certain number */
    public boolean setBalance(int cust_id, int amount){
        try{
            cleaner.setAutoCommit(con, false);
            String updateSql = "UPDATE PaymentCard SET balance = ? WHERE cust_id = ? AND active_status = 1";
            PreparedStatement update = connection.createPreparedStatement(con, updateSql);
            update.setInt(1, amount);
            update.setInt(2, cust_id);
            if(update.executeUpdate() != 0){
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
        }finally {
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /* Adds funds to the balance of the payment card associated with a customer */
    public boolean addFunds(int cust_id, double amount){
        try{
            cleaner.setAutoCommit(con, false);
            String updateSql = "UPDATE PaymentCard SET balance = balance + ? WHERE cust_id = ? AND active_status = 1";
            PreparedStatement update = connection.createPreparedStatement(con, updateSql);
            update.setDouble(1, amount);
            update.setInt(2, cust_id);
            if(update.executeUpdate() != 0){
                cleaner.commit(con);
                return true;
            }else{
                cleaner.rollback(con);
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }finally {
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /* Deducts funds from the balance of a payment card associated with a customer */
    public boolean deductFunds (int cust_id, double amount){
        try{
            cleaner.setAutoCommit(con, false);
            String updateSql = "UPDATE PaymentCard SET balance = balance - ? WHERE cust_id = ? AND active_status = 1";
            PreparedStatement update = connection.createPreparedStatement(con, updateSql);
            update.setDouble(1, amount);
            update.setInt(2, cust_id);
            if(update.executeUpdate() != 0){
                cleaner.commit(con);
                return true;
            }else{
                cleaner.rollback(con);
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }finally {
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public double checkBalance(int cust_id){
        try{
            String sentence = "SELECT balance FROM PaymentCard WHERE cust_id = "+cust_id+" AND active_status = 1";
            PreparedStatement statement = connection.createPreparedStatement(con, sentence);
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getDouble("balance");
        }catch(SQLException e){
            e.getMessage();
            return -1;
        }
    }

    public static void main(String[] args) {
        PaymentCardDatabase database = new PaymentCardDatabase();
        connection.getConnection();
        //PaymentCard patricksCard = new PaymentCard(0, 0);
        //database.regNewPaymentCard(patricksCard);
        //database.setActiveStatus(5416, false);
        //database.setBalance(1274, 300);
        database.addFunds(9511, 600);
        System.out.println(database.checkBalance(9511));
        //database.deductFunds(1274, 200);
        cleaner.closeConnection(con);
    }
}