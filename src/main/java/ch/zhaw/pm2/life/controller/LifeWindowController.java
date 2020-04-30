package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.board.Board;
import ch.zhaw.pm2.life.view.BoardView;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class LifeWindowController {

    private final int width = 800;
    private final int height = 800;
    public static final int ROWS = 16;
    public static final int COLUMNS = 16;

    private BoardView boardView;
    @FXML VBox board;

    @FXML public void initialize() {
        Board board = new Board(ROWS, COLUMNS);
        boardView = new BoardView(width, height, board);
        this.board.getChildren().add(boardView);
        boardView.draw();
    }
}
