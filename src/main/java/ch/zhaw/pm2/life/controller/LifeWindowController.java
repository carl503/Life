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
    private Board boardObject;

    @FXML private VBox board;

    @FXML public void initialize() {
        boardObject = new Board(ROWS, COLUMNS);
        boardView = new BoardView(width, height, boardObject);
        this.board.getChildren().add(boardView);
        boardView.draw();
    }

    @FXML public void nextRound() {
        //game.nextMove();
        boardView.draw();
    }

    @FXML public void stop() {
        game.stop();
    }

    public void setSetupController(SetupController setupController) {
        this.setupController = setupController;
    }

    public void initGame() {
        game = new Game(boardObject, setupController.getPlantCount(),
                setupController.getMeatEaterCount(), setupController.getPlantEaterCount());
        game.init();
    }
}
