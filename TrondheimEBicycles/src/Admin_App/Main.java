/*package Admin_App;

import javax.swing.*;
import java.sql.Connection;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

public class Main {
    BikeDatabase database = new BikeDatabase();
    private Connection connection;
    public static void main(String[] args) {
        String databaseNavn = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/quannt?user=quannt&password=roBv7vFp";
        Database databasen = new Database("com.mysql.jdbc.Driver", databaseNavn);
        BikeDatabase bikedatabase = new BikeDatabase();

        final int REG_BIKE = 0;
        //final int UPD_BIKE = 1;
        final int AVSLUTT = 1;
        String[] options = {"Register new bike", "Update existing bike", "Exit"};
        int valg = showOptionDialog(null, "Welcome", "What do I do without you?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        //Variabler som brukes flere plasser:


        while(valg != AVSLUTT) {

            switch(valg) {
                case REG_BIKE:
                   int bicycle_id = Integer.parseInt(showInputDialog("Type in Bike ID: "));
                   int dock_id  = Integer.parseInt(showInputDialog("Dock ID: "));
                   int powerlevel = Integer.parseInt(showInputDialog("Powerlevel: "));
                   String make = showInputDialog("Make ");
                   String model = showInputDialog("Model");
                   int production_date = Integer.parseInt(showInputDialog("ProductionDate"));
                   String bicycleStatus = showInputDialog("BicycleStatus");
                   int totalKM = Integer.parseInt(showInputDialog("TotalKm"));
                   int trips = Integer.parseInt(showInputDialog("trips"));
                   int nr_of_repairs = Integer.parseInt(showInputDialog("nr of repairs"));
                    Bicycle newBicycle = new Bicycle(bicycle_id, dock_id, powerlevel, make, model, production_date, bicycleStatus, totalKM, trips, nr_of_repairs);
                    if(bikedatabase.regNewBicycle(newBicycle)) {
                        showMessageDialog(null, "Bike registered");
                    } else {
                        showMessageDialog(null, "Could not register bike.");
                    }
                    break;

                /*case UPD_BIKE;
                    isbn = showInputDialog("Skriv inn isbn til boken: ");
                    int eksemplarNr = databasen.regNyttEksemplar(isbn);
                    if(eksemplarNr != 0) {
                        showMessageDialog(null, "Det ble registrert ett nytt eksemplar av denne boken. Eksempelet fikk nummer: " + eksemplarNr + ".");
                    } else {
                        showMessageDialog(null, "Det eksisterer ingen bøker med gitt isbn-verdi, derfor kunne ingen nye eksempler registreres.");
                    }
                    break;*/
/*
                default:
                    break;
            }
            valg = showOptionDialog(null, "Velkommen", "Hva vil du gjøre?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        }
    }
}*/