import controller.ConsoleUserInterface;
import controller.Server;
import model.Game;
import spark.Spark;
import utils.ApiTestUtils;
import utils.HibernateUtil;
import utils.JSONReader;
import utils.JSONWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {
    public static void main(String[] args) throws Exception{
        //new Server(); // использовать для запуска серверного варианта
        //ConsoleUserInterface.start(); // использовать для работы с консольным вариантом (реализовано хранение в БД через Hibernate)
        Game game = new JSONReader().read("src/main/resources/gameplay.json");
        String game1 = new JSONReader().read(game, true);
        Server server = new Server();
        String testUrl = "/gameplay";
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", testUrl, null);
        try {
            assertEquals(200, res.status);
            assertEquals(game, res.body);
        } catch (Exception ignored) {
        } finally {
            Spark.stop();
            HibernateUtil.shutdown();
        }
    }
}
