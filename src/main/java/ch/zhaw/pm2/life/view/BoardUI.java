package ch.zhaw.pm2.life.view;

import ch.zhaw.pm2.life.model.Board;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BoardUI {
    private final Board board;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int width;
    private final int height;
    private final int rows;
    private final int columns;

    public BoardUI(int width, int height, Board board) {
        canvas = new Canvas(width, height);
        this.width = width;
        this.height = height;
        this.board = board;
        this.rows = board.getRows();
        this.columns = board.getColumns();
        gc = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void draw() {
        drawLines();
        drawLifeForms();
    }

    private void drawLifeForms() {
//        for (LifeForm lifeForm : board.getLifeForms()) {
//            double size = lifeForm.getSize() / 10;
//
//            gc.setFill(lifeForm.getColor());
//            gc.fillOval(0, 0, (width/columns) * 0.5  , (height/rows) * 0.5);
//        }
    }

    private void drawLines() {
        gc.setLineWidth(1);
        gc.setFill(Color.BLACK);

        for (int i = 0; i <= width; i+= width / board.getColumns()) {
            gc.strokeLine(i, 0, i, height);
        }

        for (int i = 0; i <= height; i+= height / board.getRows()) {
            gc.strokeLine(0, i, width, i);
        }
    }
}
