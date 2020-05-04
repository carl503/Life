package ch.zhaw.pm2.life.model;

import java.util.Objects;

/**
 * Position Objects represent the positions of the GameObjects in the field
 */
public class Position {
    private int xValue;
    private int yValue;


    public Position(int xValue, int yValue){
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public int getX() {
        return xValue;
    }

    public int getY() {
        return yValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return xValue == position.xValue &&
                yValue == position.yValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xValue, yValue);
    }

}
