package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Game;

import java.io.FileInputStream;
import java.io.IOException;

public class JSONReader implements Reader {
    @Override
    public Game read(String file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Game result = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            result = mapper.readValue(fileInputStream, Game.class);
        } catch (DatabindException | StreamReadException ignore) {
        }
        return result;
    }

    public <T> String read(T data, boolean isString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }
}
