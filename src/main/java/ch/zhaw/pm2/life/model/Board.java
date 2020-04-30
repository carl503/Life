package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.model.GameObject;

import java.util.HashSet;
import java.util.Set;

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
        gameObjects.add(gameObject);
    }

    /**
     * Removes a game object from the set.
     * @param gameObject
     */
    public void removeGameObject(GameObject gameObject) {
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
}
