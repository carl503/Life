package ch.zhaw.pm2.life.model;

import ch.zhaw.pm2.life.controller.LifeWindowController;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameObject is an abstract superclass of AnimalObjects and PlantObjects
 * GameObjects handles the features and methods that PlantObjects and AnimalObjects have in common
 */
public abstract class GameObject {

    //private static final int MAX_SIZE = 10;

    private static final int BASIC_SIZE = 5;
    private final Random random = new Random();
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
     * Valid values: 3-10
     */
    protected int size;

    /**
     * Default constructor.
     */
    public GameObject() {
        //size = random.nextInt(MAX_SIZE - 2) + 3;
        size = BASIC_SIZE;
        calculateRandomPointOnField();
    }

    /**
     * Calculates a random Point in the field
     */
    public void calculateRandomPointOnField() {
        // TODO: exclude already occupied fields
        int xPos = random.nextInt(LifeWindowController.ROWS);
        int yPos = random.nextInt(LifeWindowController.COLUMNS);
        position = new Vector2D(xPos, yPos);
    }

    /**
     * Returns the energy of a GameObject
     * @return energy - current energy as int
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
     * Returns Position of the GameObject
     * @return position
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
     * Returns the color of the GameObject
     * @return objectColor
     */
    public Color getColor() {
        return objectColor;
    }

    /**
     * Returns the size of the GameObject
     * @return size
     */
    public int getSize() {
        return size;
    }

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

        public final Vector2D vector2D;

        Direction(final Vector2D p) {
            vector2D = p;
        }
    }

}
