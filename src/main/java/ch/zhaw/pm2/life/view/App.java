package ch.zhaw.pm2.life.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class.
 * @author lubojcar
 */
public class App extends Application {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    /**
     * Entry point for the application.
     * @param args Array of {@link String}.
     */
    public static void main(String[] args) {
        LOGGER.info("Starting the application");
        launch(args);
        LOGGER.info("Closing the application");
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlSetUp = getClass().getClassLoader().getResource("Setup.fxml");
            Scene scene = new Scene(new FXMLLoader(fxmlSetUp).load());
            primaryStage.setTitle("Setup");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to load the fxml file", e);
        }
    }
}
