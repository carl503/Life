package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.controller.GameObject;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * This class displays the board.
 * @author lubojcar, meletlea
 */
public class BoardUI extends Canvas {
    private final Board board;
    private final int width;
    private final int height;

    /**
     * Default constructor.
     * @param width The width in pixel.
     * @param height The height in pixel.
     * @param board The board containing the game objects {@see Board}.
     */
    public BoardUI(int width, int height, Board board) {
        this.width = width;
        this.height = height;
        this.board = board;
        setHeight(height);
        setWidth(width);
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
            double maxWidth = width / (double) board.getColumns();
            double maxHeight = height / (double) board.getRows();
            double scaling = 0.5;
            double translateFactor = (1 - scaling) * 0.5;
            double translatedX = position.getX() + maxHeight * translateFactor;
            double translatedY = position.getY() + maxWidth * translateFactor;
            getGraphicsContext2D().fillOval(translatedX, translatedY, maxWidth * scaling, maxHeight * scaling);
       }
    }

    private void drawLines() {
        getGraphicsContext2D().setLineWidth(1);
        getGraphicsContext2D().setFill(Color.BLACK);

        for (int i = 0; i <= width; i+= width / board.getColumns()) {
            getGraphicsContext2D().strokeLine(i, 0, i, height);
        }

        for (int i = 0; i <= height; i+= height / board.getRows()) {
            getGraphicsContext2D().strokeLine(0, i, width, i);
        }
    }

}
