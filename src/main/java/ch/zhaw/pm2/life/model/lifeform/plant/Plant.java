package ch.zhaw.pm2.life.model.lifeform.plant;

import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

/**
 * Class of a plant.
 */
public class Plant extends LifeForm {

    /**
     * A {@link Plant} has per default an energy level of {@link Plant#PLANT_ENERGY_FACTOR}
     * times default energy level of an {@link AnimalObject}.
     */
    protected static final int PLANT_ENERGY_FACTOR = 2;

    /**
     * Default constructor.
     */
    public Plant() {
        energy = AnimalObject.INIT_ENERGY_ANIMALS * PLANT_ENERGY_FACTOR;
        color = "green";
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
