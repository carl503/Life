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

    private static final int MAX_SIZE = 10;

    protected int currentEnergy;
    protected Position position;
    protected Color objectColor;

    private static final int BASIC_SIZE = 5;
    private Random random = new Random();
    /**
     * Valid values: 3-10
     */
    protected int size;

    public GameObject() {
        //size = random.nextInt(MAX_SIZE - 2) + 3;
        size = BASIC_SIZE;
        calculateRandomPointOnField();
    }

    private enum Direction {
        DOWN_LEFT(new Position(-1, 1)),
        DOWN(new Position(0, 1)),
        DOWN_RIGHT(new Position(1, 1)),
        LEFT(new Position(-1, 0)),
        NONE(new Position(0, 0)),
        RIGHT(new Position(1, 0)),
        UP_LEFT(new Position(-1, -1)),
        UP(new Position(0, -1)),
        UP_RIGHT(new Position(1, -1));

        public final Position position;
        Direction(final Position p) {
            position = p;
        }
    }

    /**
     * Calculates a random Point in the field
     */
    public void calculateRandomPointOnField() {
        // TODO: exclude already occupied fields
        int xPos = random.nextInt(LifeWindowController.ROWS);
        int yPos = random.nextInt(LifeWindowController.COLUMNS);
        position = new Position(xPos, yPos);
    }

    /**
     * Returns the energy of a GameObject
     * @return energy - current energy as int
     */
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public abstract String toString(int currentEnergy);

    /**
     * Increase the energy by a certain value.
     * @param energy
     */
    public void increaseEnergy(int energy) {
        currentEnergy += energy;
    }

    /**
     * Decrease the energy by a certain value.
     * @param consumedEnergy
     */
    public void decreaseEnergy(int consumedEnergy) {
        currentEnergy -= consumedEnergy;
    }

    /**
     * Returns Position of the GameObject
     * @return position
     */
    public Position getPosition(){
        return position;
    }

    /**
     * Chooses 1 of the 8 or less fields around the GameObject
     * Is called when a plant reproduces itself next to its current Position
     * Is called before a move is made by an animal
     */
    public Position chooseRandomNeighbourPosition() {
        List<Position> neighbours = getNeighbourFields();
        int neighbourIndex = random.nextInt(neighbours.size());
        return neighbours.get(neighbourIndex);
    }


    private List<Position> getNeighbourFields() {
        List<Position> neighbours = new ArrayList<>();
        for(Direction direction : Direction.values()) {
            int neighbourX = position.getX() + direction.position.getX();
            int neighbourY = position.getY() + direction.position.getY();
            if(neighbourX >= 0 && neighbourX < LifeWindowController.COLUMNS
                    && neighbourY >= 0 && neighbourY < LifeWindowController.ROWS) {
                neighbours.add(new Position(neighbourX, neighbourY));
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
}
