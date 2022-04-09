package model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Player implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    private long gameplayId;

    @JsonProperty("_id")
    private int num;

    @JsonProperty("_name")
    private String name;

    @JsonProperty("_symbol")
    private String symbol;

    public Player() {
    }

    public Player(long id, long gameplayId, int num, String name, String symbol) {
        this.id = id;
        this.gameplayId = gameplayId;
        this.num = num;
        this.name = name;
        this.symbol = symbol;
    }

    public Player(int num, String name, String symbol) {
        this.num = num;
        this.name = name;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "\"Player\": {\n" +
                "\"_id\" : " + num +
                ",\n \"_name\" : \"" + name + "\"" +
                ",\n \"_symbol\" : \"" + symbol + "\"" +
                "\n}";
    }

}
