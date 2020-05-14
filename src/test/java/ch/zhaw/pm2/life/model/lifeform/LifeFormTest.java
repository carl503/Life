package ch.zhaw.pm2.life.model.lifeform;

import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class LifeFormTest {

    //==================================================================================================================
    // Positive tests
    //==================================================================================================================

    @Test
    public void testLifeFormDie() {
        Plant plant = spy(Plant.class);
        plant.die();
        assertTrue(plant.isDead());
    }

    @Test
    public void testLifeFormBecomePoisoned() {
        Plant plant = spy(Plant.class);
        plant.becomePoisoned();
        assertTrue(plant.isPoisoned());
    }

    @Test
    public void testLifeFormGetPoisonedEnergyConsumption() {
        Plant plant = spy(Plant.class);
        plant.setEnergy(30);
        plant.becomePoisoned();
        assertEquals(9, plant.getPoisonedEnergyConsumption());
    }

    @Test
    public void testLifeFormNotPoisonedAnymore() {
        Plant plant = spy(Plant.class);
        plant.setEnergy(30);
        plant.becomePoisoned();
        assertTrue(plant.isPoisoned());
        // remove poison
        for(int i = 0; i < 10; i++) {
            plant.getPoisonedEnergyConsumption();
        }
        assertFalse(plant.isPoisoned());
    }

    @Test
    public void testLifeFormIsPoisonous() {
        Plant plant = spy(Plant.class);
        plant.isPoisonous = true;
        assertTrue(plant.isPoisonous());
    }

    @Test
    public void testLifeFormGenderPlant() {
        Plant plant = spy(Plant.class);
        assertEquals("N", plant.getGender());
    }

    @Test
    public void testLifeFormGenderCarnivore() {
        Carnivore carnivore = spy(Carnivore.class);
        assertThat(carnivore.getGender(), anyOf(is("F"), is("M")));
    }

    @Test
    public void testLifeFormGetFoodType() {
        LifeForm lifeForm = spy(Carnivore.class);
        assertThat(lifeForm.getFoodType(), anyOf(is(LifeForm.FoodType.MEAT), is(LifeForm.FoodType.PLANT)));
    }

}
