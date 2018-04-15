
package GUI;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
public class GoogleMapsSample extends JPanel {

    private Stage stage;
    private WebView browser;
    private JFXPanel jfxPanel;
    private JButton swingButton;
    private WebEngine webEngine;
    final URL urlGoogleMaps = getClass().getResource("demo.html");
    //trondheim Torg latitude og longitude er satt som default.
    public double lat = 63.43049;
    public double lon = 10.39506;


    public GoogleMapsSample(){
        initComponents();
    }

    public static void main(String ...args){
        // Run this later:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame();

                frame.getContentPane().add(new GoogleMapsSample());

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

                stage = new Stage();

                stage.setTitle("Hello Java FX");
                stage.setResizable(true);

                Group root = new Group();
                Scene scene = new Scene(root,80,20);
                stage.setScene(scene);

                // Set up the embedded browser:
                browser = new WebView();
                webEngine = browser.getEngine();
                webEngine.load(urlGoogleMaps.toExternalForm());
                webEngine.setJavaScriptEnabled(true);



                ObservableList<Node> children = root.getChildren();
                children.add(browser);


                //this bit of code is supposed to listen for webAlerts: (ex. javascript code that
                // sends a webalert about something. See demo.html
                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(WebEvent<String> event) {
                        String event2 = new String(event.getData());
                        System.out.println(event.getData());

                        //sets the latitude and longitude if the map marker has been moved.
                        //the webalert that tells us this has happened starts with "location: "
                        //see demo.html for the webalert code
                        if(event2.startsWith("location:")){
                            String[] split = event2.split("[,]");
                            if(split[0] != null){
                                String latitude = split[0].replaceAll("[^0-9?!\\.]","");
                                lat = Double.parseDouble(latitude);
                            }if(split[1] !=null){
                                String longitude = split[1].replaceAll("[^0-9?!\\.]","");
                                lon = Double.parseDouble(longitude);
                            }
                        }
                    }
                });



                HBox toolbar  = new HBox();
                //toolbar.getChildren().addAll(latitude, longitude, update);

                boolean add = children.add(toolbar);

                jfxPanel.setScene(scene);

            }

        });

    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}