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
    }
}
