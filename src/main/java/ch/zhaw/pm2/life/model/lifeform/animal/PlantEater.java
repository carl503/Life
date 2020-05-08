package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * First Version of a {@link PlantEater} animal.
 * @author pedernin
 */
public class PlantEater extends AnimalObject {

    private static final int REPRODUCTION_MINIMUM = 9;

    /**
     * Default constructor
     */
    public PlantEater() {
        this.objectColor = Color.LIGHTSEAGREEN;
    }

    @Override
    public void eat(LifeForm lifeForm) throws LifeFormException {
        eat(lifeForm, () -> {
            if (lifeForm.getFoodType() == FoodType.MEAT) {
                throw new LifeFormException("Cannot eat this meat, I am vegetarian.");
            }
        });
    }

    @Override
    public AnimalObject reproduce(LifeForm lifeForm) throws LifeFormException {
        Objects.requireNonNull(lifeForm, "Cannot be null.");
        if (lifeForm.getGender().equals("F") || getFertilityThreshold() < REPRODUCTION_MINIMUM) {
            throw new LifeFormException("Cannot give birth because im male or the partner cannot reproduce yet ");
        }
        resetFertilityThreashold(); // sets own counter to zero (only on females)
        PlantEater plantEaterChild = new PlantEater();
        plantEaterChild.setPositionNewBorn(this.chooseRandomNeighbourPosition());
        return plantEaterChild;
    }

}
