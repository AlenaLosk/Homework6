package model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Steps implements Serializable {

    @JsonIgnore
    //@Id
    private long id;

    @JsonIgnore
    private long gameplayId;

    @JsonProperty("model.Step")
    //@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Step> stepsList;

    public Steps() {
    }

    public Steps(long id, long gameplayId, List<Step> stepsList) {
        this.id = id;
        this.gameplayId = gameplayId;
        for (Step step: stepsList) {
            step.setId(id);
            step.setGameplayId(gameplayId);
        }
        this.stepsList = stepsList;
    }
}
