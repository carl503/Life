package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.view.BoardUI;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class LifeWindowController {

    private final int width = 800;
    private final int height = 800;
    private final int rows = 16;
    private final int columns = 16;

    private BoardUI ui;
    @FXML VBox board;

    @FXML public void initialize() {
        Board board = new Board(rows, columns);
        ui = new BoardUI(width, height, board);
        this.board.getChildren().add(ui.getCanvas());
        ui.draw();
    }
}
