package GUI;

import Admin_App.BikeDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePrice {
    public JPanel panel;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JLabel title;
    private JLabel model;
    private JLabel price;
    private JButton conPrice;
    private JButton backButton;
    BikeDatabase bd = new BikeDatabase();

    public ChangePrice() {
        conPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String model = comboBox1.getSelectedItem().toString();
                int newPrice = Integer.parseInt(textField1.getText());
                bd.changePrice(newPrice ,model);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Front page");
                frame.setContentPane(new AdminFront().adminFrontPanel);
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
}
