package GUI;

import Admin_App.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.time.*;
import java.util.*;
public class RegBicycle {
    public JPanel regBike;
    private JComboBox comboBox1;
    private JButton confirmButton;
    private JButton backButton;
    private JSpinner spinner1;
    private JTextField textField1;

    public RegBicycle() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Bicycle");
                frame.setContentPane(new BikeMain().bikeMain);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
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
                JFrame frame = new JFrame("Bikes Registered");
                frame.setContentPane(new RegBikeConfirm().regBikeConfirm);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
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
                Date date = new Date();

                BikeDatabase database = new BikeDatabase();
                Integer currentValue = (Integer)spinner1.getValue();
                String make = textField1.getText();
                String model = comboBox1.getSelectedItem().toString();
                Bicycle bicycle = new Bicycle(make, "not employed");
                if(model=="Regular") {
                    database.regRegular(bicycle, currentValue);
                }else if (model=="Cargo") {
                    database.regCargo(bicycle, currentValue);
                }else{
                    database.regFamily(bicycle, currentValue);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Register Bike");
        frame.setContentPane(new RegBicycle().regBike);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
