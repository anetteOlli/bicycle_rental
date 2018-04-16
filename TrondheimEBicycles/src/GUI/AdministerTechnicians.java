package GUI;

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
import java.util.Random;

import DatabaseHandler.*;
import Admin_App.*;

public class AdministerTechnicians {
    private JButton backButton;
    public JPanel panel1;
    private JButton setTechnicianAsInactiveButton;
    private JButton addTechnicianButton;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();
    JList<Technician> list1;
    private JLabel technicianInfo;
    private JLabel technicianInfo2;
    private JLabel technicianInfo3;
    private JLabel technicianInfo4;
    private JLabel technicianInfo5;
    private JLabel technicianInfo6;
    DefaultListModel<Technician> model = new DefaultListModel<>();

    public AdministerTechnicians() {
        list1.setModel(model);
        try{
            String getNames = "SELECT * FROM Employee WHERE isAdmin = 0";
            PreparedStatement names = connection.createPreparedStatement(con, getNames);
            ResultSet res = names.executeQuery();
            while(res.next()){
                Technician technician = new Technician(res.getInt("employee_id"), res.getString("first_name"), res.getString("last_name"), res.getInt("phone"), res.getString("address"), res.getString("email"), res.getString("password"));
                model.addElement(technician);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        list1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try{
                    Technician technician = list1.getSelectedValue();
                    boolean hired1 = true;
                    String hiredSentence = "SELECT isHired FROM Employee WHERE employee_id = "+technician.getEmployeeId();
                    PreparedStatement hired = connection.createPreparedStatement(con, hiredSentence);
                    ResultSet res = hired.executeQuery();
                    while(res.next()){
                        if(res.getInt("isHired") == 1){
                            hired1 = true;
                        }
                        else{
                            hired1 = false;
                        }
                    }
                    technicianInfo.setText("Employee ID:"+technician.getEmployeeId());
                    technicianInfo2.setText(technician.getFirstName()+" "+technician.getLastName());
                    technicianInfo3.setText("Email: "+technician.getEmail());
                    technicianInfo4.setText("Address: "+technician.getAddress());
                    technicianInfo5.setText("Phone: "+technician.getPhone());
                    if(hired1){
                        technicianInfo6.setText("This technician is currently employed");
                    }
                    else{
                        technicianInfo6.setText("This technician is not currently employed");
                    }
                }catch(Exception a){
                    System.out.println(a.getMessage());
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Usertypes");
                frame.setContentPane(new adminUsersFrontPage().panel1);
                frame.pack();
                frame.setSize(700, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        addTechnicianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Add technician");
                frame.setContentPane(new AddTechnician().panel1);
                frame.pack();
                frame.setSize(700, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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


        setTechnicianAsInactiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Technician technician = list1.getSelectedValue();
                    String sentence;
                    boolean hired1 = true;
                    String hiredSentence = "SELECT isHired FROM Employee WHERE employee_id = "+technician.getEmployeeId();
                    PreparedStatement hired = connection.createPreparedStatement(con, hiredSentence);
                    ResultSet res = hired.executeQuery();
                    while(res.next()){
                        if(res.getInt("isHired") == 1){
                            hired1 = true;
                        }
                        else{
                            hired1 = false;
                        }
                    }
                    if(hired1){
                        sentence = "UPDATE Employee SET isHired = 0 WHERE employee_id = "+technician.getEmployeeId();
                    }
                    else{
                        sentence = "UPDATE Employee SET isHired = 1 WHERE employee_id = "+technician.getEmployeeId();
                    }
                    PreparedStatement statement = connection.createPreparedStatement(con, sentence);
                    statement.executeUpdate();
                }catch(SQLException a){
                    System.out.println(a.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Administer Technicians");
        frame.setContentPane(new AdministerTechnicians().panel1);
        // frame.setContentPane(new AdministerAdmins().list1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }
}
