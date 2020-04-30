package ch.zhaw.pm2.life.model.lifeform.plant;

import javafx.scene.paint.Color;

/**
 * Implementation of the first kind of plant
 * Extends PlantObject
 */
public class FirstPlant extends PlantObject {

    public FirstPlant() {
        super();
        this.objectColor = Color.GREEN;
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
