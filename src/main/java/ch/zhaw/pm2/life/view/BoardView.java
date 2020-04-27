package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.controller.GameObject;
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
        drawLines();
        drawGameObjects();
    }

    private void drawGameObjects() {
        for (GameObject gameObject : board.getGameObjects()) {
            Position position = gameObject.getPosition();
//            double size = lifeForm.getSize() / 10;
//
//            gc.setFill(lifeForm.getColor());
            getGraphicsContext2D().setFill(Color.BLUE);
            double scaling = 0.5;
            double translateFactor = (1 - scaling) * 0.5;
            double translatedX = position.getX() + fieldDimension.getHeight() * translateFactor;
            double translatedY = position.getY() + fieldDimension.getWidth() * translateFactor;
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
