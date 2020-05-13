package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeFormException;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
    public void testNextMoveEat() throws LifeFormException {

        // setup
        Vector2D zeroPosition = new Vector2D(0,0);
        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        // plantObject Mock
        PlantObject plantObject = mock(PlantObject.class);
        when(plantObject.getEnergy()).thenReturn(10);
        when(plantObject.getGender()).thenReturn("N");
        when(plantObject.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(plantObject);
        dummyPositionsSet.add(plantObject.getPosition());

        // animalObject Mock
        AnimalObject animalObject = mock(AnimalObject.class);
        when(animalObject.getEnergy()).thenReturn(10);
        when(animalObject.getGender()).thenReturn("F");
        when(animalObject.getPosition()).thenReturn(zeroPosition);

        // Sets and game init
        dummyGameObjectsSet.add(animalObject);
        dummyPositionsSet.add(animalObject.getPosition());

        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.noAnimalExtinct()).thenReturn(true);

        game = new Game(board,1, 0, 1);
        game.init();
        game.nextMove();

        // verifies and assertions
        verify(animalObject, times(1)).eat(plantObject);
        verify(animalObject, times(1)).move(anySet());

        assertEquals( animalObject.getClass().getSimpleName() + ": Yummy food (" + plantObject.getClass().getSimpleName() + ")!\n", game.nextMove());
        dummyGameObjectsSet.remove(plantObject);
        assertEquals(1, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedPositions().size());

        // IMPORTANT TO CHECK: positions, number of occupied positions, number of game objects, message log

        // TODO: test move
        // does it move outside the grid?

        // TODO: test reproduction
        //--- valid  : is fecund and male/female
        //--- invalid: is not fecund or male/male or female/female, ...
        // plant eater tries to have a good time with plant (invalid)
        // plant eater tries to have a good time with plant eater (valid)
        // plant eater tries to have a good time with meat eater (invalid)

        // meat eater tries to have a good time with plant (invalid)
        // meat eater tries to have a good time with plant eater (invalid)
        // meat eater tries to have a good time with meat eater (valid)

        // TODO: test eat (only when not reproducing)
        // plant eater tries to eat plant (valid)
        // plant eater tries to eat plant eater (invalid)
        // plant eater tries to eat meat eater (invalid)

        // meat eater tries to eat plant (invalid)
        // meat eater tries to eat plant eater (valid)
        // meat eater tries to eat meat eater (valid)
    }

    @Test
    public void testNextMoveReproduce() throws LifeFormException {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);

        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        PlantEater plantEaterChild = mock(PlantEater.class);
        PlantEater plantEaterMale = mock(PlantEater.class);
        PlantEater plantEaterFemale = mock(PlantEater.class);

        // plantEaterChild Mock
        when(plantEaterChild.getEnergy()).thenReturn(10);
        when(plantEaterChild.getGender()).thenReturn("F");
        when(plantEaterChild.getPosition()).thenReturn(new Vector2D(1,1));

        // plantEaterMale Mock
        when(plantEaterMale.getEnergy()).thenReturn(10);
        when(plantEaterMale.getFertilityThreshold()).thenReturn(10);
        when(plantEaterMale.getGender()).thenReturn("M");
        when(plantEaterMale.reproduce(plantEaterFemale)).thenThrow(new LifeFormException("Cannot give birth because im male"));
        when(plantEaterMale.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(plantEaterMale);
        dummyPositionsSet.add(plantEaterMale.getPosition());

        // plantEaterFemale Mock
        when(plantEaterFemale.getEnergy()).thenReturn(10);
        when(plantEaterFemale.getFertilityThreshold()).thenReturn(10);
        when(plantEaterFemale.getGender()).thenReturn("F");
        when(plantEaterFemale.reproduce(plantEaterMale)).thenReturn(plantEaterChild);
        when(plantEaterFemale.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(plantEaterFemale);
        dummyPositionsSet.add(plantEaterFemale.getPosition());

        dummyGameObjectsSet.add(plantEaterChild);
        dummyPositionsSet.add(plantEaterChild.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.noAnimalExtinct()).thenReturn(true);


        game = new Game(board,0, 0, 2);
        game.init();
        game.nextMove();

        // verifies and assertions
        verify(plantEaterMale, times(0)).eat(plantEaterFemale);
        verify(plantEaterMale, times(1)).reproduce(plantEaterFemale);
        verify(plantEaterFemale, times(1)).reproduce(plantEaterMale);
        verify(plantEaterMale, times(1)).move(anySet());
        verify(plantEaterFemale, times(1)).move(anySet());

        assertThat(game.nextMove(), anyOf(is(plantEaterFemale.getClass().getSimpleName() + ": We just reproduced with each other\nCannot give birth because im male\r\n"),
                                          is("Cannot give birth because im male\r\n" + plantEaterFemale.getClass().getSimpleName() + ": We just reproduced with each other\n")));
        dummyGameObjectsSet.add(plantEaterChild);
        assertEquals(3, board.getGameObjects().size());
        dummyPositionsSet.add(plantEaterChild.getPosition());
        assertEquals(2, board.getOccupiedPositions().size());
    }

    @Test
    public void testNextMoveNoMovesDone() {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);
        Vector2D onePosition = new Vector2D(1,1);
        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        // animalObjectOne Mock
        AnimalObject animalObjectOne = mock(AnimalObject.class);
        when(animalObjectOne.getEnergy()).thenReturn(10);
        when(animalObjectOne.getGender()).thenReturn("F");
        when(animalObjectOne.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(animalObjectOne);
        dummyPositionsSet.add(animalObjectOne.getPosition());

        // animalObjectTwo Mock
        AnimalObject animalObjectTwo = mock(AnimalObject.class);
        when(animalObjectTwo.getEnergy()).thenReturn(10);
        when(animalObjectTwo.getGender()).thenReturn("F");
        when(animalObjectTwo.getPosition()).thenReturn(onePosition);

        dummyGameObjectsSet.add(animalObjectTwo);
        dummyPositionsSet.add(animalObjectTwo.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.noAnimalExtinct()).thenReturn(true);

        game = new Game(board,0, 0, 2);
        game.init();
        game.nextMove();

        // verifies and assertions
        verify(animalObjectOne, times(1)).move(anySet());
        verify(animalObjectTwo, times(1)).move(anySet());

        assertEquals( "", game.nextMove());
        assertEquals(2, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
    }
    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void testInvalidConstructorBoardNull() {
        Exception thrown = assertThrows(NullPointerException.class, () -> new Game(null, NUM_OF_PLANTS, NUM_OF_MEAT_EATERS, NUM_OF_PLANT_EATERS));
        assertEquals("Board cannot be null to create the game.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsLessThanMinimum() {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, -1, NUM_OF_MEAT_EATERS, NUM_OF_PLANT_EATERS));
        assertEquals("Number of plants is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterLessThanMinimum() {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUM_OF_PLANTS, -1, NUM_OF_PLANT_EATERS));
        assertEquals("Number of meat eaters is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterLessThanMinimum() {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUM_OF_PLANTS, NUM_OF_MEAT_EATERS, -1));
        assertEquals("Number of plant eaters is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUMBER_OF_GAME_OBJECTS, 0, 0));
        assertEquals("Number of plants exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, 0, NUMBER_OF_GAME_OBJECTS, 0));
        assertEquals("Number of meat eaters exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, 0, 0, NUMBER_OF_GAME_OBJECTS));
        assertEquals("Number of plant eaters exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidNumGameObjectsMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS);
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, NUM_OF_PLANTS + 1, NUM_OF_MEAT_EATERS, NUM_OF_PLANT_EATERS));
        assertEquals("Number of game objects exceed the number of available field.", thrown.getMessage());
    }

}