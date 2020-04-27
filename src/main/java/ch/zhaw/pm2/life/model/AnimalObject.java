package ch.zhaw.pm2.life.model;


/**
 * AnimalObjects extends GameObject
 * AnimalObjects handles all features and methods that PlantEater and MeatEater have in common
 */
public abstract class AnimalObject extends GameObject {

    protected static final int INIT_ENERGY_ANIMALS = 5;

    protected boolean eatsPlants;
    protected boolean eatsMeat;

    public AnimalObject() {
        super();
    }

    /**
     * Is called when the animal moves
     */
    public void move() {
        chooseRandomNeighbourPosition();
        decreaseEnergy();
    }

    /**
     * Is called when the animal eats meat
     */
    public void eat(AnimalObject meat) {
        increaseEnergy();
    }

    /**
     * Is called when the animal eats a plant
     */
    public void eat(PlantObject plant) {
        increaseEnergy();
    }

    
}
