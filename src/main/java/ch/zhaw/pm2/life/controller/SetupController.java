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

/**
 * Controller for the setup window.
 */
public class SetupController {

    private static final Logger LOGGER = Logger.getLogger(SetupController.class.getName());

    @FXML private Pane rootPane;
    @FXML private Spinner<Integer> plantCount;
    @FXML private Spinner<Integer> meatEaterCount;
    @FXML private Spinner<Integer> plantEaterCount;

    /**
     * Shows the simulation window and starts the simulation itself.
     */
    @FXML
    public void startSimulation() {
        try {
            URL fxmlPath = getClass().getClassLoader().getResource("Window.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlPath);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Life - Simulation");

            LifeWindowController controller = loader.getController();
            controller.setSetupController(this);
            controller.initGame();
            controller.drawBoard();
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while setting up the scene", e);
        }
    }

    /**
     * Returns the amount of plants the user has chosen to start with.
     * @return {@link Integer}
     */
    public int getPlantCount() {
        return plantCount.getValue();
    }

    /**
     * Returns the amount of meat eaters the user has chosen to start with.
     * @return {@link Integer}
     */
    public int getMeatEaterCount() {
        return meatEaterCount.getValue();
    }

    /**
     * Returns the amount of plant eaters the user has chosen to start with.
     * @return {@link Integer}
     */
    public int getPlantEaterCount() {
        return plantEaterCount.getValue();
    }

}
