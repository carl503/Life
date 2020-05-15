package ch.zhaw.pm2.life.model.lifeform.plant;

import ch.zhaw.pm2.life.controller.Game;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;

/**
 * This class inherits from {@link LifeForm}
 * Plants have a Neutral gender and cannot reproduce with each other or consume other {@link LifeForm}.
 * Plants spawn randomly on the map which is handled in the {@link Game} class.
 */
public class Plant extends LifeForm {

    /**
     * Default constructor.
     */
    public Plant() {
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
