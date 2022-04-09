package model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class GameResult implements Serializable {

    @JsonIgnore
    @Id
    private long id;

    @JsonIgnore
    private long gameplayId;

    @JsonProperty("model.Player")
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Player winner;

    public GameResult(long id, long gameplayId) {
        this.gameplayId = gameplayId;
        this.id = id;
    }

    public GameResult() {
    }
}
