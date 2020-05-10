package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {

    private static final int NUM_OF_ROWS = 3;
    private static final int NUM_OF_COLUMNS = 3;

    private static final int NUM_OF_PLANTS = 1;
    private static final int NUM_OF_MEAT_EATERS = 2;
    private static final int NUM_OF_PLANT_EATERS = 1;

    private static final int NUMBER_OF_GAME_OBJECTS = NUM_OF_MEAT_EATERS + NUM_OF_PLANT_EATERS + NUM_OF_PLANTS;

    @Mock private Board board;
    private final Random random = new Random();
    private Game game;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(board.getRows()).thenReturn(NUM_OF_ROWS);
        when(board.getColumns()).thenReturn(NUM_OF_COLUMNS);
        game = new Game(board, NUM_OF_PLANTS, NUM_OF_MEAT_EATERS, NUM_OF_PLANT_EATERS);
    }

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void testInit() {
        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_GAME_OBJECTS; i++) {
            GameObject gameObject = mock(GameObject.class);
            when(gameObject.getPosition()).thenReturn(mock(Vector2D.class));

            dummyGameObjectsSet.add(gameObject);
            dummyPositionsSet.add(gameObject.getPosition());
        }
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.getRandomPosition()).thenAnswer(invocation -> {
            int x = random.nextInt(NUM_OF_COLUMNS);
            int y = random.nextInt(NUM_OF_ROWS);
            return new Vector2D(x, y);
        });

        game.init();

        assertEquals(NUMBER_OF_GAME_OBJECTS, board.getGameObjects().size());
        assertEquals(NUMBER_OF_GAME_OBJECTS, board.getOccupiedPositions().size());
    }

    @Test
    public void testIsOngoing() {
        game = new Game(board,0, 0, 0);
        game.init();
        assertTrue(game.isOngoing());
    }

    @Test
    public void testStop() {
        game = new Game(board,0, 0, 0);
        game.init();
        game.stop();
        assertFalse(game.isOngoing());
    }

    @Test
    public void testNextMove() {
        // IMPORTANT TO CHECK: positions, number of occupied positions, number of game objects, message log

        // TODO: test move
        // does it move outside the grid?

        // TODO: test reproduction
        //--- valid  : is fecund and male/female
        //--- invalid: is not fecund or male/male or female/female, ...
        // plant eater tries to f**k plant (invalid)
        // plant eater tries to f**k plant eater (valid)
        // plant eater tries to f**k meat eater (invalid)

        // meat eater tries to f**k plant (invalid)
        // meat eater tries to f**k plant eater (invalid)
        // meat eater tries to f**k meat eater (valid)

        // TODO: test eat (only when not reproducing)
        // plant eater tries to eat plant (valid)
        // plant eater tries to eat plant eater (invalid)
        // plant eater tries to eat meat eater (invalid)

        // meat eater tries to eat plant (invalid)
        // meat eater tries to eat plant eater (valid)
        // meat eater tries to eat meat eater (valid)
    }

    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void testInvalidConstructorBoardNull() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new Game(null, NUM_OF_PLANTS, NUM_OF_MEAT_EATERS, NUM_OF_PLANT_EATERS));
        assertEquals("Board cannot be null to create the game.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsLessThanMinimum() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, -1, NUM_OF_MEAT_EATERS, NUM_OF_PLANT_EATERS));
        assertEquals("Number of plants is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterLessThanMinimum() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUM_OF_PLANTS, -1, NUM_OF_PLANT_EATERS));
        assertEquals("Number of meat eaters is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterLessThanMinimum() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUM_OF_PLANTS, NUM_OF_MEAT_EATERS, -1));
        assertEquals("Number of plant eaters is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUMBER_OF_GAME_OBJECTS, 0, 0));
        assertEquals("Number of plants exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, 0, NUMBER_OF_GAME_OBJECTS, 0));
        assertEquals("Number of meat eaters exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, 0, 0, NUMBER_OF_GAME_OBJECTS));
        assertEquals("Number of plant eaters exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidNumGameObjectsMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS);
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUM_OF_PLANTS + 1, NUM_OF_MEAT_EATERS, NUM_OF_PLANT_EATERS));
        assertEquals("Number of game objects exceed the number of available field.", thrown.getMessage());
    }

}