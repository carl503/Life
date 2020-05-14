package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.Carnivore;
import ch.zhaw.pm2.life.model.lifeform.animal.Herbivore;
import ch.zhaw.pm2.life.model.lifeform.plant.Plant;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
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

    /**
     * When a {@link GameObject} has no energy, it has this value.
     */
    public static final int ENERGY_VALUE_DEAD = 0;

    /**
     * When a next round is performed, the energy level of a plant drops by this amount.
     */
    public static final int PLANT_ENERGY_CONSUMPTION = 1;

    /**
     * Respawn chance from 0 to 10 (0% - 100%)
     */
    public static final int PLANT_RESPAWN_CHANCE = 2;

    private static final Logger logger = Logger.getLogger(Game.class.getName());
    private static final int SCAN_RADIUS = 2;

    private final Set<GameObject> deadLifeForms = new HashSet<>();
    private final Random random = new Random();
    private final Board board;
    private final int plantCount;
    private final int carnivoreCount;
    private final int herbivoreCount;

    private boolean ongoing = true;

    /**
     * Default constructor.
     * @param board          Stores all game objects.
     * @param plantCount     Initial amount of plants.
     * @param carnivoreCount Initial amount of carnivores.
     * @param herbivoreCount Initial amount of herbivores.
     * @throws NullPointerException     when the board is null
     * @throws IllegalArgumentException when the plant, herbivore or carnivore count is negative or
     *                                  the sum of all counts is higher than the number of fields on the board
     */
    public Game(Board board, int plantCount, int carnivoreCount, int herbivoreCount) {
        this.board = Objects.requireNonNull(board, "Board cannot be null to create the game.");
        validateNumOfGameObjects(plantCount, "plants");
        validateNumOfGameObjects(carnivoreCount, "carnivores");
        validateNumOfGameObjects(herbivoreCount, "herbivores");
        validateNumOfGameObjects(plantCount + carnivoreCount + herbivoreCount, "game objects");
        this.plantCount = plantCount;
        this.carnivoreCount = carnivoreCount;
        this.herbivoreCount = herbivoreCount;
    }

    private void validateNumOfGameObjects(int num, String type) {
        if (num < 0) {
            throw new IllegalArgumentException(String.format("Number of %s is less than the minimal value.", type));
        } else if (num > board.getRows() * board.getColumns()) {
            throw new IllegalArgumentException(String.format("Number of %s exceed the number of available field.", type));
        }
    }

    /**
     * Initialize the game.
     */
    public void init() {
        try {
            addLifeForm(Plant.class, plantCount);
            addLifeForm(Carnivore.class, carnivoreCount);
            addLifeForm(Herbivore.class, herbivoreCount);
        } catch (LifeFormException e) {
            logger.log(Level.SEVERE, "Error while initializing the life form classes", e);
            stop();
        }
    }

    private void addLifeForm(Class<? extends LifeForm> lifeFormClass, int count) throws LifeFormException {
        try {
            for (int i = 0; i < count; i++) {
                LifeForm lifeForm = lifeFormClass.getConstructor().newInstance();
                board.addGameObject(lifeForm, calculatePosition());
            }
        } catch (NullPointerException | InstantiationException | InvocationTargetException | NoSuchMethodException
                | IllegalAccessException e) {
            throw new LifeFormException(e.getMessage(), e);
        }
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
        StringBuilder stringBuilder = new StringBuilder();
        ongoing = false;
        stringBuilder.append("The simulation has stopped because the ending condition was met");
        return stringBuilder.toString();
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
        if (ongoing && board.noAnimalExtinct()) {
            spawnPlantRandomlyOnMap();
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
                animalObject.move(board.getNeighbourObjects(animalObject, SCAN_RADIUS));
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
            return String.format("%s: died of exhaustion.%n", lifeForm.getClass().getSimpleName());
        }
        return message;
    }

    private String interact(Map<Vector2D, Set<GameObject>> positionMap) {
        Set<GameObject> newLifeForms = new HashSet<>();
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

        return stringBuilder.toString();
    }

    private String handleCollision(Set<GameObject> sameFieldSet, AnimalObject animalObject, Set<GameObject> newLifeForms) {
        StringBuilder stringBuilder = new StringBuilder();

        sameFieldSet.stream()
                .filter(not(animalObject::equals))
                .filter(LifeForm.class::isInstance)
                .map(LifeForm.class::cast)
                .forEach(lifeForm -> {

                    try {
                        if (!(lifeForm.getGender().equals(animalObject.getGender())) && lifeForm.getClass().equals(animalObject.getClass())) {
                            newLifeForms.add(animalObject.reproduce(lifeForm));
                            stringBuilder.append(animalObject.getClass().getSimpleName()).append(": We just reproduced with each other\n");
                        } else {
                            animalObject.eat(lifeForm);
                            deadLifeForms.add(lifeForm);
                            stringBuilder.append(animalObject.getClass().getSimpleName())
                                    .append(": Yummy food (")
                                    .append(lifeForm.getClass().getSimpleName())
                                    .append(")!\n");
                        }
                    } catch (LifeFormException | NullPointerException e) {
                        stringBuilder.append(String.format("%s%n", e.getMessage()));
                    }
                });

        return stringBuilder.toString();
    }

    private void spawnPlantRandomlyOnMap() {
        int spawnChance = random.nextInt(11);
        if (spawnChance < PLANT_RESPAWN_CHANCE) {
            board.addGameObject(new Plant(), calculatePosition());
        }
    }

}
