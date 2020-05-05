package ch.zhaw.pm2.life.model.lifeform.test;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
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
    private static final int BOARD_MIN_ROWS = 3;
    private static final int BOARD_MIN_COLUMNS = 3;

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
        when(firstGameObject.getVector2D()).thenReturn(new Vector2D(0, 0));
        when(secondGameObject.getVector2D()).thenReturn(new Vector2D(0, 1));

        board.addGameObject(firstGameObject);
        board.addGameObject(secondGameObject);
        assertEquals(expectedSize, board.getGameObjects().size());
        assertEquals(expectedSize, board.getOccupiedVector2DS().size());

        board.addGameObject(firstGameObject);
        assertEquals(expectedSize, board.getGameObjects().size());
        assertEquals(expectedSize, board.getOccupiedVector2DS().size());
    }

    @Test
    public void testCleanBoard() {
        LifeForm firstLifeForm = mock(MeatEater.class);
        when(firstLifeForm.getVector2D()).thenReturn(new Vector2D(0, 0));
        when(firstLifeForm.isDead()).thenReturn(true);
        board.addGameObject(firstLifeForm);

        LifeForm secondLifeForm = mock(PlantEater.class);
        when(secondLifeForm.getVector2D()).thenReturn(new Vector2D(0, 1));
        when(secondLifeForm.isDead()).thenReturn(false);
        board.addGameObject(secondLifeForm);

        LifeForm thirdLifeForm = mock(PlantObject.class);
        when(thirdLifeForm.getVector2D()).thenReturn(new Vector2D(0, 1));
        when(thirdLifeForm.isDead()).thenReturn(false);
        board.addGameObject(thirdLifeForm);

        assertEquals(3, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedVector2DS().size()); // second and third on same position

        board.cleanBoard();

        assertEquals(2, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedVector2DS().size()); // second and third on same position
    }

    @Test
    public void testContainsNotInstanceOfAnimalObject() {
        firstGameObject = new MeatEater();
        board.addGameObject(firstGameObject);
        assertEquals(1, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedVector2DS().size());

        assertFalse(board.containsNotInstanceOfAnimalObject(MeatEater.class));
        assertTrue(board.containsNotInstanceOfAnimalObject(PlantEater.class));
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
    public void testAddGameObjectInvalidPositionRowNegative() {
        when(firstGameObject.getVector2D()).thenReturn(new Vector2D(0, -1));
        // todo: exception
        board.addGameObject(firstGameObject);
    }

    @Test
    public void testAddGameObjectInvalidPositionColumnNegative() {
        when(firstGameObject.getVector2D()).thenReturn(new Vector2D(-1, 0));
        // todo: exception
        board.addGameObject(firstGameObject);
    }

    @Test
    public void testAddGameObjectInvalidPositionBiggerThanNumberOfRows() {
        when(firstGameObject.getVector2D()).thenReturn(new Vector2D(0, BOARD_SIZE));
        // todo: exception
        board.addGameObject(firstGameObject);
    }

    @Test
    public void testAddGameObjectInvalidPositionBiggerThanNumberOfColumns() {
        when(firstGameObject.getVector2D()).thenReturn(new Vector2D(BOARD_SIZE, 0));
        // todo: exception
        board.addGameObject(firstGameObject);
    }

    @Test
    public void testContainsNotInstanceOfAnimalObjectNull() {
        firstGameObject = mock(MeatEater.class);
        when(firstGameObject.getVector2D()).thenReturn(new Vector2D(0, 0));
        board.addGameObject(firstGameObject);

        boolean containsNull = board.containsNotInstanceOfAnimalObject(null);

        assertFalse(containsNull);
    }

    @Test
    public void testInvalidConstructorRowLowerThanMinValue() {
        // todo: exception
        board = new Board(BOARD_MIN_ROWS - 1, BOARD_MIN_COLUMNS);
    }

    @Test
    public void testInvalidConstructorColumnsLowerThanMinValue() {
        // todo: exception
        board = new Board(BOARD_MIN_ROWS, BOARD_MIN_COLUMNS - 1);
    }

}
