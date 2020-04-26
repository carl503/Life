package ch.zhaw.pm2.life.controller;


/**
 * AnimalObjects extends GameObject
 * AnimalObjects handles all features and methods that PlantEater and MeatEater have in common
 */
public abstract class AnimalObject extends GameObject {

    private boolean eatsPlants;
    private boolean eatsMeat;

    public AnimalObject() {
        super();
    }

    public void makeAMove(){
        chooseRandomNeighbourField();
        move();
    }

    /**
     * Is called when the animal moves
     */
    public void move() {

    }

    /**
     * Is called when the animal eats something
     */
    public void eat() {

    }

    /**
     * Chooses 1 of the 8 or less fields around the AnimalObject
     */
    public void chooseRandomNeighbourField() {

    }
}
