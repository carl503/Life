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
        chooseRandomNeighbourPosition();
        move();
    }

    /**
     * Is called when the animal moves
     */
    public void move() {
        decreaseEnergy();
    }

    /**
     * Is called when the animal eats something
     */
    public void eat() {
        increaseEnergy();
    }

    
}
