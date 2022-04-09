package utils;

import com.google.gson.Gson;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ApiTestUtils {
    public static TestResponse request(String method, String path, String requestBody) {
        int code = 400;
        HttpURLConnection connection;
        String body;
        String temp;
        try {
            URL url = new URL("http://localhost:8080" + path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            code = connection.getResponseCode();
            try {
                body = IOUtils.toString(connection.getInputStream());
                return new TestResponse(code, body);
            } catch (IOException e) {
                temp = e.getMessage();
                return new TestResponse(code, temp);
            }
        } catch (Exception e) {
            temp = e.getMessage();
        } return new TestResponse(code, temp);
    }

    public static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public Map<String, String> json() {
            return new Gson().fromJson(body, HashMap.class);
        }
    }
}
