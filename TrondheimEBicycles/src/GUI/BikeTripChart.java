package GUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;

public class BikeTripChart extends JFrame{
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    public BikeTripChart(String appTitle, String chartTitle){
        super(appTitle);
        JFreeChart barChart = ChartFactory.createBarChart(chartTitle, "Month", "Number of trips", createDataset(), PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(barChart);
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset(){
        final String january = "January";
        final String february = "February";
        final String march = "March";
        final String april = "April";
        final String may = "May";
        final String june = "June";
        final String july = "July";
        final String august = "August";
        final String september = "September";
        final String october = "October";
        final String november = "November";
        final String december = "December";
        final String family = "Family";
        final String regular = "Regular";
        final String cargo = "Cargo";
        String[] months = {january, february, march, april, may, june, july, august, september, october, november, december};
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i<12; i++){
            try{
                String familysentence = "SELECT COUNT(trip_id) AS test FROM TripPayment t JOIN Bicycle b ON t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model='family'";
                PreparedStatement statement = connection.createPreparedStatement(con, familysentence);
                ResultSet res = statement.executeQuery();
                res.next();
                dataset.addValue(res.getInt("test"), family, months[i]);
            }catch(SQLException e){
                e.getMessage();
            }

        }
        for(int i = 0; i<12; i++){
            try{
                String cargoSentence = "SELECT COUNT(trip_id) AS test FROM TripPayment t JOIN Bicycle b ON t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model='cargo'";
                PreparedStatement statement = connection.createPreparedStatement(con, cargoSentence);
                ResultSet res = statement.executeQuery();
                res.next();
                dataset.addValue(res.getInt("test"), cargo, months[i]);
            }catch(SQLException e){
                e.getMessage();
            }

        }
        for(int i = 0; i<12; i++){
            try{
                String cargoSentence = "SELECT COUNT(trip_id) AS test FROM TripPayment t JOIN Bicycle b ON t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model='regular'";
                PreparedStatement statement = connection.createPreparedStatement(con, cargoSentence);
                ResultSet res = statement.executeQuery();
                res.next();
                dataset.addValue(res.getInt("test"), regular, months[i]);
            }catch(SQLException e){
                e.getMessage();
            }

        }
        return dataset;
    }

    public static void main(String[] args){
         BikeTripChart chart = new BikeTripChart("Bike trip staticstics", "Trips/month");
         chart.pack();
         RefineryUtilities.centerFrameOnScreen(chart);
         chart.setVisible(true);
    }

}
