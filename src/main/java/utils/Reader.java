package utils;

import model.Game;

import java.io.IOException;

public interface Reader {

    Game read(String resource) throws IOException;

}
