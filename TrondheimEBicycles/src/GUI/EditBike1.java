package GUI;

import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;
import Admin_App.*;

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
import java.util.*;


public class EditBike1 {
    JFrame frame = new JFrame("Edit bike");
    JList<BicycleE> list;
    ArrayList<BicycleE> bikeList;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    BikeDatabase bd = new BikeDatabase();


    JScrollPane splitpane;
    JButton backButton;

    JPanel panel;
    private JLabel bicycleinfo1;
    private JLabel bicycleinfo2;
    private JLabel bicycleinfo3;
    private JLabel bicycleinfo5;
    private JLabel bicycleinfo6;
    private JComboBox comboBox1;
    private JButton editButton;
    private JComboBox comboBox2;
    DefaultListModel<BicycleE> model = new DefaultListModel<>();

    public void editStatus() {
        try {
            BicycleE bicycleE = list.getSelectedValue();
            String mySQL = "UPDATE Bicycle SET bicycleStatus='" + comboBox2.getSelectedItem().toString() + "' WHERE bicycle_id=" + bicycleE.getBicycle_id();
            PreparedStatement update = connection.createPreparedStatement(con, mySQL);
            update.executeUpdate();

            model.get(list.getSelectedIndex()).setBicycleStatus(comboBox2.getSelectedItem().toString());

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

   public ArrayList<BicycleE> createTable() {
        ArrayList<BicycleE> array = new ArrayList<>();
        list.setModel(model);
        try {
            model.removeAllElements();
            String getInfo = "SELECT bicycle_id, make, model, bicycleStatus, registration_date, dock_id  FROM Bicycle ORDER BY " + comboBox1.getSelectedItem().toString() + ";";
            PreparedStatement names = connection.createPreparedStatement(con, getInfo);
            ResultSet res = names.executeQuery();
            while (res.next()) {
                BicycleE bike = new BicycleE(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("registration_date"), res.getInt("dock_id"));
                model.addElement(bike);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return array;
    }

    public EditBike1() {
        createTable();
        list.setModel(model);


        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTable();


                System.out.println(comboBox1.getSelectedItem().toString());
            }
        });


        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                BicycleE bicycleE = list.getSelectedValue();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStatus();

            }

        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Bicycle");
                frame.setContentPane(new BikeMain().bikeMain);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setExtendedState(frame.MAXIMIZED_BOTH);

                
            }
        });
    }

        public static void main(String[] args) {
            JFrame frame = new JFrame("Edit Bike Status");
            frame.setContentPane(new EditBike1().panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(frame.MAXIMIZED_BOTH);


    }
}