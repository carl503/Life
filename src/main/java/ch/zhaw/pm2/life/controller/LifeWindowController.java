package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.controller.dialogs.*;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.GameProperties;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.view.BoardView;
import ch.zhaw.pm2.life.view.StatisticView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for the main window.
 */
public class LifeWindowController {

    private static final int ROWS = 16;
    private static final int COLUMNS = 16;
    private final StatisticView statisticView = new StatisticView();
    private BoardView boardView;
    private Game game;
    private SetupController setupController;
    private GameProperties gameProperties;
    private Board boardObject;
    private boolean fullscreen;
    @FXML private BorderPane root;
    @FXML private Pane board;
    @FXML private TextArea messageField;
    @FXML private Button nextRoundButton;
    @FXML private Button stopSimButton;
    @FXML private Menu editMenu;

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
            root.addEventHandler(KeyEvent.KEY_PRESSED, fullscreenEvent());
        } catch (Exception e) {
            messageField.appendText(e.getMessage() + "\n");
        }
    }

    @FXML
    private void nextRound() {
        if (game == null) {
            return;
        }

        if (game.isOngoing()) {
            messageField.appendText(game.nextMove());
            boardView.draw();
        } else {
            nextRoundButton.setDisable(true);
            stopSimButton.setDisable(true);
            showStatistics();
        }
    }

    @FXML
    private void stopSimulation() {
        if (game == null) {
            return;
        }
        game.stop();
        nextRoundButton.setDisable(true);
        stopSimButton.setDisable(true);
        showStatistics();
    }

    @FXML
    public void updateSize() {
        double newWidth = board.getWidth();
        double newHeight = board.getHeight();
        if (newHeight > 0.0 && newWidth > 0.0) {
            boardView.updateDimension(newWidth, newHeight);
        }
    }

    @FXML
    public void toggleFullscreen() {
        fullscreen = !fullscreen;
        ((Stage) root.getScene().getWindow()).setFullScreen(fullscreen);
    }

    @FXML void toggleTextField() {
        boolean status = messageField.isVisible();
        if (status) {
            messageField.setVisible(false);
            messageField.setManaged(false);
        } else {
            messageField.setManaged(true);
            messageField.setVisible(true);
        }
    }

    private EventHandler<KeyEvent> fullscreenEvent() {
        return event -> {
            if (event.getCode() == KeyCode.F11) {
                toggleFullscreen();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                fullscreen = false;
            }
        };
    }

    private void showStatistics() {
        statisticView.setStartLifeForms(game.getStartLifeForms());
        statisticView.setBornLifeForms(game.getBornLifeForms());
        statisticView.setDiedLifeForms(game.getDeadLifeForms());
        statisticView.setSurvivedLifeForms(game.getSurvivedLifeForms());
        statisticView.setSpawnLifeForms(game.getSpawnedLifeForms());

        Set<String> species = setupController.getGameObjects()
                .keySet()
                .stream()
                .map(GameObject::getName)
                .collect(Collectors.toSet());

        statisticView.initChart((Stage) board.getScene().getWindow(), species);
        statisticView.show();
    }

    /**
     * Draws the board.
     */
    public void drawBoard() {
        if (game == null) {
            return;
        }
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
        if (boardObject == null) {
            return;
        }
        try {
            gameProperties = new GameProperties(setupController.getGameObjects());
            game = new Game(boardObject, gameProperties);
            game.setSpeciesToWatch(setupController.getStopCondition());
        } catch (Exception e) {
            messageField.appendText(e.getMessage() + "\n");
        }
    }

    public void initEditMenu() {
        setupController.getGameObjects().forEach((gameObject, amount) -> {
            if (amount > 0) {
                Menu lifeform = new Menu();
                lifeform.setText(gameObject.getName());

                lifeform.getItems().add(changeColor(gameObject));
                lifeform.getItems().add(changeName(gameObject));
                lifeform.getItems().add(changeEnergy(gameObject));

                if (gameObject instanceof AnimalObject) {
                    AnimalObject animalObject = (AnimalObject) gameObject;
                    lifeform.getItems().add(changeScanRadius(animalObject));
                }
                editMenu.getItems().add(lifeform);

            }
        });
        editMenu.getItems().add(changeStopCondition());
    }

    private MenuItem changeStopCondition() {
        MenuItem item = new MenuItem("Stoppbedingung aendern");
        item.setOnAction(event -> {
            StopConditionDialog dialog = new StopConditionDialog(boardObject.getGameObjects());
            Optional<String> response = dialog.showAndWait();
            response.ifPresent(s -> game.setSpeciesToWatch(s));
        });
        return item;
    }

    private MenuItem changeScanRadius(AnimalObject animalObject) {
        MenuItem radiusItem = new MenuItem();
        radiusItem.setText("Sichtweite aendern");
        radiusItem.setOnAction(event -> {
            ScanRadiusDialog dialog = new ScanRadiusDialog();
            Optional<Integer> response = dialog.showAndWait();
            response.ifPresent(responseValue -> boardObject.getGameObjects().stream()
                    .filter(AnimalObject.class::isInstance)
                    .map(AnimalObject.class::cast)
                    .forEach(ao -> {
                        if (ao.getName().equals(animalObject.getName())) {
                            ao.setScanRadius(response.get());
                        }
                    }));
        });
        return radiusItem;
    }

    private MenuItem changeColor(GameObject gameObject) {
        MenuItem changeColor = new MenuItem();
        changeColor.setText("Farbe aendern");
        changeColor.setOnAction(event -> {
            ColorPickerDialog dialog = new ColorPickerDialog();
            Optional<String> response = dialog.showAndWait();
            response.ifPresent(responseValue -> boardObject.getGameObjects().forEach(go -> {
                if (go.getName().equals(gameObject.getName())) {
                    go.setColor(responseValue);
                }
            }));
        });
        return changeColor;
    }

    private MenuItem changeName(GameObject gameObject) {
        MenuItem changeName = new MenuItem();
        changeName.setText("Name aendern");
        changeName.setOnAction(event -> {
            ChangeNameDialog changeNameDialog = new ChangeNameDialog();
            changeNameDialog.showAndWait();

            boardObject.getGameObjects().forEach(gameObject1 -> {
                if (gameObject1.getName().equals(gameObject.getName())) {
                    String newName = changeNameDialog.getEditor().getText();
                    gameObject1.setName(newName);
                    editMenu.getItems().forEach(menuItem -> {
                        if (menuItem.getText().equals(gameObject.getName())) {
                            menuItem.setText(newName);
                        }
                    });
                }
            });
        });
        return changeName;
    }

    private MenuItem changeEnergy(GameObject gameObject) {
        MenuItem changeEnergy = new MenuItem();
        changeEnergy.setText("Energie aendern");
        changeEnergy.setOnAction(event -> {
            ChangeEnergyDialog changeEnergyDialog = new ChangeEnergyDialog();
            changeEnergyDialog.showAndWait();
            String energyString = changeEnergyDialog.getEditor().getText();
            if (energyString.isBlank()) {
                return;
            }
            int energy = Integer.parseInt(energyString);
            boardObject.getGameObjects().forEach(gameObject1 -> {
                if(gameObject1.equals(gameObject)) {
                    gameObject1.setEnergy(energy);
                }
            });
        });
        return changeEnergy;
    }

}
