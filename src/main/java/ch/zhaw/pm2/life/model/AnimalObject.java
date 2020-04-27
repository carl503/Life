package ch.zhaw.pm2.life.model;


/**
 * AnimalObjects extends GameObject
 * AnimalObjects handles all features and methods that PlantEater and MeatEater have in common
 */
public abstract class AnimalObject extends GameObject implements Eatable {

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
        position = chooseRandomNeighbourPosition();
        decreaseEnergy();
    }

    /**
     * Is called when the animal eats meat
     */
    public void eat(Eatable food) {
        GameObject gameObject = castEatableToGameObject(food);
        if(isMeatEater && food.getFoodType() == FoodType.MEAT || isPlantEater && food.getFoodType() == FoodType.PLANT) {
            currentEnergy = currentEnergy + gameObject.getCurrentEnergy();
            gameObject.killObject();
        } else {
            System.out.println("");
        }
    }

    private GameObject castEatableToGameObject(Eatable food) {
        GameObject gameObject = null;

        if(food instanceof AnimalObject || food instanceof PlantObject) {
            gameObject = (GameObject) food;
        } else {
            //TODO Error
        }
        return gameObject;
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.MEAT;
    }


}
