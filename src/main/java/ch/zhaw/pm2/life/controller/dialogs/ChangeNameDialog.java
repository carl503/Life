package ch.zhaw.pm2.life.controller.dialogs;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;

public class ChangeNameDialog extends TextInputDialog {
    public ChangeNameDialog() {
        this.setTitle("Neuer Name");
        setUpValidator();
    }

    private void setUpValidator() {
        this.getEditor().setTextFormatter(new TextFormatter<Object>(change -> {
            if (change.getText().isEmpty()) {
                return change;
            } else if (change.getText().contains(" ")) {
                return null;
            }
            return change;
        }));
    }
}
