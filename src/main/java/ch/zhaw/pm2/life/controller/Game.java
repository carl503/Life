package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Vector2D;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.MeatEater;
import ch.zhaw.pm2.life.model.lifeform.animal.PlantEater;
import ch.zhaw.pm2.life.model.lifeform.plant.FirstPlant;
import ch.zhaw.pm2.life.model.lifeform.plant.PlantObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private final Set<GameObject> deadLifeForms = new HashSet<>();
    private boolean ongoing = true;
    private final Board board;
    private final int plantCount;
    private final int meatEaterCount;
    private final int plantEaterCount;

    /**
     * Default constructor.
     * @param board           Stores all game objects.
     * @param plantCount      Initial amount of plants.
     * @param meatEaterCount  Initial amount of meat eaters.
     * @param plantEaterCount Initial amount of plant eaters.
     */
    public Game(Board board, int plantCount, int meatEaterCount, int plantEaterCount) {
        this.board = Objects.requireNonNull(board, "Board cannot be null to create the game.");
        validateNumOfGameObjects(plantCount, "plants");
        validateNumOfGameObjects(meatEaterCount, "meat eaters");
        validateNumOfGameObjects(plantEaterCount, "plant eaters");
        validateNumOfGameObjects(plantCount + meatEaterCount + plantEaterCount, "game objects");
        this.plantCount = plantCount;
        this.meatEaterCount = meatEaterCount;
        this.plantEaterCount = plantEaterCount;
    }

    private void validateNumOfGameObjects(int num, String type) {
        if(num < 0) {
            throw new IllegalArgumentException(String.format("Number of %s is less than the minimal value.", type));
        } else if(num > board.getRows() * board.getColumns()) {
            throw new IllegalArgumentException(String.format("Number of %s exceed the number of available field.", type));
        }
    }

    /**
     * Initialize the game.
     */
    public void init() {
        try {
            addLifeForm(FirstPlant.class, plantCount);
            addLifeForm(MeatEater.class, meatEaterCount);
            addLifeForm(PlantEater.class, plantEaterCount);
        } catch (LifeFormException e) {
            LOGGER.log(Level.SEVERE, "Error while initializing the life form classes", e);
            stop();
        }
    }

    /**
     * Stop the game.
     */
    public void stop() {
        ongoing = false;
    }

    private void addLifeForm(Class<? extends LifeForm> lifeFormClass, int count) throws LifeFormException {
        try {
            for (int i = 0; i < count; i++) {
                LifeForm lifeForm = lifeFormClass.getConstructor().newInstance();

                if (board.getOccupiedPositions().contains(lifeForm.getPosition())) {
                    i--;
                } else {
                    board.addGameObject(lifeForm);
                }
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new LifeFormException(e.getMessage(), e);
        }
    }

    /**
     * Performs the next move.
     * It moves every {@link AnimalObject} and then trys to eat.
     * If then there are no more {@link MeatEater} or {@link PlantEater} then the simulation stops.
     * @return message log of every move and eat call.
     */
    public String nextMove() {
        String messageLog = "";
        if (ongoing) {
            Map<Vector2D, Set<GameObject>> positionMap = new HashMap<>();
            messageLog += move(positionMap);
            messageLog += interact(positionMap);
            if (board.containsNotInstanceOfAnimalObject(MeatEater.class) || board.containsNotInstanceOfAnimalObject(PlantEater.class)) {
                stop();
            }
        }
        return messageLog;
    }

    /**
     * Returns true if the game is ongoing otherwise false.
     * @return boolean
     */
    public boolean isOngoing() {
        return ongoing;
    }

    private String move(Map<Vector2D, Set<GameObject>> positionMap) {
        StringBuilder stringBuilder = new StringBuilder();

        for (GameObject gameObject : board.getGameObjects()) {
            if (gameObject instanceof LifeForm) {
                LifeForm lifeForm = (LifeForm) gameObject;
                if (lifeForm instanceof AnimalObject) {
                    AnimalObject animalObject = (AnimalObject) lifeForm;
                    animalObject.move();
                    dieOfExhaustion(stringBuilder, animalObject);
                } else if (lifeForm instanceof PlantObject) {
                    lifeForm.decreaseEnergy(PLANT_ENERGY_CONSUMPTION);
                    dieOfExhaustion(stringBuilder, lifeForm);
                }
            }
            if (!positionMap.containsKey(gameObject.getPosition())) {
                positionMap.put(gameObject.getPosition(), new HashSet<>());
            }
            positionMap.get(gameObject.getPosition()).add(gameObject);
        }
        board.cleanBoard();

        return stringBuilder.toString();
    }

    private void dieOfExhaustion(StringBuilder stringBuilder, LifeForm lifeForm) {
        if (lifeForm.getCurrentEnergy() < ENERGY_VALUE_DEAD) {
            lifeForm.die();
            stringBuilder.append(lifeForm.getClass().getSimpleName()).append(": died of exhaustion.\n");
        }
    }

    private String interact(Map<Vector2D, Set<GameObject>> positionMap) {
        Set<GameObject> newLifeForms = new HashSet<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (GameObject gameObject : board.getGameObjects()) {
            if (gameObject instanceof AnimalObject) {
                AnimalObject animalObject = (AnimalObject) gameObject;
                Set<GameObject> set = positionMap.get(animalObject.getPosition());

                String interactMessage = handleCollision(set, animalObject, newLifeForms);
                stringBuilder.append(interactMessage);

                set.removeAll(deadLifeForms);
            }
        }
        board.cleanBoard();
        board.getGameObjects().addAll(newLifeForms);

        return stringBuilder.toString();
    }

    private String handleCollision(Set<GameObject> sameFieldSet, AnimalObject animalObject, Set<GameObject> newLifeForms) {
        StringBuilder stringBuilder = new StringBuilder();

        sameFieldSet.stream()
                .filter(gameObject -> !gameObject.equals(animalObject))
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
                            stringBuilder.append(animalObject.getClass().getSimpleName()).append(": Yummy food!\n");
                        }
                    } catch (LifeFormException e) {
                        stringBuilder.append(String.format("%s%n", e.getMessage()));
                    }
                });

        return stringBuilder.toString();
    }

}
