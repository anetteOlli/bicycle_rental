package GUI;

import Admin_App.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class AddTechnician {
    public JPanel panel1;
    private JButton cancelButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton confirmButton;

    public AddTechnician() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Administer technicians");
                frame.setContentPane(new AdministerTechnicians().panel1);
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
                String firstname = textField1.getText();
                String surname = textField2.getText();
                String address = textField3.getText();
                String phon = textField4.getText();
                int phone = Integer.parseInt(phon);
                String email = textField5.getText();
                String password = "passord";
                int id = database.findRandomId();
                if (firstname == "" || surname == "" || phone == 0 || email == "") {
                    System.out.println("All fields must be filled out!");
                    return;
                }
                Technician technician = new Technician(id, firstname, surname, phone, address, email, password);
                if (database.regNewTechnician(technician)) {
                    showMessageDialog(null, firstname + " " + surname + " was registered as Technician!");
                } else {
                    showMessageDialog(null, "Something went wrong when registering, please try again");
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Add Technicians");
        frame.setContentPane(new AddTechnician().panel1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }

}
