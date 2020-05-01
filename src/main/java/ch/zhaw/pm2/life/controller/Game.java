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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    private Logger logger = Logger.getLogger(Game.class.getName());

    private boolean ongoing = true;
    private Board board;
    private int plantCount;
    private int meatEaterCount;
    private int plantEaterCount;

    public Game(Board board, int plantCount, int meatEaterCount, int plantEaterCount) {
        this.board = board;
        this.plantCount = plantCount;
        this.meatEaterCount = meatEaterCount;
        this.plantEaterCount = plantEaterCount;
    }

    public void init() {
        try {
            addLifeform(FirstPlant.class, plantCount);
            addLifeform(MeatEater.class, meatEaterCount);
            addLifeform(PlantEater.class, plantEaterCount);
        } catch (LifeFormException e) {
            logger.log(Level.SEVERE, "Error while initializing the life form classes", e);
            stop();
        }
    }

    public void stop() {
        ongoing = false;
    }

    private void collision() {
        //todo
    }

    private void addLifeform(Class<? extends LifeForm> lifeForm, int count) throws LifeFormException {
        try {
           for (int i = 0; i < count; i++) {
               LifeForm form = lifeForm.getConstructor().newInstance();
               board.addGameObject(form);
           }
       } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new LifeFormException(e.getMessage(), e);
        }
    }


    public void nextMove() throws LifeFormException {
        Map<Position, Set<GameObject>> positionMap = new HashMap<>();
        for(GameObject gameObject : board.getGameObjects()) {
            if(gameObject instanceof AnimalObject) {
                AnimalObject animalObject = (AnimalObject) gameObject;
                animalObject.move();
            }
            if(!positionMap.containsKey(gameObject.getPosition())) {
                positionMap.put(gameObject.getPosition(), new HashSet<>());
            }
            positionMap.get(gameObject.getPosition()).add(gameObject);
        }

        Set<GameObject> deadLifeForms = new HashSet<>();
        for(GameObject gameObject : board.getGameObjects()) {
            if(gameObject instanceof AnimalObject) {

                AnimalObject animalObject = (AnimalObject) gameObject;
                Set<GameObject> set = positionMap.get(animalObject.getPosition());
                handleCollision(set, animalObject, deadLifeForms);
                set.removeAll(deadLifeForms);
/*
                if(!animalObject.hasEnergy()) {
                    animalObject.setCurrentEnergy(0);
                }
*/
            }
        }
        board.getGameObjects().removeAll(deadLifeForms);
    }

    private void handleCollision(Set<GameObject> sameFieldSet, AnimalObject animalObject, Set<GameObject> deadLifeForms) throws LifeFormException {
        StringBuilder stringBuilder = new StringBuilder();
        for (GameObject gameObject : sameFieldSet) {
            if (gameObject.equals(animalObject)) {
                continue;
            }
            if (gameObject instanceof LifeForm) {
                LifeForm lifeForm = (LifeForm) gameObject;
                if (animalObject instanceof PlantEater) {
                    processEat(animalObject, deadLifeForms, stringBuilder, lifeForm);
                } else {
                    if(lifeForm instanceof PlantEater || (lifeForm instanceof MeatEater && lifeForm.getCurrentEnergy() < animalObject.getCurrentEnergy())) {
                        processEat(animalObject, deadLifeForms, stringBuilder, lifeForm);
                    }
                }
            }
        }
    }

    private void processEat(AnimalObject animalObject, Set<GameObject> deadLifeForms, StringBuilder stringBuilder, LifeForm lifeForm) {
        try {
            animalObject.eat(lifeForm);
            deadLifeForms.add(lifeForm);
            stringBuilder.append(animalObject.toString()).append(": Yummy food!\n");
        } catch (LifeFormException e) {
            stringBuilder.append(String.format("%s%n", e.getMessage()));
        }
    }
}
