package ch.zhaw.pm2.life.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows;
    private final int columns;
//    List<LifeForm> lifeForms = new ArrayList<>();

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

//    public void addLifeForm(LifeForm lifeForm) {
//        lifeForms.add(lifeForm);
//    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

//    public List<LifeForm> getLifeForms() {
//        return lifeForms;
//    }
}
