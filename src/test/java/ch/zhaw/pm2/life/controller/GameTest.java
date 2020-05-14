package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.animal.Herbivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {

    private static final int NUM_OF_ROWS = 3;
    private static final int NUM_OF_COLUMNS = 3;

    private static final int NUM_OF_PLANTS = 1;
    private static final int NUM_OF_CARNIVORES = 2;
    private static final int NUM_OF_HERBIVORES = 1;

    private static final int NUMBER_OF_GAME_OBJECTS = NUM_OF_CARNIVORES + NUM_OF_HERBIVORES + NUM_OF_PLANTS;

    @Mock private Board board;

    private final Random random = new Random();
    private Game game;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(board.getRows()).thenReturn(NUM_OF_ROWS);
        when(board.getColumns()).thenReturn(NUM_OF_COLUMNS);
    }

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    private Map<GameObject, Integer> getInitMap(int numOfPlants, int numOfCarnivores, int numOfHerbivores) {
        Map<GameObject, Integer> initMap = new HashMap<>();
        initMap.put(mock(Plant.class), numOfPlants);
        initMap.put(mock(Carnivore.class), numOfCarnivores);
        initMap.put(mock(Herbivore.class), numOfHerbivores);
        return initMap;
    }

    @Test
    public void testConstructorInit() {
        // prepare
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

        // execute
        game = new Game(board, getInitMap(NUM_OF_PLANTS, NUM_OF_CARNIVORES, NUM_OF_HERBIVORES));

        // assert
        assertEquals(NUMBER_OF_GAME_OBJECTS, board.getGameObjects().size());
        assertEquals(NUMBER_OF_GAME_OBJECTS, board.getOccupiedPositions().size());
    }

    @Test
    public void testIsOngoing() {
        game = new Game(board, getInitMap(0, 0, 0));
        assertTrue(game.isOngoing());
    }

    @Test
    public void testStop() {
        game = new Game(board, getInitMap(0, 0, 0));
        game.stop();
        assertFalse(game.isOngoing());
    }

    @Test
    public void testNextMoveEatHerbivoreAndPlant() throws LifeFormException {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);
        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        // plant Mock
        Plant plant = mock(Plant.class);
        when(plant.getEnergy()).thenReturn(10);
        when(plant.getGender()).thenReturn("N");
        when(plant.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(plant);
        dummyPositionsSet.add(plant.getPosition());

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

        game = new Game(board, getInitMap(1, 0, 1));

        // execute
        game.nextMove();

        // verifies and assertions
        verify(animalObject, times(1)).eat(plant);
        verify(animalObject, times(1)).move(anySet());

        assertEquals(animalObject.getName() + ": Yummy food (" + plant.getName() + ")!\n", game.nextMove());
        dummyGameObjectsSet.remove(plant);
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
    public void testNextMoveEatCarnivoreAndCarnivore() throws LifeFormException {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);
        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        // carnivoreOne Mock
        Carnivore carnivoreOne = mock(Carnivore.class);
        when(carnivoreOne.getEnergy()).thenReturn(10);
        when(carnivoreOne.getGender()).thenReturn("M");
        when(carnivoreOne.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(carnivoreOne);
        dummyPositionsSet.add(carnivoreOne.getPosition());

        // carnivoreTwo Mock
        Carnivore carnivoreTwo = mock(Carnivore.class);
        when(carnivoreTwo.getEnergy()).thenReturn(8);
        when(carnivoreTwo.getGender()).thenReturn("M");
        when(carnivoreTwo.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(carnivoreTwo);
        dummyPositionsSet.add(carnivoreTwo.getPosition());

        // Sets and game init
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.noAnimalExtinct()).thenReturn(true);

        game = new Game(board, getInitMap(1, 0, 1));

        // execute
        game.nextMove();

        // verifies and assertions
        verify(carnivoreTwo, times(1)).eat(carnivoreOne);
        verify(carnivoreTwo, times(1)).move(anySet());

        assertEquals(carnivoreOne.getName() + ": Yummy food (" + carnivoreTwo.getName() + ")!\n", game.nextMove());
        dummyGameObjectsSet.remove(carnivoreTwo);
        assertEquals(1, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedPositions().size());
    }

    @Test
    public void testNextMoveReproduce() throws LifeFormException {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);

        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        Herbivore herbivoreChild = mock(Herbivore.class);
        Herbivore herbivoreMale = mock(Herbivore.class);
        Herbivore herbivoreFemale = mock(Herbivore.class);

        // herbivoreChild Mock
        when(herbivoreChild.getEnergy()).thenReturn(10);
        when(herbivoreChild.getGender()).thenReturn("F");
        when(herbivoreChild.getPosition()).thenReturn(new Vector2D(1, 1));

        dummyGameObjectsSet.add(herbivoreChild);
        dummyPositionsSet.add(herbivoreChild.getPosition());

        // herbivoreMale Mock
        when(herbivoreMale.getEnergy()).thenReturn(10);
        when(herbivoreMale.getFertilityThreshold()).thenReturn(10);
        when(herbivoreMale.getGender()).thenReturn("M");
        when(herbivoreMale.reproduce(herbivoreFemale)).thenThrow(new LifeFormException("Cannot give birth because im male"));
        when(herbivoreMale.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(herbivoreMale);
        dummyPositionsSet.add(herbivoreMale.getPosition());

        // herbivoreFemale Mock
        when(herbivoreFemale.getEnergy()).thenReturn(10);
        when(herbivoreFemale.getFertilityThreshold()).thenReturn(10);
        when(herbivoreFemale.getGender()).thenReturn("F");
        when(herbivoreFemale.reproduce(herbivoreMale)).thenReturn(herbivoreChild);
        when(herbivoreFemale.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(herbivoreFemale);
        dummyPositionsSet.add(herbivoreFemale.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.noAnimalExtinct()).thenReturn(true);

        game = new Game(board, getInitMap(0, 0, 2));

        // execute
        game.nextMove();

        // verifies and assertions
        verify(herbivoreMale, times(0)).eat(herbivoreFemale);
        verify(herbivoreMale, times(1)).reproduce(herbivoreFemale);
        verify(herbivoreFemale, times(1)).reproduce(herbivoreMale);
        verify(herbivoreMale, times(1)).move(anySet());
        verify(herbivoreFemale, times(1)).move(anySet());

        assertThat(game.nextMove(), anyOf(is(herbivoreFemale.getName() + ": We just reproduced with each other\nCannot give birth because im male\r\n"),
                                          is("Cannot give birth because im male\r\n" + herbivoreFemale.getName() + ": We just reproduced with each other\n")));
        dummyGameObjectsSet.add(herbivoreChild);
        assertEquals(3, board.getGameObjects().size());
        dummyPositionsSet.add(herbivoreChild.getPosition());
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

        game = new Game(board, getInitMap(0, 0, 2));

        // execute
        game.nextMove();

        // verifies and assertions
        verify(animalObjectOne, times(1)).move(anySet());
        verify(animalObjectTwo, times(1)).move(anySet());

        assertEquals( "", game.nextMove());
        assertEquals(2, board.getGameObjects().size());
        assertEquals(2, board.getOccupiedPositions().size());
    }

    @Test
    public void testNextMoveElseCase() {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);
        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        // animalObjectOne Mock
        Carnivore carnivore = mock(Carnivore.class);
        when(carnivore.getEnergy()).thenReturn(10);
        when(carnivore.getGender()).thenReturn("F");
        when(carnivore.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(carnivore);
        dummyPositionsSet.add(carnivore.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.noAnimalExtinct()).thenReturn(false);


        game = new Game(board, getInitMap(0, 1, 0));

        // execute
        game.nextMove();

        // verifies and assertions
        assertEquals( "The simulation has stopped because the ending condition was met", game.nextMove());
        assertFalse(game.isOngoing());
        dummyPositionsSet.remove(carnivore.getPosition());
        assertEquals(0, board.getOccupiedPositions().size());
        dummyGameObjectsSet.remove(carnivore);
        assertEquals(0, board.getGameObjects().size());
    }

    @Test
    public void testNextMoveDiedOfExhaustion() {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);

        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        // animalObjectOne Mock
        Carnivore carnivore = mock(Carnivore.class);
        when(carnivore.getEnergy()).thenReturn(-1);
        when(carnivore.getGender()).thenReturn("F");
        when(carnivore.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(carnivore);
        dummyPositionsSet.add(carnivore.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.noAnimalExtinct()).thenReturn(true);


        game = new Game(board, getInitMap(0, 1, 0));

        // execute
        game.nextMove();

        // verifies and assertions
        verify(carnivore, times(1)).move(anySet());
        assertEquals(carnivore.getName() + ": died of exhaustion.\r\n", game.nextMove());
        dummyPositionsSet.remove(carnivore.getPosition());
        assertEquals(0, board.getOccupiedPositions().size());
        dummyGameObjectsSet.remove(carnivore);
        assertEquals(0, board.getGameObjects().size());
    }

    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void testInvalidConstructorBoardNull() {
        Map<GameObject, Integer> initMap = getInitMap(NUM_OF_PLANTS, NUM_OF_CARNIVORES, NUM_OF_HERBIVORES);
        Exception thrown = assertThrows(NullPointerException.class, () -> new Game(null, initMap));
        assertEquals("Board cannot be null to create the game.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsLessThanMinimum() {
        Map<GameObject, Integer> initMap = getInitMap(-1, NUM_OF_CARNIVORES, NUM_OF_HERBIVORES);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, initMap));
        assertEquals("Number of plants is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterLessThanMinimum() {
        Map<GameObject, Integer> initMap = getInitMap(NUM_OF_PLANTS, -1, NUM_OF_HERBIVORES);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, initMap));
        assertEquals("Number of carnivores is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterLessThanMinimum() {
        Map<GameObject, Integer> initMap = getInitMap(NUM_OF_PLANTS, NUM_OF_CARNIVORES, -1);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, initMap));
        assertEquals("Number of herbivores is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);

        Map<GameObject, Integer> initMap = getInitMap(NUMBER_OF_GAME_OBJECTS, 0, 0);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, initMap));
        assertEquals("Number of plants exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);

        Map<GameObject, Integer> initMap = getInitMap(0, NUMBER_OF_GAME_OBJECTS, 0);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, initMap));
        assertEquals("Number of carnivores exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);

        Map<GameObject, Integer> initMap = getInitMap(0, 0, NUMBER_OF_GAME_OBJECTS);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, initMap));
        assertEquals("Number of herbivores exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidNumGameObjectsMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS);
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);

        Map<GameObject, Integer> initMap = getInitMap(NUM_OF_PLANTS + 1, NUM_OF_CARNIVORES, NUM_OF_HERBIVORES);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, initMap));
        assertEquals("Number of game objects exceed the number of available field.", thrown.getMessage());
    }

}