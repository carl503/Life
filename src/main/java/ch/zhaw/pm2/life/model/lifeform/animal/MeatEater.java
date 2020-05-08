package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Set;

/**
 * First Version of a {@link MeatEater} animal.
 * @author pedernin
 */
public class MeatEater extends AnimalObject {

    private static final int REPRODUCTION_MINIMUM = 9;

    /**
     * Default constructor.
     */
    public MeatEater() {
        this.objectColor = Color.RED;
    }

    @Override
    protected Vector2D calculateNextPos(Set<GameObject> neighbourObjects) {
        return chooseRandomNeighbourPosition();
    }

    @Override
    public void eat(LifeForm lifeForm) throws LifeFormException {
        eat(lifeForm, () -> {
            if (lifeForm.getFoodType() == FoodType.PLANT) {
                throw new LifeFormException("Cannot eat this plant. Do I look like a vegetarian?!");
            } else if (lifeForm instanceof MeatEater && lifeForm.getCurrentEnergy() > this.getCurrentEnergy()) {
                throw new LifeFormException("Cannot eat this meat eater. He is stronger than I.");
            }
        });
    }

    @Override
    public AnimalObject reproduce(LifeForm partner) throws LifeFormException {
        Objects.requireNonNull(partner, "Cannot be null.");
        if (partner.getGender().equals("F")) {
            throw new LifeFormException("Cannot give birth because im male");
        } else if (getFertilityThreshold() < REPRODUCTION_MINIMUM) {
            throw new LifeFormException("Cannot reproduce because partner is not fertile yet");
        }
        resetFertilityThreshold(); // sets own counter to zero (only on females)
        MeatEater meatEaterChild = new MeatEater();
        meatEaterChild.setPosition(this.chooseRandomNeighbourPosition());
        return meatEaterChild;
    }

}
