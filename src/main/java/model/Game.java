package model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.security.SecureRandom;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game implements Serializable {

    @JsonIgnore
    private long id = new SecureRandom().nextLong(20000000L);

    @JsonProperty("model.Gameplay")
    private Gameplay gameplay;

    public Game() {
        this.gameplay = new Gameplay(id);
    }
}
