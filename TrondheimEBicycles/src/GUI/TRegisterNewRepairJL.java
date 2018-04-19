package GUI;

import Admin_App.BicycleE;
import Admin_App.Repair;
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

public class TRegisterNewRepairJL {
    public JPanel deePanel;
    private JPanel midPanel;
    private JLabel header2Label;
    private JComboBox comboBoxSort;
    private JButton selectButton;
    private JLabel header3Label;
    private JLabel statusField;
    private JScrollPane scroll1;
    private JList<BicycleE> bicyclelist;
    private JLabel header1Label;
    private JPanel regRepairPanel;
    private JLabel header4Label;
    private JButton editButton;
    private JLabel header5Label;
    private JLabel bikeIdLable;
    private JLabel messageLabel;
    private JButton backButton;
    private JScrollPane scroll2;
    private JTextArea descArea;

    private BicycleE bicycleE;
    private String bikeStatus;
    private int bikeID;
    private String description;
    DefaultListModel<BicycleE> model;

    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    public void showList() {

        model = new DefaultListModel<>();
        //model.removeAllElements();
        bicyclelist.setModel(model);

        String comboBoxvalue = (String) comboBoxSort.getSelectedItem();
        String bicycleID = "";
        String DockID = "";
        String PowerLevel = "";
        String Make = "";
        String Model = "";
        String RegistrationDate = "";
        String BicycleStatus = "";
        String TotalKM = "";
        String Trips = "";
        String NrOfRepairs = "";

        try {
            String sql = "select * from Bicycle ORDER BY " + comboBoxvalue;
            //String sql = "select * from Bicycle;";
            //PreparedStatement ps = con.prepareStatement(sql);
            PreparedStatement ps = connection.createPreparedStatement(con, sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bicycleID = rs.getString("bicycle_id");
                DockID = rs.getString("dock_id");
                PowerLevel = rs.getString("powerlevel");
                Make = rs.getString("make");
                Model = rs.getString("model");
                RegistrationDate = rs.getString("registration_date");
                BicycleStatus = rs.getString("bicycleStatus");
                TotalKM = rs.getString("totalKM");
                Trips = rs.getString("trips");
                NrOfRepairs = rs.getString("nr_of_repairs");
                //Bicycle bicycle = new Bicycle(bicycleID, DockID, PowerLevel, Make, Model, ProductionDate, BicycleStatus, TotalKM, Trips, NrOfRepairs);
                //model.addElement(bicycle);
                BicycleE bike = new BicycleE(rs.getInt("bicycle_id"), rs.getString("make"), rs.getString("model"), rs.getString("bicycleStatus"), rs.getDate("registration_date"), rs.getInt("dock_id"));
                model.addElement(bike);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public TRegisterNewRepairJL() {

        comboBoxSort.addItem("bicycle_id");
        comboBoxSort.addItem("make");
        comboBoxSort.addItem("model");

        showList();


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("RegisterRepairFrontPage");
                frame.setContentPane(new TRegisterRepairFrontPage().panel1);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                //gets rid of the previous frame
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
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //showList();
                //regRepairPanel.setVisible(true);
                bikeStatus = bicycleE.getBicycleStatus();
                statusField.setText(bikeStatus);
                bikeID = bicycleE.getBicycle_id();
                bikeIdLable.setText(Integer.toString(bikeID));
            }
        });
        comboBoxSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showList();
            }
        });


        bicyclelist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                bicycleE = bicyclelist.getSelectedValue();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Update list
                description = descArea.getText();
                System.out.println(description);
                System.out.println(Integer.toString(bikeID));
                Repair repair = new Repair(description, bikeID);

                if (repair.regNewRepair(repair)) {
                    messageLabel.setText("Registration successful!");
                } else {
                    messageLabel.setText("Something went wrong under registration (employee ID may not exist.)");
                }
                //messageLabel.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RegisterNewRepairJL");
        frame.setContentPane(new TRegisterNewRepairJL().deePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
