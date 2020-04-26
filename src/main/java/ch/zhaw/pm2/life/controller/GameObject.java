package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Position;

import java.util.HashMap;
import java.util.Map;


/**
 * GameObject is an abstract superclass of AnimalObjects and PlantObjects
 * GameObjects handles the features and methods that PlantObjects and AnimalObjects have in common
 */
public abstract class GameObject {
    protected static final int PLANT_ENERGY = 5;
    protected static final int PLANT_ANIMAL_ENERGY = 5;
    protected static final int INIT_ENERGY_ANIMALS = 5;
    protected boolean isAlive;
    protected int currentEnergy;
    protected Position position;



    public GameObject() {

    }

    /**
     * Calculates a random Point in the field
     */
    public void calculateRandomPointOnField() {

    }

    /**
     * Returns the energy of a GameObject
     * @return energy - current energy as int
     */
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    /**
     * Returns if the GameObject is alive or dead.
     * @return isAlive true if the object is alive
     */
    public boolean isAlive(){
        return isAlive;
    }

    /**
     * Kills the GameObject by setting isAlive to false;
     */
    public void killObject(){
        isAlive = false;
    }

    /**
     * Returns Position of the GameObject
     * @return position
     */
    public Position getPosition(){
        return position;
    }

    /**
     * Chooses 1 of the 8 or less fields around the AnimalObject
     * Is called when a plant reproduces itself next to its current Position
     * Is called before a move is made by an animal
     */
    public void chooseRandomNeighbourPosition() {

    }

    /**
     * Increases energy by a certain amount. Gets called when an animal eats a plant or another animal
     * (Is called after a certain amount of turns - after X Energy the plants reproduces itself)
     */
    public void increaseEnergy() {

    }

    /**
     * decreases energy by a certain amount. Gets called when an animal makes a move
     * (or a plant is not fully eaten - phase - 2/3)
     */
    public void decreaseEnergy() {

    }

    /*
    public boolean setInitialPosition(int xValue, int yValue){
        boolean result = false;
        if(isPointFree(xValue, yValue)) {
            this.position = new Position(xValue, yValue);
            result = isPointFree(xValue, yValue);
        }
        return result;
    }

    public void isPointFree(int xValue, int yValue) {
        boolean result = false;
        if() {

        }

    }

     */
}
