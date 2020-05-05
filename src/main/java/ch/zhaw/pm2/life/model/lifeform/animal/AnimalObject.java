package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Position;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;

import java.util.Objects;
import java.util.Random;

/**
 * Abstract class of an animal.
 */
public abstract class AnimalObject extends LifeForm {

    /**
     * Default energy level of an {@link AnimalObject}.
     */
    public static final int INIT_ENERGY_ANIMALS = 10;

    /**
     * Flag indicating the gender.
     */
    protected boolean isMale;

    /**
     * Determines if this {@link AnimalObject} can eat plants.
     */
    protected boolean isPlantEater;

    /**
     * Determines if this {@link AnimalObject} can eat meat (other {@link AnimalObject}).
     */
    protected boolean isMeatEater;

    /**
     * Default constructor.
     */
    public AnimalObject() {
        currentEnergy = INIT_ENERGY_ANIMALS;
    }

    @Override
    public String getGender() {
        String gender = "F";
        if(isMale) {
            gender = "M";
        }
        return gender;
    }

    /**
     *  Decides the gender by a 50/50 chance
     * @return isMale = true if its a male and false if its a female
     */
    public boolean setGender() {
        isMale = true;
        Random random = new Random();
        int randomValue = random.nextInt(10);
        if(randomValue < 5) {
            isMale = false;
        }
        return isMale;
    }

    /**
     * Is called when the animal moves
     */
    public void move() {
        Position previousPosition = position;
        position = chooseRandomNeighbourPosition();
        if(!previousPosition.equals(position)) {
            decreaseEnergy(1);
        }
    }

    /**
     * Is called when the animal eats meat
     */
    public abstract void eat(LifeForm lifeForm) throws LifeFormException;

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }

}
