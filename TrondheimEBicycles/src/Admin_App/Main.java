/*package Admin_App;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

public class Main {
    private Connection connection;
    public static void main(String[] args) throws SQLException {
        BikeDatabase databasen= new BikeDatabase();
        //BikeDatabase databasen = new BikeDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/quannt?user=quannt&password=roBv7vFp");

        final int REG_BIKE = 0;
        final int UPD_BIKE = 1;
        final int AVSLUTT = 2;
        String[] options = {"Register new bike", "Update existing bike", "Exit"};
        int valg = showOptionDialog(null, "Welcome", "What do I do without you?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        //Variabler som brukes flere plasser:
        int bicycle_id = Integer.parseInt(showInputDialog("Type in Bike ID: "));
        int dock_id  = Integer.parseInt(showInputDialog("Dock ID: "));


        String bicycleStatus = showInputDialog("BicycleStatus\nDBR\nin dock\nlost\nneed repair\nnot employed\nnot in dock");



        while(valg != AVSLUTT) {

            switch(valg) {
                case REG_BIKE:
                    int production_date = Integer.parseInt(showInputDialog("ProductionDate"));
                    String make = showInputDialog("Make ");
                    String model = showInputDialog("Model");

                    //Bicycle newBicycle = new Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs);
                    Bicycle newBicycle = new Bicycle(bicycle_id, dock_id, 100, make, model, production_date, bicycleStatus);
                    if(databasen.regNewBicycle(newBicycle)) {
                        showMessageDialog(null, "Bike registered");
                    } else {
                        showMessageDialog(null, "Could not register bike.");
                    }
                    break;

                case UPD_BIKE:
                    int powerlevel = Integer.parseInt(showInputDialog("Powerlevel: "));
                    int totalKM = Integer.parseInt(showInputDialog("TotalKm"));
                    int trips = Integer.parseInt(showInputDialog("trips"));
                    int nr_of_repairs = Integer.parseInt(showInputDialog("nr of repairs"));
                    BicycleUpdate newUpdate = new BicycleUpdate(bicycle_id, dock_id, powerlevel, bicycleStatus, totalKM, trips, nr_of_repairs);

                    if(databasen.UpdateBicycle(newUpdate)) {
                        showMessageDialog(null, "Bike ID " + bicycle_id + " updated.");
                    } else {
                        showMessageDialog(null, "Could not update");
                    }
                    break;

                default:
                    break;
            }
            valg = showOptionDialog(null, "Velkommen", "Hva vil du gjøre?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        }
    }
}*/