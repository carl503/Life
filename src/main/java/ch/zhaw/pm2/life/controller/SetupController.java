package ch.zhaw.pm2.life.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupController {

    private final Logger logger = Logger.getLogger(SetupController.class.getName());

    @FXML private Pane rootPane;
    @FXML private Spinner<Integer> plantCount;
    @FXML private Spinner<Integer> meatEaterCount;
    @FXML private Spinner<Integer> plantEaterCount;

    @FXML
    public void startSimulation() {
        try {
            URL fxmlPath = getClass().getClassLoader().getResource("Window.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlPath);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Life - Simulation");
            stage.show();
            LifeWindowController controller = loader.getController();
            controller.setSetupController(this);
            controller.initGame();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while setting up the scene", e);
        }
    }

    public int getPlantCount() {
        return plantCount.getValue();
    }

    public int getMeatEaterCount() {
        return meatEaterCount.getValue();
    }

    public int getPlantEaterCount() {
        return plantEaterCount.getValue();
    }
}
