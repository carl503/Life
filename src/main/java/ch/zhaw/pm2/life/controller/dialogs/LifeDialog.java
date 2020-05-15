package ch.zhaw.pm2.life.controller.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

/**
 * Template class to create custom dialogs in a grid pane.
 * @param <T> Object type that the dialog returns.
 */
public abstract class LifeDialog<T> extends Dialog<T> {

    private final GridPane grid = new GridPane();

    /**
     * Creates the {@link LifeDialog} instance.
     * @param title dialog title.
     */
    public LifeDialog(String title) {
        setTitle(title);
        setUpGrid();
        setUpButtons();
    }

    /**
     * General setup for dialogs.
     */
    protected abstract void setUp();

    /**
     * Return the value which the user provides us
     * @return <T> Object type that the dialog returns.
     */
    protected abstract T returnValue();

    /**
     * Returns the {@link GridPane}.
     * @return grid.
     */
    public GridPane getGrid() {
        return grid;
    }

    private void setUpButtons() {
        ButtonType confirm = new ButtonType("Bestaetigen", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().getButtonTypes().addAll(confirm, cancel);

        setResultConverter(param -> {
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
