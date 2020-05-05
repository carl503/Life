package ch.zhaw.pm2.life.model.lifeform;

import ch.zhaw.pm2.life.model.GameObject;

/**
 * All living forms have to inherit this class.
 * @author meletlea
 */
public abstract class LifeForm extends GameObject {

    /**
     * Flag to indicate the living state.
     */
    protected boolean isDead = false;

    /**
     * Enumeration of food types.
     */
    public enum FoodType {
        PLANT, MEAT
    }

    /**
     * Kills the GameObject by setting isAlive to false;
     */
    public void die() {
        isDead = true;
    }

    /**
     * Returns the type of food this life form is.
     * @return {@link FoodType}
     */
    public abstract FoodType getFoodType();

    /**
     * Returns if the GameObject is alive or dead.
     * @return isAlive true if the object is alive
     */
    public boolean isDead() {
        return isDead;
    }

}
