package ch.zhaw.pm2.life.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LifeWindowController {

    private Canvas canvas;
    @FXML VBox board;

    @FXML public void initialize() {
        canvas = new Canvas(200, 200);
        draw();
        board.getChildren().add(canvas);
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, 400, 400);
    }
}
