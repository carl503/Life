package ch.zhaw.pm2.life.controller.dialogs;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.animal.Herbivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;
import ch.zhaw.pm2.life.util.ValidationUtil;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.Set;


public class SpawnSpeciesDialog extends LifeDialog<Set<GameObject>> {

    private final TextField name = new TextField();
    private final TextField energy = new TextField();
    private final Spinner<Integer> amount = new Spinner<>();
    private final ColorPicker color = new ColorPicker();
    private final ComboBox<Type> type = new ComboBox<>();

    private enum Type {
        CARNIVORE, HERBIVORE, PLANT
    }

    public SpawnSpeciesDialog(String title) {
        super(title);
        setUp();
    }

    @Override
    protected void setUp() {
        GridPane grid = getGrid();

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

        name.setTextFormatter(ValidationUtil.getNameFormatter());
        energy.setTextFormatter(ValidationUtil.getEnergyFormatter());
        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        type.getItems().addAll(Type.CARNIVORE, Type.HERBIVORE, Type.PLANT);
        type.getSelectionModel().selectFirst();

        this.getDialogPane().setContent(grid);

    }

    @Override
    protected Set<GameObject> returnValue() {
        Set<GameObject> gameObjects = new HashSet<>();

        for (int i = 0; i < amount.getValue(); i++) {
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
                    gameObject = null;
                    break;
            }
            if (gameObject == null) {
                continue;
            } else {
                gameObject.setName(name.getText());
                gameObject.setColor(color.getValue().toString());
                gameObject.setEnergy(Integer.parseInt(energy.getText()));
                gameObjects.add(gameObject);
            }
        }

        return gameObjects;
    }
}
