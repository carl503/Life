package ch.zhaw.pm2.life.model;

/**
 * PlantObject extends GameObject
 * PlantObjects handles all features and methods that Future Plants will have in common
 */
public class PlantObject extends GameObject{

    protected static final int PLANT_ENERGY = 5;

    public PlantObject() {
        super();
    }

    /**
     * Makes a plant grow randomly on the field
     */
    public void growRandomlyOnField() {

    }

    /**
     * Increases energy by a certain amount. Gets called when an animal eats a plant or another animal
     * (Is called after a certain amount of turns - after X Energy the plants reproduces itself)
     */
    @Override
    public void increaseEnergy() {

    }

    /**
     * decreases energy by a certain amount. Gets called when an animal makes a move
     * (or a plant is not fully eaten - phase - 2/3)
     */
    @Override
    public void decreaseEnergy() {

    }
}
