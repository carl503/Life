package ch.zhaw.pm2.life.util;

import javafx.scene.control.TextFormatter;

/**
 * Validator class for the TextInputDialogs.
 */
public class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * Returns a {@link TextFormatter}
     * to verify a name entered by the user.
     * @return TextFormatter.
     */
    public static TextFormatter<Object> getNameFormatter() {
        return new TextFormatter<>(change -> {

            if (change.isContentChange()) {
                if (change.getText().matches("^[a-zA-Z]*$")) {
                    return change;
                } else {
                    return null;
                }
            }
            return change;
        });
    }

    /**
     * Returns a {@link TextFormatter}
     * to verify the energy entered by the user.
     * @return TextFormatter.
     */
    public static TextFormatter<Object> getEnergyFormatter() {
        return new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                if (change.getControlNewText().matches("[1-9]|[1-9][0-9]")) {
                    return change;
                }
                change.setText("");
            }
            return change;
        });
    }

}
