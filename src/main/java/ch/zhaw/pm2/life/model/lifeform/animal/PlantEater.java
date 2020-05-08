package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.plant.PlantObject;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * First Version of a {@link PlantEater} animal.
 * @author pedernin
 */
public class PlantEater extends AnimalObject {

    private static final int REPRODUCTION_MINIMUM = 9;

    /**
     * Default constructor.
     */
    public PlantEater() {
        this.objectColor = Color.LIGHTSEAGREEN;
    }

    @Override
    protected Vector2D calculateNextPos(Set<GameObject> neighbourObjects) {
        Vector2D nextPosition;
        Vector2D neighbourPos = null;
        int min = Integer.MAX_VALUE;

        for (GameObject neighbour : neighbourObjects) {
            if (neighbour instanceof PlantObject) {
                min = Math.min(Vector2D.dot(this.getPosition(), neighbour.getPosition()), min);
                neighbourPos = neighbour.getPosition();
            }
        }

        if (min < Integer.MAX_VALUE) {
            Vector2D distance = Vector2D.subtract(neighbourPos, this.getPosition());
            int absX = Math.abs(distance.getX());
            int absY = Math.abs(distance.getY());

            if ((absX == 0 || absX == 1) && (absY == 0 || absY == 1)) {
                nextPosition = Vector2D.add(this.getPosition(), distance);
            } else {
                nextPosition = nextPos(distance);
            }
        } else {
            nextPosition = chooseRandomNeighbourPosition();
        }

        return nextPosition;
    }

    private Vector2D nextPos(Vector2D distance) {
        Vector2D dir = Direction.NONE.getDirectionVector();
        int max = 0;

        for (Direction direction : Direction.values()) {
            int dotProduct = Vector2D.dot(distance, direction.getDirectionVector());
            if(Math.max(dotProduct, max) > max) {
                max = dotProduct;
                dir = direction.getDirectionVector();
            }
        }
        return Vector2D.add(this.getPosition(), dir);
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
    public AnimalObject reproduce(LifeForm partner) throws LifeFormException {
        Objects.requireNonNull(partner, "Cannot be null.");
        if (partner.getGender().equals("F")) {
            throw new LifeFormException("Cannot give birth because im male");
        } else if (getFertilityThreshold() < REPRODUCTION_MINIMUM) {
            throw new LifeFormException("Cannot reproduce because partner is not fertile yet");
        }
        resetFertilityThreshold(); // sets own counter to zero (only on females)
        PlantEater plantEaterChild = new PlantEater();
        plantEaterChild.setPosition(this.chooseRandomNeighbourPosition());
        return plantEaterChild;
    }

}
