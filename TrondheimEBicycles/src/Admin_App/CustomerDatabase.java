package Admin_App;

import DatabaseHandler.*;

import java.sql.*;
import java.util.Random;

public class CustomerDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
   /* private Connection connection;
    private Statement sentence;
    private String databaseDriver;
    private String databaseName;*/
    private static Random random = new Random();

  /*  public CustomerDatabase(String databaseDriver, String databaseNavn) {
        this.databaseDriver = databaseDriver;
        this.databaseName = databaseNavn;
        startConnection();
    }*/

   /* private void startConnection() {
        try {
            Class.forName(databaseDriver);
            connection = DriverManager.getConnection(databaseName);
            sentence = connection.createStatement();
        } catch (ClassNotFoundException classEx) {
            System.out.println(classEx.getMessage());
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        } catch (Exception e) {
            System.out.println(3);
        }
    }*/


 /*   public void closeConnection() {
        try {
            sentence.close();
            connection.close();
        } catch (SQLException sqlEx) {
            System.out.println("Error1");
        } catch (Exception e) {
            System.out.println("Error2");
        }
    }*/

    private int findRandomId(){
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

    public boolean regNewCustomer(Customer newCustomer) {
        try {
            cleaner.setAutoCommit(con, false);
            String insertSql = "INSERT INTO Customer(cust_id, cardNumber, first_name, last_name, phone, email, password) VALUES(?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement RegNewCustomer = connection.createPreparedStatement(con, insertSql);
            RegNewCustomer.executeQuery("SET FOREIGN_KEY_CHECKS = 0");
            RegNewCustomer.setInt(1, findRandomId());
            RegNewCustomer.setInt(2, newCustomer.getCustCardNumber());
            RegNewCustomer.setString(3, newCustomer.getFirstName());
            RegNewCustomer.setString(4, newCustomer.getLastName());
            RegNewCustomer.setInt(5, newCustomer.getCustPhone());
            RegNewCustomer.setString(6, newCustomer.getCustEmail());
            RegNewCustomer.setString(7, newCustomer.getPassword());
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

    public boolean deleteCustomer(String email, String password){
        try{
            cleaner.setAutoCommit(con, false);
            String deleteSql = "DELETE FROM Customer WHERE email = ? AND password = ?";
            PreparedStatement deleteCustomer = connection.createPreparedStatement(con, deleteSql);
            deleteCustomer.setString(1, email);
            deleteCustomer.setString(2, password);
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
    }

    public static void main(String[] args) {
        CustomerDatabase database = new CustomerDatabase();
        connection.getConnection();
        Customer patrick = new Customer(1, 24, "Patrick", "Thorkildsen", 41146453, "patrick.thorkildsen@gmail.com", "Passord123");
        Customer quan = new Customer(1, 20, "Quan", "Mann", 47867632, "Quan@gmail.com", "Quanpassord123");
        database.regNewCustomer(patrick);
        database.regNewCustomer(quan);
        database.deleteCustomer("patrick.thorkildsen@gmail.com", "Passord123");
        cleaner.closeConnection(con);
    }
}