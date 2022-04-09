package exceptions;

public class StepForAnotherPlayerException extends RuntimeException {

    public StepForAnotherPlayerException() {
        super("This is a step for another player!");
    }
}
