package ch.zhaw.pm2.life.controller.dialogs;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Set;

public class StopConditionDialog extends Dialog<String> {
    private final GridPane grid = new GridPane();
    private final ComboBox<String> lastStanding = new ComboBox<>();

    public StopConditionDialog(Set<GameObject> gameObjects) {
        this.setTitle("Neue Stoppbedingung");
        setUp(gameObjects);
    }

    private void setUp(Set<GameObject> gameObjects) {
        setUpGrid();
        setUpButtons();
        setUpComboBox(gameObjects);
        grid.add(new Label("Beende die Simultion sobald X ausstirbt"), 0, 0);
        grid.add(lastStanding, 1, 0);
        this.getDialogPane().setContent(grid);
    }

    private void setUpComboBox(Set<GameObject> gameObjects) {
        gameObjects.stream()
                .filter(AnimalObject.class::isInstance)
                .map(AnimalObject.class::cast)
                .forEach(animalObject -> lastStanding.getItems().add(animalObject.getName()));
    }

    private void setUpGrid() {
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
    }

    private void setUpButtons() {
        ButtonType confirm = new ButtonType("Bestaetigen", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(confirm, cancel);

        this.setResultConverter(param -> {
            if (param == confirm) {
                return lastStanding.getValue();
            }
            return null;
        });
    }
}
