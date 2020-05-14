package ch.zhaw.pm2.life.model.lifeform.plant;

/**
 * Implementation of the first kind of plant.
 * @author pedernin
 */
public class FirstPlant extends PlantObject {

    /**
     * Default constructor.
     */
    public FirstPlant() {
        this.color = "green";
    }

    @Override
    public String getGender() {
        return "N";
    }

}
