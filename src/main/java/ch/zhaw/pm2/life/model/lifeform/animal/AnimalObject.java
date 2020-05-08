package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.LifeFormActionCheck;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class of an animal.
 */
public abstract class AnimalObject extends LifeForm {

    /**
     * Default energy level of an {@link AnimalObject}.
     */
    public static final int INIT_ENERGY_ANIMALS = 10;
    private static final Logger LOGGER = Logger.getLogger(AnimalObject.class.getCanonicalName());

    /**
     * Indicates the current fertility value for reproduction. Needs a specific value to be able to reproduce.
     */
    protected int fertilityThreshold;

    /**
     * Default constructor.
     */
    public AnimalObject() {
        currentEnergy = INIT_ENERGY_ANIMALS;
        fertilityThreshold = 0;
    }

    /**
     * Is called when the animal moves
     */
    public void move() {
        LOGGER.log(Level.FINER, "Move {0}", getClass().getSimpleName());
        Vector2D previousPosition = position;
        position = chooseRandomNeighbourPosition();
        int consumeEnergy = 0;
        if (isPoisoned) {
            consumeEnergy = getPoisonedEnergyConsumption();
            LOGGER.log(Level.FINE, "{1} decreased energy (poisoned) by {0}", new Object[] {
                    consumeEnergy, getClass().getSimpleName()
            });
        }
        if (!previousPosition.equals(position)) {
            consumeEnergy = 1;
            LOGGER.log(Level.FINE, "{1} decreased energy (move) by {0}", new Object[] {
                    consumeEnergy, getClass().getSimpleName()
            });
        }
        fertilityThreshold++;
        decreaseEnergy(consumeEnergy);
    }

    /**
     * Is called when the animal eats meat
     * @param lifeForm {@link LifeForm}
     */
    public abstract void eat(LifeForm lifeForm) throws LifeFormException;

    /**
     * Is called when the animal eats meat
     * @param lifeForm    {@link LifeForm}
     * @param actionCheck {@link LifeFormActionCheck}
     */
    protected void eat(LifeForm lifeForm, LifeFormActionCheck actionCheck) throws LifeFormException {
        Objects.requireNonNull(lifeForm, "Cannot eat null.");
        if (actionCheck != null) {
            actionCheck.check();
        }

        LOGGER.log(Level.FINE, "{0} ate {1}", new Object[] {
                getClass().getSimpleName(),
                lifeForm.getClass().getSimpleName()
        });
        increaseEnergy(lifeForm.getCurrentEnergy());
        if (lifeForm.isPoisonous()) {
            becomePoisoned();
        }
        lifeForm.die();
    }

    /**
     * Used to determine weather this life form can reproduce or not.
     * @return reproduction counter as int.
     */
    public int getFertilityThreshold() {
        return fertilityThreshold;
    }

    /**
     * Resets the fertility threshold.
     */
    public void resetFertilityThreashold() {
        this.fertilityThreshold = 0;
    }
    /**
     * is called when the animal reproduces
     */

    public abstract AnimalObject reproduce(LifeForm lifeForm) throws LifeFormException;

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }

}
