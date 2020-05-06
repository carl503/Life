package ch.zhaw.pm2.life.model.lifeform;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.MeatEater;
import ch.zhaw.pm2.life.model.lifeform.animal.PlantEater;
import ch.zhaw.pm2.life.model.lifeform.plant.PlantObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardTest {

    private static final int BOARD_SIZE = 3;
    private static final String ILLEGAL_POSITION_MESSAGE = "The position %s of the provided game object does not exist on the board.";

    private Board board;

    @Mock private GameObject firstGameObject;
    @Mock private GameObject secondGameObject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        board = new Board(BOARD_SIZE, BOARD_SIZE);
    }

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void testAddGameObject() {
        int expectedSize = 2;
        when(firstGameObject.getPosition()).thenReturn(new Vector2D(0, 0));
        when(secondGameObject.getPosition()).thenReturn(new Vector2D(0, 1));

        board.addGameObject(firstGameObject);
        board.addGameObject(secondGameObject);
        assertEquals(expectedSize, board.getGameObjects().size());
        assertEquals(expectedSize, board.getOccupiedPositions().size());

        board.addGameObject(firstGameObject);
        assertEquals(expectedSize, board.getGameObjects().size());
        assertEquals(expectedSize, board.getOccupiedPositions().size());
    }

    @Test
    public void testCleanBoard() {
        LifeForm firstLifeForm = mock(MeatEater.class);
        when(firstLifeForm.getPosition()).thenReturn(new Vector2D(0, 0));
        when(firstLifeForm.isDead()).thenReturn(true);
        board.addGameObject(firstLifeForm);

        LifeForm secondLifeForm = mock(PlantEater.class);
        when(secondLifeForm.getPosition()).thenReturn(new Vector2D(0, 1));
        when(secondLifeForm.isDead()).thenReturn(false);
        board.addGameObject(secondLifeForm);

        LifeForm thirdLifeForm = mock(PlantObject.class);
        when(thirdLifeForm.getPosition()).thenReturn(new Vector2D(0, 1));
        when(thirdLifeForm.isDead()).thenReturn(false);
        board.addGameObject(thirdLifeForm);

        assertEquals(3, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size()); // second and third on same position

        board.cleanBoard();

        assertEquals(2, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedPositions().size()); // second and third on same position
    }

    @Test
    public void testContainsNotInstanceOfAnimalObject() {
        AnimalObject animalObject = mock(AnimalObject.class);

        AnimalObject meatEater = mock(MeatEater.class);
        when(meatEater.getPosition()).thenReturn(new Vector2D(0, 1));
        board.addGameObject(meatEater);

        assertFalse(board.containsNotInstanceOfAnimalObject(meatEater.getClass()));
        assertTrue(board.containsNotInstanceOfAnimalObject(animalObject.getClass()));
    }

    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void testAddGameObjectNull() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> board.addGameObject(null));
        assertEquals("Game object cannot be null to add it on the board.", thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionNull() {
        when(firstGameObject.getPosition()).thenReturn(null);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> board.addGameObject(firstGameObject));
        assertEquals("The position cannot be null to add the game object on the board.", thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionRowNegative() {
        Vector2D position = new Vector2D(0, -1);
        when(firstGameObject.getPosition()).thenReturn(position);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionColumnNegative() {
        Vector2D position = new Vector2D(-1, 0);
        when(firstGameObject.getPosition()).thenReturn(position);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionBiggerThanNumberOfRows() {
        Vector2D position = new Vector2D(0, BOARD_SIZE);
        when(firstGameObject.getPosition()).thenReturn(position);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionBiggerThanNumberOfColumns() {
        Vector2D position = new Vector2D(BOARD_SIZE, 0);
        when(firstGameObject.getPosition()).thenReturn(position);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testContainsNotInstanceOfAnimalObjectNull() {
        firstGameObject = mock(MeatEater.class);
        when(firstGameObject.getPosition()).thenReturn(new Vector2D(0, 0));
        board.addGameObject(firstGameObject);

        boolean containsNull = board.containsNotInstanceOfAnimalObject(null);

        assertFalse(containsNull);
    }

    @Test
    public void testInvalidConstructorRowLowerThanMinValue() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board = new Board(Board.MIN_ROWS - 1, Board.MIN_COLUMNS));
        assertEquals("The number of rows cannot be less than " + Board.MIN_ROWS, thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorColumnsLowerThanMinValue() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board = new Board(Board.MIN_ROWS, Board.MIN_COLUMNS - 1));
        assertEquals("The number of columns cannot be less than " + Board.MIN_COLUMNS, thrown.getMessage());
    }

}
