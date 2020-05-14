package ch.zhaw.pm2.life.controller.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ScanRadiusDialog extends Dialog<Integer> {
    private final GridPane grid = new GridPane();
    private final Slider slider = new Slider();

    public ScanRadiusDialog() {
        setUpGrid();
        setUpButtons();
        setUp();
    }

    public void setUp() {
        Label text = new Label();
        text.setText("Bitte waehlen Sie einen neuen Radius aus");

        slider.setMin(0);
        slider.setMax(3);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(1);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        grid.add(text, 0, 0);
        grid.add(slider, 0, 1);

        this.getDialogPane().setContent(grid);
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
                return (int) slider.getValue();
            }
            return null;
        });
    }
}
