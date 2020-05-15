package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimalObjectTest {

    private static final int MOVE_ENERGY_CONSUMPTION = 1;
    private static final int POISONED_ENERGY_CONSUMPTION = 2;
    private static final int ACTUAL_REPRODUCTION_MINIMUM = 9;
    private Set<GameObject> dummyGameObjectsSet;

    @Spy private AnimalObject animalObject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

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
        doReturn(new Vector2D(2, 2)).when(animalObject).getNearestNeighbour(anySet());
        GameObject neighbour = mock(GameObject.class, CALLS_REAL_METHODS);

        dummyGameObjectsSet.add(neighbour);
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

        int energy = animalObject.getEnergy();
        animalObject.move(dummyGameObjectsSet);

        assertEquals(energy, animalObject.getEnergy());
    }

    @Test
    public void moveTestDecreaseEnergyNotPoisonedAnotherPosition() {
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        int energy;

        animalObject.increaseEnergy(5);
        energy = animalObject.getEnergy();
        animalObject.move(dummyGameObjectsSet);

        assertEquals(energy - MOVE_ENERGY_CONSUMPTION, animalObject.getEnergy());
    }

    @Test
    public void moveTestSameEnergyPoisonedSamePosition() {
        doReturn(animalObject.getPosition()).when(animalObject).chooseRandomNeighbourPosition();
        doReturn(2).when(animalObject).getPoisonedEnergyConsumption();
        int energy;

        animalObject.becomePoisoned();
        animalObject.increaseEnergy(5);
        energy = animalObject.getEnergy();
        animalObject.move(dummyGameObjectsSet);

        assertEquals(energy - 2, animalObject.getEnergy());
    }

    @Test
    public void moveTestDecreaseEnergyPoisonedAnotherPosition() {
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        doReturn(2).when(animalObject).getPoisonedEnergyConsumption();
        int energy;

        animalObject.becomePoisoned();
        animalObject.increaseEnergy(5);
        energy = animalObject.getEnergy();
        animalObject.move(dummyGameObjectsSet);

        verify(animalObject).decreaseEnergy(MOVE_ENERGY_CONSUMPTION + POISONED_ENERGY_CONSUMPTION);
        assertEquals(energy - MOVE_ENERGY_CONSUMPTION - POISONED_ENERGY_CONSUMPTION, animalObject.getEnergy());
    }

    @Test
    public void eatTestNoRulesLifeFormActionCheck() {
        LifeForm lifeForm = mock(LifeForm.class);

        assertDoesNotThrow(() -> animalObject.eat(lifeForm));
        verify(animalObject).increaseEnergy(lifeForm.getEnergy());
        verify(lifeForm).die();
    }

    @Test
    public void eatTestRulesLifeFormActionCheckPass() {
        LifeForm lifeForm = mock(LifeForm.class);
        when(animalObject.getEatRules(lifeForm)).thenReturn(() -> {
        });

        assertDoesNotThrow(() -> animalObject.eat(lifeForm));
        verify(animalObject).increaseEnergy(lifeForm.getEnergy());
        verify(lifeForm).die();
    }

    @Test
    public void eatTestEatPoisonedLifeForm() {
        LifeForm lifeForm = mock(LifeForm.class);
        when(lifeForm.isPoisonous()).thenReturn(true);

        assertDoesNotThrow(() -> animalObject.eat(lifeForm));
        verify(animalObject).increaseEnergy(lifeForm.getEnergy());
        verify(animalObject).becomePoisoned();
        verify(lifeForm).die();
    }

    @Test
    public void reproduceTestMaleWithFemalePartner() {
        LifeForm partner = mock(LifeForm.class);
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        animalObject.fertilityThreshold = ACTUAL_REPRODUCTION_MINIMUM;
        when(animalObject.getName()).thenReturn("Herbivore");
        when(partner.getGender()).thenReturn("F");

        Exception exception = assertThrows(LifeFormException.class, () -> animalObject.reproduce(partner));
        assertEquals(animalObject.getName() + ": Kann keine Kinder gebaeren, weil ich ein Maennchen bin.", exception.getMessage());
    }

    @Test
    public void reproduceTestFemaleWithMalePartner() {
        LifeForm partner = mock(LifeForm.class);
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        animalObject.fertilityThreshold = ACTUAL_REPRODUCTION_MINIMUM;
        when(partner.getGender()).thenReturn("M");

        assertDoesNotThrow(() -> assertEquals(animalObject.getClass(), animalObject.reproduce(partner).getClass()));
        assertEquals(0, animalObject.fertilityThreshold);
    }

    @Test
    public void reproduceTestFemaleWithLowFertilityThreshold() {
        LifeForm partner = mock(LifeForm.class);
        doReturn(new Vector2D(0, 0)).when(animalObject).chooseRandomNeighbourPosition();
        when(animalObject.getName()).thenReturn("Herbivore");
        animalObject.fertilityThreshold = 0;
        when(partner.getGender()).thenReturn("M");

        Exception exception = assertThrows(LifeFormException.class, () -> animalObject.reproduce(partner));
        assertEquals(animalObject.getName() + ": Kann nicht paaren, weil mein Partner noch nicht fruchtbar ist.", exception.getMessage());
    }

    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void moveTestGameObjectSetNullInvalid() {
        assertThrows(NullPointerException.class, () -> animalObject.move(null));
    }

    @Test
    public void eatTestLifeFormNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> animalObject.eat(null));
        assertEquals("Cannot eat null.", exception.getMessage());
    }

    @Test
    public void eatTestRulesLifeFormActionCheckFail() {
        LifeForm lifeForm = mock(LifeForm.class);
        when(animalObject.getEatRules(lifeForm)).thenReturn(() -> {
            throw new LifeFormException("Exception");
        });

        Exception exception = assertThrows(LifeFormException.class, () -> animalObject.eat(lifeForm));
        assertEquals("Exception", exception.getMessage());
        verify(animalObject, never()).increaseEnergy(lifeForm.getEnergy());
        verify(lifeForm, never()).die();
    }

    @Test
    public void reproduceTestLifeFormNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> animalObject.reproduce(null));
        assertEquals("Cannot be null.", exception.getMessage());
    }

}