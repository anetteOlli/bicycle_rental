package Admin_App;

import DatabaseHandler.*;
import java.sql.*;
import java.util.Random;

import static javax.swing.JOptionPane.showMessageDialog;


public class EmployeeDatabase {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();

    public int findRandomId(){
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
        CustomerDatabase database = new CustomerDatabase();
        String password = database.generateRandomPassword();
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
            RegNewAdmin.setString(2, PasswordStorage.createHash(password));
            RegNewAdmin.setString(3, newAdmin.getEmail());
            RegNewAdmin.setString(4, newAdmin.getFirstName());
            RegNewAdmin.setString(5, newAdmin.getLastName());
            RegNewAdmin.setString(6, newAdmin.getAddress());
            RegNewAdmin.setInt(7, newAdmin.getPhone());
            RegNewAdmin.setInt(8, hired);
            RegNewAdmin.setInt(9, 1);
            //SendMail send = new SendMail(newAdmin.getEmail(), "Welcome to Trondheim Bicycle Rental, "+newAdmin.getFirstName(), "Thank you for registering your administrator account with Trondheim Bicycle Rental! \n \n Use your email and this temporary password to log in: "+password+". Please change your password the first time you log in");
            if(RegNewAdmin.executeUpdate() != 0){
                cleaner.commit(con);
                SendMail send = new SendMail(newAdmin.getEmail(), "Welcome to Trondheim Bicycle Rental, "+newAdmin.getFirstName(), "Thank you for registering your administrator account with Trondheim Bicycle Rental! \n \n Use your email and this temporary password to log in: "+password+". Please change your password the first time you log in");
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
        CustomerDatabase database = new CustomerDatabase();
        String password = database.generateRandomPassword();
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
            RegNewTechnician.setString(2, PasswordStorage.createHash(password));
            RegNewTechnician.setString(3, newTechnician.getEmail());
            RegNewTechnician.setString(4, newTechnician.getFirstName());
            RegNewTechnician.setString(5, newTechnician.getLastName());
            RegNewTechnician.setString(6, newTechnician.getAddress());
            RegNewTechnician.setInt(7, newTechnician.getPhone());
            RegNewTechnician.setInt(8, hired);
            RegNewTechnician.setInt(9, 0);
            //SendMail send = new SendMail(newTechnician.getEmail(), "Welcome to Trondheim Bicycle Rental, "+newTechnician.getFirstName(), "Thank you for registering your employee account with Trondheim Bicycle Rental! \n \n Use your email and this temporary password to log in: "+password+". Please change your password the first time you log in");
            if(RegNewTechnician.executeUpdate() != 0){
                cleaner.commit(con);
                SendMail send = new SendMail(newTechnician.getEmail(), "Welcome to Trondheim Bicycle Rental, "+newTechnician.getFirstName(), "Thank you for registering your employee account with Trondheim Bicycle Rental! \n \n Use your email and this temporary password to log in: "+password+". Please change your password the first time you log in");
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

    public boolean resetPassword(String email, String email2){
        try{
            cleaner.setAutoCommit(con, false);
            if(email.equals(email2)){
                PasswordStorage storage = new PasswordStorage();
                CustomerDatabase database = new CustomerDatabase();
                String newPassword = database.generateRandomPassword();
                String sentence = "UPDATE Employee SET password = '"+storage.createHash(newPassword)+"' WHERE email = '"+email+"'";
                PreparedStatement statement = connection.createPreparedStatement(con, sentence);
                if(statement.executeUpdate() != 0){
                    cleaner.commit(con);
                    SendMail send = new SendMail(email, "Password reset requested", "You just requested a password reset for your employee account. Here is your new password: "+newPassword+"\n \n Please remember to change your password the first time you log in");
                    showMessageDialog(null, "Successfully reset password! You will receive an email with your new temporary password");
                    return true;
                }
                else{
                    cleaner.rollback(con);
                    return false;
                }
            }else{
                showMessageDialog(null, "Email does not match, please try again");
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }catch(PasswordStorage.CannotPerformOperationException a){
            System.out.println(a.getMessage());
            return false;
        }finally {
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    public boolean changePassword(String email, String oldPass, String newPass, String newPass2){
        try{
            PasswordStorage storage = new PasswordStorage();
            cleaner.setAutoCommit(con, false);
            String sentence = "SELECT * FROM Employee WHERE email = ?";
            PreparedStatement statement = connection.createPreparedStatement(con, sentence);
            statement.setString(1, email);
            ResultSet res = statement.executeQuery();
            if(res.next()){
                if(storage.verifyPassword(oldPass, res.getString("password"))){
                    if(newPass.equals(newPass2)){
                        if(newPass.length() > 7){
                            if(checkString(newPass)){
                                String sentence2 = "UPDATE Employee SET password = '"+storage.createHash(newPass)+"' WHERE email = '"+email+"'";
                                PreparedStatement statement1 = connection.createPreparedStatement(con, sentence2);
                                if(statement1.executeUpdate()!= 0){
                                    cleaner.commit(con);
                                    SendMail send = new SendMail(email, "Password changed", "The password for your employee account was successfully changed");
                                    showMessageDialog(null, "Password changed successfully!");
                                    return true;
                                }else{
                                    cleaner.rollback(con);
                                    return false;
                                }
                            }else{
                                showMessageDialog(null, "New password must contain at least one lowercase letter, one uppercase letter, and one number");
                                return false;
                            }
                        }else{
                            showMessageDialog(null, "Password must be at least 7 characters long");
                            return false;
                        }
                    }else{
                        showMessageDialog(null, "New password must match in both fields");
                        return false;
                    }
                }else{
                    showMessageDialog(null, "Email and password do not match!");
                    return false;
                }
            }else{
                showMessageDialog(null, "No account registered with that email!");
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }catch(PasswordStorage.CannotPerformOperationException a){
            System.out.println(a.getMessage());
            return false;
        }catch(PasswordStorage.InvalidHashException h){
            System.out.println(h.getMessage());
            return false;
        }finally {
            try{
                cleaner.setAutoCommit(con, true);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
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
       //Admin patrick1 = new Admin(1, "Admin", "Admin", 12345678, "Adminvei1","admin@admin.com", "admin");
        //Technician patrick = new Technician(1, "Technician", "Technician", 12345678, "Technicianvei1","technician@technician.com", "technician");
        //database.regNewAdmin(patrick1);
        database.changePassword("patrick.thorkildsen@gmail.com", "9wtt0", "Passord123", "Passord123");
        //database.regNewTechnician(patrick);
        //database.deleteEmployee("patrick.thorkildsen@gmail.com", "Password123");
        cleaner.closeConnection(con);
    }
}
