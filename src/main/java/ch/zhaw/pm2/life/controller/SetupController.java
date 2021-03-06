package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.parser.ConfigParser;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

    /**
     * Defines the minimum height of the setup window.
     */
    public static final int HEIGHT_BOXING = 50;
    private static final int WIDTH_BOXING = 20;
    private static final Logger logger = Logger.getLogger(SetupController.class.getName());
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final HashMap<GameObject, Spinner<Integer>> gameObjectMap = new HashMap<>();
    @FXML private Pane rootPane;
    @FXML private GridPane pane;

    private HostServices hostServices;

    /**
     * Initializes the set up controller with {@link GameObject}
     * and a Spinner to chose the amount to spawn.
     */
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
                if (gameObject instanceof AnimalObject) {
                    comboBox.getItems().add(gameObject.getName());
                }
            }
            comboBox.getSelectionModel().selectFirst();
            pane.add(new Label("Stoppen sobald nur noch"), 0, index);
            pane.add(comboBox, 1, index);
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
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(fxmlPath);
            Pane mainWindowRootPane = loader.load();
            stage.setScene(new Scene(mainWindowRootPane));
            stage.setTitle("Life - Simulation");
            stage.setMinHeight(mainWindowRootPane.getMinHeight() + HEIGHT_BOXING);
            stage.setMinWidth(mainWindowRootPane.getMinWidth() + WIDTH_BOXING);

            LifeWindowController controller = loader.getController();
            controller.setSetupController(this);
            controller.initGame();
            controller.initEditMenu();
            controller.drawBoard();
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while setting up the scene", e);
        }
    }

    /**
     * Returns a map of all {@link GameObject} and the amount that should be spawned.
     * @return map of game objects.
     */
    public Map<GameObject, Integer> getGameObjects() {
        Map<GameObject, Integer> gameObjects = new HashMap<>();
        gameObjectMap.forEach((gameObject, integerSpinner) -> gameObjects.put(gameObject, integerSpinner.getValue()));
        return gameObjects;
    }

    /**
     * Returns the species name that should be monitored to stop the simulation.
     * @return String species name.
     */
    public String getStopCondition() {
        return comboBox.getValue();
    }

    /**
     * Sets the host services
     * @param hostServices {@link HostServices}
     */
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    /**
     * Returns the host services
     * @return HostServices
     */
    public HostServices getHostServices() {
        return hostServices;
    }
}
