package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;

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
     * Default constructor.
     */
    public AnimalObject() {
        currentEnergy = INIT_ENERGY_ANIMALS;
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
        decreaseEnergy(consumeEnergy);
    }

    /**
     * Is called when the animal eats meat
     */
    public abstract void eat(LifeForm lifeForm) throws LifeFormException;

    /**
     * is called when the animal reproduces
     */
    public abstract AnimalObject reproduce(LifeForm lifeForm) throws LifeFormException;

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }

}
