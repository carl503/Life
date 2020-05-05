package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * First Version of a PlantEater animal that extends AnimalObject
 */
public class PlantEater extends AnimalObject {

    /**
     * Default constructor
     */
    public PlantEater() {
        this.objectColor = Color.LIGHTSEAGREEN;
        isPlantEater = true;
        isMale = setGender();
    }

    @Override
    public void eat(LifeForm lifeForm) throws LifeFormException {
        Objects.requireNonNull(lifeForm, "Cannot eat null.");
        if(lifeForm.getFoodType() == FoodType.MEAT) {
            throw new LifeFormException("Cannot eat this meat, I am vegetarian.");
        }
        increaseEnergy(lifeForm.getCurrentEnergy());
        if(lifeForm.isPoisonous()) {
            poisoned();
        }
        lifeForm.die();
    }
}
