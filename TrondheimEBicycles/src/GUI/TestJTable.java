package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class TestJTable implements ActionListener{
    private JFrame frame, frame1;
    //private JTextField textbox;
    private JComboBox comboBox;
    private JLabel label;
    private JButton button;
    private JPanel panel;
    private static JTable table;

    private String userName = "elisamop";
    private String password = "q9rmRFsi";
    private String driverName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/elisamop?user=elisamop&password=q9rmRFsi;";
    private String[] columnNames = {"Bicycle ID", "Dock ID", "Powerlevel", "Make", "Model", "Production date", "Bicycle status", "Total Km", "Trips", "Number of repairs"};

    public void createUI()
    {
        frame = new JFrame("Database Search Result");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        //textbox = new JTextField();
        comboBox = new JComboBox();
        comboBox.addItem("bicycle_id");
        comboBox.addItem("make");
        comboBox.addItem("model");
        //textbox.setBounds(120,30,150,20);
        comboBox.setBounds(120,30,150,20);
        label = new JLabel("Enter your bicyckle ID");
        label.setBounds(10, 30, 100, 20);
        button = new JButton("search");
        button.setBounds(120,130,150,20);
        button.addActionListener(this);

        frame.add(comboBox);
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
        //String textvalue = textbox.getText();
        String comboBoxvalue = (String) comboBox.getSelectedItem();
        String bicycleID= "";
        String DockID= "";
        String PowerLevel = "";
        String Make = "";
        String Model = "";
        String ProductionDate = "";
        String BicycleStatus = "";
        String TotalKM = "";
        String Trips = "";
        String NrOfRepairs = "";
        try
        {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, userName, password);
            //String sql = "select * from Bicycle where bicycle_id = "+textvalue;
            String sql = "select * from Bicycle ORDER BY " + comboBoxvalue;
            //String sql = "select * from Bicycle;";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i =0;
            while(rs.next())
            {
                bicycleID = rs.getString("bicycle_id");
                DockID = rs.getString("dock_id");
                PowerLevel = rs.getString("powerlevel");
                Make = rs.getString("make");
                Model = rs.getString("model");
                ProductionDate = rs.getString("production_date");
                BicycleStatus = rs.getString("bicycleStatus");
                TotalKM = rs.getString("totalKM");
                Trips = rs.getString("trips");
                NrOfRepairs = rs.getString("nr_of_repairs");
                model.addRow(new Object[]{bicycleID, DockID, PowerLevel, Make, Model, ProductionDate, BicycleStatus, TotalKM, Trips, NrOfRepairs});
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
        TestJTable sr = new TestJTable();
        sr.createUI();
    }
}
