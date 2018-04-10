package Admin_App;

import DatabaseHandler.*;
import java.sql.*;
import java.util.Random;


public class EmployeeDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();

    private int findRandomId(){
        boolean exists = true;
        int randomNum = 0;
        while(exists){
            randomNum = random.nextInt(9000)+1000;
            try{
                String sentence = "SELECT * FROM Employee WHERE employee_id = '"+randomNum+"';";
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

    public boolean regNewAdmin(Admin newAdmin){
        int hired = 0;
        try{
            cleaner.setAutoCommit(con, false);
            if(newAdmin.isHired()){
                hired = 1;
            }
            else{
                hired = 0;
            }
            String insertSql = "INSERT INTO Employee(employee_id, password, email, first_name, last_name, address, phone, isHired, isAdmin) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement RegNewAdmin = connection.createPreparedStatement(con, insertSql);
            RegNewAdmin.executeQuery("SET FOREIGN_KEY_CHECKS = 0");
            RegNewAdmin.setInt(1, findRandomId());
            RegNewAdmin.setString(2, newAdmin.getPassword());
            RegNewAdmin.setString(3, newAdmin.getEmail());
            RegNewAdmin.setString(4, newAdmin.getFirstName());
            RegNewAdmin.setString(5, newAdmin.getLastName());
            RegNewAdmin.setString(6, newAdmin.getAddress());
            RegNewAdmin.setInt(7, newAdmin.getPhone());
            RegNewAdmin.setInt(8, hired);
            RegNewAdmin.setInt(9, 1);
            if(RegNewAdmin.executeUpdate() != 0){
                cleaner.commit(con);
                return true;
            }
            else{
                cleaner.rollback(con);
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean regNewTechnician(Technician newTechnician){
        int hired = 0;
        try{
            cleaner.setAutoCommit(con, false);
            if(newTechnician.isHired()){
                hired = 1;
            }
            else{
                hired = 0;
            }
            String insertSql = "INSERT INTO Employee(employee_id, password, email, first_name, last_name, address, phone, isHired, isAdmin) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement RegNewTechnician = connection.createPreparedStatement(con, insertSql);
            RegNewTechnician.executeQuery("SET FOREIGN_KEY_CHECKS = 0");
            RegNewTechnician.setInt(1, findRandomId());
            RegNewTechnician.setString(2, newTechnician.getPassword());
            RegNewTechnician.setString(3, newTechnician.getEmail());
            RegNewTechnician.setString(4, newTechnician.getFirstName());
            RegNewTechnician.setString(5, newTechnician.getLastName());
            RegNewTechnician.setString(6, newTechnician.getAddress());
            RegNewTechnician.setInt(7, newTechnician.getPhone());
            RegNewTechnician.setInt(8, hired);
            RegNewTechnician.setInt(9, 0);
            if(RegNewTechnician.executeUpdate() != 0){
                cleaner.commit(con);
                return true;
            }
            else{
                cleaner.rollback(con);
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

   public boolean deleteEmployee(String email, String password){
        try{
            cleaner.setAutoCommit(con, false);
            String deleteSql = "DELETE FROM Employee WHERE email = ? AND password = ?";
            PreparedStatement deleteEmployee = connection.createPreparedStatement(con, deleteSql);
            deleteEmployee.setString(1, email);
            deleteEmployee.setString(2, password);
            if(deleteEmployee.executeUpdate() != 0){
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

    public static void main(String[] args){
        EmployeeDatabase database = new EmployeeDatabase();
        connection.getConnection();
        Admin patrick1 = new Admin(1, "Patrick", "Thorkildsen", 41146453, "Nedre Lunds Vei 3","Patrick.thorkildsen@gmail.com", "Password123");
        Technician patrick = new Technician(1, "Patrick", "Thorkildsen", 41146453, "Nedre Lunds Vei 3","Patrick.thorkildsen@gmail.com", "Password123");
        database.regNewAdmin(patrick1);
        database.regNewTechnician(patrick);
        database.deleteEmployee("patrick.thorkildsen@gmail.com", "Password123");
        cleaner.closeConnection(con);
    }
}
