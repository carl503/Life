package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

/**
 * This class displays the board.
 * @author lubojcar, meletlea
 */
public class BoardView extends Canvas {

    private static final double GAME_OBJECT_SIZE_SCALING = 0.1;
    private static final double HALF = 0.5;
    private static final double POISON_SCALE_X = 0.35;
    private static final double POISON_SCALE_Y = 0.6;
    private static final double ENERGY_SCALE_X = 0.55;
    private static final double ENERGY_SCALE_Y = 0.25;

    private final Board board;
    private final int width;
    private final int height;
    private final Dimension2D fieldDimension;

    /**
     * Default constructor.
     * @param width The width in pixel.
     * @param height The height in pixel.
     * @param board The board containing the game objects {@see Board}.
     */
    public BoardView(int width, int height, Board board) {
        this.width = width;
        this.height = height;
        this.board = Objects.requireNonNull(board, "Board cannot be null to display it.");
        setHeight(height);
        setWidth(width);
        fieldDimension = new Dimension2D(width / (double) board.getColumns(), height / (double) board.getRows());
    }

    /**
     * Draw the board on the canvas.
     */
    public void draw() {
        getGraphicsContext2D().clearRect(0,0, getWidth(), getHeight());
        drawLines();
        drawGameObjects();
    }

    private void drawLines() {
        getGraphicsContext2D().setLineWidth(1);
        getGraphicsContext2D().setFill(Color.BLACK);

        for (int column = 0; column <= width; column += fieldDimension.getWidth()) {
            getGraphicsContext2D().strokeLine(column, 0, column, height);
        }

        for (int row = 0; row <= height; row += fieldDimension.getHeight()) {
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

            double translatedX = fieldPosX + fieldDimension.getWidth() * translateFactor;
            double translatedY = fieldPosY + fieldDimension.getHeight() * translateFactor;

            drawGameObject(gameObject, scaling, translatedX, translatedY);
            drawGender(gameObject, translatedX, translatedY);
            drawCurrentEnergy(gameObject, fieldPosX, fieldPosY);
            drawPoisonStatus(gameObject, fieldPosX, fieldPosY);
        }
    }

    private void drawGameObject(GameObject gameObject, double scaling, double translatedX, double translatedY) {
        getGraphicsContext2D().setFill(gameObject.getColor());
        getGraphicsContext2D().fillOval(translatedX, translatedY, fieldDimension.getWidth() * scaling, fieldDimension.getHeight() * scaling);
    }

    private void drawGender(GameObject gameObject, double translatedX, double translatedY) {
        if(gameObject instanceof LifeForm) {
            getGraphicsContext2D().setStroke(Color.BLACK);
            LifeForm lifeForm = (LifeForm) gameObject;
            getGraphicsContext2D().strokeText(lifeForm.getGender(), translatedX, translatedY);
        }
    }

    private void drawCurrentEnergy(GameObject gameObject, double fieldPosX, double fieldPosY) {
        double energyPositionX = fieldPosX + fieldDimension.getWidth() * ENERGY_SCALE_X;
        double energyPositionY = fieldPosY + fieldDimension.getHeight() * ENERGY_SCALE_Y;
        getGraphicsContext2D().strokeText(String.valueOf(gameObject.getCurrentEnergy()), energyPositionX, energyPositionY);
    }

    private void drawPoisonStatus(GameObject gameObject, double fieldPosX, double fieldPosY) {
        if(gameObject instanceof LifeForm) {
            double poisonStatusPositionX = fieldPosX + fieldDimension.getHeight() * POISON_SCALE_X;
            double poisonStatusPositionY = fieldPosY + fieldDimension.getHeight() * POISON_SCALE_Y;
            LifeForm lifeForm = (LifeForm) gameObject;
            String labelValue = lifeForm.isPoisonous() ? "S" : "";
            labelValue += (lifeForm.isPoisoned()) ? "D" : "";
            getGraphicsContext2D().strokeText(labelValue, poisonStatusPositionX, poisonStatusPositionY);
        }
    }
}
