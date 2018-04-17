package GUI;

import Admin_App.BicycleE;
import Admin_App.BicycleS;
import Admin_App.BikeDatabase;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class BikeStats {
    private JList<BicycleS> list;
    private JButton backButton;
    private JLabel KM;
    private JLabel Rep;
    private JLabel Trip;
    private JLabel Power;
    private JLabel Price;
    private JLabel PDate;
    private JLabel DS;
    private JScrollPane scroll;
    private JPanel panel;
    private JLabel Status;
    private JLabel Make;
    private JLabel Cost;

    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    BikeDatabase db = new BikeDatabase();
    BicycleS bicycleS;

    DefaultListModel<BicycleS> model = new DefaultListModel<>();
    public ArrayList<BicycleS> createTable() {
        ArrayList<BicycleS> array = new ArrayList<>();
        list.setModel(model);
        try {
            //list = new JList<>();
            String getInfo = "SELECT bicycle_id, make, b.model, bicycleStatus, registration_date, b.dock_id, totalKM, nr_of_repairs, trips, powerlevel, price, name, ds.station_id FROM Bicycle b LEFT JOIN Model m ON b.model = m.model LEFT JOIN Dock d ON b.dock_id = d.dock_id LEFT JOIN DockingStation ds ON d.station_id = ds.station_id ORDER BY bicycle_id ASC;";

            PreparedStatement names = connection.createPreparedStatement(con, getInfo);
            ResultSet res = names.executeQuery();
            while (res.next()) {
                BicycleS bike = new BicycleS(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("registration_date"), res.getInt("dock_id"), res.getDouble("totalKM"), res.getInt("nr_of_repairs"), res.getInt("trips"), res.getInt("powerlevel"), res.getDouble("price"), res.getDouble("price_of_bike"), res.getInt("station_id"), res.getString("name"));
                model.addElement(bike);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return array;
    }

    public BikeStats() {

        createTable();
        //db.RegRepairs(1000);
        //db.RegTrips(1000);
        //db.UpdateKM(1000);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                bicycleS = list.getSelectedValue();
                KM.setText("Total KM: " + bicycleS.getTotal_km());
                Rep.setText("Nr of repairs: " + bicycleS.getTotal_rep());
                Trip.setText("Nr of trips" + bicycleS.getTotal_trip());
                Power.setText("Power: " + bicycleS.getPowerlevel());
                Price.setText("Price per trip: " + bicycleS.getPrice());
                PDate.setText("Date registered: " + bicycleS.getRegistration_date().toString());
                DS.setText(bicycleS.getDsName() + " Station ID: " + bicycleS.getDockingstation());
                Status.setText("Bicycle Status: " + bicycleS.getBicycleStatus());
                Make.setText("Make: " + bicycleS.getMake());
                Cost.setText("Price of bicycle: " + bicycleS.getPriceBicycle());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bike Statistics");
        frame.setContentPane(new BikeStats().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
