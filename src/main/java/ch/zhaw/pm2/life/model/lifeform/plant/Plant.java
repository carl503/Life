package ch.zhaw.pm2.life.model.lifeform.plant;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

/**
 * Class of a plant.
 */
public class Plant extends LifeForm {

    private static final int PLANT_ENERGY_FACTOR = 2;

    /**
     * Default constructor.
     */
    public Plant() {
        energy = AnimalObject.INIT_ENERGY_ANIMALS * PLANT_ENERGY_FACTOR;
        setName("Plant");
    }

    @Override
    public FoodType getFoodType() {
        return FoodType.PLANT;
    }

    @Override
    public String getGender() {
        return "N";
    }
}
