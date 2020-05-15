package ch.zhaw.pm2.life.model.lifeform.animal;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.LifeFormActionCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class CarnivoreTest {
    private Carnivore carnivore;
    private Carnivore carnivore2;
    private Set<GameObject> dummyNeighbourSet;

    @Spy private GameObject gameObject;
    @Spy private LifeForm lifeForm;
    @Spy private AnimalObject animalObject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        carnivore = new Carnivore();
        carnivore2 = new Carnivore();
        dummyNeighbourSet = new HashSet<>();

        carnivore.setPosition(new Vector2D(1, 1));
    }


    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void getNearestNeighbourEmptyGameObjectSet() {
        assertNull(carnivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourOneAnimalObject() {
        animalObject.setPosition(new Vector2D(0, 1));
        dummyNeighbourSet.add(animalObject);

        assertEquals(animalObject.getPosition(), carnivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourTwoAnimalObjects() {
        AnimalObject animalObject2 = spy(AnimalObject.class);

        animalObject.setPosition(new Vector2D(0, 1));
        animalObject2.setPosition(new Vector2D(2, 0));
        dummyNeighbourSet.add(animalObject);
        dummyNeighbourSet.add(animalObject2);

        assertEquals(animalObject.getPosition(), carnivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourOneAnimalObjectOneGameObject() {
        animalObject.setPosition(new Vector2D(0, 0));
        gameObject.setPosition(new Vector2D(0, 1));
        dummyNeighbourSet.add(animalObject);
        dummyNeighbourSet.add(gameObject);

        assertEquals(animalObject.getPosition(), carnivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourNoNearestAnimalObjects() {
        gameObject.setPosition(new Vector2D(0, 1));
        dummyNeighbourSet.add(gameObject);

        assertNull(carnivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getEatRulesEatMeat() {
        LifeFormActionCheck lifeFormActionCheck = carnivore.getEatRules(lifeForm);
        when(lifeForm.getFoodType()).thenReturn(LifeForm.FoodType.MEAT);

        assertDoesNotThrow(lifeFormActionCheck::check);
    }

    @Test
    public void getEatRulesEatMeatLowEnergy() {
        LifeFormActionCheck lifeFormActionCheck = carnivore.getEatRules(carnivore2);
        carnivore.setEnergy(0);

        Exception exception = assertThrows(LifeFormException.class, lifeFormActionCheck::check);
        assertEquals("Kann dieses Tier nicht fressen, weil es staerker ist als ich.", exception.getMessage());
    }

    @Test
    public void getEatRulesEatCarnivoreEnoughEnergy() {
        carnivore.setEnergy(20);
        LifeFormActionCheck lifeFormActionCheck = carnivore.getEatRules(carnivore2);
        assertDoesNotThrow(lifeFormActionCheck::check);
    }

    @Test
    public void getEatRulesEatCarnivoreSameEnergy() {
        LifeFormActionCheck lifeFormActionCheck = carnivore.getEatRules(carnivore2);
        assertDoesNotThrow(lifeFormActionCheck::check);
    }

    @Test
    public void getEatRulesEatCarnivore() {
        LifeFormActionCheck lifeFormActionCheck = carnivore.getEatRules(carnivore);
        assertDoesNotThrow(lifeFormActionCheck::check);
    }

    @Test
    public void getEatRulesEatPlant() {
        LifeFormActionCheck lifeFormActionCheck = carnivore.getEatRules(lifeForm);
        when(lifeForm.getFoodType()).thenReturn(LifeForm.FoodType.PLANT);

        Exception exception = assertThrows(LifeFormException.class, lifeFormActionCheck::check);
        assertEquals("Ich fresse leider keine Pflanzen.", exception.getMessage());
    }


    //==================================================================================================================
    // Negative tests
    //==================================================================================================================

    @Test
    public void getNearestNeighbourNullGameObjectSet() {
        assertThrows(NullPointerException.class, () -> carnivore.getNearestNeighbour(null));
    }

    @Test
    public void getNearestNeighbourAnimalObjectSameCarnivorePosition() {
        animalObject.setPosition(carnivore.getPosition());
        dummyNeighbourSet.add(animalObject);

        assertEquals(carnivore.getPosition(), carnivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getNearestNeighbourGameObjectSameCarnivorePosition() {
        gameObject.setPosition(carnivore.getPosition());
        dummyNeighbourSet.add(gameObject);

        assertNull(carnivore.getNearestNeighbour(dummyNeighbourSet));
    }

    @Test
    public void getEatRulesCarnivoreEatLifeForm() {
        LifeFormActionCheck lifeFormActionCheck = carnivore.getEatRules(lifeForm);
        assertDoesNotThrow(lifeFormActionCheck::check);
    }
}
