package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This model class represents the board containing all the game objects.
 * @author lubojcar, meletlea
 */
public class Board {
    private final int rows;
    private final int columns;
    private final Set<GameObject> gameObjects = new HashSet<>();

    /**
     * Default constructor.
     * @param rows Number of rows as int.
     * @param columns Number of columns as int.
     */
    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Adds a game object to the set.
     * @param gameObject {@link GameObject}
     */
    public void addGameObject(GameObject gameObject) {
        Objects.requireNonNull(gameObject, "Game object cannot be null to add it on the board.");
        gameObjects.add(gameObject);
    }

    /**
     * Removes a game object from the set.
     * @param gameObject
     */
    public void removeGameObject(GameObject gameObject) {
        Objects.requireNonNull(gameObject, "Game object cannot be null to remove it from the board.");
        gameObjects.remove(gameObject);
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
     * Check if and instance of a specific animal object exists on the board.
     * @param clazz {@link Class<? extends AnimalObject>} does an instance of this class exist?
     * @return true if an instance of the provided class exists otherwise false
     */
    public boolean containsNotInstanceOfAnimalObject(Class<? extends AnimalObject> clazz) {
        if(clazz == null) {
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
