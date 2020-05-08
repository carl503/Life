package ch.zhaw.pm2.life.model.lifeform.plant;

import ch.zhaw.pm2.life.model.Vector2D;
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

    @Override
    protected void setPosition(Vector2D newBornPosition) {

    }

}
