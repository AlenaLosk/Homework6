package utils;

import model.Game;
import model.Player;
import model.Step;
import utils.ConsoleHelper;

import java.util.List;

public class View {

    public static void refresh(String[][] symbols) {
        StringBuilder resultString;
        for (int i = 0; i < 3; i++) {
            resultString = new StringBuilder("|");
            for (int j = 0; j < 3; j++) {
                resultString.append(symbols[i][j]).append("|");
            }
            System.out.println(resultString);
        }
    }

    public static void refresh(int pause, String[][] symbols) {
        refresh(symbols);
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void replay(Game game) {
        String[][] gameField = {{"-", "-", "-"},
                {"-", "-", "-"},
                {"-", "-", "-"}};
        List<Step> steps = game.getGameplay().getSteps().getStepsList();
        Player[] players = game.getGameplay().getPlayers();
        Player winner = game.getGameplay().getResult().getWinner();
        if (players.length == 2) {
            System.out.println();
            System.out.printf("model.Player 1 -> %s as '%s'" +
                    System.lineSeparator(), players[0].getName(), players[0].getSymbol());
            System.out.printf("model.Player 2 -> %s as '%s'" +
                    System.lineSeparator(), players[1].getName(), players[1].getSymbol());
        } else {
            ConsoleHelper.printMessage("The file with game result doesn't include all players!");
        }
        String symbol = "";
        int cell;
        for (Step step : steps) {
            int playerId = step.getPlayerId();
            if (playerId == players[0].getNum()) {
                symbol = players[0].getSymbol();
            }
            if (playerId == players[1].getNum()) {
                symbol = players[1].getSymbol();
            }
            cell = (step.getCell() - 1);
            gameField[cell / 3][cell % 3] = symbol;
            refresh(800, gameField);
            System.out.println();
        }
        if (winner != null) {
            System.out.printf("model.Player %d -> %s is winner as '%s'!\n", winner.getNum(), winner.getName(), winner.getSymbol());
        } else {
            System.out.println("Drawn game!");
        }
    }
}
