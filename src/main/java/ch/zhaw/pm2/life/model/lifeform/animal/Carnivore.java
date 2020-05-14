package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.LifeFormActionCheck;

import java.util.Objects;
import java.util.Set;

/**
 * First Version of a {@link Carnivore} animal.
 * @author pedernin
 */
public class Carnivore extends AnimalObject {

    private static final int REPRODUCTION_MINIMUM = 9;

    /**
     * Default constructor.
     */
    public Carnivore() {
        this.color = "red";
    }

    @Override
    protected Vector2D getNearestNeighbour(Set<GameObject> gameObjects) {
        return chooseRandomNeighbourPosition();
    }

    @Override
    protected LifeFormActionCheck getEatRules(LifeForm lifeForm) {
        return () -> {
            if (lifeForm.getFoodType() == FoodType.PLANT) {
                throw new LifeFormException("Cannot eat this plant. Do I look like a vegetarian?!");
            } else if (lifeForm instanceof Carnivore && lifeForm.getEnergy() > this.getEnergy()) {
                throw new LifeFormException("Cannot eat this carnivore. He is stronger than I.");
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
        Carnivore carnivoreChild = new Carnivore();
        carnivoreChild.setPosition(this.chooseRandomNeighbourPosition());
        return carnivoreChild;
    }

}
