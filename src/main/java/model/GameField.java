package model;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;

public class GameField implements Serializable {
    @Getter
    @Setter
    private String[][] item = new String[DIMENSION][DIMENSION];

    @Getter
    private static final int DIMENSION = 3;

    @Getter()
    private static final int SIZE = DIMENSION * DIMENSION;

    public GameField() {
        item = Arrays.stream(item).map(s -> Arrays.stream(s).map(e -> "-").toArray(String[]::new)).toArray(String[][]::new);
    }

}
