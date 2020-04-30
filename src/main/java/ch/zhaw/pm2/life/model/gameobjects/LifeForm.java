package ch.zhaw.pm2.life.model.gameobjects;

/**
 *
 * @author Leandro Meleti
 * @version 2020-04-29
 */
public abstract class LifeForm extends GameObject {

    private boolean isAlive = true;

    /**
     *
     */
    public enum FoodType {
        PLANT, MEAT
    }

    /**
     *
     */
    public void killObject() {
        isAlive = false;
    }

    /**
     *
     * @return
     */
    public abstract FoodType getFoodType();

    public boolean isAlive() {
        return isAlive;
    }

}
