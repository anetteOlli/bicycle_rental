package GUI;

import Admin_App.EmployeeDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePassword {
    private JTextField email1;
    public JPanel panel1;
    private JTextField email2;
    private JButton confirmPasswordResetButton;
    private JButton backButton;
    EmployeeDatabase database = new EmployeeDatabase();

    public ChangePassword() {
        confirmPasswordResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.resetPassword(email1.getText(), email2.getText());
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("log in");
                frame.setContentPane(new LogInPage().panel1);
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
