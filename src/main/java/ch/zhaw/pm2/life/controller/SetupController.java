package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.parser.ConfigParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the setup window.
 */
public class SetupController {

    private static final Logger logger = Logger.getLogger(SetupController.class.getName());

    @FXML private Pane rootPane;
    @FXML private GridPane pane;

    private HashMap<GameObject, Spinner<Integer>> gameObjectMap = new HashMap<>();

    @FXML
    public void initialize() {
        try {
            ConfigParser config = ConfigParser.getInstance();
            List<GameObject> gameObjects = config.parseObjects();

            int index = 0;
            for (GameObject gameObject : gameObjects) {
                Label name = new Label();
                name.setText("Anzahl " + gameObject.getName());
                Spinner<Integer> amount = new Spinner<>();
                amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
                gameObjectMap.put(gameObject, amount);
                pane.add(name, 0, index);
                pane.add(amount, 1, index);
                index++;
                pane.addRow(index);
            }
            pane.setVgap(10);
            pane.setHgap(100);


        } catch (LifeException e) {
            logger.log(Level.SEVERE, "An error occured while parsing the config", e);
        }
    }

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
            logger.log(Level.SEVERE, "Error while setting up the scene", e);
        }
    }

    public Map<GameObject, Integer> getGameObjects() {
        Map<GameObject, Integer> gameObjects = new HashMap<>();
        gameObjectMap.forEach((gameObject, integerSpinner) -> gameObjects.put(gameObject, integerSpinner.getValue()));
        return gameObjects;
    }
}
