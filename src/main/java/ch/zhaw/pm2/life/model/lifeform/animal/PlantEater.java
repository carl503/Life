package ch.zhaw.pm2.life.model.lifeform.animal;

import javafx.scene.paint.Color;

/**
 * First Version of a PlantEater animal that extends AnimalObject
 */
public class PlantEater extends AnimalObject {

    /**
     * Default constructor
     */
    public PlantEater() {
        this.objectColor = Color.LIGHTSEAGREEN;
        isPlantEater = true;
        isMale = setGender();
    }

}
