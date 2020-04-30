package ch.zhaw.pm2.life.model.gameobjects;


import ch.zhaw.pm2.life.model.board.Position;

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
            decreaseEnergy();
        }
    }

    /**
     * Is called when the animal eats meat
     */
    public void eat(LifeForm lifeForm) {
        if(isMeatEater && lifeForm.getFoodType() == FoodType.MEAT || isPlantEater && lifeForm.getFoodType() == FoodType.PLANT) {
            currentEnergy = currentEnergy + lifeForm.getCurrentEnergy();
            lifeForm.killObject();
        } else {
            System.out.println("");
        }
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }


}
