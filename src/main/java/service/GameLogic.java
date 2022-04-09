package service;

import exceptions.CellOutOfBoundsException;
import exceptions.OccupiedCellException;
import exceptions.PlayerWithoutIdException;
import exceptions.PlayerWithoutSymbolException;
import model.GameField;
import model.Player;
import model.Step;

public class GameLogic {

    public static Step putSymbol(GameField gameField, Player player, int cell) throws CellOutOfBoundsException,
            OccupiedCellException, PlayerWithoutSymbolException, PlayerWithoutIdException {
        int x = 0;
        Step step = null;
        x = cell - 1;
        if (x < 0 || x > 8) {
            throw new CellOutOfBoundsException(cell); // ячейка находится за границами игрового поля
        }
        String[][] item = gameField.getItem();
        if (item[x / 3][x % 3].equals("-")) {
            if (player.getSymbol() != null) {
                item[x / 3][x % 3] = player.getSymbol();
            } else {
                throw new PlayerWithoutSymbolException(player); //игроку не назначен символ
            }
            if (player.getNum() != 0) {
                step = new Step(player.getId(), player.getGameplayId(), player.getNum(), x + 1);
            } else {
                throw new PlayerWithoutIdException(player); //игроку не назначен id
            }
        } else {
            throw new OccupiedCellException(cell); // ячейка уже занята ранее установленным символов
        }
        return step;
    }

    public static boolean hasFreeCell(GameField gameField) {
        String[][] item = gameField.getItem();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (item[i][j].equals("-")) return true;
            }
        }
        return false;
    }

    public static boolean isWin(GameField gameField, Player player) throws PlayerWithoutSymbolException {
        String symbol = null;
        if (player.getSymbol() != null) {
            symbol = player.getSymbol();
        } else {
            throw new PlayerWithoutSymbolException(player); //игроку не назначен символ
        }
        String[][] item = gameField.getItem();
        for (int i = 0; i < 3; i++) {
            if ((item[i][0].equals(symbol) && item[i][1].equals(symbol) && item[i][2].equals(symbol)) ||
                    (item[0][i].equals(symbol) && item[1][i].equals(symbol) && item[2][i].equals(symbol))) {
                return true;
            }

            if (item[0][0].equals(symbol) && item[1][1].equals(symbol) && item[2][2].equals(symbol))
                return true;
            if (item[0][2].equals(symbol) && item[1][1].equals(symbol) && item[2][0].equals(symbol))
                return true;
        }
        return false;
    }
}
