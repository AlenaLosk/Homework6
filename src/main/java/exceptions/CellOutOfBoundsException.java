package exceptions;

import model.*;

public class CellOutOfBoundsException extends RuntimeException {

    public CellOutOfBoundsException(int cell) {
        super(String.format("The cell #%d is out of game field range with %d cells!", cell, GameField.getSIZE()));
    }
}
