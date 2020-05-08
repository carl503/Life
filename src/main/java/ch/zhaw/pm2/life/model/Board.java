package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.*;

/**
 * This model class represents the board containing all the game objects.
 * @author lubojcar, meletlea
 */
public class Board {

    /**
     * Minimal number of rows.
     */
    public static final int MIN_ROWS = 3;

    /**
     * Minimal number of columns.
     */
    public static final int MIN_COLUMNS = 3;

    private final Random random = new Random();
    private final int rows;
    private final int columns;
    private final Set<GameObject> gameObjects = new HashSet<>();
    private final Set<Vector2D> occupiedPositions = new HashSet<>();
    private final Set<Vector2D> borders = new HashSet<>();

    /**
     * Default constructor.
     * @param rows    Number of rows as int.
     * @param columns Number of columns as int.
     * @throws IllegalArgumentException when rows is less than {@link Board#MIN_ROWS} or columns is less than {@link Board#MIN_COLUMNS}
     */
    public Board(int rows, int columns) {
        if (rows < MIN_ROWS) {
            throw new IllegalArgumentException("The number of rows cannot be less than " + MIN_ROWS);
        }
        if (columns < MIN_COLUMNS) {
            throw new IllegalArgumentException("The number of columns cannot be less than " + MIN_COLUMNS);
        }
        this.rows = rows;
        this.columns = columns;
        generateBorderPoints();
    }

    /**
     * Adds a game object to the set.
     * @param gameObject {@link GameObject}
     * @throws NullPointerException when the {@link GameObject} is null.
     */
    public void addGameObject(GameObject gameObject) {
        Objects.requireNonNull(gameObject, "Game object cannot be null to add it on the board.");

        Vector2D position = calculateRandomPosition(rows, columns);
        gameObject.setPosition(position);

        gameObjects.add(gameObject);
        occupiedPositions.add(gameObject.getPosition());
    }

    private Vector2D calculateRandomPosition(int rows, int columns) {
        int xPos = random.nextInt(columns);
        int yPos = random.nextInt(rows);
        return new Vector2D(xPos, yPos);
    }

    /**
     * Remove all dead life forms from the board.
     */
    public void cleanBoard() {
        Set<LifeForm> deadLifeForms = gameObjects.stream()
                .filter(LifeForm.class::isInstance)
                .map(LifeForm.class::cast)
                .filter(LifeForm::isDead)
                .collect(Collectors.toSet());
        gameObjects.removeAll(deadLifeForms);
        occupiedPositions.removeAll(getFreedPositions(deadLifeForms));
    }

    private Set<Vector2D> getFreedPositions(Set<LifeForm> deadLifeForms) {
        Set<Vector2D> currentOccupiedPositions = gameObjects.stream()
                .map(GameObject::getPosition)
                .collect(Collectors.toSet());

        return deadLifeForms.stream()
                .map(GameObject::getPosition)
                .filter(not(currentOccupiedPositions::contains))
                .collect(Collectors.toSet());
    }

    /**
     * Returns the number of rows.
     * @return rows as int.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns.
     * @return columns as int.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns a set of game objects
     * @return set of game objects.
     */
    public Set<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * Returns the game object of a position
     * @param pos Position on the field
     * @return game object if found otherwise null
     */
    public GameObject getGameObject(Vector2D pos) {
        GameObject gameObject = null;
        for (GameObject go : gameObjects) {
            if (go.getPosition().equals(pos)) {
                gameObject = go;
            }
        }
        return gameObject;
    }

    private void generateBorderPoints() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                borders.add(new Vector2D(j, 0));
                borders.add(new Vector2D(j, columns));
            }
            borders.add(new Vector2D(0, i));
            borders.add(new Vector2D(rows, i));
        }
    }

    /**
     * Returns a set of occupied positions
     * @return set of position objects
     */
    public Set<Vector2D> getOccupiedPositions() {
        return occupiedPositions;
    }

    /**
     * Check if and instance of a specific animal object exists on the board.
     * @param clazz {@link Class<? extends AnimalObject>} does an instance of this class exist?
     * @return true if an instance of the provided class exists otherwise false
     */
    public boolean containsNotInstanceOfAnimalObject(Class<? extends AnimalObject> clazz) {
        if (clazz == null) {
            return false;
        }

        Set<Class<? extends AnimalObject>> animalClassSet = gameObjects.stream()
                .filter(AnimalObject.class::isInstance)
                .map(AnimalObject.class::cast)
                .map(AnimalObject::getClass)
                .collect(Collectors.toSet());

        return !animalClassSet.contains(clazz);
    }

}
