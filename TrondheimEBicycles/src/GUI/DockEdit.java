package GUI;

import Admin_App.DockingStation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static javax.swing.JOptionPane.*;

public class DockEdit {
    public JPanel dockEditPanel;
    public JPanel southPanel;
    private JButton backButton;
    private JButton confirmButton;
    public JPanel northPanel;
    private JComboBox dockComboBox;
    private JLabel informationLabel;
    private JPanel midPanel;
    private JLabel oldNameLabel;
    private JLabel oldStatusLabel;
    private JButton deletebutton;
    private JTextField newNameTextField;
    private JLabel infoNameLabel;
    private DockingStation dock;
    private int dockId;


    public DockEdit() {
        dock = new DockingStation();
        dockId = 0;
        ArrayList<Integer> dockN = dock.getAllDockingStationIDs();
        for (int i = 0; i < dockN.size(); i++) {
            dockComboBox.addItem(dockN.get(i));
        }


        backButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Dockstation front page");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new DockFrontPage().dockFront);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        dockComboBox.addItemListener(new ItemListener() {
            /**
             * Invoked when an item has been selected or deselected by the user.
             * The code written for this method performs the operations
             * that need to occur when an item is selected (or deselected).
             *
             * @param e
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object n = e.getItem();
                if (n instanceof Integer) {
                    dockId = (int) n;
                }
                oldNameLabel.setText(dock.getName(dockId));
                String active = "inactive";
                if (dock.getDockingStationStatus(dockId) > 0) {
                    active = "active";
                }
                oldStatusLabel.setText(active);


            }
        });
        dockComboBox.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                midPanel.setVisible(true);
                //her skal kartet genereres

            }
        });
        deletebutton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = showConfirmDialog(null, "do you really want to delete ", "confirmDialog", YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean sucess = dock.setDockingStationActivity(false, dockId);
                    if (sucess) {
                        showMessageDialog(null, dockId + " have been deleted");
                    } else {
                        showMessageDialog(null, "database error, deletion could not be made");
                    }
                }
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = dock.setName(dockId, newNameTextField.getText());
                if (success) {
                    showMessageDialog(null, "dockid: " + dockId + " have changed name");
                } else {
                    showMessageDialog(null, "database error, could not update name");
                }


            }
        });

    }



}
