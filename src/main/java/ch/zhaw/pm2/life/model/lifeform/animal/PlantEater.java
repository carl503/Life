package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * First Version of a PlantEater animal that extends AnimalObject
 */
public class PlantEater extends AnimalObject {

    private static final Logger LOGGER = Logger.getLogger(PlantEater.class.getCanonicalName());

    /**
     * Default constructor
     */
    public PlantEater() {
        this.objectColor = Color.LIGHTSEAGREEN;
    }

    @Override
    public void eat(LifeForm lifeForm) throws LifeFormException {
        Objects.requireNonNull(lifeForm, "Cannot eat null.");
        if(lifeForm.getFoodType() == FoodType.MEAT) {
            throw new LifeFormException("Cannot eat this meat, I am vegetarian.");
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
