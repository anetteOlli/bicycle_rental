package GUI;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import DatabaseHandler.*;
import Admin_App.*;

public class AdministerAdmins {
    private JButton backButton;
    public JPanel panel1;
    private JButton setAdminAsInactiveButton;
    private JButton addAdminButton;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();
    JList<Admin> list1;
    private JLabel adminInfo;
    private JLabel adminInfo2;
    private JLabel adminInfo3;
    private JLabel adminInfo4;
    private JLabel adminInfo5;
    private JLabel adminInfo6;
    DefaultListModel<Admin> model = new DefaultListModel<>();

    public AdministerAdmins() {
        list1.setModel(model);
        try {
            String getNames = "SELECT * FROM Employee WHERE isAdmin = 1";
            PreparedStatement names = connection.createPreparedStatement(con, getNames);
            ResultSet res = names.executeQuery();
            while (res.next()) {
                Admin admin = new Admin(res.getInt("employee_id"), res.getString("first_name"), res.getString("last_name"), res.getInt("phone"), res.getString("address"), res.getString("email"), res.getString("password"));
                model.addElement(admin);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        list1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Admin admin = list1.getSelectedValue();
                    boolean hired1 = true;
                    String hiredSentence = "SELECT isHired FROM Employee WHERE employee_id = " + admin.getEmployeeId();
                    PreparedStatement hired = connection.createPreparedStatement(con, hiredSentence);
                    ResultSet res = hired.executeQuery();
                    while (res.next()) {
                        if (res.getInt("isHired") == 1) {
                            hired1 = true;
                        } else {
                            hired1 = false;
                        }
                    }
                    adminInfo.setText("Employee ID:" + admin.getEmployeeId());
                    adminInfo2.setText(admin.getFirstName() + " " + admin.getLastName());
                    adminInfo3.setText("Email: " + admin.getEmail());
                    adminInfo4.setText("Address: " + admin.getAddress());
                    adminInfo5.setText("Phone: " + admin.getPhone());
                    if (hired1) {
                        adminInfo6.setText("This administrator is currently employed");
                    } else {
                        adminInfo6.setText("This administrator is not currently employed");
                    }
                } catch (Exception a) {
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
        addAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Add admin");
                frame.setContentPane(new AddAdmin().panel1);
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


        setAdminAsInactiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Admin admin = list1.getSelectedValue();
                    String sentence;
                    boolean hired1 = true;
                    String hiredSentence = "SELECT isHired FROM Employee WHERE employee_id = " + admin.getEmployeeId();
                    PreparedStatement hired = connection.createPreparedStatement(con, hiredSentence);
                    ResultSet res = hired.executeQuery();
                    while (res.next()) {
                        if (res.getInt("isHired") == 1) {
                            hired1 = true;
                        } else {
                            hired1 = false;
                        }
                    }
                    if (hired1) {
                        sentence = "UPDATE Employee SET isHired = 0 WHERE employee_id = " + admin.getEmployeeId();
                    } else {
                        sentence = "UPDATE Employee SET isHired = 1 WHERE employee_id = " + admin.getEmployeeId();
                    }
                    PreparedStatement statement = connection.createPreparedStatement(con, sentence);
                    statement.executeUpdate();
                } catch (SQLException a) {
                    System.out.println(a.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Administer Admins");
        frame.setContentPane(new AdministerAdmins().panel1);
        // frame.setContentPane(new AdministerAdmins().list1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }

}
