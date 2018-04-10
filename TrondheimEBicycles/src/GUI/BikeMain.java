package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BikeMain {
    private JPanel bikeMain;
    private JButton editBike;
    private JButton regBike;
    private JButton regRep;
    private JButton showStatus;

    public BikeMain() {

        regBike.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Register New Bike");
                frame.setContentPane(new RegBike().oki);
                frame.setVisible(true);

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bicycle Front Page");
        frame.setContentPane(new BikeMain().regBike);
        frame.setContentPane(new BikeMain().editBike);
        frame.setContentPane(new BikeMain().regRep);
        frame.setContentPane(new BikeMain().showStatus);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
