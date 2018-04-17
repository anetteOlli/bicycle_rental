package GUI;

import Admin_App.*;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import javax.swing.*;
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

    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    Statement stm;



    private JList<Bicycle> list1;
    private JButton registerMoreBikesButton;
    private JTextArea textArea1;
    private JButton bicycleFrontPageButton;
    public JPanel regBikeConfirm;
    BikeDatabase database = new BikeDatabase();

    public void createTable(){
    ArrayList<Bicycle> bicycles = new ArrayList<>();
    DefaultListModel<Bicycle> model = new DefaultListModel<>();
    list1.setModel(model);
        try {
        RegBicycle regBicycle = new RegBicycle();
        String mySQL = "SELECT * FROM Bicycle ORDER BY (bicycle_id) DESC LIMIT ?";
        PreparedStatement SQL = connection.createPreparedStatement(con, mySQL);
        System.out.println("hei ho" + regBicycle.getCurrentValue());
        SQL.setInt(1, regBicycle.getCurrentValue());
        ResultSet res = SQL.executeQuery();
        while (res.next()) {
            Bicycle bike = new Bicycle(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("registration_date"), res.getInt("dock_id"));
            model.addElement(bike);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    public RegBikeConfirm() {
        ArrayList<Bicycle> bicycles = new ArrayList<>();
        DefaultListModel<Bicycle> model = new DefaultListModel<>();



        list1.setModel(model);
        try {
            RegBicycle regBicycle = new RegBicycle();
            String mySQL = "SELECT * FROM Bicycle ORDER BY (bicycle_id) DESC LIMIT 20";
            PreparedStatement SQL = connection.createPreparedStatement(con, mySQL);
            ResultSet res = SQL.executeQuery();
            while (res.next()) {
                Bicycle bike = new Bicycle(res.getInt("bicycle_id"), res.getString("make"), res.getString("model"), res.getString("bicycleStatus"), res.getDate("registration_date"), res.getInt("dock_id"));
                model.addElement(bike);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }







        bicycleFrontPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Bicycle");
                frame.setContentPane(new BikeMain().bikeMain);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
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
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RegBikeConfirm");
        frame.setContentPane(new RegBikeConfirm().regBikeConfirm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
