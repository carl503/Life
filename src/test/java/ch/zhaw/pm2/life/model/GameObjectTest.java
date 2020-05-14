package ch.zhaw.pm2.life.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameObjectTest {
    private static final int BOARD_SIZE = 3;

    @Spy private GameObject gameObject;
    @Mock private Random random;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

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
            when(random.nextInt(anyInt())).thenReturn(i);
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
            when(random.nextInt(anyInt())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionCenterTopStart() {
        gameObject.setPosition(new Vector2D(1, 0));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE - 1; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(anyInt())).thenReturn(i);
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
            when(random.nextInt(anyInt())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionCenterRightStart() {
        gameObject.setPosition(new Vector2D(2, 1));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 1; x < BOARD_SIZE; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(anyInt())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionBottomLeftCornerStart() {
        gameObject.setPosition(new Vector2D(0, 2));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 1; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE - 1; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(anyInt())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionCenterBottomStart() {
        gameObject.setPosition(new Vector2D(1, 2));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 1; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(anyInt())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionBottomRightCornerStart() {
        gameObject.setPosition(new Vector2D(2, 2));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 1; y < BOARD_SIZE; y++) {
            for (int x = 1; x < BOARD_SIZE; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(anyInt())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void testChooseRandomNeighbourPositionCenterLeftStart() {
        gameObject.setPosition(new Vector2D(0, 1));

        List<Vector2D> expectedNeighbours = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE - 1; x++) {
                expectedNeighbours.add(new Vector2D(x, y));
            }
        }

        for (int i = 0; i < expectedNeighbours.size(); i++) {
            when(random.nextInt(anyInt())).thenReturn(i);
            Vector2D neighbour = gameObject.chooseRandomNeighbourPosition();
            assertTrue(expectedNeighbours.contains(neighbour));
        }
    }

    @Test
    public void increaseEnergyPositiveInput() {
        int energy = gameObject.energy;
        gameObject.increaseEnergy(10);
        assertEquals(energy + 10, gameObject.energy);
    }

    @Test
    public void increaseEnergyZeroInput() {
        int energy = gameObject.energy;
        gameObject.increaseEnergy(0);
        assertEquals(energy, gameObject.energy);
    }

    @Test
    public void decreaseEnergyPositiveInput() {
        int energy = gameObject.energy;
        gameObject.decreaseEnergy(10);
        assertEquals(energy - 10, gameObject.energy);
    }

    @Test
    public void decreaseEnergyZeroInput() {
        int energy = gameObject.energy;
        gameObject.decreaseEnergy(0);
        assertEquals(energy, gameObject.energy);
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

    @Test
    public void increaseEnergyNegativeInputInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gameObject.increaseEnergy(-10));
        assertEquals("Energy cannot increase with a negative value.", exception.getMessage());
    }

    @Test
    public void decreaseEnergyNegativeInputInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gameObject.decreaseEnergy(-10));
        assertEquals("Energy cannot decrease with a negative value.", exception.getMessage());
    }
}
