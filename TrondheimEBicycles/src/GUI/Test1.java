package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class Test1 extends JPanel implements ActionListener{
    String[] OrderList = {"bicycle_id", "make", "modell", "bicycleStatus", "production_date", "nr_of_repairs"};

    JFrame frame, frame1;
    JComboBox combobox = new JComboBox(OrderList);

    JLabel label;
    JButton button;
    JPanel panel;
    static JTable table;

    String driverName = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/quannt";
    String userName = "quannt";
    String password = "roBv7vFp";
    String[] columnNames = {"bicycle_id", "make", "model", "bicycleStatus", "production_date", "dock"};



    String sort = (String)combobox.getSelectedItem();

    public void createUI()
    {
        frame = new JFrame("Database Search Result");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        combobox = new JComboBox();
        combobox.setBounds(120,30,150,20);
        label = new JLabel("Enter your roll no");
        label.setBounds(10, 30, 100, 20);
        button = new JButton("search");
        button.setBounds(120,130,150,20);
        button.addActionListener(this);

        frame.add(combobox);
        frame.add(label);
        frame.add(button);
        frame.setVisible(true);
        frame.setSize(500, 400);
    }

    public void actionPerformed(ActionEvent ae)
    {
        button = (JButton)ae.getSource();
        System.out.println("Showing Table Data.......");
        showTableData();
    }

    public void showTableData()
    {

        frame1 = new JFrame("Database Search Result");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLayout(new BorderLayout());
//TableModel tm = new TableModel();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
//DefaultTableModel model = new DefaultTableModel(tm.getData1(), tm.getColumnNames());
//table = new JTable(model);
        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        String sort = combobox.getSelectedItem().toString();
        String bicycle_id= "";
        String make= "";
        String modell = "";
        String bicycleStatus = "";
        String production_date= "";
        String nr_of_repairs= "";
        try
        {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, userName, password);
            String sql = "select * from Bicycle GROUP BY = "+sort+ "ORDER BY"+sort+" DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i =0;
            if(rs.next())
            {
                bicycle_id = rs.getString("bicycle_id");
                make = rs.getString("make");
                modell = rs.getString("model");
                bicycleStatus = rs.getString("bicycleStatus");
                production_date = rs.getString("production_date");
                nr_of_repairs = rs.getString("nr_of_repairs");
                model.addRow(new Object[]{bicycle_id, make, modell, bicycleStatus, production_date, nr_of_repairs});
                i++;
            }
            if(i <1)
            {
                JOptionPane.showMessageDialog(null, "No Record Found","Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            if(i ==1)
            {
                System.out.println(i+" Record Found");
            }
            else
            {
                System.out.println(i+" Records Found");
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400,300);
    }

    public static void main(String args[])
    {
        Test1 sr = new Test1();
        sr.createUI();
    }
}