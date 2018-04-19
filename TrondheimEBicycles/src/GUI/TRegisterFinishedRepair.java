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

public class TRegisterFinishedRepair {
    public JPanel deePanel;
    private JPanel midPanel;
    private JComboBox comboBoxSort;
    private JButton selectButton;
    private JScrollPane scroll1;
    private JList<Repair> repairlist;
    private JPanel regFinRepairPanel;
    private JLabel bikeIdLabel;
    private JTextField costField;
    private JComboBox comboBoxStatus;
    private JButton editButton;
    private JTextField employeeIDField;
    private JLabel messageLabel;
    private JButton backButton;
    private JScrollPane scroll2;
    private JTextArea descriptionArea;

    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    private Repair repair;
    private int bicycleID;
    private int repairCost;
    private String newDescription;
    private int employeeID;
    private BicycleE bike;
    private String bikeStatus;
    DefaultListModel<Repair> model;

    public void showList() {

        model = new DefaultListModel<>();
        //model.removeAllElements();
        repairlist.setModel(model);

        String comboBoxvalue = (String) comboBoxSort.getSelectedItem();
        String repairID = "";
        String description = "";
        String dateBroken = "";
        String dateRepaired = "";
        String repairCosts = "";
        String repairedDescription = "";
        String employeeID = "";
        String bicycleID = "";


        try {
            String sql = "select * from Repair ORDER BY " + comboBoxvalue;
            //String sql = "select * from Bicycle;";
            //PreparedStatement ps = con.prepareStatement(sql);
            PreparedStatement ps = connection.createPreparedStatement(con, sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                repairID = rs.getString("repair_id");
                description = rs.getString("description_before");
                dateBroken = rs.getString("date_sent");
                dateRepaired = rs.getString("date_received");
                repairCosts = rs.getString("repair_cost");
                repairedDescription = rs.getString("repair_description_after");
                employeeID = rs.getString("employee_id");
                bicycleID = rs.getString("bicycle_id");
                //Bicycle bicycle = new Bicycle(bicycleID, DockID, PowerLevel, Make, Model, ProductionDate, BicycleStatus, TotalKM, Trips, NrOfRepairs);
                //model.addElement(bicycle);
                Repair repair1 = new Repair(rs.getInt("repair_id"), rs.getString("description_before"), rs.getDate("date_sent"), rs.getDate("date_received"), rs.getInt("repair_cost"), rs.getString("repair_description_after"), rs.getInt("employee_id"), rs.getInt("bicycle_id"));
                model.addElement(repair1);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public TRegisterFinishedRepair() {

        comboBoxSort.addItem("repair_id");
        comboBoxSort.addItem("date_sent");
        comboBoxSort.addItem("date_received");
        comboBoxSort.addItem("bicycle_id");

        comboBoxStatus.addItem("In dock");
        comboBoxStatus.addItem("Not in dock");
        comboBoxStatus.addItem("Need repair");
        comboBoxStatus.addItem("Damage beyond");
        comboBoxStatus.addItem("Lost");
        comboBoxStatus.addItem("In storage");

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
                bicycleID = repair.getBicycleID();
                bikeIdLabel.setText(Integer.toString(bicycleID));
            }
        });


        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String repairCostS = costField.getText();
                repairCost = Integer.parseInt(repairCostS);
                newDescription = descriptionArea.getText();
                String employeeIDS = employeeIDField.getText();
                employeeID = Integer.parseInt(employeeIDS);
                bikeStatus = comboBoxStatus.getSelectedItem().toString();

                System.out.println(repair.getRepairID());
                System.out.println(newDescription);
                System.out.println(repairCost);
                System.out.println(employeeID);

                if (repair.regFinishRepair(repair.getRepairID(), newDescription, repairCost, employeeID) /*&& bike.editStatus() */) {
                    messageLabel.setText("Registration successful!");
                } else {
                    messageLabel.setText("Something went wrong under registration (employee ID may not exist.)");
                }
                //messageLabel.setVisible(true);
                showList();
            }
        });


        comboBoxSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showList();
            }
        });


        repairlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                repair = repairlist.getSelectedValue();
            }
        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RegisterFinishedRepair");
        frame.setContentPane(new TRegisterFinishedRepair().deePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
