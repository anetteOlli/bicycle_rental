package GUI;

import javax.swing.*;

public class Main extends JFrame {


    public static void main(String[] args) {
        JFrame frame = new JFrame("Dock front page");
        frame.setContentPane(new DockFrontPage().dockFront);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);





    }


}
