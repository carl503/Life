package ch.zhaw.pm2.life.model.lifeform.animal;


import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Position;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;

/**
 * AnimalObjects extends GameObject
 * AnimalObjects handles all features and methods that PlantEater and MeatEater have in common
 */
public abstract class AnimalObject extends LifeForm {

    protected static final int INIT_ENERGY_ANIMALS = 5;

    protected boolean isPlantEater;
    protected boolean isMeatEater;

    public AnimalObject() {
        super();
        currentEnergy = INIT_ENERGY_ANIMALS;
    }

    /**
     * Is called when the animal moves
     */
    public void move() {
        Position oldPosition = position;
        position = chooseRandomNeighbourPosition();
        if(!oldPosition.equals(position)) {
            currentEnergy--;
        }
    }

    /**
     * Is called when the animal eats meat
     */
    public void eat(LifeForm lifeForm) throws LifeFormException {
        if(isPlantEater && lifeForm.getFoodType() == FoodType.MEAT) {
            throw new LifeFormException("Cannot eat this meat, i am vegetarian.");
        }
        if(isMeatEater && lifeForm.getFoodType() == FoodType.PLANT) {
            throw new LifeFormException("Cannot eat this plant. Do i look like a vegetarian?!");
        }
        currentEnergy = currentEnergy + lifeForm.getCurrentEnergy();
        lifeForm.killObject();
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }


}
