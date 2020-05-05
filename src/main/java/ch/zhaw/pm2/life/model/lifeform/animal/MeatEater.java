package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * First Version of a MeatEater Animal that extends AnimalObject
 */
public class MeatEater extends AnimalObject {

    private static final Logger LOGGER = Logger.getLogger(MeatEater.class.getCanonicalName());

    /**
     * Default constructor.
     */
    public MeatEater() {
        this.objectColor = Color.RED;
        isMale = setGender();
    }

    @Override
    public void eat(LifeForm lifeForm) throws LifeFormException {
        Objects.requireNonNull(lifeForm, "Cannot eat null.");

        if(lifeForm.getFoodType() == FoodType.PLANT) {
            throw new LifeFormException("Cannot eat this plant. Do I look like a vegetarian?!");
        } else if (lifeForm instanceof MeatEater && lifeForm.getCurrentEnergy() > this.getCurrentEnergy()) {
            throw new LifeFormException("Cannot eat this meat eater. He is stronger than I.");
        }

        LOGGER.log(Level.FINE, "{0} ate {1}",
                new Object[] { getClass().getSimpleName(), lifeForm.getClass().getSimpleName() });
        increaseEnergy(lifeForm.getCurrentEnergy());
        if(lifeForm.isPoisonous()) {
            poisoned();
        }
        lifeForm.die();
    }
}
