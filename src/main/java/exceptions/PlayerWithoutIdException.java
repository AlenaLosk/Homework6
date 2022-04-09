package exceptions;

import model.Player;

public class PlayerWithoutIdException extends RuntimeException {
    public PlayerWithoutIdException(Player player) {
        super(String.format("The player '%s' hasn't any id!", player));
    }
}
