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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterNewRepairJL {
    public JPanel deePanel;
    private JComboBox<String> comboBoxSort;
    private JButton backButton;
    private JButton selectButton;
    private JList<BicycleE> bicyclelist;
    private JPanel midPanel;
    private JPanel regRepairPanel;
    private JTextArea descArea;
    private JLabel bikeIdLable;
    private JButton editButton;
    private JLabel statusField;
    private JLabel header4Label;
    private JLabel header1Label;
    private JLabel header2Label;
    private JLabel header3Label;
    private JLabel header5Label;
    private JLabel messageLabel;
    private JScrollPane scroll1;
    private JScrollPane scroll2;

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


    public RegisterNewRepairJL() {

        comboBoxSort.addItem("bicycle_id");
        comboBoxSort.addItem("make");
        comboBoxSort.addItem("model");

        showList();


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("RegisterRepairFrontPage");
                frame.setContentPane(new RegisterRepairFrontPage().panel1);
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
        frame.setContentPane(new RegisterNewRepairJL().deePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        deePanel = new JPanel();
        deePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(20, 20, 20, 20), -1, -1));
        midPanel = new JPanel();
        midPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        deePanel.add(midPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        header2Label = new JLabel();
        header2Label.setText("Sort table:");
        midPanel.add(header2Label, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxSort = new JComboBox();
        midPanel.add(comboBoxSort, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectButton = new JButton();
        selectButton.setText("Select");
        midPanel.add(selectButton, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bicyclelist = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        bicyclelist.setModel(defaultListModel1);
        midPanel.add(bicyclelist, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 2, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        header3Label = new JLabel();
        header3Label.setText("Bike status:");
        midPanel.add(header3Label, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusField = new JLabel();
        statusField.setText("Status");
        statusField.setVisible(true);
        midPanel.add(statusField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        header1Label = new JLabel();
        header1Label.setText("Select bike for register new repair");
        deePanel.add(header1Label, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        regRepairPanel = new JPanel();
        regRepairPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        regRepairPanel.setVisible(true);
        deePanel.add(regRepairPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        header4Label = new JLabel();
        header4Label.setText("Register new repair:");
        regRepairPanel.add(header4Label, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editButton = new JButton();
        editButton.setText("Edit");
        regRepairPanel.add(editButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        descArea = new JTextArea();
        descArea.setVisible(true);
        regRepairPanel.add(descArea, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        header5Label = new JLabel();
        header5Label.setText("Repair description:");
        regRepairPanel.add(header5Label, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bikeIdLable = new JLabel();
        bikeIdLable.setText("bikeId");
        bikeIdLable.setVisible(true);
        regRepairPanel.add(bikeIdLable, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        messageLabel = new JLabel();
        messageLabel.setText("Message");
        messageLabel.setVisible(true);
        regRepairPanel.add(messageLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        regRepairPanel.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return deePanel;
    }
}
