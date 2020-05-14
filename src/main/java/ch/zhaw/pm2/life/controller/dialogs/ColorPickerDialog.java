package ch.zhaw.pm2.life.controller.dialogs;


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ColorPickerDialog extends Dialog<String> {

    private final GridPane grid = new GridPane();
    private final ColorPicker colorPicker = new ColorPicker();

    public ColorPickerDialog() {
        this.setTitle("Neue Farbe");
        setUp();
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
                return colorPicker.getValue().toString();
            }
            return null;
        });
    }

    private void setUp() {
        setUpGrid();
        setUpButtons();

        Label text = new Label();
        text.setText("Bitte waehlen Sie eine neue Farbe aus");
        grid.add(text, 0, 0);
        grid.add(colorPicker, 0, 1);

        this.getDialogPane().setContent(grid);
    }
}
