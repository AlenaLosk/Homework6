package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import utils.*;
import model.*;
import exceptions.*;
import java.util.*;

import static spark.Spark.*;


public class Server {
    static {
        port(8080);
    }

    public Server() {
        Game game = new Game();
        final long[] lastGameplayId = new long[1];
        lastGameplayId[0] = game.getGameplay().getGameplayId();
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
                    lastGameplayId[0] = ((DBReader)reader2).readLastGameplayId();
                    System.out.println(lastGameplayId[0]);
                    Game game1 = reader2.read(Long.toString(lastGameplayId[0]));
                    System.out.println(game1.toString());
                    return ((JSONWriter) writer1).write(reader2.read(Long.toString(lastGameplayId[0])));
                } catch (Exception ex) {
                    halt(500, "Convertation mistake on the server!");
                }
                return null;
            });

            get("/winner", (req, res) -> {
                res.type("application/json");
                res.status(200);
                Player winner = game.getGameplay().getResult().getWinner();
                if (winner != null) {
                    try {
                        return ((JSONWriter) writer1).write(winner);
                    } catch (Exception ex) {
                        halt(500, "Convertation mistake on the server!");
                    }
                } else {
                    try {
                        lastGameplayId[0] = ((DBReader)reader2).readLastGameplayId();
                        GameResult gameResult = ((DBReader)reader2).readGameResult(Long.toString(lastGameplayId[0]));
                        System.out.println(gameResult.getWinner().toString());
                        return ((JSONWriter) writer1).write(gameResult.getWinner());
                    } catch (Exception ex) {
                        halt(500, "Convertation mistake on the server!");
                    }
                }
                return null;
            });

            delete("", (req, res) -> {
                res.type("application/json");
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
                                ((DBWriter) writer3).writePlayers(game, "src\\main\\resources\\hibernate.cfg.xml");
                                return ((JSONWriter) writer1).write(players);
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
                                ((DBWriter) writer3).writeStep(steps.get(steps.size() - 1), "jdbc:h2:tcp://localhost:8080/homework6_db");
                                if (winner != null) {
                                    writer1.write(game, "src/main/resources/gameplay.json");
                                    writer2.write(game, "src/main/resources/gameplay.xml");
                                    ((DBWriter) writer3).writeGameResult(game, "src\\main\\resources\\hibernate.cfg.xml");
                                    try {
                                        res.status(200);
                                        lastGameplayId[0] = game.getGameplay().getGameplayId();
                                        Player[] players = game.getGameplay().getPlayers();
                                        game.setGameplay(new Gameplay(game.getId()));
                                        game.getGameplay().setPlayers(players);
                                        return "[" + ((JSONWriter) writer1).write(steps.get(steps.size() - 1)) +
                                                ", " + ((JSONWriter) writer1).write(winner) + "]";
                                    } catch (JsonProcessingException ex) {
                                        halt(500, "Convertation mistake on the server!");
                                    }
                                } else {
                                    try {
                                        res.status(200);
                                        return ((JSONWriter) writer1).write(steps.get(steps.size() - 1));
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




