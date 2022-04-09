package exceptions;

public class OccupiedCellException extends RuntimeException{

    public OccupiedCellException(int cell) {
        super(String.format("The cell #%d isn't free!", cell));
    }
}
