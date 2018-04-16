package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Repair {
    private final int repairID;
    private String description;
    private int dateBroken; //The date the bicycle got flagged as broken
    private int dateRepaired; //The date the bicycle got flagged as repaired
    private int repairCosts;
    private String repairedDescription;
    private int employeeID;
    private int bicycleID;

    public Repair(int repairID, String description, int dateBroken, int repairCosts, String repairedDescription, int dateRepaired, int employeeID, int bicycleID){
        if(description == null || repairedDescription == null || repairID < 0 || description.trim().equals("") || repairCosts < 0 || repairedDescription.trim().equals("") || dateBroken < 10000000 || dateRepaired < 10000000 || employeeID < 0 || bicycleID < 0){
            throw new IllegalArgumentException("Descriptions and dates can not be empty and the IDs and repair costs must have a value over 0!");
        }
        this.repairID = repairID;
        this.description = description;
        this.repairCosts = repairCosts;
        this.repairedDescription = repairedDescription;
        this.dateBroken = dateBroken;
        this.dateRepaired = dateRepaired;
        this.employeeID = employeeID;
        this.bicycleID = bicycleID;
    }

    public Repair(int repairID, String description, int dateBroken, int employeeID, int bicycleID){
        if(description == null || repairID < 0 || description.trim().equals("") || dateBroken < 10000000 || employeeID < 0 || bicycleID < 0){
            throw new IllegalArgumentException("Description and date can not be empty and the IDs must have a value over 0!");
        }
        this.repairID = repairID;
        this.description = description;
        this.dateBroken = dateBroken;
        this.employeeID = employeeID;
        this.bicycleID = bicycleID;
    }

    public Repair(){
        //
    }

    //Get-methods:
    public int getRepairID(){
        return repairID;
    }
    public String getDescription(){
        return description;
    }
    public int getDateBroken(){
        return dateBroken;
    }
    public int getDateRepaired(){
        return dateRepaired;
    }
    public int getRepairCosts(){
        return repairCosts;
    }
    public String getRepairedDescription(){
        return repairedDescription;
    }
    public int getEmployeeID(){
        return employeeID;
    }
    public int getBicycleID(){
        return bicycleID;
    }

    public void String setDescription(String newDescription) {
        this.description = newDescription;
    }
    public void Date setDateBroken(Date newDateBroken) {
        this.dateBroken = newDateBroken;
    }
    public void Date setDateRepaired(Date newDateRepaired) {
        this.dateRepaired = newDateRepaired;
    }
    public void int setRepairCosts(String newRepairCosts) {
        this.repairCosts = newRepairCosts;
    }
    public void String getRepairedDescription(String newRepairedDescription) {
        this.repairedDescription = newRepairedDescription;
    }

    //ToString-method:
    public String toString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        if(repairCosts >0 && repairedDescription != null && dateRepaired > 10000000) {
            return "Repair ID: " + repairID +
                    "\nDescription of damage: " + description +
                    "\nWritten: " + dateBroken +
                    "\nRepair costs: " + repairCosts +
                    "\nDescription after repair: " + repairedDescription +
                    "\nWritten: " + dateRepaired +
                    "\nBy employee: " + employeeID +
                    "\nFor bicycle: " + bicycleID;
        } else {
            return "Repair ID: " + repairID +
                    "\nDescription of damage: " + description +
                    "\nWritten: " + dateBroken +
                    "\nBy employee: " + employeeID +
                    "\nFor bicycle: " + bicycleID;
        }
    }

    //HER KOMMER REPAIR DATABASE METODENE:
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


    //TestClass-method:
    //public static void main(String[]args){
        //Repair repair1 = new Repair(1, "This bike have a broken seat.", 20180605, 345, "Repaired the seat.", 20180608, 1, 4);
        //Repair repair3 = new Repair(3, "This bike is dangerous!", 20180410, 3, 5);
        /*
        System.out.println(repair1.getRepairID());
        System.out.println(repair1.getDescription());
        System.out.println(repair1.getDateBroken());
        System.out.println(repair1.getRepairCosts());
        System.out.println(repair1.getRepairedDescription());
        System.out.println(repair1.getDateRepaired());
        System.out.println(repair1.getEmployeeID());
        System.out.println(repair1.getBicycleID());
        System.out.println();
        System.out.println(repair1.toString());
        System.out.println();
        System.out.println(repair3.toString());
        */
    //}
}
