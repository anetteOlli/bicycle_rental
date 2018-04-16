package GUI;

import javax.swing.*;

public class Main extends JFrame {


    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Front page");
        frame.setContentPane(new AdminFront().adminFrontPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);






    }


}
