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
import java.util.ArrayList;


public class AdminAdmin {
    JFrame frame = new JFrame("Administer Admins");
    JList<Admin> list = new JList<>();
    DefaultListModel<Admin> model = new DefaultListModel<>();
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    ArrayList<String> id = new ArrayList<>();
    int i = 0;

    JPanel panel1 = new JPanel();
    JSplitPane splitpane = new JSplitPane();
    JButton backButton = new JButton("Back");
    JButton viewAdmin = new JButton("View admin");
    JButton addAdmin = new JButton("Add admin");
    JLabel adminInfo = new JLabel();
    JLabel adminInfo2 = new JLabel();
    JLabel adminInfo3 = new JLabel();
    JLabel adminInfo4 = new JLabel();
    JLabel adminInfo5 = new JLabel();
    JLabel adminInfo6 = new JLabel();


    public AdminAdmin() {
        list.setModel(model);
        try {
            String getInfo = "SELECT * FROM Employee WHERE isAdmin = 1";
            PreparedStatement names = connection.createPreparedStatement(con, getInfo);
            ResultSet res = names.executeQuery();
            while (res.next()) {
                Admin admin = new Admin(res.getInt("employee_id"), res.getString("first_name"), res.getString("last_name"), res.getInt("phone"), res.getString("address"), res.getString("email"), res.getString("password"));
                model.addElement(admin);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Admin admin = list.getSelectedValue();
                    adminInfo.setText("Employee ID:" + admin.getEmployeeId());
                    adminInfo2.setText(admin.getFirstName() + " " + admin.getLastName());
                    adminInfo3.setText("Email: " + admin.getEmail());
                    adminInfo4.setText("Address: " + admin.getAddress());
                    adminInfo5.setText("Phone: " + admin.getPhone());
                } catch (Exception a) {
                    System.out.println(a.getMessage());
                }
            }
        });

        splitpane.setLeftComponent(new JScrollPane(list));
        panel1.add(backButton);
        panel1.add(viewAdmin);
        panel1.add(addAdmin);
        panel1.add(adminInfo);
        panel1.add(adminInfo2);
        panel1.add(adminInfo3);
        panel1.add(adminInfo4);
        panel1.add(adminInfo5);
        splitpane.setRightComponent(panel1);
        frame.setSize(700, 500);
        frame.add(splitpane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        addAdmin.addActionListener(new ActionListener() {
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
        SwingUtilities.invokeLater(AdministerAdmins1::new);
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
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitpane = new JSplitPane();
        panel1.add(splitpane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        list = new JList();
        splitpane.setLeftComponent(list);
        viewAdmin = new JButton();
        viewAdmin.setText("View admin");
        panel1.add(viewAdmin, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        panel1.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addAdmin = new JButton();
        addAdmin.setText("Add admin");
        panel1.add(addAdmin, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
