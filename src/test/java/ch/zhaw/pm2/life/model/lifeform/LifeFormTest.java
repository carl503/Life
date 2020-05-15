package ch.zhaw.pm2.life.model.lifeform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LifeFormTest {

    @Spy private LifeForm lifeform;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void testLifeFormDie() {
        lifeform.die();
        assertTrue(lifeform.isDead());
    }

    @Test
    public void testLifeFormBecomePoisoned() {
        lifeform.becomePoisoned();
        assertTrue(lifeform.isPoisoned());
    }

    @Test
    public void testLifeFormGetPoisonedEnergyConsumption() {
        lifeform.setEnergy(30);
        lifeform.becomePoisoned();
        assertEquals(9, lifeform.getPoisonedEnergyConsumption());
    }

    @Test
    public void testLifeFormNotPoisonedAnymore() {
        lifeform.setEnergy(30);
        lifeform.becomePoisoned();
        assertTrue(lifeform.isPoisoned());
        // remove poison
        for (int i = 0; i < 10; i++) {
            lifeform.getPoisonedEnergyConsumption();
        }
        assertFalse(lifeform.isPoisoned());
    }

    @Test
    public void testLifeFormIsPoisonous() {
        lifeform.isPoisonous = true;
        assertTrue(lifeform.isPoisonous());
    }

    @Test
    public void testLifeFormGenderCarnivore() {
        assertThat(lifeform.getGender(), anyOf(is("F"), is("M")));
    }

    @Test
    public void testLifeFormGetFoodType() {
        when(lifeform.getFoodType()).thenReturn(LifeForm.FoodType.PLANT);
        assertEquals(LifeForm.FoodType.PLANT, lifeform.getFoodType());
    }

}
