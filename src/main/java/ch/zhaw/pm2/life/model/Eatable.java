package ch.zhaw.pm2.life.model;

public interface Eatable {

    public FoodType getFoodType();

    public enum FoodType {
        PLANT, MEAT
    }
}
