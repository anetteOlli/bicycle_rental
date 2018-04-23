package GUI;

import Admin_App.*;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

public class RegBikeConfirm {
    ArrayList<Bicycle> bicycles = new ArrayList<>();
    DefaultListModel<Bicycle> model = new DefaultListModel<>();
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    Statement stm;



    private JList<Bicycle> list1;
    private JButton registerMoreBikesButton;
    private JButton bicycleFrontPageButton;
    public JPanel regBikeConfirm;
    private JLabel make;
    private JLabel status;
    private JLabel model1;
    private JLabel price;
    BikeDatabase database = new BikeDatabase();


    /**
     * method creates an ArrayList of the class Bicycle
     * @return ArrayList for Jlist list1
     * it shows the 20 last registered bicycles.
     */
    public ArrayList<Bicycle> createTable(){
    list1.setModel(model);
        try {
        String mySQL = "SELECT * FROM Bicycle ORDER BY (bicycle_id) DESC LIMIT 20";
        PreparedStatement SQL = connection.createPreparedStatement(con, mySQL);
        ResultSet res = SQL.executeQuery();
        while (res.next()) {
            Bicycle bike = new Bicycle(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("registration_date"), res.getInt("dock_id"), res.getDouble("price_of_bike"));
            model.addElement(bike);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}



    public RegBikeConfirm() {
        createTable();

        bicycleFrontPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Bicycle");
                frame.setContentPane(new BikeMain().bikeMain);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setExtendedState(frame.MAXIMIZED_BOTH);

                Object source = e.getSource();
                if (source instanceof Component) {
                    Component c = (Component) source;
                    Frame frame2 = JOptionPane.getFrameForComponent(c);
                    if (frame2 != null) {
                        frame2.dispose();

                    }
                }
            }
        });
        registerMoreBikesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Register New Bike");
                frame.setContentPane(new RegBicycle().regBike);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setExtendedState(frame.MAXIMIZED_BOTH);

                Object source = e.getSource();
                if (source instanceof Component) {
                    Component c = (Component) source;
                    Frame frame2 = JOptionPane.getFrameForComponent(c);
                    if (frame2 != null) {
                        frame2.dispose();

                    }
                }
            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Bicycle bicycle = list1.getSelectedValue();
                status.setText("Status: " + bicycle.getBicycleStatus());
                model1.setText("Model: " + bicycle.getModell());
                make.setText("Make: " + bicycle.getMake());
                price.setText("Price: " + bicycle.getPrice());

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registered Bikes");
        frame.setContentPane(new RegBikeConfirm().regBikeConfirm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(frame.MAXIMIZED_BOTH);
    }
}
