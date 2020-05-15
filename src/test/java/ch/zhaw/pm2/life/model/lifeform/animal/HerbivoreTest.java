package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.LifeFormActionCheck;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HerbivoreTest {
    private Herbivore herbivore;
    private Set<GameObject> dummyNeighbourSet;
    private Plant plant;

    @Spy private GameObject gameObject;
    @Spy private LifeForm lifeForm;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        herbivore = new Herbivore();
        dummyNeighbourSet = new HashSet<>();
        plant = new Plant();

        herbivore.setPosition(new Vector2D(1, 1));
    }


    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void getNearestNeighbourEmptyGameObjectSet() {
        assertNull(herbivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourOnePlant() {
        plant.setPosition(new Vector2D(0, 1));
        dummyNeighbourSet.add(plant);

        assertEquals(plant.getPosition(), herbivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourTwoPlants() {
        Plant plant2 = new Plant();

        plant.setPosition(new Vector2D(0, 1));
        plant2.setPosition(new Vector2D(2, 0));
        dummyNeighbourSet.add(plant);
        dummyNeighbourSet.add(plant2);

        assertEquals(plant.getPosition(), herbivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourOnePlantOneGameObject() {
        plant.setPosition(new Vector2D(0, 0));
        gameObject.setPosition(new Vector2D(0, 1));
        dummyNeighbourSet.add(plant);
        dummyNeighbourSet.add(gameObject);

        assertEquals(plant.getPosition(), herbivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourNoNearestPlants() {
        gameObject.setPosition(new Vector2D(0, 1));
        dummyNeighbourSet.add(gameObject);

        assertNull(herbivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getEatRulesEatPlantObject() {
        LifeFormActionCheck lifeFormActionCheck = herbivore.getEatRules(plant);
        assertDoesNotThrow(lifeFormActionCheck::check);
    }

    @Test
    public void getEatRulesEatPlant() {
        when(lifeForm.getFoodType()).thenReturn(LifeForm.FoodType.PLANT);
        LifeFormActionCheck lifeFormActionCheck = herbivore.getEatRules(lifeForm);

        assertDoesNotThrow(lifeFormActionCheck::check);
    }

    @Test
    public void getEatRulesEatHerbivore() {
        LifeFormActionCheck lifeFormActionCheck = herbivore.getEatRules(herbivore);

        Exception exception = assertThrows(LifeFormException.class, lifeFormActionCheck::check);
        assertEquals("Ich fresse leider kein Fleisch.", exception.getMessage());
    }

    @Test
    public void getEatRulesEatMeat() {
        when(lifeForm.getFoodType()).thenReturn(LifeForm.FoodType.MEAT);
        LifeFormActionCheck lifeFormActionCheck = herbivore.getEatRules(lifeForm);

        Exception exception = assertThrows(LifeFormException.class, lifeFormActionCheck::check);
        assertEquals("Ich fresse leider kein Fleisch.", exception.getMessage());
    }


    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void getNearestNeighbourNullGameObjectSet() {
        assertThrows(NullPointerException.class, () -> herbivore.getNearestNeighbour(null));
    }

    @Test
    public void getNearestNeighbourPlantSameHerbivorePosition() {
        plant.setPosition(herbivore.getPosition());
        dummyNeighbourSet.add(plant);

        assertEquals(herbivore.getPosition(), herbivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourGameObjectSameHerbivorePosition() {
        gameObject.setPosition(herbivore.getPosition());
        dummyNeighbourSet.add(gameObject);

        assertNull(herbivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getEatRulesHerbivoreEatLifeForm() {
        LifeFormActionCheck lifeFormActionCheck = herbivore.getEatRules(lifeForm);
        assertDoesNotThrow(lifeFormActionCheck::check);
    }
}
