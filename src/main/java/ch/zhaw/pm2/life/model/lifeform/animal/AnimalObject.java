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
        super();
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
    public void eat(LifeForm lifeForm) throws LifeFormException {
        Objects.requireNonNull(lifeForm, "Cannot eat null.");
        if(isPlantEater && lifeForm.getFoodType() == FoodType.MEAT) {
            throw new LifeFormException("Cannot eat this meat, I am vegetarian.");
        }
        if(isMeatEater && lifeForm.getFoodType() == FoodType.PLANT) {
            throw new LifeFormException("Cannot eat this plant. Do I look like a vegetarian?!");
        }
        increaseEnergy(lifeForm.getCurrentEnergy());
        lifeForm.die();
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }


}
