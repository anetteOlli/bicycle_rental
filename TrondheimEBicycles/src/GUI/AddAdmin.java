package GUI;

import Admin_App.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class AddAdmin {
    private JButton cancelButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton confirmButton;
    public JPanel panel1;

    public AddAdmin() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Administer admins");
                frame.setContentPane(new AdministerAdmins().panel1);
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
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeDatabase database = new EmployeeDatabase();
                String firstname = textField2.getText();
                String surname = textField3.getText();
                String address = textField4.getText();
                String phon = textField1.getText();
                int phone = Integer.parseInt(phon);
                String email = textField5.getText();
                String password = "passord";
                int id = database.findRandomId();
                Admin admin = new Admin(id, firstname, surname, phone, address, email, password);
                if (database.regNewAdmin(admin)) {
                    showMessageDialog(null, firstname + " " + surname + " was registered as Admin!");
                } else {
                    showMessageDialog(null, "Something went wrong when registering, please try again");
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Add Admins");
        frame.setContentPane(new AddAdmin().panel1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }

}
