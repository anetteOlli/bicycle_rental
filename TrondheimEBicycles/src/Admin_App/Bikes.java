package Admin_App;

import java.sql.*;
import DatabaseHandler.*;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class Bikes extends javax.swing.JFrame {
    static DatabaseCleanup cleaner = new DatabaseCleanup();
    static DatabaseConnection connection = new DatabaseConnection();
    private static Connection con = connection.getConnection();

    String query = "SELECT bicycle_id, make, model, bicycleStatus, production_date, dock_id FROM Bicycle;";
    Connection connect = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    ResultSet rs2 = null;

    public void populateJList() throws SQLException {
        DefaultListModel model = new DefaultListModel(); //create a new list model
        JList list = new JList();
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query); //run your query

        while (resultSet.next()) //go through each row that your query returns
        {
            String bicycle_id = resultSet.getString("bicycle_id"); //get the element in column "item_code"
            model.addElement(bicycle_id); //add each item to the model
            String make = resultSet.getString("make");
            model.addElement(make);
            String modell = resultSet.getString("model");
            model.addElement(modell);
            String status = resultSet.getString("bicycleStatus");
            model.addElement(status);
            int date = resultSet.getInt("production_date");
            model.addElement(date);
            int dock = resultSet.getInt("dock_id");
            model.addElement(dock);
        }
        list.setModel(model);

        resultSet.close();
        statement.close();

    }
}