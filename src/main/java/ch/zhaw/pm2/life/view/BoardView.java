package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.Position;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * This class displays the board.
 * @author lubojcar, meletlea
 */
public class BoardView extends Canvas {
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
        this.board = board;
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

    private void drawGameObjects() {
        for (GameObject gameObject : board.getGameObjects()) {
            Position position = gameObject.getPosition();
//            gc.setFill(lifeForm.getColor());
            getGraphicsContext2D().setFill(gameObject.getColor());
            double scaling = gameObject.getSize() * 0.1;
            double translateFactor = (1 - scaling) * 0.5;

            double fieldPosX = position.getX() * fieldDimension.getWidth();
            double fieldPosY = position.getY() * fieldDimension.getHeight();

            double translatedX = fieldPosX + fieldDimension.getWidth() * translateFactor;
            double translatedY = fieldPosY + fieldDimension.getHeight() * translateFactor;

            getGraphicsContext2D().fillOval(translatedX, translatedY, fieldDimension.getWidth() * scaling, fieldDimension.getHeight() * scaling);
       }
    }

    private void drawLines() {
        getGraphicsContext2D().setLineWidth(1);
        getGraphicsContext2D().setFill(Color.BLACK);

        for (int i = 0; i <= width; i+= fieldDimension.getWidth()) {
            getGraphicsContext2D().strokeLine(i, 0, i, height);
        }

        for (int i = 0; i <= height; i+= fieldDimension.getHeight()) {
            getGraphicsContext2D().strokeLine(0, i, width, i);
        }
    }

}
