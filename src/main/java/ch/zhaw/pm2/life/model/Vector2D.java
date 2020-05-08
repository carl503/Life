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
     * Multiplies a vector by factor factor
     * @param factor factor
     * @param vector vector
     * @return result of the multiplication as {@link Vector2D}
     */
    public static Vector2D multiply(int factor, Vector2D vector) {
        return new Vector2D(multiplyX(factor, vector).getX(), multiplyY(factor, vector).getY());
    }

    /**
     * Calculates the dot product of two vectors.
     * @param first vector
     * @param second vector
     * @return dot product
     */
    public static int dot(Vector2D first, Vector2D second) {
        return (first.getX() * second.getX()) + (first.getY() * second.getY());
    }

    /**
     * Checks if the x and y values of the vector are positive
     * @param vector vector to be checked
     * @return true if positive otherwise false
     */
    public static boolean isPositive(Vector2D vector) {
        return vector.getX() >= 0 && vector.getY() >= 0;
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
