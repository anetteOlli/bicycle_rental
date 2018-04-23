package GUI;

import Admin_App.EmployeeDatabase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePassword {
    private JTextField email1;
    public JPanel panel1;
    private JTextField email2;
    private JButton confirmPasswordResetButton;
    EmployeeDatabase database = new EmployeeDatabase();

    public ChangePassword() {
        confirmPasswordResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.resetPassword(email1.getText(), email2.getText());
            }
        });
    }
}
