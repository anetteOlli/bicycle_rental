package GUI;

import Admin_App.SendMail;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SendCustomerMail {
    private JTextArea textArea1;
    private JButton sendMailButton;
    private JButton HOMEButton;
    private JTextField subject;
    public JPanel panel1;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    ArrayList<String> emailList = new ArrayList<String>();

    public SendCustomerMail() {
        sendMailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String sentence = "SELECT * FROM Customer";
                    PreparedStatement statement = connection.createPreparedStatement(con, sentence);
                    ResultSet res = statement.executeQuery();
                    while(res.next()){
                        emailList.add(res.getString("email"));
                    }
                    for(int i = 0; i < emailList.size(); i++){
                        SendMail send = new SendMail(emailList.get(i), subject.getText(), textArea1.getText());
                    }
                }catch(SQLException a){
                    System.out.println(a.getMessage());
                }
            }
        });
        HOMEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Send newsletter");
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
}
