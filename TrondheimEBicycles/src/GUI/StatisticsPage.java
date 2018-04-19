package GUI;

import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StatisticsPage {
    private JButton backButton;
    public JPanel panel1;
    private JButton usageOfBikesPerButton;
    private JButton averageLengthPerTripButton;
    private JButton numberOfRepairsPerButton;

    public StatisticsPage() {
        usageOfBikesPerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BikeTripChart chart = new BikeTripChart("Bike trip staticstics", "Trips/month");
                chart.pack();
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
                chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        numberOfRepairsPerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BikeRepairChart chart = new BikeRepairChart("Bike repair statistics", "Repairs/month");
                chart.pack();
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
                chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        averageLengthPerTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KmTripChart chart = new KmTripChart("Trip length staticstics", "Km/trip");
                chart.pack();
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
                chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Front page");
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("View statistics");
        frame.setContentPane(new StatisticsPage().panel1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }
}
