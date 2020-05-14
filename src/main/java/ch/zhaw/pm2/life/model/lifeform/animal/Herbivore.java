package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.LifeFormActionCheck;
import ch.zhaw.pm2.life.model.lifeform.plant.PlantObject;

import java.util.Objects;
import java.util.Set;

/**
 * First Version of a {@link Herbivore} animal.
 * @author pedernin
 */
public class Herbivore extends AnimalObject {

    private static final int REPRODUCTION_MINIMUM = 9;

    /**
     * Default constructor.
     */
    public Herbivore() {
        this.color = "lightseagreen";
    }

    @Override
    protected Vector2D getNearestNeighbour(Set<GameObject> neighbours) {
        int min = Integer.MAX_VALUE;
        Vector2D neighbourPos = null;

        for (GameObject neighbour : neighbours) {
            int distance = Vector2D.dot(this.getPosition(), neighbour.getPosition());
            if (neighbour instanceof PlantObject && distance < min) {
                min = distance;
                neighbourPos = neighbour.getPosition();
            }
        }
        return neighbourPos;
    }

    @Override
    protected LifeFormActionCheck getEatRules(LifeForm lifeForm) {
        return () -> {
            if (lifeForm.getFoodType() == FoodType.MEAT) {
                throw new LifeFormException("Cannot eat this meat, I am vegetarian.");
            }
        };
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
        Herbivore herbivoreChild = new Herbivore();
        herbivoreChild.setPosition(this.chooseRandomNeighbourPosition());
        return herbivoreChild;
    }

}
