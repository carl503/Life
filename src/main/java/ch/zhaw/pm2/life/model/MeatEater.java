package ch.zhaw.pm2.life.model;

import javafx.scene.paint.Color;

/**
 * First Version of a MeatEater Animal that extends AnimalObject
 */
public class MeatEater extends AnimalObject {

    public MeatEater() {
        super();
        this.objectColor = Color.RED;
        isMeatEater = true;

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
