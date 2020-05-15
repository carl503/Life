package ch.zhaw.pm2.life.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores initial values for the game.
 */
public class GameProperties {

    private final Map<GameObject, Integer> initGameObjects;

    private final Map<String, IntegerProperty> energyProperties = new HashMap<>();

    /**
     * Default constructor.
     * @param initGameObjects map of {@link GameObject} with initial amount.
     */
    public GameProperties(Map<GameObject, Integer> initGameObjects) {
        this.initGameObjects = Objects.requireNonNull(initGameObjects, "The map with the init game objects cannot be null.");
        initGameObjects.forEach((gameObject, integer) ->
                                        energyProperties.putIfAbsent(gameObject.getName(),
                                                                     new SimpleIntegerProperty(integer)));
    }

    /**
     * Returns the energy property for a given species.
     * @param species String.
     * @return IntegerProperty
     */
    public IntegerProperty getEnergyProperty(String species) {
        return energyProperties.get(species);
    }

    /**
     * Returns map of {@link GameObject} with initial amount.
     * @return Map.
     */
    public Map<GameObject, Integer> getInitGameObjects() {
        return initGameObjects;
    }

}
