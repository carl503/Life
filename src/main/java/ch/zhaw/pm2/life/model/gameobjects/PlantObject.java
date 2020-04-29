package ch.zhaw.pm2.life.model.gameobjects;

import ch.zhaw.pm2.life.model.lifeforms.Eatable;

/**
 * PlantObject extends GameObject
 * PlantObjects handles all features and methods that Future Plants will have in common
 */
public abstract class PlantObject extends GameObject implements Eatable {

    protected static final int PLANT_ENERGY = 5;

    public PlantObject() {
        super();
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.PLANT;
    }
}
