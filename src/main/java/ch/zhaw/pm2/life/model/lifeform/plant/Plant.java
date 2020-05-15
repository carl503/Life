package ch.zhaw.pm2.life.model.lifeform.plant;

import ch.zhaw.pm2.life.controller.Game;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;

/**
 * This class inherits from {@link LifeForm}
 * Plants have a Neutral gender and cannot reproduce with each other or consume other {@link LifeForm}.
 * Plants spawn randomly on the map which is handled in the {@link Game} class.
 */
public class Plant extends LifeForm {

    /**
     * Plants have per default double the energy of a {@link AnimalObject}
     */
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
