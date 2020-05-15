package ch.zhaw.pm2.life.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * This class is the superior object of every model that is part of the game.
 * It stores common attributes and behaviour of every other model that inherits from it.
 */
public abstract class GameObject {

    private static final int BASIC_SIZE = 5;
    /**
     * The current energy of this {@link GameObject}.
     */
    protected int energy;
    /**
     * The current position of this {@link GameObject}.
     */
    protected Vector2D position;
    /**
     * The size used to scale this object in the visualization.
     * Valid values: 3-10
     */
    protected int size;
    private Random random;
    private String name;
    private String color;
    private int rows;
    private int columns;

    /**
     * Default constructor.
     */

    public GameObject() {
        size = BASIC_SIZE;
        random = new Random();
    }

    /**
     * Increases the energy by a certain value.
     * @param energy increasing energy by this amount.
     */
    public void increaseEnergy(int energy) {
        if (energy < 0) {
            throw new IllegalArgumentException("Energy cannot increase with a negative value.");
        }
        this.energy += energy;
    }

    /**
     * Decrease the energy by a certain value.
     * @param energy reducing energy by this amount.
     */
    public void decreaseEnergy(int energy) {
        if (energy < 0) {
            throw new IllegalArgumentException("Energy cannot decrease with a negative value.");
        }
        this.energy -= energy;
    }

    /**
     * Chooses 1 of the 8 fields around the {@link GameObject} or the current position.
     */
    public Vector2D chooseRandomNeighbourPosition() {
        List<Vector2D> neighbours = getNeighbourFields();
        int neighbourIndex = random.nextInt(neighbours.size());
        return neighbours.get(neighbourIndex);
    }

    private List<Vector2D> getNeighbourFields() {
        List<Vector2D> neighbours = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Vector2D neighbourPosition = Vector2D.add(position, direction.directionVector);
            int neighbourX = neighbourPosition.getX();
            int neighbourY = neighbourPosition.getY();
            if (Vector2D.isPositive(neighbourPosition) && neighbourX < columns && neighbourY < rows) {
                neighbours.add(neighbourPosition);
            }
        }
        return neighbours;
    }

    /**
     * Returns the color of the {@link GameObject}.
     * @return color as {@link String}.
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of this {@link GameObject}.
     * @param color sets the color as {@link String};
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Returns the size of the {@link GameObject}.
     * @return size as int.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns position of the {@link GameObject}.
     * @return position as {@link Vector2D}.
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Sets the position of the {@link GameObject}.
     * @param position position as {@link Vector2D}.
     * @throws NullPointerException when the position is null.
     */
    public void setPosition(Vector2D position) {
        Objects.requireNonNull(position, "The position of the game object cannot be null.");
        this.position = position;
    }

    /**
     * Sets the number of rows on the board.
     * @param rows as int
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Sets the number of columns on the board.
     * @param columns as int
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Returns the random object
     * @return Random
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Sets the random object
     * @param random {@link Random}
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Returns the energy of this {@link GameObject}.
     * @return current energy as int.
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Sets the energy of this {@link GameObject}.
     * @param energy the energy as int.
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    /**
     * Returns the name of an object
     * @return the name as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a game object
     * @param name the name as {@link GameObject}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Enumeration of {@link Vector2D} pointing in every direction with length 1.
     */
    public enum Direction {
        DOWN_LEFT(new Vector2D(-1, 1)),
        DOWN(new Vector2D(0, 1)),
        DOWN_RIGHT(new Vector2D(1, 1)),
        LEFT(new Vector2D(-1, 0)),
        NONE(new Vector2D(0, 0)),
        RIGHT(new Vector2D(1, 0)),
        UP_LEFT(new Vector2D(-1, -1)),
        UP(new Vector2D(0, -1)),
        UP_RIGHT(new Vector2D(1, -1));

        private final Vector2D directionVector;

        Direction(final Vector2D p) {
            directionVector = p;
        }

        /**
         * Returns the directionVector
         * @return directionVector as {@link Vector2D}
         */
        public Vector2D getDirectionVector() {
            return directionVector;
        }
    }

}
