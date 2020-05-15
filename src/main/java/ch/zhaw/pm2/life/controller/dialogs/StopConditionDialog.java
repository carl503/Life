package ch.zhaw.pm2.life.controller.dialogs;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.Set;

/**
 * Allows the user to change the stop condition
 */
public class StopConditionDialog extends LifeDialog<String> {
    private final ComboBox<String> choice = new ComboBox<>();
    private final Set<GameObject> gameObjects = new HashSet<>();

    /**
     * Creates the stop condition dialog
     */
    public StopConditionDialog() {
        super("Neue Stoppbedingung");
        setUp();
    }

    @Override
    protected void setUp() {
        GridPane grid = getGrid();
        setUpComboBox(gameObjects);
        grid.add(new Label("Beende die Simultion sobald X ausstirbt"), 0, 0);
        grid.add(choice, 1, 0);
        this.getDialogPane().setContent(grid);
    }

    @Override
    protected String returnValue() {
        return choice.getValue();
    }

    /**
     * Take a set of {@link GameObject} to add them to the combo box
     * @param gameObjects {@link GameObject} to add
     */
    public void setUpComboBox(Set<GameObject> gameObjects) {
        gameObjects.stream()
                .filter(AnimalObject.class::isInstance)
                .map(AnimalObject.class::cast)
                .forEach(animalObject -> choice.getItems().add(animalObject.getName()));
        choice.getSelectionModel().selectFirst();
    }
}
