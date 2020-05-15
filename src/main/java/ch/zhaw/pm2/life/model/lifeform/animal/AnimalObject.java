package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.LifeFormActionCheck;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class of an animal.
 */
public abstract class AnimalObject extends LifeForm {

    /**
     * Default energy level of an {@link AnimalObject}.
     */
    public static final int INIT_ENERGY_ANIMALS = 10;

    private static final int REPRODUCTION_MINIMUM = 9;

    private static final Logger logger = Logger.getLogger(AnimalObject.class.getCanonicalName());

    private int fertilityThreshold;
    private int scanRadius = 1;

    /**
     * Default constructor.
     */
    public AnimalObject() {
        energy = INIT_ENERGY_ANIMALS;
        fertilityThreshold = 0;
    }

    /**
     * Is called when the animal moves.
     */
    public void move(Set<GameObject> neighbourObjs) {
        logger.log(Level.FINER, "Move {0}", getClass().getSimpleName());
        Vector2D previousPosition = position;
        if (neighbourObjs.isEmpty()) {
            position = chooseRandomNeighbourPosition();
        } else {
            position = calculateNextPos(neighbourObjs);
        }
        int consumeEnergy = 0;
        if (isPoisoned) {
            int poisonedEnergyConsumption = getPoisonedEnergyConsumption();
            consumeEnergy += poisonedEnergyConsumption;
            logger.log(Level.FINE, "{1} decreased energy (poisoned) by {0}", new Object[] {
                    poisonedEnergyConsumption, getName()
            });
        }
        if (!previousPosition.equals(position)) {
            int moveEnergyConsumption = 1;
            consumeEnergy += moveEnergyConsumption;
            logger.log(Level.FINE, "{1} decreased energy (move) by {0}", new Object[] {
                    moveEnergyConsumption, getName()
            });
        }
        fertilityThreshold++;
        decreaseEnergy(consumeEnergy);
    }

    private Vector2D calculateNextPos(Set<GameObject> neighbourObjects) {
        Vector2D nextPosition;
        Vector2D neighbourPos = getNearestNeighbour(neighbourObjects);

        if (neighbourPos != null) {
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
            if (Math.max(dotProduct, max) > max) {
                max = dotProduct;
                dir = direction.getDirectionVector();
            }
        }
        return Vector2D.add(this.getPosition(), dir);
    }

    /**
     * Returns the position of the nearest neighbour.
     * @param gameObjects set of {@link GameObject} containing the position.
     * @return the position of a neighbour as {@link Vector2D}.
     */
    protected abstract Vector2D getNearestNeighbour(Set<GameObject> gameObjects);

    /**
     * Is called when the animal eats meat.
     * @param lifeForm {@link LifeForm}
     * @throws LifeFormException    when could not eat the provided life form.
     * @throws NullPointerException When the provided life form wanted to eat is null.
     */
    public void eat(LifeForm lifeForm) throws LifeFormException {
        Objects.requireNonNull(lifeForm, "Cannot eat null.");
        LifeFormActionCheck eatRules = getEatRules(lifeForm);
        if (eatRules != null) {
            eatRules.check();
        }

        logger.log(Level.FINE, "{0} ate {1}", new Object[] {
                getName(),
                lifeForm.getClass().getSimpleName()
        });
        increaseEnergy(lifeForm.getEnergy());
        if (lifeForm.isPoisonous()) {
            becomePoisoned();
        }
        lifeForm.die();
    }

    /**
     * Returns the rules to be checked on before an animal can eat.
     * @param lifeForm the {@link LifeForm} this animal wants to eat.
     * @return rules to check before eating the {@link LifeForm}.
     */
    protected abstract LifeFormActionCheck getEatRules(LifeForm lifeForm);

    /**
     * Used to determine weather this life form can reproduce or not.
     * @return reproduction counter as int.
     */
    public int getFertilityThreshold() {
        return fertilityThreshold;
    }


    public void setScanRadius(int scanRadius) {
        this.scanRadius = scanRadius;
    }

    public int getScanRadius() {
        return scanRadius;
    }

    /**
     * Resets the fertility threshold.
     */
    private void resetFertilityThreshold() {
        this.fertilityThreshold = 0;
    }

    /**
     * Is called when the animal reproduces.
     * @param partner the {@link LifeForm} this animal try to reproduce with.
     * @throws LifeFormException    could not reproduce with the life form.
     * @throws NullPointerException the provided life form wanted to reproduce with is null.
     * @return animalObjectChild depending on which Object called the method.
     */
    public AnimalObject reproduce(LifeForm partner) throws LifeFormException {
        Objects.requireNonNull(partner, "Cannot be null.");
        if (partner.getGender().equals("F")) {
            throw new LifeFormException("Cannot give birth because im male");
        } else if (getFertilityThreshold() < REPRODUCTION_MINIMUM) {
            throw new LifeFormException("Cannot reproduce because partner is not fertile yet");
        }
        resetFertilityThreshold(); // sets own counter to zero (only on females)
        AnimalObject animalObjectChild = createChild();
        animalObjectChild.setPosition(this.chooseRandomNeighbourPosition());
        return animalObjectChild;
    }

    private AnimalObject createChild() throws LifeFormException {
        try {
            return getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new LifeFormException("There were complications at birth", e);
        }
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }

}
