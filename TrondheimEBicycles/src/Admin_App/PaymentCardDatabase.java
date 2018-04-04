package Admin_App;

import DatabaseHandler.*;

import java.sql.*;
import java.util.Random;

public class PaymentCardDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();

    private int findRandomId(){
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

    //UNTESTED METHOD!
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
            RegNewCard.executeQuery("SET FOREIGN_KEY_CHECKS = 0"); //REMOVE THIS, ONLY FOR TESTING
            RegNewCard.setInt(1, findRandomId());
            RegNewCard.setInt(2, newPaymentCard.getCustId());
            RegNewCard.setInt(3, newPaymentCard.getBalance());
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

    public static void main(String[] args) {
        PaymentCardDatabase database = new PaymentCardDatabase();
        connection.getConnection();
        PaymentCard patricksCard = new PaymentCard(0, 0);
        database.regNewPaymentCard(patricksCard);
        cleaner.closeConnection(con);
    }
}