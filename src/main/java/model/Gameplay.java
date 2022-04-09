package model;
import utils.*;
import model.*;
import exceptions.*;
import service.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gameplay implements Serializable {
    @JsonIgnore
    private long id = 0;

    @JsonIgnore
    private long gameplayId = new Date().getTime();

    @JsonIgnore
    //@Transient
    private GameField gameField;

    @JsonProperty("model.Player")
    //@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Player[] players;

    @JsonProperty("model.Game")
    //@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Steps steps;

    @JsonProperty("model.GameResult")
    //@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private GameResult result;

    @JsonIgnore
    //@Transient
    private int stepCounter;

    @JsonIgnore
    //@Transient
    private Player currentPlayer;

    public Gameplay() {
        this.id = gameplayId;
        ++gameplayId;
    }

    public Gameplay(long id) {
        ++gameplayId;
        this.id = id;
        steps = new Steps(id, gameplayId, new ArrayList<Step>());
        result = new GameResult(id, gameplayId);
        stepCounter = 1;
        gameField = new GameField();
    }

    public void setPlayers(Player[] players) {
        for (Player player: players) {
            player.setId(id);
            player.setGameplayId(gameplayId);
        }
        this.players = players;
        currentPlayer = players[0];
    }

    public Player process(Player player, int cell) throws StepForAnotherPlayerException, NoFreeCellException {
        if (gameField == null) {
            gameField = new GameField();
        }
        if (GameLogic.hasFreeCell(gameField)) {
            if (this.currentPlayer.equals(player)) {
                Step step = GameLogic.putSymbol(gameField, currentPlayer, cell);
                if (step != null) {
                    step.setNum(stepCounter++);
                    steps.getStepsList().add(step);
                    if (GameLogic.isWin(gameField, currentPlayer)) {
                        result.setWinner(currentPlayer);
                    }
                    currentPlayer = changePlayer(currentPlayer);
                }
            } else {
                throw new StepForAnotherPlayerException();
            }
        } else {
            throw new NoFreeCellException();
        }
        return result.getWinner();
    }

    public Player changePlayer(Player player) {
        if (player.equals(players[0])) {
            return players[1];
        } else {
            return players[0];
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
