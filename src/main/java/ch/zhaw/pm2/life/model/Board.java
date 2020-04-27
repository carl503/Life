package ch.zhaw.pm2.life.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This model class represents the board containing all the game objects.
 * @author lubojcar, meletlea
 */
public class Board {
    private final int rows;
    private final int columns;
//    List<LifeForm> lifeForms = new ArrayList<>();

    /**
     * Default constructor.
     * @param rows Number of rows as int.
     * @param columns Number of columns as int.
     */
    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

//    public void addLifeForm(LifeForm lifeForm) {
//        lifeForms.add(lifeForm);
//    }

    /**
     * Returns the number of rows.
     * @return rows as int.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns.
     * @return columns as int.
     */
    public int getColumns() {
        return columns;
    }

//    public List<LifeForm> getLifeForms() {
//        return lifeForms;
//    }
}
