package model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonIgnore
    private long gameplayId;

    @JsonProperty("_num")
    private int num;

    @JsonProperty("_playerId")
    private int playerId;

    @JsonProperty("_text")
    private int cell;

    public Step() {
    }

    public Step(long id, long gameplayId, int num, int playerId, int cell) {
        this.id = id;
        this.gameplayId = gameplayId;
        this.num = num;
        this.playerId = playerId;
        this.cell = cell;
    }

    public Step(long id, long gameplayId, int playerId, int cell) {
        this.id = id;
        this.gameplayId = gameplayId;
        this.num = 0;
        this.playerId = playerId;
        this.cell = cell;
    }

    @Override
    public String toString() {
        return "model.Step{" +
                "num=" + num +
                ", playerId='" + playerId + '\'' +
                ", symbol='" + cell + '\'' +
                '}';
    }
}
