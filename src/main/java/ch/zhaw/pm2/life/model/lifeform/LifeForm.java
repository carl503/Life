package ch.zhaw.pm2.life.model.lifeform;

import ch.zhaw.pm2.life.model.GameObject;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All living forms have to inherit this class.
 * @author meletlea
 */
public abstract class LifeForm extends GameObject {

    private static final Logger LOGGER = Logger.getLogger(LifeForm.class.getCanonicalName());
    private static final double POISON_ENERGY_CONSUMPTION_START_FACTOR = 0.3;
    private static final int POISONOUS_PROBABILITY = 3;
    protected int reproduceCounter;

    /**
     * Flag to indicate the living state.
     */
    protected boolean isDead = false;

    /**
     * Flag if this life form is poisonous.
     */
    protected boolean isPoisonous;

    /**
     * Flag if this life form is poisonous.
     */
    protected boolean isPoisoned = false;

    /**
     * Flag indicating the gender
     */
    protected boolean isMale;

    private int nextPoisonedEnergyConsumption;

    /**
     * Default constructor.
     */
    public LifeForm() {
        Random random = new Random();
        int genderValue = random.nextInt(10);
        isMale = genderValue > 4;
        int poisonValue = random.nextInt(10);
        isPoisonous = poisonValue < POISONOUS_PROBABILITY;
        reproduceCounter = 0;
    }

    /**
     * Kills the GameObject by setting isAlive to false;
     */
    public void die() {
        LOGGER.log(Level.FINE, "{0} died", getClass().getSimpleName());
        isDead = true;
    }

    /**
     * Poison this life form.
     */
    public void poisoned() {
        LOGGER.log(Level.FINE, "{0} got poisoned", getClass().getSimpleName());
        isPoisoned = true;
        nextPoisonedEnergyConsumption = (int) (currentEnergy * POISON_ENERGY_CONSUMPTION_START_FACTOR);
    }

    /**
     * Returns the energy consumption of this life form if it's poisoned.
     * @return consumptions as int or 0 if the life form is not poisoned.
     */
    public int getPoisonedEnergyConsumption() {
        int energyConsumption = nextPoisonedEnergyConsumption;
        if ((energyConsumption - 1) == -1) {
            LOGGER.log(Level.FINE, "{0} is not poisoned anymore", getClass().getSimpleName());
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
     * Used to display on board
     * @return the reproductionCounter as {@link String}
     */
    public String displayReproductionCounter() {
        return String.valueOf(reproduceCounter);
    }

    /**
     * Used to determine wether animalobjects can reproduce or not
     * @return reproductionCounter as int
     */
    public int getReproductionCounter() {
        return reproduceCounter;
    }

    /**
     * Sets reproduceCounter to 0 after successful reproduction, but only on females because males
     * are not really affected by birth
     */
    public void setReproduceCounter() {
        this.reproduceCounter = 0;
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

    /**
     * Indicates if this life form is poisonous or not.
     * @return true if this life form is poisonous
     */
    public boolean isPoisonous() {
        return isPoisonous;
    }

    /**
     * Indicates if this life form is poisoned or not.
     * @return true if this life form is poisoned
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
