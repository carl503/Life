package ch.zhaw.pm2.life.model;

import java.util.Objects;

/**
 * Position Objects represent the positions of the GameObjects in the field
 */
public class Vector2D {

    private int x;
    private int y;

    /**
     * Default constructor.
     * @param x X-Coordinate as int
     * @param y Y-Coordinate as int
     */
    public Vector2D(int x, int y){
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

    /**
     * Subtracts the second from the first
     * @param first vector
     * @param second vector
     * @return new Position
     */
    public static Vector2D subtract(Vector2D first, Vector2D second) {
        int newX = first.getX() - second.getX();
        int newY = first.getY() - second.getY();
        return new Vector2D(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("[x = %d, y = %d]", x, y);
    }
}
