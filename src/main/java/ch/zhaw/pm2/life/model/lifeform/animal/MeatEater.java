package ch.zhaw.pm2.life.model.lifeform.animal;

import javafx.scene.paint.Color;

/**
 * First Version of a MeatEater Animal that extends AnimalObject
 */
public class MeatEater extends AnimalObject {

    public MeatEater() {
        super();
        this.objectColor = Color.RED;
        isMeatEater = true;

    }

    @Override
    public String toString(int currentEnergy) {
        return String.valueOf(currentEnergy);
    }


}
