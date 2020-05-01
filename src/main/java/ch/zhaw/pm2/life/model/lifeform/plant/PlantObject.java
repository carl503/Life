package ch.zhaw.pm2.life.model.lifeform.plant;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

/**
 * PlantObject extends GameObject
 * PlantObjects handles all features and methods that Future Plants will have in common
 */
public abstract class PlantObject extends LifeForm {

    protected static final int PLANT_ENERGY_FACTOR = 2;

    public PlantObject() {
        super();
        currentEnergy = AnimalObject.INIT_ENERGY_ANIMALS * PLANT_ENERGY_FACTOR;
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.PLANT;
    }
}
