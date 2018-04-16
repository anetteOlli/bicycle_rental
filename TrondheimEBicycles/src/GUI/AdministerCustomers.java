package GUI;

import Admin_App.*;
import DatabaseHandler.DatabaseCleanup;
import DatabaseHandler.DatabaseConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javax.swing.JOptionPane.showMessageDialog;


public class AdministerCustomers {
    public JPanel panel1;
    private JButton backButton;
    private JButton generateNewPaymentcardButton;
    private JButton addNewCustomerButton;
    private JLabel customerInfo;
    private JLabel customerInfo2;
    private JLabel customerInfo3;
    private JLabel customerInfo4;
    private JLabel customerInfo5;
    private JLabel customerInfo6;
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();
    JList<Customer> list1;
    private JLabel cardInfo;
    private JLabel cardInfo2;
    DefaultListModel<Customer> model = new DefaultListModel<>();

    public AdministerCustomers() {
        list1.setModel(model);
        try {
            String getNames = "SELECT * FROM Customer";
            PreparedStatement names = connection.createPreparedStatement(con, getNames);
            ResultSet res = names.executeQuery();
            while (res.next()) {
                System.out.println("test1");
                Customer customer = new Customer(res.getInt("cust_id"), res.getInt("cardNumber"), res.getString("first_name"), res.getString("last_name"), res.getInt("phone"), res.getString("email"), res.getString("password"));
                System.out.println("Test2");
                model.addElement(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        list1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Customer customer = list1.getSelectedValue();
                    String sentence = "SELECT * FROM PaymentCard WHERE cust_id = " + customer.getCustId() + " AND active_status = 1";
                    PreparedStatement statement = connection.createPreparedStatement(con, sentence);
                    ResultSet res = statement.executeQuery();
                    res.next();
                    int balance = res.getInt("balance");
                    PaymentCard card = new PaymentCard(res.getInt("cardNumber"), res.getInt("cust_id"));
                    customerInfo.setText("Customer ID:" + customer.getCustId());
                    customerInfo2.setText(customer.getFirstName() + " " + customer.getLastName());
                    customerInfo3.setText("Email: " + customer.getCustEmail());
                    cardInfo.setText("Card number: " + card.getCardNumber());
                    cardInfo2.setText("Balance: " + balance);
                    customerInfo4.setText("Phone: " + customer.getCustPhone());
                } catch (Exception a) {
                    System.out.println(a.getMessage());
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Usertypes");
                frame.setContentPane(new adminUsersFrontPage().panel1);
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
        });
        addNewCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Add Customer");
                frame.setContentPane(new AddCustomer().panel1);
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
        });


        generateNewPaymentcardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Customer customer = list1.getSelectedValue();
                    CustomerDatabase database = new CustomerDatabase();
                    if (database.getNewCard(customer.getCustId())) {
                        showMessageDialog(null, "New card generated!");
                    } else {
                        showMessageDialog(null, "Something went wrong when registering a new card");
                    }
                } catch (Exception a) {
                    System.out.println(a.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Administer customers");
        frame.setContentPane(new AdministerCustomers().panel1);
        // frame.setContentPane(new AdministerAdmins().list1);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 500);
    }
}

