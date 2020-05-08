package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.controller.LifeWindowController;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is the superior object of every model that is part of the game.
 * It stores common attributes and behaviour of every other model.
 * @author pedernin
 */
public abstract class GameObject {

    //private static final int MAX_SIZE = 10;

    private static final int BASIC_SIZE = 5;
    private static int gameObjectCount;
    private final Random random = new Random();
    private final int id;
    /**
     * The current energy of this game object.
     */
    protected int currentEnergy;
    /**
     * The current position of this game object.
     */
    protected Vector2D position;
    /**
     * The color of this game object.
     */
    protected Color objectColor;
    /**
     * The size used to scale this object in the visualization.
     * Valid values: 3-10
     */
    protected int size;

    /**
     * Default constructor.
     */
    public GameObject() {
        //size = random.nextInt(MAX_SIZE - 2) + 3;
        size = BASIC_SIZE;
        calculateRandomPositionOnField();
        gameObjectCount++;
        id = gameObjectCount;
    }

    /**
     * Calculates a random position on the field.
     */
    public void calculateRandomPositionOnField() {
        // TODO: exclude already occupied fields
        int xPos = random.nextInt(LifeWindowController.ROWS);
        int yPos = random.nextInt(LifeWindowController.COLUMNS);
        position = new Vector2D(xPos, yPos);
    }

    /**
     * Returns the energy of this {@link GameObject}.
     * @return current energy as int.
     */
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    /**
     * Increase the energy by a certain value.
     * @param energy increasing energy by this amount.
     */
    public void increaseEnergy(int energy) {
        currentEnergy += energy;
    }

    /**
     * Decrease the energy by a certain value.
     * @param consumedEnergy reducing energy by this amount.
     */
    public void decreaseEnergy(int consumedEnergy) {
        currentEnergy -= consumedEnergy;
    }

    /**
     * Returns position of the {@link GameObject}.
     * @return position as {@link Vector2D}
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Chooses 1 of the 8 or less fields around the GameObject
     * Is called when a plant reproduces itself next to its current Position
     * Is called before a move is made by an animal
     */
    public Vector2D chooseRandomNeighbourPosition() {
        List<Vector2D> neighbours = getNeighbourFields();
        int neighbourIndex = random.nextInt(neighbours.size());
        return neighbours.get(neighbourIndex);
    }

    private List<Vector2D> getNeighbourFields() {
        List<Vector2D> neighbours = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int neighbourX = position.getX() + direction.vector2D.getX();
            int neighbourY = position.getY() + direction.vector2D.getY();
            if (neighbourX >= 0 && neighbourX < LifeWindowController.COLUMNS
                    && neighbourY >= 0 && neighbourY < LifeWindowController.ROWS) {
                neighbours.add(new Vector2D(neighbourX, neighbourY));
            }
        }
        return neighbours;
    }

    /**
     * Returns the color of the {@link GameObject}.
     * @return color as {@link Color}.
     */
    public Color getColor() {
        return objectColor;
    }

    /**
     * Returns the size of the {@link GameObject}
     * @return size as int.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the position of a newborn
     * @param newBornPosition
     */
    protected void setPositionNewBorn(Vector2D newBornPosition) {
        this.position = newBornPosition;
    }

    /**
     * Enumeration of {@link Vector2D} pointing in any direction with length 1.
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

        private final Vector2D direction;

        Direction(final Vector2D p) {
            direction = p;
        }

        public Vector2D getDirection() {
            return direction;
        }
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj instanceof GameObject) {
            equal = ((GameObject) obj).id == this.id;
        }
        return equal;
    }
}
