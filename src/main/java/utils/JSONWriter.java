package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Game;

import java.io.FileOutputStream;
import java.io.IOException;

public class JSONWriter implements Writer {
    @Override
    public void write(Game game, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (FileOutputStream stream = new FileOutputStream(fileName)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(stream, game);
        } catch (StreamWriteException | DatabindException ignore) {
        }
    }

    public <T> String write(T data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }
}
