package ch.zhaw.pm2.life.model;

import java.util.Objects;

/**
 * Vector in a two dimensional room.
 * @author pedernin
 */
public class Vector2D {

    private final int x;
    private final int y;

    /**
     * Default constructor.
     * @param x X-Coordinate as int.
     * @param y Y-Coordinate as int.
     */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Subtracts the second {@link Vector2D} from the first {@link Vector2D}.
     * @param first {@link Vector2D}.
     * @param second {@link Vector2D}.
     * @return new {@link Vector2D}.
     */
    public static Vector2D subtract(Vector2D first, Vector2D second) {
        int newX = first.getX() - second.getX();
        int newY = first.getY() - second.getY();
        return new Vector2D(newX, newY);
    }

    /**
     * Adds two {@link Vector2D}.
     * @param first {@link Vector2D}.
     * @param second {@link Vector2D}.
     * @return the result of the addition as {@link Vector2D}.
     */
    public static Vector2D add(Vector2D first, Vector2D second) {
        int newX = first.getX() + second.getX();
        int newY = first.getY() + second.getY();
        return new Vector2D(newX, newY);
    }

    /**
     * Multiplies a {@link Vector2D} x component with a scalar.
     * @param scalar as int.
     * @param vector {@link Vector2D}.
     * @return the result of the multiplication as {@link Vector2D}.
     */
    public static Vector2D multiplyX(int scalar, Vector2D vector) {
        int newX = scalar * vector.getX();
        return new Vector2D(newX, vector.getY());
    }

    /**
     * Multiplies a {@link Vector2D} y component with a scalar.
     * @param scalar as int.
     * @param vector {@link Vector2D}.
     * @return the result of the multiplication as {@link Vector2D}.
     */
    public static Vector2D multiplyY(int scalar, Vector2D vector) {
        int newY = scalar * vector.getY();
        return new Vector2D(vector.getX(), newY);
    }

    /**
     * Returns the x value.
     * @return x coordinate as int.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y value.
     * @return y coordinate as int.
     */
    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public String toString() {
        return String.format("[x = %d, y = %d]", x, y);
    }

}
