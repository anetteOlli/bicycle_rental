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

public class KmTripChart extends JFrame{
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    public KmTripChart(String appTitle, String chartTitle){
        super(appTitle);
        JFreeChart barChart = ChartFactory.createBarChart(chartTitle, "Month", "Average length of trip", createDataset(), PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(barChart);
        //chartPanel.setPreferredSize(new java.awt.Dimension(700, 500));
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
                String sentence2 = "SELECT SUM(tripKM) AS km FROM TripPayment t JOIN Bicycle b on t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model = 'family'";
                PreparedStatement statement = connection.createPreparedStatement(con, familysentence);
                PreparedStatement statement1 = connection.createPreparedStatement(con, sentence2);
                ResultSet res = statement.executeQuery();
                ResultSet res1 = statement1.executeQuery();
                res.next();
                res1.next();
                if(res.getInt("test") != 0){
                    int value = res1.getInt("km")/res.getInt("test");
                    dataset.addValue(value, family, months[i]);
                }else{
                    int value = 0;
                    dataset.addValue(value, family, months[i]);
                }
            }catch(SQLException e){
                e.getMessage();
            }

        }
        for(int i = 0; i<12; i++){
            try{
                String cargoSentence = "SELECT COUNT(trip_id) AS test FROM TripPayment t JOIN Bicycle b ON t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model='cargo'";
                String sentence2 = "SELECT SUM(tripKM) AS km FROM TripPayment t JOIN Bicycle b on t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model = 'cargo'";
                PreparedStatement statement1 = connection.createPreparedStatement(con, sentence2);
                ResultSet res1 = statement1.executeQuery();
                PreparedStatement statement = connection.createPreparedStatement(con, cargoSentence);
                ResultSet res = statement.executeQuery();
                res.next();
                res1.next();
                if(res.getInt("test") != 0){
                    dataset.addValue(res1.getInt("km")/res.getInt("test"), cargo, months[i]);
                }else{
                    dataset.addValue(0, cargo, months[i]);
                }
            }catch(SQLException e){
                e.getMessage();
            }

        }
        for(int i = 0; i<12; i++){
            try{
                String cargoSentence = "SELECT COUNT(trip_id) AS test FROM TripPayment t JOIN Bicycle b ON t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model='regular'";
                String sentence2 = "SELECT SUM(tripKM) AS km FROM TripPayment t JOIN Bicycle b on t.bicycle_id=b.bicycle_id WHERE Month(time_received)="+(i+1)+" AND model = 'regular'";
                PreparedStatement statement1 = connection.createPreparedStatement(con, sentence2);
                ResultSet res1 = statement1.executeQuery();
                PreparedStatement statement = connection.createPreparedStatement(con, cargoSentence);
                ResultSet res = statement.executeQuery();
                res.next();
                res1.next();
                if(res.getInt("test") != 0){
                    dataset.addValue(res1.getInt("km")/res.getInt("test"), regular, months[i]);
                }else{
                    dataset.addValue(0, regular, months[i]);
                }
            }catch(SQLException e){
                e.getMessage();
            }

        }
        return dataset;
    }

    public static void main(String[] args){
        KmTripChart chart = new KmTripChart("Trip length staticstics", "Km/trip");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

}
