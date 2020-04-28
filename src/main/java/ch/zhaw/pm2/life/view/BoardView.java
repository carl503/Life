package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.Position;
import javafx.beans.InvalidationListener;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeListener;

/**
 * This class displays the board.
 * @author lubojcar, meletlea
 */
public class BoardView extends Pane {
    private final Board board;
    private final GraphicsContext graphicsContext;
    private Dimension2D fieldDimension;

    /**
     * Default constructor.
     * @param width The width in pixel.
     * @param height The height in pixel.
     * @param board The board containing the game objects {@see Board}.
     */
    public BoardView(int width, int height, Board board) {
        this.board = board;
        setHeight(height);
        setWidth(width);

        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());

        InvalidationListener resizeListener = observable -> draw();
        canvas.widthProperty().addListener(resizeListener);
        canvas.heightProperty().addListener(resizeListener);

        graphicsContext = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
    }

    /**
     * Draw the board on the canvas.
     */
    public void draw() {
        fieldDimension = new Dimension2D(getWidth() / (double) board.getColumns(), getHeight()/ (double) board.getRows());
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
        drawLines();
        drawGameObjects();
    }

    private void drawGameObjects() {
        for (GameObject gameObject : board.getGameObjects()) {
            Position position = gameObject.getPosition();

            double scaling = gameObject.getSize() * 0.1;
            double translateFactor = (1 - scaling) * 0.5;

            double fieldPosX = position.getX() * fieldDimension.getWidth();
            double fieldPosY = position.getY() * fieldDimension.getHeight();

            double translatedX = fieldPosX + fieldDimension.getWidth() * translateFactor;
            double translatedY = fieldPosY + fieldDimension.getHeight() * translateFactor;

            graphicsContext.setFill(gameObject.getColor());
            graphicsContext.fillOval(translatedX, translatedY, fieldDimension.getWidth() * scaling, fieldDimension.getHeight() * scaling);
       }
    }

    private void drawLines() {
        graphicsContext.setLineWidth(1);
        graphicsContext.setFill(Color.BLACK);

        for (double i = 0; i <= getWidth(); i+= fieldDimension.getWidth()) {
            graphicsContext.strokeLine(i, 0, i, getHeight());
        }

        for (double i = 0; i <= getHeight(); i+= fieldDimension.getHeight()) {
            graphicsContext.strokeLine(0, i, getWidth(), i);
        }
    }

}
