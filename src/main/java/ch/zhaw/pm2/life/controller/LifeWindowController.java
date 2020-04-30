package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.view.BoardView;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class LifeWindowController {

    private final int width = 800;
    private final int height = 800;
    public static final int ROWS = 16;
    public static final int COLUMNS = 16;

    private BoardView boardView;
    private Game game;
    private SetupController setupController;

    @FXML private VBox board;

    @FXML public void initialize() {
        Board board = new Board(ROWS, COLUMNS);
        game = new Game(board);
        boardView = new BoardView(width, height, board);
        this.board.getChildren().add(boardView);
        boardView.draw();
    }

    @FXML public void nextRound() {
        //todo
    }

    @FXML public void stop() {
        game.stop();
    }

    public void setSetupController(SetupController setupController) {
        this.setupController = setupController;
    }
}
