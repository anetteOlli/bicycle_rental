package Admin_App;

import java.sql.*;
import java.util.Random;

public class EmployeeDatabase {
    private Connection connection;
    private Statement sentence;
    private String databaseDriver;
    private String databaseName;
    private static Random random = new Random();

    public EmployeeDatabase(String databaseDriver, String databaseNavn) {
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

    public boolean regNewAdmin(Admin newAdmin){
        PreparedStatement insertSentence;
        int hired = 0;
        try{
            connection.setAutoCommit(false);
            int adminID = findRandomId();
            if(newAdmin.isHired()){
                hired = 1;
            }
            else{
                hired = 0;
            }
            String insertSql = "INSERT INTO Employee(employee_id, password, email, first_name, last_name, address, phone, isHired, isAdmin) VALUES('" + adminID + "','" + newAdmin.getPassword() + "','" + newAdmin.getEmail() + "','" + newAdmin.getFirstName() + "','" + newAdmin.getLastName() + "','" + newAdmin.getAddress() + "','" + newAdmin.getPhone() + "','" + hired + "','" + 1 + "');";
            insertSentence = connection.prepareStatement(insertSql);
            if(insertSentence.executeUpdate() != 0){
                connection.commit();
                return true;
            }
            else{
                connection.rollback();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                connection.setAutoCommit(true);
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean regNewTechnician(Technician newTechnician){
        PreparedStatement insertSentence;
        int hired = 0;
        try{
            connection.setAutoCommit(false);
            int techID = findRandomId();
            if(newTechnician.isHired()){
                hired = 1;
            }
            else{
                hired = 0;
            }
            String insertSql = "INSERT INTO Employee(employee_id, password, email, first_name, last_name, address, phone, isHired, isAdmin) VALUES('" + techID + "','" + newTechnician.getPassword() + "','" + newTechnician.getEmail() + "','" + newTechnician.getFirstName() + "','" + newTechnician.getLastName() + "','" + newTechnician.getAddress() + "','" + newTechnician.getPhone() + "','" + hired + "','" + 0 + "');";
            insertSentence = connection.prepareStatement(insertSql);
            if(insertSentence.executeUpdate() != 0){
                connection.commit();
                return true;
            }
            else{
                connection.rollback();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                connection.setAutoCommit(true);
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public static void main(String[] args){
        EmployeeDatabase database = new EmployeeDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/patrickt?user=patrickt&password=6r1KVxDT");
        Admin patrick1 = new Admin(1, "Patrick", "Thorkildsen", 41146453, "Nedre Lunds Vei 3","Patrick.thorkildsen@gmail.com", "Password123");
        Technician patrick = new Technician(1, "Patrick", "Thorkildsen", 41146453, "Nedre Lunds Vei 3","Patrick.thorkildsen@gmail.com", "Password123");
        database.regNewAdmin(patrick1);
        database.regNewTechnician(patrick);
        database.closeConnection();
    }
}
