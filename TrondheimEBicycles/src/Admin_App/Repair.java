package Admin_App;

public class Repair {
    private final int repairID;
    private String description;
    private int dateBroken; //The date the bicycle got flagged as broken
    private int dateRepaired; //The date the bicycle got flagged as repaired
    private int repairCosts;
    private String repairedDescription;

    public Repair(int repairID, String description, int dateBroken, int repairCosts, String repairedDescription, int dateRepaired){
        if(description == null || repairedDescription == null || repairID < 0 || description.trim().equals("") || repairCosts < 0 || repairedDescription.trim().equals("") || dateBroken < 10000000 || dateRepaired < 10000000){
            throw new IllegalArgumentException("Descriptions and dates can not be empty and the repairID and repair costs must have a value over 0!");
        }
        this.repairID = repairID;
        this.description = description;
        this.repairCosts = repairCosts;
        this.repairedDescription = repairedDescription;
        this.dateBroken = dateBroken;
        this.dateRepaired = dateRepaired;
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

    //ToString-method:
    public String toString(){
        return "Description of damage: " + description +
                "\nWritten: " + dateBroken +
                "\nRepair costs: " + repairCosts +
                "\nDescription after repair: " + repairedDescription +
                "\nWritten: " + dateRepaired;
    }

    //TestClass-method:
    public static void main(String[]args){
        Repair repair1 = new Repair(1, "This bike have a broken seat.", 20180605, 345, "Repaired the seat.", 20180608 );
        //Repair repair3 = new Repair(3, "This bike is dangerous!", 10, 1000, "Changed the breakes.", 20180310);

        System.out.println(repair1.getRepairID());
        System.out.println(repair1.getDescription());
        System.out.println(repair1.getDateBroken());
        System.out.println(repair1.getRepairCosts());
        System.out.println(repair1.getRepairedDescription());
        System.out.println(repair1.getDateRepaired());
        System.out.println();
        System.out.println(repair1.toString());
        //System.out.println(repair3.toString());
    }
}
