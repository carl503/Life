package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.controller.SetupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class is the main class of the application.
 */
public class App extends Application {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    /**
     * Default constructor.
     */
    public App() {
        try (InputStream config = App.class.getClassLoader().getResourceAsStream("log.properties")) {
            LogManager.getLogManager().readConfiguration(config);
        } catch (IOException e) {
            logger.log(Level.CONFIG, "No log.properties", e);
        }
    }

    /**
     * Entry point for the application.
     * @param args Array of {@link String}.
     */
    public static void main(String[] args) {
        logger.info("Starting the application");
        launch(args);
        logger.info("Closing the application");
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlSetUp = getClass().getClassLoader().getResource("Setup.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlSetUp);
            Scene scene = new Scene(loader.load());
            SetupController controller = loader.getController();
            controller.setHostServices(getHostServices());
            primaryStage.setTitle("Setup");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load the fxml file", e);
        }
    }

}
