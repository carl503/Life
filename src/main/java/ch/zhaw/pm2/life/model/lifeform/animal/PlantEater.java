package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * First Version of a PlantEater animal that extends AnimalObject
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
    protected void setPosition(Vector2D newBornPosition) {
        this.position = newBornPosition;
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
        if (lifeForm.getGender().equals("F") || getReproductionCounter() > REPRODUCTION_MINIMUM) {
            throw new LifeFormException("Cannot give birth because im male or the partner cannot reproduce yet ");
        }
        resetReproductionCounter(); // sets own counter to zero (only on females)
        PlantEater plantEaterChild = new PlantEater();
        plantEaterChild.setPosition(this.chooseRandomNeighbourPosition());
        return plantEaterChild;
    }

}
