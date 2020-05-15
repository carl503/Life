package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * This class displays the board and all the {@link GameObject} that are alive including their attributes.
 * The board has an overlay of lines that are drawn to improve the understanding of the coordinate system
 * and the movements done by the {@link AnimalObject}.
 */
public class BoardView extends Canvas {

    private static final double GAME_OBJECT_SIZE_SCALING = 0.1;
    private static final double HALF = 0.5;
    private static final double POISON_SCALE_X = 0.35;
    private static final double POISON_SCALE_Y = 0.6;
    private static final double ENERGY_SCALE_X = 0.55;
    private static final double ENERGY_SCALE_Y = 0.25;
    private static final int FERTILITY_Y_POSITION = 35;

    private final Board board;
    private double width;
    private double height;
    private Dimension2D fieldDimension;

    /**
     * Creates the board view instance.
     * @param board from type {@link Board}.
     */
    public BoardView(Board board) {
        this.board = Objects.requireNonNull(board, "Board cannot be null to display it.");
    }

    /**
     * Updates the dimension if the newHeight and newWidth values of updateSize() are valid.
     * @param width  as double for the width of the dimension.
     * @param height as double for the height of the dimension.
     */
    public void updateDimension(double width, double height) {
        this.width = width;
        this.height = height;
        setHeight(height);
        setWidth(width);
        fieldDimension = new Dimension2D(width / (double) board.getColumns(), height / (double) board.getRows());
        draw();
    }

    /**
     * Draws all {@link GameObject} including all its attributes and the grid lines
     * representing the coordinate system on the {@link Board}.
     */
    public void draw() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        drawLines();
        drawGameObjects();
    }

    private void drawLines() {
        getGraphicsContext2D().setLineWidth(1);
        getGraphicsContext2D().setFill(Color.BLACK);

        for (double column = 0; column <= width; column += fieldDimension.getWidth()) {
            getGraphicsContext2D().strokeLine(column, 0, column, height);
        }

        for (double row = 0; row <= height; row += fieldDimension.getHeight()) {
            getGraphicsContext2D().strokeLine(0, row, width, row);
        }
    }

    private void drawGameObjects() {
        for (GameObject gameObject : board.getGameObjects()) {
            Vector2D vector2D = gameObject.getPosition();

            double fieldPosX = vector2D.getX() * fieldDimension.getWidth();
            double fieldPosY = vector2D.getY() * fieldDimension.getHeight();

            double scaling = gameObject.getSize() * GAME_OBJECT_SIZE_SCALING;
            double translateFactor = (1 - scaling) * HALF;

            double fieldDimensionDiff = fieldDimension.getWidth() - fieldDimension.getHeight();

            double translatedX = fieldPosX + (fieldDimension.getWidth() + fieldDimensionDiff) * translateFactor;
            double translatedY = fieldPosY + fieldDimension.getHeight() * translateFactor;

            drawGameObject(gameObject, scaling, translatedX, translatedY);
            drawGender(gameObject, translatedX, translatedY);
            drawCurrentEnergy(gameObject, fieldPosX, fieldPosY);
            drawPoisonStatus(gameObject, fieldPosX, fieldPosY);
            drawFertilityThreshold(gameObject, translatedX, translatedY);
        }
    }

    private void drawGameObject(GameObject gameObject, double scaling, double translatedX, double translatedY) {
        getGraphicsContext2D().setFill(Color.valueOf(gameObject.getColor()));
        getGraphicsContext2D().fillOval(translatedX, translatedY, fieldDimension.getHeight() * scaling, fieldDimension.getHeight() * scaling);
    }

    private void drawGender(GameObject gameObject, double translatedX, double translatedY) {
        if (gameObject instanceof LifeForm) {
            getGraphicsContext2D().setStroke(Color.BLACK);
            LifeForm lifeForm = (LifeForm) gameObject;
            getGraphicsContext2D().strokeText(lifeForm.getGender(), translatedX, translatedY);
        }
    }

    private void drawFertilityThreshold(GameObject gameObject, double translatedX, double translatedY) {
        if (gameObject instanceof AnimalObject) {
            getGraphicsContext2D().setStroke(Color.BLACK);
            AnimalObject animalObject = (AnimalObject) gameObject;
            getGraphicsContext2D().strokeText(String.valueOf(animalObject.getFertilityThreshold()), translatedX, translatedY + FERTILITY_Y_POSITION);
        }
    }

    private void drawCurrentEnergy(GameObject gameObject, double fieldPosX, double fieldPosY) {
        double energyPositionX = fieldPosX + fieldDimension.getWidth() * ENERGY_SCALE_X;
        double energyPositionY = fieldPosY + fieldDimension.getHeight() * ENERGY_SCALE_Y;
        getGraphicsContext2D().strokeText(String.valueOf(gameObject.getEnergy()), energyPositionX, energyPositionY);
    }

    private void drawPoisonStatus(GameObject gameObject, double fieldPosX, double fieldPosY) {
        if (gameObject instanceof LifeForm) {
            double poisonStatusPositionX = fieldPosX + fieldDimension.getHeight() * POISON_SCALE_X;
            double poisonStatusPositionY = fieldPosY + fieldDimension.getHeight() * POISON_SCALE_Y;
            LifeForm lifeForm = (LifeForm) gameObject;
            String labelValue = lifeForm.isPoisonous() ? "S" : "";
            labelValue += (lifeForm.isPoisoned()) ? "D" : "";
            getGraphicsContext2D().strokeText(labelValue, poisonStatusPositionX, poisonStatusPositionY);
        }
    }

}
