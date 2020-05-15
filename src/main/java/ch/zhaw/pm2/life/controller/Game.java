package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.GameProperties;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.animal.Herbivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.function.Predicate.*;

/**
 * This controller class handles the logic of the game.
 */
public class Game {

    private static final int ENERGY_VALUE_DEAD = 0;
    private static final int PLANT_ENERGY_CONSUMPTION = 1;
    private static final int PLANT_RESPAWN_CHANCE = 2;

    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private final List<Plant> startPlants = new ArrayList<>();
    private final Set<LifeForm> startLifeForms = new HashSet<>();
    private final Set<LifeForm> bornLifeForms = new HashSet<>();
    private final Set<LifeForm> deadLifeForms = new HashSet<>();
    private final Set<LifeForm> spawnedLifeForms = new HashSet<>();
    private final Random random = new Random();
    private final Board board;
    private final GameProperties gameProperties;
    private boolean ongoing = true;

    /**
     * Default constructor.
     * @param board          Stores all game objects.
     * @throws NullPointerException     when the board is null
     * @throws IllegalArgumentException when the plant, herbivore or carnivore count is negative or
     *                                  the sum of all counts is higher than the number of fields on the board
     */
    public Game(Board board, GameProperties gameProperties) {
        this.board = Objects.requireNonNull(board, "Board cannot be null to create the game.");
        this.gameProperties = Objects.requireNonNull(gameProperties, "The game properties cannot be null.");
        addLifeForms();
    }

    private void validateNumOfGameObjects(int num, String type) {
        if (num < 0) {
            throw new IllegalArgumentException(String.format("Number of %s is less than the minimal value.", type));
        } else if (num > board.getRows() * board.getColumns()) {
            throw new IllegalArgumentException(String.format("Number of %s exceed the number of available field.", type));
        }
    }

    private void addLifeForms() {
        gameProperties.getInitGameObjects().forEach((gameObject, amount) -> {
            validateNumOfGameObjects(amount, gameObject.getName());
            for (int i = 0; i < amount; i++) {
                try {
                    GameObject go = gameObject.getClass().getConstructor().newInstance();
                    go.setName(gameObject.getName());
                    go.setEnergy(gameObject.getEnergy());
                    go.setColor(gameObject.getColor());
                    board.addGameObject(go, calculatePosition());
                    if(go instanceof LifeForm) {
                        startLifeForms.add((LifeForm) go);
                    }
                    if(go instanceof Plant) {
                        startPlants.add((Plant) go);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error while initializing the life form classes", e);
                    stop();
                }
            }
        });
    }

    private Vector2D calculatePosition() {
        Vector2D position;
        do {
            position = board.getRandomPosition();
        } while (board.getOccupiedPositions().contains(position));
        return position;
    }

    /**
     * Stops the game.
     */
    public String stop() {
        ongoing = false;
        return "The simulation has stopped because the ending condition was met";
    }

    /**
     * Returns true if the game is ongoing otherwise false.
     * @return boolean
     */
    public boolean isOngoing() {
        return ongoing;
    }

    /**
     * Performs the next move.
     * It moves every {@link AnimalObject} and then trys to eat.
     * If then there are no more {@link Carnivore} or {@link Herbivore} then the simulation stops.
     * @return message log of every move and eat call.
     */
    public String nextMove() {
        StringBuilder messageLog = new StringBuilder();
        if (ongoing && board.areAllSpeciesAlive()) {
            try {
                spawnPlantRandomlyOnMap();
            } catch (LifeFormException e) {
                messageLog.append(e.getMessage()).append("\n");
            }
            Map<Vector2D, Set<GameObject>> positionMap = new HashMap<>();
            messageLog.append(move(positionMap));
            messageLog.append(interact(positionMap));
        } else {
            messageLog.append(stop());
        }
        return messageLog.toString();
    }

    private String move(Map<Vector2D, Set<GameObject>> positionMap) {
        StringBuilder stringBuilder = new StringBuilder();

        for (GameObject gameObject : board.getGameObjects()) {
            if (gameObject instanceof AnimalObject) {
                AnimalObject animalObject = (AnimalObject) gameObject;
                animalObject.move(board.getNeighbourObjects(animalObject, animalObject.getScanRadius()));
            } else if (gameObject instanceof Plant) {
                gameObject.decreaseEnergy(PLANT_ENERGY_CONSUMPTION);
            }
            stringBuilder.append(dieOfExhaustion(gameObject));
            positionMap.putIfAbsent(gameObject.getPosition(), new HashSet<>());
            positionMap.get(gameObject.getPosition()).add(gameObject);
        }
        board.cleanBoard();

        return stringBuilder.toString();
    }

    private String dieOfExhaustion(GameObject gameObject) {
        String message = "";
        if (gameObject instanceof LifeForm && gameObject.getEnergy() < ENERGY_VALUE_DEAD) {
            LifeForm lifeForm = (LifeForm) gameObject;
            lifeForm.die();
            deadLifeForms.add(lifeForm);
            return String.format("%s: died of exhaustion.%n", gameObject.getName());
        }
        return message;
    }

    private String interact(Map<Vector2D, Set<GameObject>> positionMap) {
        Set<LifeForm> newLifeForms = new HashSet<>();
        StringBuilder stringBuilder = new StringBuilder();

        board.getGameObjects().stream()
                .filter(AnimalObject.class::isInstance)
                .map(AnimalObject.class::cast)
                .forEach(animalObject -> {
                    Set<GameObject> set = positionMap.get(animalObject.getPosition());
                    String interactMessage = handleCollision(set, animalObject, newLifeForms);
                    stringBuilder.append(interactMessage);
                    set.removeAll(deadLifeForms);
                });

        board.cleanBoard();
        newLifeForms.forEach(lifeForm -> board.addGameObject(lifeForm, lifeForm.getPosition()));
        bornLifeForms.addAll(newLifeForms);

        return stringBuilder.toString();
    }

    private String handleCollision(Set<GameObject> sameFieldSet, AnimalObject animalObject, Set<LifeForm> newLifeForms) {
        StringBuilder stringBuilder = new StringBuilder();

        sameFieldSet.stream()
                .filter(not(animalObject::equals))
                .filter(LifeForm.class::isInstance)
                .map(LifeForm.class::cast)
                .forEach(lifeForm -> {

                    try {
                        if (!(lifeForm.getGender().equals(animalObject.getGender())) && lifeForm.getClass().equals(animalObject.getClass())) {
                            newLifeForms.add(animalObject.reproduce(lifeForm));
                            stringBuilder.append(animalObject.getName()).append(": We just reproduced with each other\n");
                        } else {
                            animalObject.eat(lifeForm);
                            deadLifeForms.add(lifeForm);
                            stringBuilder.append(animalObject.getName())
                                    .append(": Yummy food (")
                                    .append(lifeForm.getName())
                                    .append(")!\n");
                        }
                    } catch (LifeFormException | NullPointerException e) {
                        stringBuilder.append(String.format("%s%n", e.getMessage()));
                    }
                });

        return stringBuilder.toString();
    }

    private void spawnPlantRandomlyOnMap() throws LifeFormException {
        try {
            int spawnChance = random.nextInt(11);
            if (spawnChance < PLANT_RESPAWN_CHANCE) {
                int randomPlantIndex = random.nextInt(startPlants.size());
                Plant originalPlant = startPlants.get(randomPlantIndex);

                Plant plant = originalPlant.getClass().getConstructor().newInstance();
                plant.setName(originalPlant.getName());
                plant.setColor(originalPlant.getColor());
                plant.setEnergy(gameProperties.getEnergyProperty(originalPlant.getName()).getValue());

                board.addGameObject(plant, calculatePosition());
                spawnedLifeForms.add(plant);
            }
        } catch (ReflectiveOperationException e) {
            throw new LifeFormException("Could not spawn new plant.");
        }
    }

    public Set<LifeForm> getStartLifeForms() {
        return startLifeForms;
    }

    public Set<LifeForm> getBornLifeForms() {
        return bornLifeForms;
    }

    public Set<LifeForm> getDeadLifeForms() {
        return deadLifeForms;
    }

    public Set<LifeForm> getSurvivedLifeForms() {
        return board.getLifeForms();
    }

    public Set<LifeForm> getSpawnedLifeForms() {
        return spawnedLifeForms;
    }

}
