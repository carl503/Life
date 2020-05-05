package ch.zhaw.pm2.life.model;

import java.util.Objects;

/**
 * Position Objects represent the positions of the GameObjects in the field
 */
public class Position {

    private int x;
    private int y;

    /**
     * Default constructor.
     * @param x X-Coordinate as int
     * @param y Y-Coordinate as int
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x value.
     * @return x coordinate as int
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y value.
     * @return y coordinate as int
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
