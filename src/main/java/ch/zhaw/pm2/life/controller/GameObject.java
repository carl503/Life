package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Position;


/**
 * GameObject is an abstract superclass of AnimalObjects and PlantObjects
 * GameObjects handles the features and methods that PlantObjects and AnimalObjects have in common
 */
public abstract class GameObject {
    private static final int PLANT_ENERGY = 5;
    private static final int PLANT_ANIMAL_ENERGY = 5;
    private static final int INIT_ENERGY_ANIMALS = 5;
    private boolean isAlive;
    private int currentEnergy;
    private Position position;




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
