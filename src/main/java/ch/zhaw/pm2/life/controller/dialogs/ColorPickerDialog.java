package ch.zhaw.pm2.life.controller.dialogs;

import ch.zhaw.pm2.life.model.GameObject;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Allows the user to change the color of a {@link GameObject}.
 */
public class ColorPickerDialog extends LifeDialog<String> {

    private final ColorPicker colorPicker = new ColorPicker();

    /**
     * Creates a color picker dialog instance.
     */
    public ColorPickerDialog() {
        super("Neue Farbe");
        setUp();
    }

    @Override
    protected void setUp() {
        GridPane grid = getGrid();
        grid.add(new Label("Bitte waehlen Sie eine neue Farbe aus"), 0, 0);
        grid.add(colorPicker, 0, 1);

        getDialogPane().setContent(grid);
    }

    @Override
    protected String returnValue() {
        return colorPicker.getValue().toString();
    }

}
