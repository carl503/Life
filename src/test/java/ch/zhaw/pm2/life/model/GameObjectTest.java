package ch.zhaw.pm2.life.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameObjectTest {
    private static final int BOARD_SIZE = 3;

    private GameObject gameObject;
    private Random random;

    @BeforeEach
    public void setUp() {
        random = mock(Random.class);
        gameObject = Mockito.mock(GameObject.class, Mockito.CALLS_REAL_METHODS);

        gameObject.setRandom(random);
        gameObject.setColumns(BOARD_SIZE);
        gameObject.setRows(BOARD_SIZE);
    }

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void testChooseRandomNeighbourPositionCenterStart() {
        gameObject.setPosition(new Vector2D(1, 1));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(expectedNeighbours.size())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionTopLeftCornerStart() {
        gameObject.setPosition(new Vector2D(0, 0));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE - 1; y++) {
            for (int x = 0; x < BOARD_SIZE - 1; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(expectedNeighbours.size())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionTopRightCornerStart() {
        gameObject.setPosition(new Vector2D(2, 0));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE - 1; y++) {
            for (int x = 1; x < BOARD_SIZE; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(expectedNeighbours.size())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionUnderLeftCornerStart() {
        gameObject.setPosition(new Vector2D(0, 2));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 1; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE - 1; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(expectedNeighbours.size())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionUnderRightCornerStart() {
        gameObject.setPosition(new Vector2D(2, 2));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 1; y < BOARD_SIZE; y++) {
            for (int x = 1; x < BOARD_SIZE; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(expectedNeighbours.size())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void testChooseRandomNeighbourPositionInvalidPositionInvalid() {
        gameObject.setPosition(new Vector2D(10, 10));
        when(random.nextInt(2)).then(CALLS_REAL_METHODS);

        assertThrows(IndexOutOfBoundsException.class, () -> gameObject.chooseRandomNeighbourPosition());
    }
}
