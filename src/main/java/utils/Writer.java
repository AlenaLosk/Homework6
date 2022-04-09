package utils;

import model.Game;

import java.io.IOException;

public interface Writer {
    void write(Game game, String resource) throws IOException;

}
