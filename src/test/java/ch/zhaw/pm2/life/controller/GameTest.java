package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.GameProperties;
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
    private static final String PLANT = "plant";
    private static final String CARNIVORE = "carnivore";
    private static final String HERBIVORE = "herbivore";
    private static final String ANIMAL = "animal";
    private static final String MALE = "M";
    private static final String FEMALE = "F";

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

    private GameProperties getGameProperties(int numOfPlants, int numOfCarnivores, int numOfHerbivores) {
        GameObject plant = mock(Plant.class);
        GameObject carnivore = mock(Carnivore.class);
        GameObject herbivore = mock(Herbivore.class);

        when(plant.getName()).thenReturn(PLANT);
        when(carnivore.getName()).thenReturn(CARNIVORE);
        when(herbivore.getName()).thenReturn(HERBIVORE);

        Map<GameObject, Integer> initMap = new HashMap<>();
        initMap.put(plant, numOfPlants);
        initMap.put(carnivore, numOfCarnivores);
        initMap.put(herbivore, numOfHerbivores);
        return new GameProperties(initMap);
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
        game = new Game(board, getGameProperties(NUM_OF_PLANTS, NUM_OF_CARNIVORES, NUM_OF_HERBIVORES));

        // assert
        assertEquals(NUMBER_OF_GAME_OBJECTS, board.getGameObjects().size());
        assertEquals(NUMBER_OF_GAME_OBJECTS, board.getOccupiedPositions().size());
    }

    @Test
    public void testIsOngoing() {
        game = new Game(board, getGameProperties(0, 0, 0));
        assertTrue(game.isOngoing());
    }

    @Test
    public void testStop() {
        game = new Game(board, getGameProperties(0, 0, 0));
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
        when(plant.getName()).thenReturn(PLANT);
        when(plant.getEnergy()).thenReturn(10);
        when(plant.getGender()).thenReturn("N");
        when(plant.getPosition()).thenReturn(zeroPosition);
        when(plant.isAlive()).thenReturn(true);

        dummyGameObjectsSet.add(plant);
        dummyPositionsSet.add(plant.getPosition());

        // animalObject Mock
        AnimalObject animalObject = mock(AnimalObject.class);
        when(animalObject.getName()).thenReturn(CARNIVORE);
        when(animalObject.getEnergy()).thenReturn(10);
        when(animalObject.getGender()).thenReturn(FEMALE);
        when(animalObject.getPosition()).thenReturn(zeroPosition);
        when(animalObject.isAlive()).thenReturn(true);

        // Sets and game init
        dummyGameObjectsSet.add(animalObject);
        dummyPositionsSet.add(animalObject.getPosition());

        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.isSpeciesAlive(null)).thenReturn(true);

        game = new Game(board, getGameProperties(1, 0, 1));

        // execute
        String messageLog = game.nextMove();

        // verifies and assertions
        verify(animalObject, times(1)).eat(plant);
        verify(animalObject, times(1)).move(anySet());

        assertEquals(animalObject.getName() + ": Das war lecker (" + plant.getName() + ")!\n", messageLog);
        dummyGameObjectsSet.remove(plant);
        assertEquals(1, board.getGameObjects().size());
        assertEquals(1, board.getOccupiedPositions().size());
    }

    @Test
    public void testNextMoveEatCarnivoreAndCarnivore() throws LifeFormException {
        // setup
        Vector2D zeroPosition = new Vector2D(0,0);
        Set<GameObject> dummyGameObjectsSet = new HashSet<>();
        Set<Vector2D> dummyPositionsSet = new HashSet<>();

        // carnivoreOne Mock
        Carnivore carnivoreOne = mock(Carnivore.class);
        when(carnivoreOne.getName()).thenReturn(CARNIVORE);
        when(carnivoreOne.getEnergy()).thenReturn(10);
        when(carnivoreOne.getGender()).thenReturn(MALE);
        when(carnivoreOne.getPosition()).thenReturn(zeroPosition);
        when(carnivoreOne.isAlive()).thenReturn(true);

        dummyGameObjectsSet.add(carnivoreOne);
        dummyPositionsSet.add(carnivoreOne.getPosition());

        // carnivoreTwo Mock
        Carnivore carnivoreTwo = mock(Carnivore.class);
        when(carnivoreTwo.getName()).thenReturn(CARNIVORE);
        when(carnivoreTwo.getEnergy()).thenReturn(8);
        when(carnivoreTwo.getGender()).thenReturn(MALE);
        when(carnivoreTwo.getPosition()).thenReturn(zeroPosition);
        when(carnivoreTwo.isAlive()).thenReturn(true);
        doThrow(new LifeFormException("Kann dieses Tier nicht fressen, weil es staerker ist als ich.")).when(carnivoreTwo).eat(carnivoreOne);

        dummyGameObjectsSet.add(carnivoreTwo);
        dummyPositionsSet.add(carnivoreTwo.getPosition());

        // Sets and game init
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.isSpeciesAlive(null)).thenReturn(true);

        game = new Game(board, getGameProperties(1, 0, 1));

        // execute
        String messageLog = game.nextMove();

        // verifies and assertions
        verify(carnivoreOne, times(1)).eat(carnivoreTwo);
        verify(carnivoreOne, times(1)).move(anySet());
        verify(carnivoreTwo, times(1)).eat(carnivoreOne);
        verify(carnivoreTwo, times(1)).move(anySet());

        String firstEatsSecondMessage = carnivoreOne.getName() + ": Das war lecker (" + carnivoreTwo.getName() + ")!\n";


        assertThat(messageLog, anyOf(is("Kann dieses Tier nicht fressen, weil es staerker ist als ich.\r\n" + firstEatsSecondMessage),
                                     is(firstEatsSecondMessage + "Kann dieses Tier nicht fressen, weil es staerker ist als ich.\r\n")));
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
        when(herbivoreChild.getName()).thenReturn(HERBIVORE);
        when(herbivoreChild.getGender()).thenReturn(FEMALE);
        when(herbivoreChild.getPosition()).thenReturn(new Vector2D(1, 1));

        dummyGameObjectsSet.add(herbivoreChild);
        dummyPositionsSet.add(herbivoreChild.getPosition());

        // herbivoreMale Mock
        when(herbivoreMale.getEnergy()).thenReturn(10);
        when(herbivoreMale.getName()).thenReturn(HERBIVORE);
        when(herbivoreMale.getFertilityThreshold()).thenReturn(10);
        when(herbivoreMale.getGender()).thenReturn(MALE);
        when(herbivoreMale.reproduce(herbivoreFemale)).thenThrow(new LifeFormException("Kann keine Kinder gebaeren, weil ich ein Maennchen bin."));
        when(herbivoreMale.getPosition()).thenReturn(zeroPosition);
        when(herbivoreMale.isAlive()).thenReturn(true);

        dummyGameObjectsSet.add(herbivoreMale);
        dummyPositionsSet.add(herbivoreMale.getPosition());

        // herbivoreFemale Mock
        when(herbivoreFemale.getEnergy()).thenReturn(10);
        when(herbivoreFemale.getName()).thenReturn(HERBIVORE);
        when(herbivoreFemale.getFertilityThreshold()).thenReturn(10);
        when(herbivoreFemale.getGender()).thenReturn(FEMALE);
        when(herbivoreFemale.reproduce(herbivoreMale)).thenReturn(herbivoreChild);
        when(herbivoreFemale.getPosition()).thenReturn(zeroPosition);
        when(herbivoreFemale.isAlive()).thenReturn(true);

        dummyGameObjectsSet.add(herbivoreFemale);
        dummyPositionsSet.add(herbivoreFemale.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.isSpeciesAlive(null)).thenReturn(true);

        game = new Game(board, getGameProperties(0, 0, 2));

        // execute
        String messageLog = game.nextMove();

        // verifies and assertions
        verify(herbivoreMale, times(0)).eat(herbivoreFemale);
        verify(herbivoreMale, times(1)).reproduce(herbivoreFemale);
        verify(herbivoreFemale, times(1)).reproduce(herbivoreMale);
        verify(herbivoreMale, times(1)).move(anySet());
        verify(herbivoreFemale, times(1)).move(anySet());

        assertThat(messageLog, anyOf(is(herbivoreFemale.getName() + ": Wir haben uns soeben gepaart\nKann keine Kinder gebaeren, weil ich ein Maennchen bin.\r\n"),
                                          is("Kann keine Kinder gebaeren, weil ich ein Maennchen bin.\r\n" + herbivoreFemale.getName() + ": Wir haben uns soeben gepaart\n")));
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
        when(animalObjectOne.getName()).thenReturn(ANIMAL);
        when(animalObjectOne.getEnergy()).thenReturn(10);
        when(animalObjectOne.getGender()).thenReturn(FEMALE);
        when(animalObjectOne.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(animalObjectOne);
        dummyPositionsSet.add(animalObjectOne.getPosition());

        // animalObjectTwo Mock
        AnimalObject animalObjectTwo = mock(AnimalObject.class);
        when(animalObjectTwo.getName()).thenReturn(ANIMAL);
        when(animalObjectTwo.getEnergy()).thenReturn(10);
        when(animalObjectTwo.getGender()).thenReturn(FEMALE);
        when(animalObjectTwo.getPosition()).thenReturn(onePosition);

        dummyGameObjectsSet.add(animalObjectTwo);
        dummyPositionsSet.add(animalObjectTwo.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.isSpeciesAlive(null)).thenReturn(true);

        game = new Game(board, getGameProperties(0, 0, 2));

        // execute
        String messageLog = game.nextMove();

        // verifies and assertions
        verify(animalObjectOne, times(1)).move(anySet());
        verify(animalObjectTwo, times(1)).move(anySet());

        assertEquals( "", messageLog);
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
        when(carnivore.getName()).thenReturn(CARNIVORE);
        when(carnivore.getEnergy()).thenReturn(10);
        when(carnivore.getGender()).thenReturn(FEMALE);
        when(carnivore.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(carnivore);
        dummyPositionsSet.add(carnivore.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.isSpeciesAlive(null)).thenReturn(false);

        game = new Game(board, getGameProperties(0, 1, 0));

        // execute
        String messageLog = game.nextMove();

        // verifies and assertions
        assertEquals( "Die Simulation wurde beendet, weil die Endbedingung erfuellt wurde.", messageLog);
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
        when(carnivore.getName()).thenReturn(CARNIVORE);
        when(carnivore.getEnergy()).thenReturn(-1);
        when(carnivore.getGender()).thenReturn(FEMALE);
        when(carnivore.getPosition()).thenReturn(zeroPosition);

        dummyGameObjectsSet.add(carnivore);
        dummyPositionsSet.add(carnivore.getPosition());

        //board mock
        when(board.getGameObjects()).thenReturn(dummyGameObjectsSet);
        when(board.getOccupiedPositions()).thenReturn(dummyPositionsSet);
        when(board.isSpeciesAlive(null)).thenReturn(true);


        game = new Game(board, getGameProperties(0, 1, 0));

        // execute
        String messageLog = game.nextMove();

        // verifies and assertions
        verify(carnivore, times(1)).move(anySet());
        assertEquals(carnivore.getName() + ": ist vor Ermuedung gestorben.\r\n", messageLog);
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
        GameProperties gameProperties = getGameProperties(NUM_OF_PLANTS, NUM_OF_CARNIVORES, NUM_OF_HERBIVORES);
        Exception thrown = assertThrows(NullPointerException.class, () -> new Game(null, gameProperties));
        assertEquals("Board cannot be null to create the game.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsLessThanMinimum() {
        GameProperties gameProperties = getGameProperties(-1, NUM_OF_CARNIVORES, NUM_OF_HERBIVORES);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, gameProperties));
        assertEquals("Number of plant is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterLessThanMinimum() {
        GameProperties gameProperties = getGameProperties(NUM_OF_PLANTS, -1, NUM_OF_HERBIVORES);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, gameProperties));
        assertEquals("Number of carnivore is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterLessThanMinimum() {
        GameProperties gameProperties = getGameProperties(NUM_OF_PLANTS, NUM_OF_CARNIVORES, -1);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, gameProperties));
        assertEquals("Number of herbivore is less than the minimal value.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantsMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);

        GameProperties gameProperties = getGameProperties(NUMBER_OF_GAME_OBJECTS, 0, 0);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, gameProperties));
        assertEquals("Number of plant exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumMeatEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);

        GameProperties gameProperties = getGameProperties(0, NUMBER_OF_GAME_OBJECTS, 0);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, gameProperties));
        assertEquals("Number of carnivore exceed the number of available field.", thrown.getMessage());
    }

    @Test
    public void testInvalidConstructorNumPlantEaterMoreThanFields() {
        int dimension = (int) Math.sqrt(NUMBER_OF_GAME_OBJECTS) - 1;
        when(board.getRows()).thenReturn(dimension);
        when(board.getColumns()).thenReturn(dimension);

        GameProperties gameProperties = getGameProperties(0, 0, NUMBER_OF_GAME_OBJECTS);
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> new Game(board, gameProperties));
        assertEquals("Number of herbivore exceed the number of available field.", thrown.getMessage());
    }

}