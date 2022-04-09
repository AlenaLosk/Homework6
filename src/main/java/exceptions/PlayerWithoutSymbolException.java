package exceptions;

import model.Player;

public class PlayerWithoutSymbolException extends RuntimeException {

    public PlayerWithoutSymbolException(Player player) {
        super(String.format("The player '%s' hasn't any symbol!", player));
    }
}
