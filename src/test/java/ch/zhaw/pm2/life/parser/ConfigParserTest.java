package ch.zhaw.pm2.life.parser;

import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.animal.Herbivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigParserTest {

    @Test
    public void testInstanceNotNull() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        assertNotNull(parser);
    }

    @Test
    public void testValidConfig() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadValidConfig());

        List<GameObject> expected = getExpectedValid();
        List<GameObject> actual = assertDoesNotThrow(parser::parseObjects);

        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++) {
            LifeForm expectedLifeForm = (LifeForm) expected.get(i);
            LifeForm actualLifeForm = (LifeForm) actual.get(i);
            assertEquals(expectedLifeForm.getName(), actualLifeForm.getName());
            assertEquals(expectedLifeForm.getEnergy(), actualLifeForm.getEnergy());
            assertEquals(expectedLifeForm.getColor(), actualLifeForm.getColor());
        }
    }

    private Properties loadValidConfig() {
        Properties config = new Properties();

        config.put("wolf.type", "carnivore");
        config.put("wolf.name", "Wolf");
        config.put("wolf.energy", 7);
        config.put("wolf.color", "#D3D3D3");

        config.put("rabbit.type", "herbivore");
        config.put("rabbit.name", "Hase");
        config.put("rabbit.energy", 4);
        config.put("rabbit.color", "#8AF1FE");

        config.put("dandelion.type", "plant");
        config.put("dandelion.name", "Löwenzahn");
        config.put("dandelion.color", "#FAFE4B");
        config.put("dandelion.energy", 20);

        return config;
    }

    private List<GameObject> getExpectedValid() {
        List<GameObject> expected = new ArrayList<>();

        LifeForm bear = new Carnivore();
        bear.setName("Wolf");
        bear.setEnergy(7);
        bear.setColor("#D3D3D3");
        expected.add(bear);

        LifeForm rabbit = new Herbivore();
        rabbit.setName("Hase");
        rabbit.setEnergy(4);
        rabbit.setColor("#8AF1FE");
        expected.add(rabbit);

        LifeForm dandelion = new Plant();
        dandelion.setName("Löwenzahn");
        dandelion.setEnergy(20);
        dandelion.setColor("#FAFE4B");
        expected.add(dandelion);

        return expected;
    }

}
