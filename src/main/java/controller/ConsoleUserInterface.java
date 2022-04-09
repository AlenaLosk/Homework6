package controller;
import utils.*;
import model.*;
import exceptions.*;
import java.io.IOException;

public class ConsoleUserInterface {
    public static void start() {
        ConsoleHelper.printMessage("Welcome to game 'TicTacToe' menu!", true);
        int menuPoint = 0;
        Game game = null;
        long lastGameplayId = 0L;
        while (menuPoint == 0 || menuPoint == 1 || menuPoint == 2 || menuPoint == 3) {
            menuPoint = askMenuPoint();
            switch (menuPoint) {
                case 1:
                    game = new Game();
                    Writer writer1 = new JSONWriter();
                    Writer writer2 = new XMLWriter();
                    Writer writer3 = new DBWriter();
                    boolean willGameContinue = true;
                    Player[] players = askPlayersNames();
                    for (Player player : players) {
                        player.setId(game.getId());
                        player.setGameplayId(game.getGameplay().getGameplayId());
                    }
                    game.getGameplay().setPlayers(players);
                    while (willGameContinue) {
                        Player winner = game.getGameplay().getResult().getWinner();
                        do {
                            int cell = askCell(game.getGameplay().getCurrentPlayer());
                            try {
                                winner = game.getGameplay().process(game.getGameplay().getCurrentPlayer(), cell);
                                View.refresh(game.getGameplay().getGameField().getItem());
                            } catch (NoFreeCellException e) {
                                break;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } while (winner == null);
                        if (winner == null) {
                            ConsoleHelper.printMessage("Drawn game!", true);
                        } else {
                            ConsoleHelper.printMessage(String.format("Winner is %s!", winner.getName()), true);
                        }
                        try {
                            writer1.write(game);
                            writer2.write(game);
                            ((DBWriter) writer3).write(game);
                            lastGameplayId = game.getGameplay().getGameplayId();
                        } catch (IOException e) {
                            ConsoleHelper.printMessage("The file for writing game steps wasn't found!" + System.lineSeparator(), true);
                        }
                        willGameContinue = askGameGoingOn();
                        if (willGameContinue) {
                            players = game.getGameplay().getPlayers();
                            long gameId = game.getId();
                            game = new Game();
                            game.setId(gameId);
                            game.getGameplay().setPlayers(players);
                        }
                    }
                    break;
                case 2:
                    try {
                        Reader reader = new DBReader();
                        if (lastGameplayId == 0L) {
                            lastGameplayId = ((DBReader) reader).readLastGameplayId();
                        }
                        Game game1 = reader.read(Long.toString(lastGameplayId));
                        View.replay(game1);
                    } catch (Exception ex) {
                        ConsoleHelper.printMessage("The file with game steps wasn't found!", true);
                    }
                    menuPoint = 0;
                    break;
                case 3:
                    ConsoleHelper.printMessage("Menu is closed.");
                    menuPoint = 4;
                    ConsoleHelper.close();
                    HibernateUtil.shutdown();
                    break;
                default:
                    break;
            }
        }
    }

    private static int askMenuPoint() {
        int menuPoint = 0;
        while (menuPoint != 1 && menuPoint != 2 && menuPoint != 3) {
            ConsoleHelper.printMessage("Enter '1', if you want to play TicTacToe,"
                    + System.lineSeparator() + "Enter '2' - to see previous game"
                     + System.lineSeparator() + "Enter '3', if you want to exit: ");
            String temp = ConsoleHelper.readMessage();
            if (temp.equals("1") || temp.equals("2") || temp.equals("3")) {
                menuPoint = Integer.parseInt(temp);
            } else {
                ConsoleHelper.printMessage("Invalid input! Please, try again.", true);
            }
        }
        return menuPoint;
    }

    private static Player[] askPlayersNames() {
        Player[] result = new Player[2];
        ConsoleHelper.printMessage("Enter 1st player's name: ");
        String name = "";
        while (name.isEmpty()) {
            name = ConsoleHelper.readMessage();
        }
        result[0] = new Player(1, name, "X");
        name = "";
        ConsoleHelper.printMessage("Enter 2nd player's name: ");
        while (name.isEmpty()) {
            name = ConsoleHelper.readMessage();
            if (name.equals(result[0].getName())) {
                ConsoleHelper.printMessage("Don't repeat 1st player's name and enter 2nd player's name again: ");
                name = "";
            }
        }
        result[1] = new Player(2, name, "O");
        return result;
    }

    private static int askCell(Player player) {
        int cell = -1;
        ConsoleHelper.printMessage(String.format("%dst player is moving", player.getNum()), true);
        int cells = GameField.getSIZE();
        while (cell < 0) {
            ConsoleHelper.printMessage(String.format("Enter the number of the cell (from 1 to %s): ", cells));
            String input = null;
            try {
                input = ConsoleHelper.readMessage();
                cell = Integer.parseInt(input);
                if (cell < 1 || cell > 9) {
                    ConsoleHelper.printMessage(String.format("The cell #%d is out of game field range with %d cells!", cell, cells), true);
                    cell = -1;
                }
            } catch (NumberFormatException ex) {
                ConsoleHelper.printMessage(String.format("The string '%s' wasn't recognized as digits!", input), true);
            }
        }
        return cell;
    }

    private static boolean askGameGoingOn() {
        ConsoleHelper.printMessage("Do you want to play again?", true);
        ConsoleHelper.printMessage("If no, enter '1'. For continue playing enter any symbol: ");
        String wantToPlay = ConsoleHelper.readMessage();
        if (wantToPlay.equals("1")) {
            return false;
        }
        return true;
    }
}
