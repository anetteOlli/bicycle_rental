package GUI;


import Admin_App.DockingStation;
import com.sun.javafx.application.PlatformImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;




/**
 * SwingFXWebView
 */
public class DockStatusMap extends JPanel {

    private Stage stage;
    private WebView browser;
    private JFXPanel jfxPanel;
    private JButton swingButton;
    private WebEngine webEngine;
    private String google = "https://maps.googleapis.com/maps/api/staticmap?center=Trondheim,+Norge&zoom=13&scale=1&size=640x600&maptype=roadmap&format=png&visual_refresh=true&markers=color:blue";




    public DockStatusMap(){
        /*
        DockingStation dock = new DockingStation();
        double[][] dockLocation = dock.getAllDockingStationLocation();
        //starts generating URL adress:
        for(int row = 0; row < dockLocation.length; row++){
            if(dockLocation[row][1] >0 && dockLocation[row][2] >0){
                google += "|" + dockLocation[row][1] +","  + dockLocation[row][2];
            }
        }

        for(int i = 0; i < 5; i++){
            System.out.println("dritt");
        }
        DockingStation dock = new DockingStation();
        double[][] dockLocation = dock.getAllDockingStationLocation();
        */
        initComponents();
    }



    public static void main(String ...args){
        // Run this later:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame();

                frame.getContentPane().add(new DockStatusMap());

                frame.setMinimumSize(new Dimension(640, 600));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

            }
        });
    }

    private void initComponents(){

        jfxPanel = new JFXPanel();
        createScene();

        setLayout(new BorderLayout());
        add(jfxPanel, BorderLayout.CENTER);

        swingButton = new JButton();

        swingButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        webEngine.reload();


                    }
                });
            }
        });
        swingButton.setText("Reload");

        add(swingButton, BorderLayout.SOUTH);

    }


    /**
     * createScene
     *
     * Note: Key is that Scene needs to be created and run on "FX user thread"
     *       NOT on the AWT-EventQueue Thread
     *
     */
    private void createScene() {
        PlatformImpl.startup(new Runnable() {
            @Override
            public void run() {
                DockingStation dock = new DockingStation();
                double[][] dockLocation = dock.getAllDockingStationLocation();
                //starts generating URL adress:
                for(int row = 0; row < dockLocation.length; row++){
                    if(dockLocation[row][1] >0 && dockLocation[row][2] >0){
                        google += "|" + dockLocation[row][1] +","  + dockLocation[row][2];
                    }
                }
                stage = new Stage();

                stage.setTitle("Hello Java FX");
                stage.setResizable(true);

                Group root = new Group();
                Scene scene = new Scene(root,80,20);
                stage.setScene(scene);

                // Set up the embedded browser:
                browser = new WebView();
                webEngine = browser.getEngine();
                webEngine.load(google);
                webEngine.setJavaScriptEnabled(true);



                ObservableList<Node> children = root.getChildren();
                children.add(browser);

                HBox toolbar  = new HBox();
                //toolbar.getChildren().addAll(latitude, longitude, update);

                boolean add = children.add(toolbar);

                jfxPanel.setScene(scene);
                //this sets the javaFX thread to exit, once the window is being called exit.
                Platform.setImplicitExit(false);

            }

        });

    }
}