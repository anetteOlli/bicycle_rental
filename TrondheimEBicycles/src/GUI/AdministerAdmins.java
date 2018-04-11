package GUI;

import javax.swing.*;
import javax.swing.event.ListDataListener;
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
    private JButton viewAdminButton;
    private JButton addAdminButton;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    private static Random random = new Random();
    JList<String> list1 = new JList<>();
    DefaultListModel<String> model = new DefaultListModel<>();

    public AdministerAdmins() {
        list1.setModel(model);
        try{
            String getNames = "SELECT first_name, last_name FROM Employee WHERE isAdmin = 1";
            PreparedStatement names = connection.createPreparedStatement(con, getNames);
            ResultSet res = names.executeQuery();
            while(res.next()){
                model.addElement(res.getString("first_name") + " " + res.getString("last_name"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }



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


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Administer Admins");
        frame.setContentPane(new AdministerAdmins().panel1);
        frame.setContentPane(new AdministerAdmins().list1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }
}
