package GUI;

import Admin_App.Admin;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.sql.*;

public class AdministerAdmins2 {
    private JPanel panel1;
    private JPanel panel;
    private JList<Admin> list;
    DefaultListModel<Admin> model = new DefaultListModel<>();
    private JButton backButton;
    private JButton viewAdmin;
    private JButton addAdmin;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JSplitPane splitpane;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    public  void writeToList(){
        list.setModel(model);
        try{
            String getInfo = "SELECT * FROM Employee WHERE isAdmin = 1";
            PreparedStatement names = connection.createPreparedStatement(con, getInfo);
            ResultSet res = names.executeQuery();
            while(res.next()){
                Admin admin = new Admin(res.getInt("employee_id"), res.getString("first_name"), res.getString("last_name"), res.getInt("phone"), res.getString("address"), res.getString("email"), res.getString("password"));
                model.addElement(admin);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        AdministerAdmins2 administer = new AdministerAdmins2();

        JFrame frame = new JFrame("Administer Admins");
        frame.setContentPane(new AdministerAdmins2().list);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
        administer.writeToList();

    }

}
