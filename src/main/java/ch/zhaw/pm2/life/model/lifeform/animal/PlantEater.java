package ch.zhaw.pm2.life.model.lifeform.animal;

import javafx.scene.paint.Color;

/**
 * First Version of a PlantEater animal that extends AnimalObject
 */
public class PlantEater extends AnimalObject {

    public PlantEater() {
        super();
        this.objectColor = Color.LIGHTSEAGREEN;
        isPlantEater = true;
        isMale = setGender();
    }

    @Override
    public String toString(int currentEnergy) {
        return String.valueOf(currentEnergy);
    }

}
