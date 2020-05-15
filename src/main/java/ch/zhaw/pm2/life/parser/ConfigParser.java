package ch.zhaw.pm2.life.parser;

import ch.zhaw.pm2.life.exception.LifeException;
import ch.zhaw.pm2.life.model.GameObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Parses the config file for the simulation
 * and provides all found {@link GameObject}.
 */
public class ConfigParser {

    private static final String CONFIG_PATH = "config";
    private static final String FILE_NAME = "config.properties";
    private static final String DELIMITER = ".";
    private static final String DELIMITER_REGEX = "\\" + DELIMITER;
    private static final String LIFE_FORM_PACKAGE = "ch.zhaw.pm2.life.model.lifeform";
    private static final URL templateFile = ConfigParser.class.getClassLoader()
            .getResource(CONFIG_PATH + File.separator + FILE_NAME);
    private static final File configFile = new File(CONFIG_PATH + File.separator + FILE_NAME);
    private static ConfigParser instance = null;
    private Properties config = new Properties();

    private ConfigParser() throws LifeException {
        try {
            if (Files.notExists(configFile.toPath())) {
                copyConfig();
            }
            config.load(new FileReader(configFile));
        } catch (IOException | URISyntaxException e) {
            throw new LifeException(e);
        }
    }

    /**
     * Returns the instance of the {@link ConfigParser}.
     * @return ConfigParser reference.
     * @throws LifeException if the config file could not be copied
     *                       or if the URISyntax is wrong.
     */
    public static ConfigParser getInstance() throws LifeException {
        if (instance == null) {
            instance = new ConfigParser();
        }
        return instance;
    }

    /**
     * Parses the game objects out of a config file.
     * @return a {@link List<GameObject>} containing the parsed game objects.
     * @throws LifeException if the provided class was not found.
     */
    public List<GameObject> parseObjects() throws LifeException {
        List<GameObject> parsedObjects = new ArrayList<>();

        Set<String> lifeForms = new HashSet<>();
        Enumeration<Object> property = config.keys();
        while (property.hasMoreElements()) {
            lifeForms.add(property.nextElement().toString().split(DELIMITER_REGEX)[0]);
        }

        try {
            for (String lifeForm : lifeForms) {
                Class<?> clazz = getGameObjectClass(lifeForm);
                if (clazz == null) {
                    throw new LifeException("Could not parse the config file");
                }

                int energy = Integer.parseInt(getConfigValue(lifeForm, Options.ENERGY.name()));
                String color = getConfigValue(lifeForm, Options.COLOR.name());
                String name = getConfigValue(lifeForm, Options.NAME.name());

                // Valid color formats (Hex) #000 to #FFF or #000000 to #FFFFFF
                if (color.matches("#([A-Fa-f0-9]{3}){1,2}")) {
                    GameObject gameObject = (GameObject) clazz.getConstructor().newInstance();
                    gameObject.setColor(color);
                    gameObject.setEnergy(energy);
                    gameObject.setName(name);

                    parsedObjects.add(gameObject);
                } else {
                    throw new LifeException("Could not parse the config file");
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new LifeException(e);
        }

        return parsedObjects;
    }

    /**
     * Sets the config field of type {@link Properties}.
     * @param config of type {@link Properties}.
     */
    public void setConfigProperties(Properties config) {
        this.config = (Properties) config.clone();
    }

    private Class<?> getGameObjectClass(String lifeForm) throws ClassNotFoundException {
        Class<?> clazz = null;
        Type type = Type.getType(getConfigValue(lifeForm, Options.TYPE.name()));
        if (type == null) {
            return null;
        } else {
            switch (type) {
                case HERBIVORE:
                    clazz = Class.forName(Type.HERBIVORE.value);
                    break;
                case CARNIVORE:
                    clazz = Class.forName(Type.CARNIVORE.value);
                    break;
                case PLANT:
                    clazz = Class.forName(Type.PLANT.value);
                    break;
                default:
                    break;
            }
        }
        return clazz;
    }

    private String getConfigValue(String lifeForm, String property) {
        return config.get(String.join(DELIMITER, lifeForm, property.toLowerCase())).toString();
    }

    private void copyConfig() throws IOException, URISyntaxException {
        File configFolder = new File(CONFIG_PATH);
        File configFile = new File(configFolder + File.separator + FILE_NAME);
        if (Files.notExists(configFolder.toPath())) {
            Files.createDirectory(configFolder.toPath());
        } else if (Files.notExists(configFile.toPath())) {
            Files.copy(Path.of(templateFile.toURI()), configFile.toPath());
        }
    }

    private enum Options {
        ENERGY,
        TYPE,
        COLOR,
        NAME
    }

    private enum Type {
        CARNIVORE(LIFE_FORM_PACKAGE + ".animal.Carnivore"),
        HERBIVORE(LIFE_FORM_PACKAGE + ".animal.Herbivore"),
        PLANT(LIFE_FORM_PACKAGE + ".plant.Plant");

        private final String value;

        Type(final String v) {
            value = v;
        }

        /**
         * Takes a string and searches for the matching enum
         * @param val name of the class
         * @return Type
         */
        public static Type getType(String val) {
            for (Type type : Type.values()) {
                String[] values = type.value.split(DELIMITER_REGEX);
                if (values[values.length - 1].equalsIgnoreCase(val)) {
                    return type;
                }
            }
            return null;
        }

    }

}
