package ch.zhaw.pm2.life.controller;

import ch.zhaw.pm2.life.model.Board;

public class Game {
    private Board board;
    private boolean ongoing = true;

    public Game(Board board) {
        this.board = board;
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
