package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.LifeFormActionCheck;

import java.util.Set;

/**
 * Carnivore extends {@link AnimalObject} and adds logic, necessary to eat and the determination
 * of the nearest neighbour of the {@link Carnivore}.
 */
public class Carnivore extends AnimalObject {

    /**
     * Default constructor.
     */
    public Carnivore() {
        setScanRadius(0);
    }

    @Override
    protected Vector2D getNearestNeighbour(Set<GameObject> neighbours) {
        int min = Integer.MAX_VALUE;
        Vector2D neighbourPos = null;

        for (GameObject neighbour : neighbours) {
            int distance = Vector2D.dot(position, neighbour.getPosition());
            if (neighbour instanceof AnimalObject && distance < min) {
                min = distance;
                neighbourPos = neighbour.getPosition();
            }
        }
        return neighbourPos;
    }

    @Override
    protected LifeFormActionCheck getEatRules(LifeForm lifeForm) {
        return () -> {
            if (lifeForm.getFoodType() == FoodType.PLANT) {
                throw new LifeFormException(String.format("%s: Ich fresse leider keine Pflanzen.", getName()));
            } else if (lifeForm instanceof Carnivore && lifeForm.getEnergy() > this.getEnergy()) {
                throw new LifeFormException(String.format("%s: Kann dieses Tier nicht fressen, weil es staerker ist als ich.", getName()));
            }
        };
    }

}
