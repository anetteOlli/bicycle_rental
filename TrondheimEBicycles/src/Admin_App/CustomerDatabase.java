package Admin_App;

import java.sql.*;
import java.util.Random;

public class CustomerDatabase {
    private Connection connection;
    private Statement sentence;
    private String databaseDriver;
    private String databaseName;
    private static Random random = new Random();

    public CustomerDatabase(String databaseDriver, String databaseNavn) {
        this.databaseDriver = databaseDriver;
        this.databaseName = databaseNavn;
        startConnection();
    }

    private void startConnection() {
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
    }


    public void closeConnection() {
        try {
            sentence.close();
            connection.close();
        } catch (SQLException sqlEx) {
            System.out.println("Error1");
        } catch (Exception e) {
            System.out.println("Error2");
        }
    }

    public int findRandomId(){
        boolean exists = true;
        int randomNum = 0;
        while(exists){
            randomNum = random.nextInt(9000)+1000;
            try{
                ResultSet res = sentence.executeQuery("SELECT * FROM Customer WHERE cust_id = '"+randomNum+"';");
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
        PreparedStatement insertSentence;
        try {
            connection.setAutoCommit(false);
            sentence.executeQuery("SET FOREIGN_KEY_CHECKS = 0"); //DETTE SKAL FJERNES! kun til testing
            int custID = findRandomId();
            String insertSql = "INSERT INTO Customer(cust_id, cardNumber, first_name, last_name, phone, email, password) VALUES('" + custID + "','" + newCustomer.getCustCardNumber() + "','" + newCustomer.getFirstName() + "','" + newCustomer.getLastName() + "','" + newCustomer.getCustPhone() + "','" + newCustomer.getCustEmail() + "','" + newCustomer.getPassword() + "');";
            insertSentence = connection.prepareStatement(insertSql);
            if (insertSentence.executeUpdate() != 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean deleteCustomer(String email, String password){
        PreparedStatement deleteSentence;
        try{
            connection.setAutoCommit(false);
            String deleteSql = "DELETE FROM Customer WHERE email ='"+email+"' AND password='"+password+"'";
            deleteSentence = connection.prepareStatement(deleteSql);
            if(deleteSentence.executeUpdate() != 0){
                connection.commit();
                return true;
            }
            else{
                connection.rollback();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        CustomerDatabase database = new CustomerDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/patrickt?user=patrickt&password=6r1KVxDT");
        Customer patrick = new Customer(1, 24, "Patrick", "Thorkildsen", 41146453, "patrick.thorkildsen@gmail.com", "Passord123");
        Customer quan = new Customer(1, 20, "Quan", "Mann", 47867632, "Quan@gmail.com", "Quanpassord123");
        database.regNewCustomer(patrick);
        database.regNewCustomer(quan);
        database.deleteCustomer("patrick.thorkildsen@gmail.com", "Passord123");
        database.closeConnection();
    }
}