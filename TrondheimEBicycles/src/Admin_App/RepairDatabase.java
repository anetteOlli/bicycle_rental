package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import java.util.Date;

public class RepairDatabase {
    private Connection forbindelse;
    private Statement setning;
    private String databaseDriver;
    private String databaseNavn;

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public RepairDatabase(String databaseDriver, String databaseNavn) {
        this.databaseDriver = databaseDriver;
        this.databaseNavn = databaseNavn;
        startForbindelse();
    }

    private void startForbindelse() {
        try {
            Class.forName(databaseDriver);
            forbindelse = DriverManager.getConnection(databaseNavn);
            setning = forbindelse.createStatement();
        } catch (ClassNotFoundException classEx) {
            System.out.println(classEx.getMessage());
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        } catch (Exception e) {
            System.out.println(3);
        }
    }

    public void lukkForbindelse() {
        try {
            setning.close();
            forbindelse.close();
        } catch (SQLException sqlEx) {
            System.out.println("Error1");
        } catch (Exception e) {
            System.out.println("Error2");
        }
    }

    public boolean regNewRepair(String description, int bicycleID) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String mysql = "INSERT INTO Repair (repair_id, description_before, date_sent, bicycle_id) VALUES(DEFAULT, ?, ?, ?)";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        Calendar calendar = Calendar.getInstance();
        java.sql.Date ourJavaDate = new java.sql.Date(calendar.getTime().getTime());
        try {
            sentence.setString(1, description);
            sentence.setDate(2, ourJavaDate);
            sentence.setInt(3, bicycleID);
            sentence.execute();

        } catch (SQLException e) {
            return false;
        }
        if (cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
            return true;
        }
        return false;

    }

    public boolean regFinishRepair(int repairID, String descriptionAfter, int repairCosts, int employeeID) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();

        //Sjekker at repairID finnes i tabellen
        if (repairIDExist(repairID) > 0) {
            String mysql = "UPDATE Repair SET date_received = ? , repair_cost = ? , repair_description_after = ? , employee_id = ? WHERE repair_id = ? ";
            PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
            Calendar calendar = Calendar.getInstance();
            java.sql.Date ourJavaDateAfter = new java.sql.Date(calendar.getTime().getTime());
            try {
                sentence.setDate(1, ourJavaDateAfter);
                sentence.setInt(2, repairCosts);
                sentence.setString(3, descriptionAfter);
                sentence.setInt(4, employeeID);
                sentence.setInt(5, repairID);
                sentence.execute();
            } catch (SQLException e) {
                return false;
            }
            if (cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                return true;
            }
            return false;
        }
        System.out.println("ID finnes ikke i databasen");//Ta denne bort senere
        return false;
    }

    public int repairIDExist(int repairID) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();

        String mysql = "SELECT repair_id, description_before, date_sent FROM Repair WHERE repair_id = '" + repairID + "';";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try {
            ResultSet res = sentence.executeQuery();

            if (res.next() && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                System.out.println("ID finnes!"); //Ta bort denne senere
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            return -2;
        }
    }



    public String showAllRepairs() {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();

        String theTable = "";
        String heletabellen = "SELECT * FROM Repair;";

        PreparedStatement sentence1 = connection.createPreparedStatement(con, heletabellen);

        try {
            ResultSet res1 = sentence1.executeQuery();



            while (res1.next()) {
                String ID = res1.getObject(1).toString();
                String DescrB = res1.getObject(2).toString();
                String DateS = res1.getObject(3).toString();
                String DateR = res1.getObject(4).toString();
                String Cost = res1.getObject(5).toString();
                String DescA = res1.getObject(6).toString();
                String Employee = res1.getObject(7).toString();
                String Bicycle = res1.getObject(8).toString();
                theTable += (ID + ", " + DescrB + ", " + DateS + ", " + DateR + ", " + Cost + ", " + DescA + ", " + Employee + ", " + Bicycle + "\n");
            }




        } catch (SQLException e) {
            return null;
        }
        if (cleaner.closeSentence(sentence1) &&
                cleaner.closeConnection(con)) {
            //return repairs
            return theTable;
        }
        return null;

    }

    public int nrRepairs(int bicycleID) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();

        int antRepairs = -1;

        String mysql = "SELECT COUNT(*) as 'Antall repairs' FROM Repair WHERE bicycle_id LIKE ?";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);

        try {
            sentence.setInt(1, bicycleID);
            ResultSet res = sentence.executeQuery();
            if(res.next()){
                antRepairs = res.getInt(1);
            }

        }catch(SQLException e){
            System.out.println("Error");
            return antRepairs;
        }
        if(cleaner.closeSentence(sentence)&&cleaner.closeConnection(con)){
            return antRepairs;
        }
        System.out.println("ikke Error");
        return antRepairs;
    }


    //test
    /*
    public static void main(String[]args) {
        String databaseNavn = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/elisamop?user=elisamop&password=q9rmRFsi";
        RepairDatabase databasen = new RepairDatabase("com.mysql.jdbc.Driver", databaseNavn);

        //System.out.println(databasen.regNewRepair("This bike is dangerous!", 3));
        //System.out.println(databasen.regNewRepair("This bike is dangerous!", 2));
        //System.out.println(databasen.regNewRepair("The handle is broken.", 1));
        //System.out.println(databasen.regNewRepair("This is crap.", 3));
        //System.out.println(databasen.regNewRepair("This is super crap.", 2));
        //System.out.println(databasen.regFinishRepair(3, "Skriftet bremsene.", 550, 1));
        //System.out.println(databasen.regFinishRepair(1, "Grøt.", 50, 2));
        //System.out.println(databasen.regFinishRepair(2, "Meh.", 50, 2));
        //System.out.println(databasen.showAllRepairs());
        //System.out.println(databasen.nrRepairs(2));


        databasen.lukkForbindelse();
    }*/
}