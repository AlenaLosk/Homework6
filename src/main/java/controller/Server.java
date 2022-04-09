package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import utils.*;
import model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static spark.Spark.*;


public class Server {
    static {
        port(8080);
    }

    public Server() {
        final long[] lastGameplayId = new long[1];
        Writer writer1 = new JSONWriter();
        Writer writer2 = new XMLWriter();
        Writer writer3 = new DBWriter();
        Reader reader1 = new JSONReader();
        Reader reader2 = new DBReader();
        init();

        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Use any of the next request formats: for starting game and POST new players' name - http://localhost:8080/gameplay/new?name1=Ivan&name2=Maria;" +
                    " for POST step - http://localhost:8080/gameplay/step?player=1&cell=1;" +
                    " for GET winner - http://localhost:8080/gameplay/winner; for DELETE current game result - http://localhost:8080/gameplay" +
                    " for GET last game record - http://localhost:8080/gameplay\"}";
        });

        path("/gameplay", () -> {
            get("", (req, res) -> {
                res.type("application/json");
                res.status(200);
                try {
                    DBReader dBReader = new DBReader();
                    lastGameplayId[0] = dBReader.readLastGameplayId();
                    Game game = dBReader.read(Long.toString(lastGameplayId[0]));
                    Reader reader = new JSONReader();
                    String jsonGame = ((JSONReader) reader).read(game, true);
                    System.out.println(jsonGame);
                    return jsonGame;
                } catch (Exception ex) {
                    halt(500, "Convertation mistake on the server!");
                }
                return null;
            });

            get("/winner", (req, res) -> {
                res.type("application/json");
                res.status(200);
                Game game = new Game();
                Player winner = game.getGameplay().getResult().getWinner();
                if (winner != null) {
                    try {
                        return ((JSONReader) reader1).read(winner, true);
                    } catch (Exception ex) {
                        halt(500, "Convertation mistake on the server!");
                    }
                } else {
                    try {
                        lastGameplayId[0] = ((DBReader)reader2).readLastGameplayId();
                        GameResult gameResult = ((DBReader)reader2).readGameResult(Long.toString(lastGameplayId[0]));
                        System.out.println(gameResult.getWinner().toString());
                        return ((JSONReader) reader1).read(gameResult.getWinner(), true);
                    } catch (Exception ex) {
                        halt(500, "Convertation mistake on the server!");
                    }
                }
                return null;
            });

            delete("", (req, res) -> {
                res.type("application/json");
                Game game = new Game();
                if (game.getGameplay().getPlayers() != null) {
                    lastGameplayId[0] = game.getGameplay().getGameplayId();
                    res.status(200);
                    Game game1 = new Game();
                    game.setId(game1.getId());
                    game.setGameplay(new Gameplay(game1.getId()));
                    return "{\"message\":\"The current game has been deleted\"}";
                } else {
                    halt(406, "There is no any current game!");
                }
                return null;
            });

            post("/new", (req, res) -> {
                res.type("application/json");
                Game game = new Game();
                if (game.getGameplay().getPlayers() == null) {
                    String name1 = req.queryParams("name1");
                    String name2 = req.queryParams("name2");
                    if (name1 == null || name2 == null) {
                        halt(406, "Input names for both players!");
                    } else {
                        if (name1.equals(name2)) {
                            halt(406, "Don't repeat 1st player's name");
                        } else {
                            Player player1 = new Player(1, name1, "X");
                            Player player2 = new Player(2, name2, "O");
                            Player[] players = {player1, player2};
                            for (Player player : players) {
                                player.setId(game.getId());
                                player.setGameplayId(game.getGameplay().getGameplayId());
                            }
                            game.getGameplay().setPlayers(players);
                            res.status(200);
                            try {
                                ((DBWriter) writer3).writePlayers(game);
                                return ((JSONReader) reader1).read(players, true);
                            } catch (JsonProcessingException ex) {
                                halt(500, "Convertation mistake on the server!");
                            }
                        }
                    }
                } else {
                    halt(409, "The players are already exist! Use the request format - http://localhost:8080/gameplay/step?name=Ivan&cell=1");
                }
                return null;
            });

            post("/step", (req, res) -> {
                res.type("application/json");
                Game game = new Game();
                if (game.getGameplay().getPlayers() == null) {
                    halt(425, "Set the players' names! Use the request format - http://localhost:8080/gameplay/new?name1=Ivan&name2=Maria");
                } else {
                    int playerNum;
                    int cell;
                    try {
                        playerNum = Integer.parseInt(req.queryParams("player"));
                        cell = Integer.parseInt(req.queryParams("cell"));
                        Player currentPlayer = null;
                        for (Player player : game.getGameplay().getPlayers()) {
                            if (playerNum == player.getNum()) {
                                currentPlayer = player;
                            }
                        }
                        if (currentPlayer == null) {
                            halt(409, "Incorrect player's number in current game! Choose only 1 or 2.");
                        } else {
                            Player winner = null;
                            try {
                                winner = game.getGameplay().process(currentPlayer, cell);
                                List<Step> steps = game.getGameplay().getSteps().getStepsList();
                                ((DBWriter) writer3).writeStep(steps.get(steps.size() - 1));
                                if (winner != null) {
                                    writer1.write(game);
                                    writer2.write(game);
                                    ((DBWriter) writer3).writeGameResult(game);
                                    try {
                                        res.status(200);
                                        lastGameplayId[0] = game.getGameplay().getGameplayId();
                                        Player[] players = game.getGameplay().getPlayers();
                                        game.setGameplay(new Gameplay(game.getId()));
                                        game.getGameplay().setPlayers(players);
                                        return "[" + ((JSONReader) reader1).read(steps.get(steps.size() - 1), true) +
                                                ", " + ((JSONReader) reader1).read(winner, true) + "]";
                                    } catch (JsonProcessingException ex) {
                                        halt(500, "Convertation mistake on the server!");
                                    }
                                } else {
                                    try {
                                        res.status(200);
                                        return ((JSONReader) reader1).read(steps.get(steps.size() - 1), true);
                                    } catch (JsonProcessingException ex) {
                                        halt(500, "Convertation mistake on the server!");
                                    }
                                }
                            } catch (Exception ex) {
                                halt(409, ex.getMessage());
                            }
                        }
                    } catch (NumberFormatException ex) {
                        halt(415, "Incorrect format of cell's number!");
                    }
                }
                return null;
            });
        });
    }
}




