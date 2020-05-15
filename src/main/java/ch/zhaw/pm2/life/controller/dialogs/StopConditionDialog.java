package ch.zhaw.pm2.life.controller.dialogs;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.Set;

public class StopConditionDialog extends LifeDialog<String> {
    private final ComboBox<String> lastStanding = new ComboBox<>();
    Set<GameObject> gameObjects = new HashSet<>();

    public StopConditionDialog() {
        super("Neue Stoppbedingung");
        setUp();
    }

    @Override
    protected void setUp() {
        GridPane grid = getGrid();
        setUpComboBox(gameObjects);
        grid.add(new Label("Beende die Simultion sobald X ausstirbt"), 0, 0);
        grid.add(lastStanding, 1, 0);
        this.getDialogPane().setContent(grid);
    }

    @Override
    protected String returnValue() {
        return lastStanding.getValue();
    }

    public void setUpComboBox(Set<GameObject> gameObjects) {
        gameObjects.stream()
                .filter(AnimalObject.class::isInstance)
                .map(AnimalObject.class::cast)
                .forEach(animalObject -> lastStanding.getItems().add(animalObject.getName()));
    }
}
