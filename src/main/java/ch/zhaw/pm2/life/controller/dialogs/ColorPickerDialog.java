package ch.zhaw.pm2.life.controller.dialogs;


import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ColorPickerDialog extends LifeDialog<String> {

    private final ColorPicker colorPicker = new ColorPicker();

    public ColorPickerDialog() {
        super("Neue Farbe");
        setUp();
    }

    @Override
    protected void setUp() {
        GridPane grid = getGrid();
        grid.add(new Label("Bitte waehlen Sie eine neue Farbe aus"), 0, 0);
        grid.add(colorPicker, 0, 1);

        this.getDialogPane().setContent(grid);
    }

    @Override
    protected String returnValue() {
        return colorPicker.getValue().toString();
    }
}
