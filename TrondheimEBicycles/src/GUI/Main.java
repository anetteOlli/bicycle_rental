package GUI;

import javax.swing.*;

public class Main extends JFrame {


    public static void main(String[] args) {

        JFrame frame = new JFrame("Log in");
        frame.setContentPane(new LogInPage().panel1);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}
