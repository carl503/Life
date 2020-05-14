package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.view.BoardView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Controller for the main window.
 */
public class LifeWindowController {

    private static final int ROWS = 16;
    private static final int COLUMNS = 16;

    private BoardView boardView;
    private Game game;
    private SetupController setupController;
    private Board boardObject;

    @FXML private Pane board;
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
            boardView = new BoardView(boardObject);
            board.getChildren().add(boardView);
            board.widthProperty().addListener(observable -> updateSize());
            board.heightProperty().addListener(observable -> updateSize());
        } catch (Exception e) {
            messageField.appendText(e.getMessage() + "\n");
        }
    }

    @FXML
    private void nextRound() {
        if (game != null) {
            messageField.appendText(game.nextMove());
            boardView.draw();
            if (!game.isOngoing()) {
                nextRoundButton.setDisable(true);
                stopSimButton.setDisable(true);
            }
        }
    }

    @FXML
    private void stopSimulation() {
        if (game != null) {
            game.stop();
            nextRoundButton.setDisable(true);
            stopSimButton.setDisable(true);
        }
    }

    @FXML
    public void updateSize() {
        double newWidth = board.getWidth();
        double newHeight = board.getHeight();
        if (newHeight > 0.0 && newWidth > 0.0) {
            boardView.updateDimension(newWidth, newHeight);
        }
    }

    /**
     * Draws the board.
     */
    public void drawBoard() {
        if (game != null) {
            boardView.draw();
        }
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
        if (boardObject != null) {
            try {
                game = new Game(boardObject, setupController.getPlantCount(),
                                setupController.getCarnivoreCount(), setupController.getHerbivoreCount());
                game.init();
            } catch (Exception e) {
                messageField.appendText(e.getMessage() + "\n");
            }
        }
    }

}
