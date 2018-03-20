package Admin_App;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
/**
 *
 * @author quan_
 */
public class CustomerDatabase {
    private Connection connection;
    private Statement sentence;
    private String databaseDriver;
    private String databaseName;

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

    public boolean regNewCustomer(Customer newCustomer) {
        PreparedStatement insertSentence;
        try {
            connection.setAutoCommit(false);
            String insertSql = "INSERT INTO Customer(cust_id, cardNumber, first_name, last_name, phone, email, password) VALUES('" + newCustomer.getCustId() + "','" + newCustomer.getCustCardNumber() + "','" + newCustomer.getFirstName() + "','" + newCustomer.getLastName() + "','" + newCustomer.getCustPhone() + "','" + newCustomer.getCustEmail() + "','" + newCustomer.getPassword() + "');";
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

    public static void main(String[] args) {
        CustomerDatabase database = new CustomerDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/patrickt?user=patrickt&password=6r1KVxDT");
        Customer patrick = new Customer(1, 24, "Patrick", "Thorkildsen", 41146453, "patrick.thorkildsen@gmail.com", "Passord123");
        database.regNewCustomer(patrick);
        database.closeConnection();
    }
}