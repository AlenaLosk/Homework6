package exceptions;

public class NoFreeCellException extends RuntimeException {

    public NoFreeCellException() {
        super("There is no free cell!");
    }
}
