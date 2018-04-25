package GUI;
import Admin_App.BikeDatabase;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Demonstrates that we were capable of injecting javascript code while webview was inside JavaFX
 * It was not possible to reproduce the result in swing, as we would get nullpointer exception whenever we attempted to do so
 * (even when we did webview.executeScript in RunLater parts, we added pageloaderListeners and many other attempts to wait for page to finish
 * loading before webview.executeScript but result was always the same)
 * error message we got "Exception in thread "JavaFX Application Thread" netscape.javascript.JSException: TypeError:
 * document.addBikes is not a function. (In 'document.addBikes(63.43049,10.39506,1.0)', 'document.addBikes' is undefined)"
 */
public class MapFX extends Application {


    private Scene scene;
    MyBrowser myBrowser;
    double tingLat = 0;
    double tingLong = 0;
    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;


        myBrowser = new MyBrowser();
        Scene scene = new Scene(myBrowser);

        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(600);
        stage.show();
    }


    class MyBrowser extends Pane {

        BikeDatabase bikeDatabase = new BikeDatabase();
        double[][] bikeplaces = bikeDatabase.getAllBicycleLovations();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();


        public MyBrowser() {


            final URL urlGoogleMaps = getClass().getResource("javaFXMap.html");
            webEngine.load(urlGoogleMaps.toExternalForm());

            webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                @Override
                public void handle(WebEvent<String> e) {
                    System.out.println(e.toString());
                }
            });

            getChildren().add(webView);


            Button update = new Button("Update");

            update.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {

                    //we never worked past this, as we could never inject javascript code, once webview was inside a swing panel.
                    //if we were to work past this point additional coding would be necessary: first off we would need some sort of mechanism to
                    // automatically update the map
                    //then we would need some code to remove previous markers.
                    //and then some javascript code in the javaFXMap.html to detect when the user was clicking on a marker
                    for(int teller = 0; teller < bikeplaces.length; teller++){
                        webEngine.executeScript("document.addBikes("+bikeplaces[teller][1] +"," +bikeplaces[teller][2]+ ","+bikeplaces[teller][0]+ ");");
                    }

                }
            });



            HBox toolbar = new HBox();
            toolbar.getChildren().addAll(update);

            getChildren().addAll(toolbar);

            webView.getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {
                @Override
                public void handle(WebEvent<String> event) {
                    String event2 = new String(event.getData());
                    System.out.println("ser en event");
                    System.out.println(event.getData());


                    if(event2.startsWith("location:")){
                        System.out.println("ser location");
                        String[] split = event2.split("[,]");
                        if(split[0] != null){
                            String latitude = split[0].replaceAll("[^0-9?!\\.]","");
                            tingLat = Double.parseDouble(latitude);
                        }if(split[1] !=null){
                            String longitude = split[1].replaceAll("[^0-9?!\\.]","");
                            tingLong = Double.parseDouble(longitude);
                        }
                        System.out.println("tinglat: " + tingLat);
                        System.out.println("tingLong: " + tingLong);
                    }


                }
            });
        }
    }

}