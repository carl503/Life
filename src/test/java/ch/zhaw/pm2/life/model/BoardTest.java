package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.animal.Herbivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;
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
    private static final String ILLEGAL_POSITION_MESSAGE = "The position %s of the provided game object does not exist on the board.";

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
        board.addGameObject(firstGameObject, firstGameObject.getPosition());
        board.addGameObject(secondGameObject, secondGameObject.getPosition());
        assertEquals(2, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));

        // execute + assert
        board.addGameObject(firstGameObject, firstGameObject.getPosition());
        assertEquals(2, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));

        // execute + assert
        board.addGameObject(thirdGameObject, thirdGameObject.getPosition());
        verify(firstGameObject, times(3)).getPosition();
        verify(secondGameObject, times(2)).getPosition();
        verify(thirdGameObject, times(2)).getPosition();
        assertEquals(3, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));
        assertTrue(board.getGameObjects().containsAll(gameObjects));
        assertTrue(board.getOccupiedPositions().containsAll(positions));
    }

    @Test
    public void testCleanBoard() {
        // prepare
        LifeForm firstLifeForm = mock(Carnivore.class);
        when(firstLifeForm.getPosition()).thenReturn(new Vector2D(0, 0));
        when(firstLifeForm.isDead()).thenReturn(true);
        board.addGameObject(firstLifeForm, firstLifeForm.getPosition());

        LifeForm secondLifeForm = mock(Herbivore.class);
        when(secondLifeForm.getPosition()).thenReturn(new Vector2D(0, 1));
        when(secondLifeForm.isDead()).thenReturn(false);
        board.addGameObject(secondLifeForm, secondLifeForm.getPosition());

        LifeForm thirdLifeForm = mock(Plant.class);
        when(thirdLifeForm.getPosition()).thenReturn(new Vector2D(0, 1));
        when(thirdLifeForm.isDead()).thenReturn(false);
        board.addGameObject(thirdLifeForm, thirdLifeForm.getPosition());

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
        verify(firstLifeForm, times(1)).isDead();
        verify(secondLifeForm, times(1)).isDead();
        verify(thirdLifeForm, times(1)).isDead();
        verify(firstLifeForm, times(3)).getPosition();
        verify(secondLifeForm, times(3)).getPosition();
        verify(thirdLifeForm, times(3)).getPosition();
        assertEquals(2, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedPositions().size()); // second and third on same position
        assertTrue(gameObjects.containsAll(board.getGameObjects()));
        assertTrue(positions.containsAll(board.getOccupiedPositions()));
        assertTrue(board.getGameObjects().containsAll(gameObjects));
        assertTrue(board.getOccupiedPositions().containsAll(positions));
    }

    @Test
    public void testAreAllSpeciesAlive() {
        AnimalObject animalObject = mock(AnimalObject.class);

        AnimalObject meatEater = mock(Carnivore.class);
        when(meatEater.getPosition()).thenReturn(new Vector2D(0, 1));
        board.addGameObject(meatEater, meatEater.getPosition());

        assertTrue(board.areAllSpeciesAlive());
    }

    }

    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void testAddGameObjectNull() {
        Exception thrown = assertThrows(NullPointerException.class, () -> board.addGameObject(null, new Vector2D(0, 0)));
        assertEquals("Game object cannot be null to add it on the board.", thrown.getMessage());
    }


    @Test
    public void testAddGameObjectInvalidPositionNull() {
        Exception thrown = assertThrows(NullPointerException.class, () -> board.addGameObject(firstGameObject, null));
        assertEquals("The position cannot be null to add the game object on the board.", thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionRowNegative() {
        Vector2D position = new Vector2D(0, -1);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject, position));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionColumnNegative() {
        Vector2D position = new Vector2D(-1, 0);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject, position));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionBiggerThanNumberOfRows() {
        Vector2D position = new Vector2D(0, board.getRows());
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject, position));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testAddGameObjectInvalidPositionBiggerThanNumberOfColumns() {
        Vector2D position = new Vector2D(board.getColumns(), 0);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> board.addGameObject(firstGameObject, position));
        assertEquals(String.format(ILLEGAL_POSITION_MESSAGE, position), thrown.getMessage());
    }

    @Test
    public void testAreAllSpeciesAliveInvalid() {
        Herbivore herbivore = mock(Herbivore.class);
        Carnivore carnivore = mock(Carnivore.class);
        when(carnivore.getPosition()).thenReturn(new Vector2D(0, 1));
        when(herbivore.getPosition()).thenReturn(new Vector2D(0, 2));
        when(carnivore.isDead()).thenReturn(true);
        board.addGameObject(carnivore, carnivore.getPosition());
        board.addGameObject(herbivore, herbivore.getPosition());
        carnivore.die();
        board.cleanBoard();
        assertFalse(board.areAllSpeciesAlive());
    }

    @Test
    public void testInvalidConstructorRowLowerThanMinValue() {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> board = new Board(Board.MIN_ROWS - 1, Board.MIN_COLUMNS));
        assertEquals("The number of rows cannot be less than " + Board.MIN_ROWS, thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorColumnsLowerThanMinValue() {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> board = new Board(Board.MIN_ROWS, Board.MIN_COLUMNS - 1));
        assertEquals("The number of columns cannot be less than " + Board.MIN_COLUMNS, thrown.getMessage());
    }

}
