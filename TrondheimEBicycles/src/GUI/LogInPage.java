package GUI;

import Admin_App.PasswordStorage;
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

import static javax.swing.JOptionPane.showMessageDialog;

public class LogInPage {
    public JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton logInButton;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    public LogInPage() {
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    PasswordStorage storage = new PasswordStorage();
                    boolean isAdmin = false;
                    boolean isTechnician = true;
                    String username = textField1.getText();
                    String password = passwordField1.getText();
                    String sentence = "SELECT * FROM Employee WHERE email = ?";
                    PreparedStatement statement = connection.createPreparedStatement(con, sentence);
                    statement.setString(1, username);
                    ResultSet res = statement.executeQuery();
                    if(res.next()){
                        if(storage.verifyPassword(password, res.getString("password"))){
                            if(res.getInt("isAdmin") == 1){
                                isAdmin = true;
                            }
                        }else{
                            showMessageDialog(null, "Incorrect password");
                            isTechnician = false;

                        }

                    }else{
                        showMessageDialog(null, "Unknown username");
                        isTechnician = false;
                    }
                    if(isAdmin){
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
                    }else if(isTechnician){
                        JFrame frame = new JFrame("Administer Repairs");
                        frame.setContentPane(new TRegisterRepairFrontPage().panel1);
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
                }catch(SQLException a){
                    System.out.println(a.getMessage());
                }catch (PasswordStorage.InvalidHashException h){
                    System.out.println(h.getMessage());
                }catch(PasswordStorage.CannotPerformOperationException o){
                    System.out.println(o.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Log in");
        frame.setContentPane(new LogInPage().panel1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }
}
