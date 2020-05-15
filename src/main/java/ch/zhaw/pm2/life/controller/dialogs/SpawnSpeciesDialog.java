package ch.zhaw.pm2.life.controller.dialogs;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.animal.Herbivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;
import ch.zhaw.pm2.life.util.ValidationUtil;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.Set;

/**
 * Allows the user to create new additional species.
 */
public class SpawnSpeciesDialog extends LifeDialog<Set<GameObject>> {

    private final TextField name = new TextField();
    private final TextField energy = new TextField();
    private final Spinner<Integer> amount = new Spinner<>();
    private final ColorPicker color = new ColorPicker();
    private final ComboBox<Type> type = new ComboBox<>();

    /**
     * Creates the spawn species dialog.
     * @param title title for the dialog.
     */
    public SpawnSpeciesDialog(String title) {
        super(title);
        setUp();
    }

    @Override
    protected void setUp() {
        GridPane grid = getGrid();
        setUpGrid(grid);

        name.setTextFormatter(ValidationUtil.getNameFormatter());
        energy.setTextFormatter(ValidationUtil.getEnergyFormatter());
        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        type.getItems().addAll(Type.CARNIVORE, Type.HERBIVORE, Type.PLANT);
        type.getSelectionModel().selectFirst();

        getDialogPane().setContent(grid);
    }

    @Override
    protected Set<GameObject> returnValue() {
        Set<GameObject> gameObjects = new HashSet<>();

        if (name.getText().isBlank() || energy.getText().isBlank()) {
            return gameObjects;
        }

        for (int i = 0; i < amount.getValue(); i++) {
            GameObject gameObject = getGameObject();
            gameObject.setName(name.getText());
            gameObject.setColor(color.getValue().toString());
            gameObject.setEnergy(Integer.parseInt(energy.getText()));
            gameObjects.add(gameObject);
        }

        return gameObjects;
    }

    private GameObject getGameObject() {
        GameObject gameObject;
        switch (type.getValue()) {
            case PLANT:
                gameObject = new Plant();
                break;

            case CARNIVORE:
                gameObject = new Carnivore();
                break;

            case HERBIVORE:
                gameObject = new Herbivore();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + type.getValue());
        }
        return gameObject;
    }

    private void setUpGrid(GridPane grid) {
        grid.add(new Label("Name der Spezies"), 0, 0);
        grid.add(new Label("Energie"), 0, 1);
        grid.add(new Label("Anzahl"), 0, 2);
        grid.add(new Label("Farbe"), 0, 3);
        grid.add(new Label("Typ"), 0, 4);

        grid.add(name, 1, 0);
        grid.add(energy, 1, 1);
        grid.add(amount, 1, 2);
        grid.add(color, 1, 3);
        grid.add(type, 1, 4);
    }

    private enum Type {
        CARNIVORE, HERBIVORE, PLANT
    }

}
