package ch.zhaw.pm2.life.controller.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

public abstract class LifeDialog<T> extends Dialog<T> {

    private final GridPane grid = new GridPane();

    public LifeDialog(String title) {
        this.setTitle(title);
        setUpGrid();
        setUpButtons();
    }

    protected abstract void setUp();
    protected abstract T returnValue();

    public GridPane getGrid() {
        return grid;
    }

    private void setUpButtons() {
        ButtonType confirm = new ButtonType("Bestaetigen", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(confirm, cancel);

        this.setResultConverter(param -> {
            if (param == confirm) {
                return returnValue();
            }
            return null;
        });
    }

    private void setUpGrid() {
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
    }
}
