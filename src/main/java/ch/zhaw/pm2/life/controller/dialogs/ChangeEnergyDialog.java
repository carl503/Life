package ch.zhaw.pm2.life.controller.dialogs;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;

public class ChangeEnergyDialog extends TextInputDialog {
    public ChangeEnergyDialog() {
        this.setTitle("Neuer Energiewert");
        setUpValidator();
    }

    private void setUpValidator() {
        this.getEditor().setTextFormatter(new TextFormatter<Object>(change -> {
            try {
                if (change.getText().isEmpty()) {
                    return null;
                }

                int energy = Integer.parseInt(change.getText());

                if (energy < 1) {
                    return null;
                }

                return change;
            } catch (NumberFormatException e) {
                return null;
            }
        }));
    }
}
