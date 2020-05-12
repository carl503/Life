package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.view.BoardView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * Controller for the main window.
 */
public class LifeWindowController {

    private static final int ROWS = 16;
    private static final int COLUMNS = 16;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private BoardView boardView;
    private Game game;
    private SetupController setupController;
    private Board boardObject;

    @FXML private VBox board;
    @FXML private TextArea messageField;
    @FXML private Button nextRoundButton;
    @FXML private Button stopSimButton;

    /**
     * Initializes everything after the JavaFX components are injected,
     */
    @FXML
    public void initialize() {
        try {
            boardObject = new Board(ROWS, COLUMNS);
            boardView = new BoardView(WIDTH, HEIGHT, boardObject);
            this.board.getChildren().add(0, boardView);
        } catch (NullPointerException | IllegalArgumentException e) {
            messageField.appendText(e.getMessage());
        }
    }

    @FXML
    private void nextRound() {
        messageField.appendText(game.nextMove());
        boardView.draw();
        if (!game.isOngoing()) {
            nextRoundButton.setDisable(true);
            stopSimButton.setDisable(true);
        }
    }

    @FXML
    private void stopSimulation() {
        game.stop();
        nextRoundButton.setDisable(true);
        stopSimButton.setDisable(true);
    }

    /**
     * Draws the board.
     */
    public void drawBoard() {
        boardView.draw();
    }

    /**
     * Sets a reference of the {@link SetupController}.
     * @param setupController {@link SetupController}
     */
    public void setSetupController(SetupController setupController) {
        this.setupController = Objects.requireNonNull(setupController, "The setup controller cannot be null.");
    }

    /**
     * Initializes the game.
     */
    public void initGame() {
        game = new Game(boardObject, setupController.getPlantCount(),
                        setupController.getMeatEaterCount(), setupController.getPlantEaterCount());
        game.init();
    }

}
