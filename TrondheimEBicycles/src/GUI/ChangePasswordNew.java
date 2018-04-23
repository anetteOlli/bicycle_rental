package GUI;

import Admin_App.EmployeeDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ChangePasswordNew {
    private JTextField textField1;
    public JPanel panel1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JPasswordField passwordField3;
    private JButton confirmPasswordChangeButton;
    private JButton HOMEButton;
    EmployeeDatabase database = new EmployeeDatabase();

    public ChangePasswordNew() {
        confirmPasswordChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(textField1.getText());
                System.out.println(passwordField1.getText());
                System.out.println(passwordField2.getText());
                System.out.println(passwordField3.getText());
                database.changePassword(textField1.getText(), passwordField1.getText(), passwordField2.getText(), passwordField3.getText());
            }
        });
        HOMEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Send newsletter");
                frame.setContentPane(new AdminFront().adminFrontPanel);
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
    }
}
