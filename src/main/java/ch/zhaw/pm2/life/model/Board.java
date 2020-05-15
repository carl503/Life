package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.zhaw.pm2.life.model.GameObject.*;
import static java.util.function.Predicate.*;

/**
 * This model class represents the board containing all the {@link GameObject}.
 */
public class Board {

    /**
     * Minimum number of rows.
     */
    public static final int MIN_ROWS = 3;

    /**
     * Minimum number of columns.
     */
    public static final int MIN_COLUMNS = 3;

    private final Random random = new Random();
    private final Set<GameObject> gameObjects = new HashSet<>();
    private final Set<Vector2D> occupiedPositions = new HashSet<>();
    private final int rows;
    private final int columns;

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
     * Adds a {@link GameObject} to the set of GameObjects on the board and it's position to the set of
     * occupiedPositions which is later used to handle collisions and interactions.
     * @param gameObject {@link GameObject}
     * @param position   {@link Vector2D}
     * @throws NullPointerException when the {@link GameObject} is null.
     */
    public void addGameObject(GameObject gameObject, Vector2D position) {
        Objects.requireNonNull(gameObject, "Game object cannot be null to add it on the board.");
        Objects.requireNonNull(position, "The position cannot be null to add the game object on the board.");
        if (Vector2D.isNegative(position) || position.getX() >= columns || position.getY() >= rows) {
            String message = String.format("The position %s of the provided game object does not exist on the board.", position);
            throw new IllegalArgumentException(message);
        }

        gameObject.setPosition(position);
        gameObject.setColumns(columns);
        gameObject.setRows(rows);

        gameObjects.add(gameObject);
        occupiedPositions.add(position);
    }

    /**
     * Returns a random but valid position on the board as {@link Vector2D}.
     * @return a position as {@link Vector2D}.
     */
    public Vector2D getRandomPosition() {
        int xPos = random.nextInt(columns);
        int yPos = random.nextInt(rows);
        return new Vector2D(xPos, yPos);
    }

    /**
     * Remove all dead {@link LifeForm} from the board.
     */
    public void removeDeadLifeForms() {
        Set<LifeForm> deadLifeForms = getLifeForms().stream()
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
                .filter(not(currentOccupiedPositions::contains)) // positions of dead life forms that were alone on that position
                .collect(Collectors.toSet());
    }

    /**
     * Returns the {@link LifeForm} on the board.
     * @return Set<LifeForm> on the board
     */
    public Set<LifeForm> getLifeForms() {
        return gameObjects.stream()
                .filter(LifeForm.class::isInstance)
                .map(LifeForm.class::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Returns all {@link GameObject} of a position.
     * @param pos Position on the field as {@link Vector2D}.
     * @return Set<GameObject> of all the {@link GameObject} of a position.
     */
    public Set<GameObject> getAllGameObjects(Vector2D pos) {
        Set<GameObject> found = new HashSet<>();

        if (isNeighbourPositionOnBoard(pos)) {
            gameObjects.stream()
                    .filter(gameObject -> gameObject.getPosition().equals(pos))
                    .forEach(found::add);
        }

        return found;
    }

    /**
     * Searches and returns all neighbours of a {@link GameObject} with radius "radius".
     * The Methods scans all the fields in the radius and adds whatever {@link GameObject} there is
     * and adds it to the set of neighbours.
     * @param gameObject {@link GameObject} where the neighbours should be searched.
     * @param radius     radius to search from {@link GameObject}.
     * @return Set<GameObject> of neighbours.
     */
    public Set<GameObject> getNeighbourObjects(GameObject gameObject, int radius) {
        Set<GameObject> neighbours = new HashSet<>();
        int diameter = 2 * radius;
        if (gameObject == null || diameter <= 0) {
            return neighbours;
        }

        Vector2D topLeftCorner = Vector2D.add(gameObject.getPosition(),
                                              Vector2D.multiply(radius, Direction.UP_LEFT.getDirectionVector()));

        for (int i = 0; i <= diameter; i++) {
            for (int j = 0; j <= diameter; j++) {
                Vector2D next = new Vector2D(topLeftCorner.getX() + j, topLeftCorner.getY() + i);
                if (isNeighbourPositionOnBoard(next)) {
                    Set<GameObject> found = getAllGameObjects(next);
                    if (found.size() > 0) {
                        found.remove(gameObject);
                        neighbours.addAll(found);
                    }
                }
            }
        }

        return neighbours;
    }

    private boolean isNeighbourPositionOnBoard(Vector2D vector) {
        return (Vector2D.isPositive(vector) && vector.getY() < columns && vector.getX() < rows);
    }

    /**
     * Checks if a species still has members that are alive. It filters them by name.
     * @param species name of the species as {@link String}.
     * @return true if species is still alive, else false.
     */
    public boolean isSpeciesAlive(String species) {
        return gameObjects.stream()
                .filter(AnimalObject.class::isInstance)
                .map(AnimalObject.class::cast)
                .map(AnimalObject::getName)
                .anyMatch(species::equals);
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
     * Returns a set of occupied positions.
     * @return set of position objects.
     */
    public Set<Vector2D> getOccupiedPositions() {
        return occupiedPositions;
    }

}
