package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;

public class Game {
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
        //todo
    }

    public void stop() {
        ongoing = false;
    }

    private void collision() {
        //todo
    }

}
