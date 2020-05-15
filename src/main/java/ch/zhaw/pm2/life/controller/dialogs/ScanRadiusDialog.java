package ch.zhaw.pm2.life.controller.dialogs;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

/**
 * Allows the user to change the scan radius of a {@link ch.zhaw.pm2.life.model.GameObject}
 */
public class ScanRadiusDialog extends LifeDialog<Integer> {

    private final Slider slider = new Slider();

    /**
     * Creates a scan radius dialog
     */
    public ScanRadiusDialog() {
        super("Neuer Suchradius");
        setUp();
    }

    @Override
    protected void setUp() {
        Label text = new Label("Bitte waehlen Sie einen neuen Radius aus");

        slider.setMin(0);
        slider.setMax(3);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(1);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        GridPane grid = getGrid();

        grid.add(text, 0, 0);
        grid.add(slider, 0, 1);

        this.getDialogPane().setContent(grid);
    }

    @Override
    protected Integer returnValue() {
        return (int) slider.getValue();
    }

}
