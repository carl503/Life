package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.MeatEater;
import ch.zhaw.pm2.life.model.lifeform.animal.PlantEater;
import ch.zhaw.pm2.life.model.lifeform.plant.PlantObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardTest {

    private static final int BOARD_SIZE = 3;

    private Board board;

    @Mock private GameObject firstGameObject;
    @Mock private GameObject secondGameObject;
    @Mock private GameObject thirdGameObject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        board = new Board(BOARD_SIZE, BOARD_SIZE);
    }

    // TODO:  add positive and negative tests for missing non-private methods (getRandomPosition, getNeighbourObjects, ...)

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void testAddGameObject() {
        // prepare
        when(firstGameObject.getPosition()).thenReturn(new Vector2D(0, 0));
        when(secondGameObject.getPosition()).thenReturn(new Vector2D(0, 1));
        when(thirdGameObject.getPosition()).thenReturn(new Vector2D(0, 1));

        Set<GameObject> gameObjects = new HashSet<>();
        gameObjects.add(firstGameObject);
        gameObjects.add(secondGameObject);
        gameObjects.add(thirdGameObject);

        Set<Vector2D> positions = new HashSet<>();
        positions.add(firstGameObject.getPosition());
        positions.add(secondGameObject.getPosition());
        positions.add(thirdGameObject.getPosition());

        // execute + assert
        board.addGameObject(firstGameObject);
        board.addGameObject(secondGameObject);
        assertEquals(2, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));

        // execute + assert
        board.addGameObject(firstGameObject);
        assertEquals(2, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));

        // execute + assert
        board.addGameObject(thirdGameObject);
        assertEquals(3, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));
    }

    @Test
    public void testCleanBoard() {
        // prepare
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

        Set<GameObject> gameObjects = new HashSet<>();
        gameObjects.add(secondLifeForm);
        gameObjects.add(thirdLifeForm);

        Set<Vector2D> positions = new HashSet<>();
        positions.add(secondLifeForm.getPosition());
        positions.add(thirdLifeForm.getPosition());

        // execute
        board.cleanBoard();
        gameObjects.remove(firstLifeForm);
        positions.remove(firstLifeForm.getPosition());

        // check
        assertEquals(2, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedPositions().size()); // second and third on same position
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));
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

    // TODO: add negtive test for addGameObject with invalid positions (maybe reuse code from this commit? f10b6f7d2cd422d4089ffa21bee0ead454b39ce5)

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
