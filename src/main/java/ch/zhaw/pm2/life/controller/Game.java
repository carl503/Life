package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.exception.LifeFormException;
import ch.zhaw.pm2.life.model.Board;
import ch.zhaw.pm2.life.model.GameObject;
import ch.zhaw.pm2.life.model.Position;
import ch.zhaw.pm2.life.model.lifeform.LifeForm;
import ch.zhaw.pm2.life.model.lifeform.animal.AnimalObject;
import ch.zhaw.pm2.life.model.lifeform.animal.MeatEater;
import ch.zhaw.pm2.life.model.lifeform.animal.PlantEater;
import ch.zhaw.pm2.life.model.lifeform.plant.FirstPlant;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This controller class handles the logic of the game.
 */
public class Game {

    /**
     * When a {@link GameObject} has no energy, it has this value.
     */
    public static final int ENERGY_VALUE_DEAD = -1;

    /**
     * When a next round is performed, the energy level of a plant drops by this amount.
     */
    public static final int PLANT_ENERGY_CONSUMPTION = 1;

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private Set<GameObject> deadLifeForms = new HashSet<>();
    private boolean ongoing = true;
    private Board board;
    private int plantCount;
    private int meatEaterCount;
    private int plantEaterCount;

    /**
     * Default constructor.
     * @param board Stores all game objects.
     * @param plantCount Initial amount of plants.
     * @param meatEaterCount Initial amount of meat eaters.
     * @param plantEaterCount Initial amount of plant eaters.
     */
    public Game(Board board, int plantCount, int meatEaterCount, int plantEaterCount) {
        this.board = Objects.requireNonNull(board, "Board cannot be null to create the game.");
        this.plantCount = plantCount;
        this.meatEaterCount = meatEaterCount;
        this.plantEaterCount = plantEaterCount;
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
    
    private void addLifeForm(Class<? extends LifeForm> lifeForm, int count) throws LifeFormException {
        try {
           for (int i = 0; i < count; i++) {
               LifeForm form = lifeForm.getConstructor().newInstance();
               board.addGameObject(form);
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
        if(ongoing) {
            Map<Position, Set<GameObject>> positionMap = new HashMap<>();
            messageLog += move(positionMap);
            messageLog += eat(positionMap);
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

    private String move(Map<Position, Set<GameObject>> positionMap) {
        StringBuilder stringBuilder = new StringBuilder();

        for(GameObject gameObject : board.getGameObjects()) {
            if(gameObject instanceof AnimalObject) {
                AnimalObject animalObject = (AnimalObject) gameObject;
                animalObject.move();
                dieOfExhaustion(stringBuilder, deadLifeForms, animalObject);
            } else {
                gameObject.decreaseEnergy(PLANT_ENERGY_CONSUMPTION);
                dieOfExhaustion(stringBuilder, deadLifeForms, gameObject);
            }
            if(!positionMap.containsKey(gameObject.getPosition())) {
                positionMap.put(gameObject.getPosition(), new HashSet<>());
            }
            positionMap.get(gameObject.getPosition()).add(gameObject);
        }
        board.getGameObjects().removeAll(deadLifeForms);

        return stringBuilder.toString();
    }

    private void dieOfExhaustion(StringBuilder stringBuilder, Set<GameObject> deadLifeForms, GameObject gameObject) {
        if (gameObject.getCurrentEnergy() == ENERGY_VALUE_DEAD) {
            deadLifeForms.add(gameObject);
            stringBuilder.append(gameObject.toString()).append(": died of exhaustion.\n");
        }
    }

    private String eat(Map<Position, Set<GameObject>> positionMap) {
        StringBuilder stringBuilder = new StringBuilder();

        for(GameObject gameObject : board.getGameObjects()) {
            if(gameObject instanceof AnimalObject) {
                AnimalObject animalObject = (AnimalObject) gameObject;
                Set<GameObject> set = positionMap.get(animalObject.getPosition());

                String eatMessage = handleCollision(set, animalObject, deadLifeForms);
                stringBuilder.append(eatMessage);

                set.removeAll(deadLifeForms);
            }
        }
        board.getGameObjects().removeAll(deadLifeForms);

        return stringBuilder.toString();
    }

    private String handleCollision(Set<GameObject> sameFieldSet, AnimalObject animalObject, Set<GameObject> deadLifeForms) {
        StringBuilder stringBuilder = new StringBuilder();

        sameFieldSet.stream()
                .filter(gameObject -> !gameObject.equals(animalObject))
                .filter(LifeForm.class::isInstance)
                .map(LifeForm.class::cast)
                .forEach(lifeForm -> {

            boolean isPlantEater = lifeForm instanceof PlantEater;
            boolean isMeatEater = lifeForm instanceof MeatEater;
            boolean isFoodEnergyLower = lifeForm.getCurrentEnergy() < animalObject.getCurrentEnergy();

            if (isPlantEater || (isMeatEater && isFoodEnergyLower)) {
                eatProcess(animalObject, deadLifeForms, stringBuilder, lifeForm);
            }
        });

        return stringBuilder.toString();
    }

    private void eatProcess(AnimalObject animalObject, Set<GameObject> deadLifeForms, StringBuilder stringBuilder, LifeForm lifeForm) {
        try {
            animalObject.eat(lifeForm);
            deadLifeForms.add(lifeForm);
            stringBuilder.append(animalObject.toString()).append(": Yummy food!\n");
        } catch (LifeFormException e) {
            stringBuilder.append(String.format("%s%n", e.getMessage()));
        }
    }

}
