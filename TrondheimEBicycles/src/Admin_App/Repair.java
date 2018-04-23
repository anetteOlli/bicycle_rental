package Admin_App;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Repair {

    private int repairID;
    private String description;
    private Date dateBroken; //The date the bicycle got flagged as broken
    private Date dateRepaired; //The date the bicycle got flagged as repaired
    private int repairCosts;
    private String repairedDescription;
    private int employeeID;
    private int bicycleID;


    public Repair(int repairID, String description, Date dateBroken, Date dateRepaired, int repairCosts, String repairedDescription, int employeeID, int bicycleID){

        this.repairID = repairID;
        this.description = description;
        this.repairCosts = repairCosts;
        this.repairedDescription = repairedDescription;
        this.dateBroken = dateBroken;
        this.dateRepaired = dateRepaired;
        this.employeeID = employeeID;
        this.bicycleID = bicycleID;
    }

    public Repair(int repairID, String description, Date dateBroken, int bicycleID){

        this.repairID = repairID;
        this.description = description;
        this.dateBroken = dateBroken;
        this.bicycleID = bicycleID;
    }

    public Repair(String description, int bicycleID){
        this.description = description;
        this.bicycleID = bicycleID;
    }

    public Repair(Repair newrepair){
        repairID = newrepair.getRepairID();
        description = newrepair.getDescription();
        dateBroken = newrepair.getDateBroken();
        bicycleID = newrepair.getBicycleID();
    }

    public Repair(){
        //Standard construktor
    }


    //Get-methods:
    public int getRepairID(){
        return repairID;
    }
    public String getDescription(){
        return description;
    }
    public Date getDateBroken(){
        return dateBroken;
    }
    public Date getDateRepaired(){
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

    public void setRepairID(int newrepairID){
        repairID = newrepairID;
    }
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
    public void setDateBroken(Date newDateBroken) {
        this.dateBroken = newDateBroken;
    }
    public void setDateRepaired(Date newDateRepaired) {
        this.dateRepaired = newDateRepaired;
    }
    public void setRepairCosts(int newRepairCosts) {
        this.repairCosts = newRepairCosts;
    }
    public void getRepairedDescription(String newRepairedDescription) {
        this.repairedDescription = newRepairedDescription;
    }

    //ToString-method:
    public String toString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        if(repairCosts >0 && repairedDescription != null) {
            return  repairID + "       |        " +
                    description + "       |        " +
                    dateBroken + "       |        " +
                    repairCosts + "       |        " +
                    repairedDescription + "       |        " +
                    dateRepaired + "       |        " +
                    employeeID + "       |        " +
                    bicycleID;
        } else {
            return  repairID + "       |        " +
                    description + "       |        " +
                    dateBroken + "       |        " +
                    bicycleID;
        }
    }


    //HER KOMMER REPAIR DATABASE METODENE:
    private Connection forbindelse;
    private Statement setning;
    private String databaseDriver;
    private String databaseNavn;

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     *
     * @param newRepair is the new repair object
     * @return true if it succeeds, false if it doesn't
     */
    public boolean regNewRepair(Repair newRepair) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();
        String mysql = "INSERT INTO Repair (repair_id, description_before, date_sent, bicycle_id) VALUES(DEFAULT, ?, ?, ?)";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        Calendar calendar = Calendar.getInstance();
        java.sql.Date ourJavaDate = new java.sql.Date(calendar.getTime().getTime());


        try {
            sentence.setString(1, newRepair.getDescription());
            sentence.setDate(2, ourJavaDate);
            sentence.setInt(3, newRepair.getBicycleID());
            sentence.execute();

            String mysql2 = "SELECT MAX(repair_id) FROM Repair;";
            PreparedStatement sentence2 = connection.createPreparedStatement(con, mysql2);
            ResultSet res2 = sentence2.executeQuery();
            while(res2.next()) {
                repairID = res2.getInt(1);
                //Object obj = res2.getDate(3);
                newRepair.setRepairID(repairID);
            }
            String mysql3 = "SELECT date_sent FROM Repair WHERE repair_id LIKE '" + repairID + "';";
            PreparedStatement sentence3 = connection.createPreparedStatement(con, mysql3);
            ResultSet res3 = sentence3.executeQuery();
            while(res3.next()) {
                dateBroken = res3.getDate(1);
                newRepair.setDateBroken(dateBroken);
            }
        } catch (SQLException e) {
            return false;
        }
        if (cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
            return true;
        }
        return false;

    }





    /**
     *
     * @param repairID is the id for the repair you want to update
     * @param descriptionAfter is the description of the repairs done on the bicycle
     * @param repairCosts is how much the repair cost
     * @param employeeID is the id of the employee that repaired the bicycle
     * @return true if it succeeds, false if it doesn't
     */
    public boolean regFinishRepair(int repairID, String descriptionAfter, int repairCosts, int employeeID) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();

        //Sjekker at repairID finnes i tabellen
        if (repairIDExist(repairID) > 0 && employeeIDExist(employeeID) > 0) {
            String mysql = "UPDATE Repair SET date_received = ? , repair_cost = ? , repair_description_after = ? , employee_id = ? WHERE repair_id = ? ";
            PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
            Calendar calendar = Calendar.getInstance();
            java.sql.Date ourJavaDateAfter = new java.sql.Date(calendar.getTime().getTime());

            this.repairID = repairID;
            this.dateRepaired = ourJavaDateAfter;
            this.repairedDescription = descriptionAfter;
            this.repairCosts = repairCosts;
            this.employeeID = employeeID;


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
        System.out.println("RepairID or employeeID don't exist");//Ta denne bort senere
        return false;
    }

    /**
     *
     * @param repairID is the id of the repair you want to check if exist in the database
     * @return 1 if it exist, below 0 if it doesn't
     */
    public int repairIDExist(int repairID) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();

        String mysql = "SELECT repair_id, description_before, date_sent FROM Repair WHERE repair_id = '" + repairID + "';";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try {
            ResultSet res = sentence.executeQuery();

            if (res.next() && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                System.out.println("RepID finnes!"); //Ta bort denne senere
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            return -2;
        }
    }

    /**
     *
     * @param employeeID is the id of the employee you want to check if exist in the database
     * @return 1 if it exist, below 0 if it doesn't
     */
    public int employeeIDExist(int employeeID) {
        DatabaseCleanup cleaner = new DatabaseCleanup();
        DatabaseConnection connection = new DatabaseConnection();
        Connection con = connection.getConnection();

        String mysql = "SELECT employee_id FROM Employee WHERE employee_id = '" + employeeID + "';";
        PreparedStatement sentence = connection.createPreparedStatement(con, mysql);
        try {
            ResultSet res = sentence.executeQuery();

            if (res.next() && cleaner.closeSentence(sentence) && cleaner.closeConnection(con)) {
                System.out.println("EmpID finnes!"); //Ta bort denne senere
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            return -2;
        }
    }


    /**
     * Is only used for the test-method
     * @return a string with all the repairs
     */
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


    //Test-method: OK
    /*
    public static void main(String[]args){

        Calendar calendar = Calendar.getInstance();
        java.sql.Date registration_date = new java.sql.Date(calendar.getTime().getTime());

        //Repair repair1 = new Repair(1, "This bike have a broken seat.", registration_date, 345, "Repaired the seat.", registration_date, 1, 4);
        //Repair repair3 = new Repair(3, "This bike is dangerous!", registration_date, 3, 5);
        Repair repair2 = new Repair("Dette er noe dritt", 2);
        repair2.regNewRepair(repair2);

        System.out.println(repair2.getRepairID());
        System.out.println(repair2.getDescription());
        System.out.println(repair2.getDateBroken());


        repair2.regFinishRepair(12, "Dette fungerer bedre", 110, 1);

        System.out.println(repair2.getRepairID());
        System.out.println(repair2.getRepairCosts());
        System.out.println(repair2.getRepairedDescription());
        System.out.println(repair2.getDateRepaired());
        System.out.println(repair2.getEmployeeID());
        System.out.println(repair2.getBicycleID());
    }*/
}
