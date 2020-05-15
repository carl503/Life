package ch.zhaw.pm2.life.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {

    @Test
    public void testSubtract() {
        Vector2D first = new Vector2D(1, 2);
        Vector2D second = new Vector2D(-2, 3);
        assertEquals(new Vector2D(3, -1), Vector2D.subtract(first, second));
    }

    @Test
    public void testAdd() {
        Vector2D first = new Vector2D(1, 2);
        Vector2D second = new Vector2D(-2, 3);
        assertEquals(new Vector2D(-1, 5), Vector2D.add(first, second));
    }

    @Test
    public void testMultiplyX() {
        assertEquals(new Vector2D(-3, 2), Vector2D.multiplyX(-3, new Vector2D(1, 2)));
        assertEquals(new Vector2D(3, 2), Vector2D.multiplyX(3, new Vector2D(1, 2)));
        assertEquals(new Vector2D(0, 2), Vector2D.multiplyX(0, new Vector2D(1, 2)));
    }

    @Test
    public void testMultiplyY() {
        assertEquals(new Vector2D(1, -6), Vector2D.multiplyY(-3, new Vector2D(1, 2)));
        assertEquals(new Vector2D(1, 6), Vector2D.multiplyY(3, new Vector2D(1, 2)));
        assertEquals(new Vector2D(1, 0), Vector2D.multiplyY(0, new Vector2D(1, 2)));
    }

    @Test
    public void testMultiply() {
        assertEquals(new Vector2D(-3, -6), Vector2D.multiply(-3, new Vector2D(1, 2)));
        assertEquals(new Vector2D(3, 6), Vector2D.multiply(3, new Vector2D(1, 2)));
        assertEquals(new Vector2D(0, 0), Vector2D.multiply(0, new Vector2D(1, 2)));
    }

    @Test
    public void testDot() {
        assertEquals(4, Vector2D.dot(new Vector2D(1, 2), new Vector2D(-2, 3)));
        assertEquals(8, Vector2D.dot(new Vector2D(1, 2), new Vector2D(2, 3)));
        assertEquals(8, Vector2D.dot(new Vector2D(-1, -2), new Vector2D(-2, -3)));
        assertEquals(-8, Vector2D.dot(new Vector2D(-1, 2), new Vector2D(2, -3)));
        assertEquals(-8, Vector2D.dot(new Vector2D(1, -2), new Vector2D(-2, 3)));
    }

    @Test
    public void testIsPositive() {
        assertTrue(Vector2D.isPositive(new Vector2D(0, 0)));
        assertTrue(Vector2D.isPositive(new Vector2D(0, 1)));
        assertTrue(Vector2D.isPositive(new Vector2D(1, 0)));
        assertTrue(Vector2D.isPositive(new Vector2D(1, 1)));
        assertFalse(Vector2D.isPositive(new Vector2D(-1, 1)));
        assertFalse(Vector2D.isPositive(new Vector2D(-1, 0)));
        assertFalse(Vector2D.isPositive(new Vector2D(-1, -1)));
        assertFalse(Vector2D.isPositive(new Vector2D(1, -1)));
        assertFalse(Vector2D.isPositive(new Vector2D(0, -1)));
        assertFalse(Vector2D.isPositive(new Vector2D(1, -1)));
    }

    @Test
    public void testIsNegative() {
        assertFalse(Vector2D.isNegative(new Vector2D(0, 0)));
        assertFalse(Vector2D.isNegative(new Vector2D(0, 1)));
        assertFalse(Vector2D.isNegative(new Vector2D(1, 0)));
        assertFalse(Vector2D.isNegative(new Vector2D(1, 1)));
        assertTrue(Vector2D.isNegative(new Vector2D(-1, 1)));
        assertTrue(Vector2D.isNegative(new Vector2D(-1, 0)));
        assertTrue(Vector2D.isNegative(new Vector2D(-1, -1)));
        assertTrue(Vector2D.isNegative(new Vector2D(1, -1)));
        assertTrue(Vector2D.isNegative(new Vector2D(0, -1)));
        assertTrue(Vector2D.isNegative(new Vector2D(1, -1)));
    }

}
