package ch.zhaw.pm2.life.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameProperties {

    private final Map<GameObject, Integer> initGameObjects;

    private final Map<String, IntegerProperty> energyProperties = new HashMap<>();

    public GameProperties(Map<GameObject, Integer> initGameObjects) {
        this.initGameObjects = Objects.requireNonNull(initGameObjects, "The map with the init game objects cannot be null.");
        initGameObjects.forEach((gameObject, integer) ->
                                        energyProperties.putIfAbsent(gameObject.getName(),
                                                                     new SimpleIntegerProperty(integer)));
    }

    public IntegerProperty getEnergyProperty(String species) {
        return energyProperties.get(species);
    }

    public Map<GameObject, Integer> getInitGameObjects() {
        return initGameObjects;
    }

}
