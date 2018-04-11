
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
    double lat;
    double lon;


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

                webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(WebEvent<String> e) {
                        System.out.println(e.toString());
                    }
                });


                ObservableList<Node> children = root.getChildren();
                children.add(browser);
                /*
                lat = 63.254650;
                lon = 10.238997;
                String posistion = "var rhodos = {lat:" + lat+ ", lng: " + lon + "}";
                webEngine.executeScript(posistion);
                webEngine.executeScript("var marker = new google.maps.Marker({ position: rhodos, map: map });");
                */


                final TextField latitude = new TextField("" + 63.254650 * 1.00007);
                final TextField longitude = new TextField("" + 10.238997 * 1.00007);
                Button update = new Button("Update");
                update.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

                    @Override
                    public void handle(javafx.event.ActionEvent arg0) {
                        lat = Double.parseDouble(latitude.getText());
                        lon = Double.parseDouble(longitude.getText());

                        System.out.printf("%.2f %.2f%n", lat, lon);

                        webEngine.executeScript("" +
                                "window.lat = " + lat + ";" +
                                "window.lon = " + lon + ";" +
                                "document.goToLocation(window.lat, window.lon);"
                        );
                    }
                });

                HBox toolbar  = new HBox();
                toolbar.getChildren().addAll(latitude, longitude, update);

                boolean add = children.add(toolbar);

                jfxPanel.setScene(scene);

            }

        });

    }

}