package Admin_App;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

    //TestClass-method:
    public static void main(String[]args){
        Repair repair1 = new Repair(1, "This bike have a broken seat.", 20180605, 345, "Repaired the seat.", 20180608, 1, 4);
        Repair repair3 = new Repair(3, "This bike is dangerous!", 20180410, 3, 5);

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
    }
}
