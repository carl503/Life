package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.view.BoardView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class LifeWindowController {

    private final int width = 800;
    private final int height = 800;
    public static final int ROWS = 16;
    public static final int COLUMNS = 16;

    @FXML private BorderPane rootPane;

    private BoardView boardView;

    @FXML public void initialize() {
        Board board = new Board(ROWS, COLUMNS);
        boardView = new BoardView(width, height, board);
        boardView.draw();
        rootPane.setCenter(boardView);
    }

    @FXML private void close() {
        Platform.exit();
    }
}
