package ch.zhaw.pm2.life.model.lifeform.plant;

import javafx.scene.paint.Color;

/**
 * Implementation of the first kind of plant
 * Extends PlantObject
 */
public class FirstPlant extends PlantObject {

    /**
     * Default constructor.
     */
    public FirstPlant() {
        this.objectColor = Color.GREEN;
    }

    @Override
    public String getGender() {
        return "N";
    }

}
