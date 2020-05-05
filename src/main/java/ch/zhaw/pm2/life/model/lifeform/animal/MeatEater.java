package ch.zhaw.pm2.life.model.lifeform.animal;

import javafx.scene.paint.Color;

/**
 * First Version of a MeatEater Animal that extends AnimalObject
 */
public class MeatEater extends AnimalObject {

    /**
     * Default constructor.
     */
    public MeatEater() {
        this.objectColor = Color.RED;
        isMeatEater = true;
        isMale = setGender();
    }

}
