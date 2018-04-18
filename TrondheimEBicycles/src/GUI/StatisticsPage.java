package GUI;

import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class StatisticsPage {
    private JButton backButton;
    private JPanel panel1;
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
