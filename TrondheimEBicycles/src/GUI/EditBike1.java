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
    BikeDatabase bd;


    JScrollPane splitpane;
    JButton backButton;
    BicycleE bicycleE;
    TripPaymentDatabase tpb;
    JPanel panel;
    private JLabel bicycleinfo1;
    private JLabel bicycleinfo2;
    private JLabel bicycleinfo3;
    private JLabel bicycleinfo5;
    private JLabel bicycleinfo6;
    private JComboBox comboBox1;
    private JButton editButton;
    private JComboBox comboBox2;
    private JCheckBox takeOutOfDockCheckBox;
    private JCheckBox setInDockCheckBox;
    private JTextField stationid;
    private JLabel station;
    DefaultListModel<BicycleE> model = new DefaultListModel<>();

    /**
     *
     * @return arraylist for JList list
     */

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



    public void setInDock(){
        String inDock = "UPDATE Bicycle SET dock_id=(SELECT MIN(dock_id) FROM Dock WHERE station_id=? AND isAvailable=1), bicycleStatus = 'in dock' WHERE bicycle_id=?;";
        PreparedStatement update = connection.createPreparedStatement(con, inDock);
        int Input = Integer.parseInt(stationid.getText());
        try{
            update.setInt(1,Input);
            update.setInt(2, bicycleE.getBicycle_id());
            update.executeUpdate();
            model.get(list.getSelectedIndex()).setBicycleStatus("in dock");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectInDock(){
        String inDock = "SELECT dock_id FROM Bicycle WHERE bicycle_id=?;";
        PreparedStatement select = connection.createPreparedStatement(con, inDock);
        try{
            select.setInt(1, bicycleE.getBicycle_id());
            ResultSet res = select.executeQuery();
            while(res.next()){
                model.get(list.getSelectedIndex()).setDock_id(res.getInt("dock_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUnavailable(){
        String update = "UPDATE Dock SET isAvailable=0 WHERE dock_id=(SELECT dock_id FROM Bicycle WHERE bicycle_id=?);";
        PreparedStatement set = connection.createPreparedStatement(con, update);
        try{
            set.setInt(1, bicycleE.getBicycle_id());
            set.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void takeOutDock(){
        String outDock = "Update Bicycle SET dock_id=NULL WHERE bicycle_id=?;";
        PreparedStatement update = connection.createPreparedStatement(con, outDock);
        try{
            update.setInt(1, bicycleE.getBicycle_id());
            update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.get(list.getSelectedIndex()).setDock_id(0);
    }


    public void setAvailable(){
        String update = "UPDATE Dock SET isAvailable=1 WHERE dock_id=(SELECT dock_id FROM Bicycle WHERE bicycle_id=?);";
        PreparedStatement set = connection.createPreparedStatement(con, update);
        try{
            set.setInt(1, bicycleE.getBicycle_id());
            set.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public EditBike1() {
        createTable();
        list.setModel(model);
        station.setVisible(false);
        stationid.setVisible(false);
        setInDockCheckBox.setVisible(false);
        takeOutOfDockCheckBox.setVisible(false);


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
                bicycleE = list.getSelectedValue();
                if(bicycleE.getDock_id() == 0) {
                    setInDockCheckBox.setVisible(true);
                    takeOutOfDockCheckBox.setSelected(false);
                    takeOutOfDockCheckBox.setVisible(false);
                }else{
                    setInDockCheckBox.setVisible(false);
                    setInDockCheckBox.setSelected(false);
                    takeOutOfDockCheckBox.setVisible(true);
                }

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(setInDockCheckBox.isSelected()) {
                    setInDock();
                    selectInDock();
                    setUnavailable();
                }else if(takeOutOfDockCheckBox.isSelected()){
                    setAvailable();
                    takeOutDock();
                    editStatus();
                } else {
                    editStatus();
                }

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

        setInDockCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(setInDockCheckBox.isSelected()){
                    station.setVisible(true);
                    stationid.setVisible(true);
                } else {
                    station.setVisible(false);
                    stationid.setVisible(false);
                }
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