package ch.zhaw.pm2.life.parser;

import ch.zhaw.pm2.life.exception.LifeException;
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

    private static final String COULD_NOT_PARSE_THE_CONFIG_FILE = "Could not parse the config file";
    private static final String CARNIVORE = "carnivore";
    private static final String WOLF = "Wolf";
    private static final String WOLF_COLOR = "#D3D3D3";
    private static final String WOLF_TYPE = "wolf.type";
    private static final String WOLF_NAME = "wolf.name";
    private static final String WOLF_ENERGY_KEY = "wolf.energy";
    private static final String WOLF_COLOR_KEY = "wolf.color";
    private static final String HASE = "Hase";
    private static final String DANDELION = "Löwenzahn";
    private static final String RABBIT_COLOR = "#8AF1FE";
    private static final String DANDELION_COLOR = "#FAFE4B";
    private static final int WOLF_ENERGY_VALUE = 7;
    private static final int DANDELION_ENERGY = 20;
    private static final int RABBIT_ENERGY = 4;

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
        for (int i = 0; i < expected.size(); i++) {
            LifeForm expectedLifeForm = (LifeForm) expected.get(i);
            LifeForm actualLifeForm = (LifeForm) actual.get(i);
            assertEquals(expectedLifeForm.getName(), actualLifeForm.getName());
            assertEquals(expectedLifeForm.getEnergy(), actualLifeForm.getEnergy());
            assertEquals(expectedLifeForm.getColor(), actualLifeForm.getColor());
        }
    }

    @Test
    public void testInvalidType() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidType());
        Exception thrown = assertThrows(LifeException.class, parser::parseObjects);
        assertEquals(COULD_NOT_PARSE_THE_CONFIG_FILE, thrown.getMessage());
    }

    @Test
    public void testInvalidConfigKey() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidConfigKey());
        assertThrows(NullPointerException.class, parser::parseObjects);
    }

    @Test
    public void testInvalidTextValue() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidTextValue());
        Exception thrown = assertThrows(LifeException.class, parser::parseObjects);
        assertEquals(COULD_NOT_PARSE_THE_CONFIG_FILE, thrown.getMessage());
    }

    @Test
    public void testInvalidNumberValue() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidNumberValue());
        assertThrows(NumberFormatException.class, parser::parseObjects);
    }

    @Test
    public void testInvalidMissingPropertyNumber() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidMissingPropertyNumber());
        assertThrows(NullPointerException.class, parser::parseObjects);
    }

    @Test
    public void testInvalidMissingPropertyString() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidMissingPropertyString());
        assertThrows(NullPointerException.class, parser::parseObjects);
    }

    @Test
    public void testInvalidWrongColorHexFive() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidWrongColorHexFive());
        Exception thrown = assertThrows(LifeException.class, parser::parseObjects);
        assertEquals(COULD_NOT_PARSE_THE_CONFIG_FILE, thrown.getMessage());
    }

    @Test
    public void testInvalidWrongColorHexFour() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidWrongColorHexFour());
        Exception thrown = assertThrows(LifeException.class, parser::parseObjects);
        assertEquals(COULD_NOT_PARSE_THE_CONFIG_FILE, thrown.getMessage());
    }

    @Test
    public void testInvalidWrongColorHexTwo() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidWrongColorHexTwo());
        Exception thrown = assertThrows(LifeException.class, parser::parseObjects);
        assertEquals(COULD_NOT_PARSE_THE_CONFIG_FILE, thrown.getMessage());
    }

    @Test
    public void testInvalidWrongColorHexSeven() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidWrongColorHexSeven());
        Exception thrown = assertThrows(LifeException.class, parser::parseObjects);
        assertEquals(COULD_NOT_PARSE_THE_CONFIG_FILE, thrown.getMessage());
    }

    @Test
    public void testInvalidWrongColorHexUnsupportedChar() {
        ConfigParser parser = assertDoesNotThrow(ConfigParser::getInstance);
        parser.setConfigProperties(loadInvalidWrongColorHexUnsupportedChar());
        Exception thrown = assertThrows(LifeException.class, parser::parseObjects);
        assertEquals(COULD_NOT_PARSE_THE_CONFIG_FILE, thrown.getMessage());
    }

    private Properties loadInvalidWrongColorHexUnsupportedChar() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, "#00X");
        return config;
    }

    private Properties loadInvalidWrongColorHexSeven() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, "#D3Abdg4");
        return config;
    }

    private Properties loadInvalidWrongColorHexTwo() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, "#D3");
        return config;
    }

    private Properties loadInvalidWrongColorHexFour() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, "#D3D3");
        return config;
    }

    private Properties loadInvalidWrongColorHexFive() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, "#D3D3D");
        return config;
    }

    private Properties loadInvalidMissingPropertyString() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, WOLF_COLOR);
        return config;
    }

    private Properties loadInvalidMissingPropertyNumber() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_COLOR_KEY, WOLF_COLOR);
        return config;
    }

    private Properties loadInvalidNumberValue() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, "a");
        config.put(WOLF_COLOR_KEY, WOLF_COLOR);
        return config;
    }

    private Properties loadInvalidTextValue() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, "\"carnivore\"");
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, WOLF_COLOR);
        return config;
    }

    private Properties loadInvalidConfigKey() {
        Properties config = new Properties();
        config.put("wolf.hello.world", CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, WOLF_COLOR);
        return config;
    }

    private Properties loadInvalidType() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, "test");
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, WOLF_COLOR);
        return config;
    }

    private Properties loadValidConfig() {
        Properties config = new Properties();
        config.put(WOLF_TYPE, CARNIVORE);
        config.put(WOLF_NAME, WOLF);
        config.put(WOLF_ENERGY_KEY, WOLF_ENERGY_VALUE);
        config.put(WOLF_COLOR_KEY, WOLF_COLOR);

        config.put("rabbit.type", "herbivore");
        config.put("rabbit.name", HASE);
        config.put("rabbit.energy", RABBIT_ENERGY);
        config.put("rabbit.color", RABBIT_COLOR);

        config.put("dandelion.type", "plant");
        config.put("dandelion.name", DANDELION);
        config.put("dandelion.color", DANDELION_COLOR);
        config.put("dandelion.energy", DANDELION_ENERGY);

        return config;
    }

    private List<GameObject> getExpectedValid() {
        List<GameObject> expected = new ArrayList<>();

        LifeForm bear = new Carnivore();
        bear.setName(WOLF);
        bear.setEnergy(WOLF_ENERGY_VALUE);
        bear.setColor(WOLF_COLOR);
        expected.add(bear);

        LifeForm rabbit = new Herbivore();
        rabbit.setName(HASE);
        rabbit.setEnergy(RABBIT_ENERGY);
        rabbit.setColor(RABBIT_COLOR);
        expected.add(rabbit);

        LifeForm dandelion = new Plant();
        dandelion.setName(DANDELION);
        dandelion.setEnergy(DANDELION_ENERGY);
        dandelion.setColor(DANDELION_COLOR);
        expected.add(dandelion);

        return expected;
    }

}
