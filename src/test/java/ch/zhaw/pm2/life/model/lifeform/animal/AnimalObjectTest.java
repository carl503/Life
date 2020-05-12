package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AnimalObjectTest {
    private static final int MOVE_ENERGY_CONSUMPTION = 1;
    private static final int POISONED_ENERGY_CONSUMPTION = 2;
    private AnimalObject animalObject;
    private Set<GameObject> dummyGameObjectsSet;

    @BeforeEach
    public void setUp() {
        animalObject = mock(AnimalObject.class, Mockito.CALLS_REAL_METHODS);
        animalObject.setPosition(new Vector2D(1, 1));

        dummyGameObjectsSet = new HashSet<>();
    }

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void moveTestEmptyNeighbourObjs() {
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();

        animalObject.move(dummyGameObjectsSet);
        verify(animalObject).chooseRandomNeighbourPosition();
        assertEquals(new Vector2D(0, 0), animalObject.getPosition());
    }

    @Test
    public void moveTestWithNeighbourObj() {
        GameObject neighbour = mock(GameObject.class, CALLS_REAL_METHODS);

        dummyGameObjectsSet.add(neighbour);

        doReturn(new Vector2D(2, 2)).when(animalObject).getNearestNeighbour(dummyGameObjectsSet);
        animalObject.move(dummyGameObjectsSet);

        assertEquals(new Vector2D(2, 2), animalObject.getPosition());
    }

    @Test
    public void moveTestFertilityThresholdIncrease() {
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        int fertilityThreshold = animalObject.getFertilityThreshold();
        animalObject.move(dummyGameObjectsSet);
        assertEquals(fertilityThreshold + 1, animalObject.getFertilityThreshold());
    }

    @Test
    public void moveTestSameEnergyNotPoisonedSamePosition() {
        doReturn(animalObject.getPosition()).when(animalObject).chooseRandomNeighbourPosition();
        int energy = animalObject.getCurrentEnergy();
        animalObject.move(dummyGameObjectsSet);
        assertEquals(energy, animalObject.getCurrentEnergy());
    }

    @Test
    public void moveTestDecreaseEnergyNotPoisonedAnotherPosition() {
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        animalObject.increaseEnergy(5);
        int energy = animalObject.getCurrentEnergy();
        animalObject.move(dummyGameObjectsSet);
        assertEquals(energy - MOVE_ENERGY_CONSUMPTION, animalObject.getCurrentEnergy());
    }

    @Test
    public void moveTestSameEnergyPoisonedSamePosition() {
        doReturn(animalObject.getPosition()).when(animalObject).chooseRandomNeighbourPosition();
        doReturn(2).when(animalObject).getPoisonedEnergyConsumption();
        animalObject.becomePoisoned();
        animalObject.increaseEnergy(5);
        int energy = animalObject.getCurrentEnergy();
        animalObject.move(dummyGameObjectsSet);
        assertEquals(energy - 2, animalObject.getCurrentEnergy());
    }

    @Test
    public void moveTestDecreaseEnergyPoisonedAnotherPosition() {
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        doReturn(2).when(animalObject).getPoisonedEnergyConsumption();
        animalObject.becomePoisoned();
        animalObject.increaseEnergy(5);
        int energy = animalObject.getCurrentEnergy();
        animalObject.move(dummyGameObjectsSet);

        verify(animalObject).decreaseEnergy(MOVE_ENERGY_CONSUMPTION + POISONED_ENERGY_CONSUMPTION);
        assertEquals(energy - MOVE_ENERGY_CONSUMPTION - POISONED_ENERGY_CONSUMPTION, animalObject.getCurrentEnergy());
    }

    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

}
