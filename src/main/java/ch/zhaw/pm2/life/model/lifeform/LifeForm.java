package ch.zhaw.pm2.life.model.lifeform;

import ch.zhaw.pm2.life.model.GameObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All living forms have to inherit this class.
 */
public abstract class LifeForm extends GameObject {

    private static final int POISONOUS_PROBABILITY = 3;
    private static final double POISON_ENERGY_CONSUMPTION_START_FACTOR = 0.3;
    private static final Logger logger = Logger.getLogger(LifeForm.class.getCanonicalName());

    private final boolean isMale;
    private boolean isDead;
    private int nextPoisonedEnergyConsumption;

    /**
     * Flag if this life form is poisoned.
     */
    protected boolean isPoisoned;

    /**
     * Flag if this life form is poisonous.
     */
    protected boolean isPoisonous;

    /**
     * Default constructor.
     */
    public LifeForm() {
        int genderValue = getRandom().nextInt(10);
        isMale = genderValue > 4;
        int poisonValue = getRandom().nextInt(10);
        isPoisonous = poisonValue < POISONOUS_PROBABILITY;
    }

    /**
     * Kills the life form by setting the isDead boolean to true;
     */
    public void die() {
        logger.log(Level.FINE, "{0} died", getName());
        isDead = true;
    }

    /**
     * {@link LifeForm} that calls this methods becomes poisoned, thus losing more energy per move
     */
    public void becomePoisoned() {
        logger.log(Level.FINE, "{0} got poisoned", getName());
        isPoisoned = true;
        nextPoisonedEnergyConsumption = (int) (energy * POISON_ENERGY_CONSUMPTION_START_FACTOR);
    }

    /**
     * Returns the energy consumption of this life form if it's poisoned.
     * @return consumption as int or 0 if the life form is not poisoned.
     */
    public int getPoisonedEnergyConsumption() {
        int energyConsumption = nextPoisonedEnergyConsumption;
        if ((energyConsumption - 1) == -1) {
            logger.log(Level.FINE, "{0} is not poisoned anymore", getName());
            isPoisoned = false;
        }
        nextPoisonedEnergyConsumption--;
        return Math.max(energyConsumption, 0);
    }

    /**
     * Returns the symbol of the gender.
     * @return the symbol of the gender as {@link String}.
     */
    public String getGender() {
        return isMale ? "M" : "F";
    }

    /**
     * Returns which type of food this life form is.
     * @return {@link FoodType}
     */
    public abstract FoodType getFoodType();

    /**
     * Indicates if this life form is alive or dead.
     * @return true if the object is dead.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Indicates if this life form is poisonous or not.
     * @return true if this life form is poisonous.
     */
    public boolean isPoisonous() {
        return isPoisonous;
    }

    /**
     * Indicates if this life form is poisoned or not.
     * @return true if this life form is poisoned.
     */
    public boolean isPoisoned() {
        return isPoisoned;
    }

    /**
     * Enumeration of food types.
     */
    public enum FoodType {
        PLANT, MEAT
    }

}
