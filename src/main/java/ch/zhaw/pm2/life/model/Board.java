package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

import java.util.HashSet;
import java.util.Objects;
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

    private final int rows;
    private final int columns;
    private final Set<GameObject> gameObjects = new HashSet<>();
    private final Set<Vector2D> occupiedPositions = new HashSet<>();

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
    }

    /**
     * Adds a game object to the set.
     * @param gameObject {@link GameObject}
     * @throws NullPointerException when the argument gameObject or its position is null.
     * @throws NullPointerException when the argument gameObject is null.
     */
    public void addGameObject(GameObject gameObject) {
        Objects.requireNonNull(gameObject, "Game object cannot be null to add it on the board.");
        Objects.requireNonNull(gameObject.getPosition(), "The position cannot be null to add the game object on the board.");
        Vector2D position = gameObject.getPosition();
        if (position.getX() < 0 || position.getX() >= columns || position.getY() < 0 || position.getY() >= rows) {
            String message = String.format("The position %s of the provided game object does not exist on the board.", position);
            throw new IllegalArgumentException(message);
        }
        gameObjects.add(gameObject);
        occupiedPositions.add(position);
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
