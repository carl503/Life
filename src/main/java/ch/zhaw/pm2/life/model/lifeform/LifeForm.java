package ch.zhaw.pm2.life.model.lifeform;

import ch.zhaw.pm2.life.model.GameObject;

/**
 *
 * @author Leandro Meleti
 * @version 2020-04-29
 */
public abstract class LifeForm extends GameObject {

    protected boolean isDead = false;

    /**
     *
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
     *
     * @return
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
