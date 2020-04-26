package ch.zhaw.pm2.life.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting the application");
        launch(args);
        LOGGER.info("Closing the application");
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlPath = getClass().getClassLoader().getResource("Window.fxml");
            primaryStage.setScene(new Scene(new FXMLLoader(fxmlPath).load()));
            primaryStage.setTitle("Life - simulation");
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to load the fxml file", e);
        }
    }


}
