import antlr.GrammarAnalyzer;
import com.fasterxml.jackson.core.JsonProcessingException;
import controller.Server;
import model.Game;
import model.Player;
import model.Step;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import spark.Spark;
import utils.ApiTestUtils;
import utils.JSONReader;
import utils.JSONWriter;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ServerTest {
    @BeforeAll
    public static void setUp() throws Exception {
        Server server = new Server();
        Spark.awaitInitialization();
    }

    @AfterAll
    public static void tearDown() {
        Spark.stop();
    }

    @Test
    @Order(1)
    public void test1_GetPreviousGame() throws IOException {
        Server server = new Server();
        String testUrl = "/gameplay";
        Game game = new JSONReader().read("src/main/resources/gameplay.json");
        String game1 = new JSONReader().read(game, true);
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(game, res.body);
        Spark.stop();
    }

    @Test
    @Order(2)
    public void test2_GetPreviousWinner() throws IOException {
        Spark.awaitInitialization();
        String testUrl = "/gameplay/winner";
        String winner = new JSONReader().read("src/main/resources/gameplay.json").getGameplay().getResult().getWinner().toString();
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(winner, res.body);
        Spark.stop();
    }

    @Test
    @Order(3)
    public void test3_play_one_step_and_delete() throws JsonProcessingException {
        Spark.awaitInitialization();
        String testUrl = "/gameplay/new?name1=Ivan&name2=Maria";
        Player[] players = {new Player(1, "Ivan", "X"), new Player(2, "Maria", "O")};
        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(players, true), res.body);

        testUrl = "/gameplay/step?player=1&cell=1";
        Step step = new Step(1L, 1L,1, 1, 1);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(step, true), res.body);

        testUrl = "/gameplay";
        res = ApiTestUtils.request("DELETE", testUrl, null);
        assertEquals(200, res.status);
        assertEquals("{\"message\":\"The current game has been deleted\"}", res.body);
        Spark.stop();
    }

    @Test
    @Order(4)
    public void test4_play_2_similar_steps_and_delete() throws JsonProcessingException {
        Spark.awaitInitialization();
        String testUrl = "/gameplay/new?name1=Oleg&name2=Anna";
        Player[] players = {new Player(1, "Oleg", "X"), new Player(2, "Anna", "O")};
        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(players, true), res.body);

        testUrl = "/gameplay/step?player=1&cell=1";
        Step step = new Step(1L, 14564646999L, 1, 1);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(step, true), res.body);

        testUrl = "/gameplay/step?player=1&cell=2";
        step = new Step(14564646999L, 1L, 2, 1, 2);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(409, res.status);
        assertNotEquals(new JSONReader().read(step, true), res.body);

        testUrl = "/gameplay";
        res = ApiTestUtils.request("DELETE", testUrl, null);
        assertEquals(200, res.status);
        assertEquals("{\"message\":\"The current game has been deleted\"}", res.body);
        Spark.stop();
    }

    @Test
    @Order(5)
    public void test5_play_5_steps_and_delete() throws JsonProcessingException {
        Spark.awaitInitialization();
        String testUrl = "/gameplay/new?name1=Oleg&name2=Anna";
        Player[] players = {new Player(1, "Oleg", "X"), new Player(2, "Anna", "O")};
        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(players, true), res.body);

        testUrl = "/gameplay/step?player=1&cell=1";
        Step step = new Step(1L, 1L,1, 1, 1);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(step, true), res.body);

        testUrl = "/gameplay/step?player=2&cell=2";
        step = new Step(1L, 1L,2, 2, 2);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(step, true), res.body);

        testUrl = "/gameplay/step?player=1&cell=4";
        step = new Step(1L, 1L,3, 1, 4);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(step, true), res.body);

        testUrl = "/gameplay/step?player=2&cell=5";
        step = new Step(1L, 1L,4, 2, 5);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals(new JSONReader().read(step, true), res.body);

        testUrl = "/gameplay/step?player=1&cell=7";
        String step1 = new JSONReader().read(new Step(1L, 1L,5, 1, 7), true);
        String winner =new JSONReader().read(new Player(1, "Oleg", "X"), true);
        res = ApiTestUtils.request("POST", testUrl, null);
        assertEquals(200, res.status);
        assertEquals("[" + step1 + ", " + winner + "]", res.body);

        testUrl = "/gameplay";
        res = ApiTestUtils.request("DELETE", testUrl, null);
        assertEquals(200, res.status);
        assertEquals("{\"message\":\"The current game has been deleted\"}", res.body);
        Spark.stop();
    }

    @Test
    @Order(6)
    public void test6_DeleteCurrentGame() {
        Spark.awaitInitialization();
        String testUrl = "/gameplay";
        ApiTestUtils.TestResponse res = ApiTestUtils.request("DELETE", testUrl, null);
        assertEquals(406, res.status);
        assertNotEquals("{\"message\":\"The current game has been deleted\"}", res.body);
        Spark.stop();
    }

    @Test
    @Order(7)
    public void test7_play_5_steps() throws JsonProcessingException {
        Spark.awaitInitialization();
        String testUrl = "/gameplay/new?name1=Жучка&name2=Стрелка";
        Player[] players = {new Player(1, "Жучка", "X"), new Player(2, "Стрелка", "O")};
        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);

        testUrl = "/gameplay/step?player=1&cell=1";
        Step step = new Step(1L, 1L,1, 1, 1);
        res = ApiTestUtils.request("POST", testUrl, null);

        testUrl = "/gameplay/step?player=2&cell=2";
        step = new Step(1L, 1L,2, 2, 2);
        res = ApiTestUtils.request("POST", testUrl, null);

        testUrl = "/gameplay/step?player=1&cell=4";
        step = new Step(1L, 1L,3, 1, 4);
        res = ApiTestUtils.request("POST", testUrl, null);

        testUrl = "/gameplay/step?player=2&cell=5";
        step = new Step(1L, 1L,4, 2, 5);
        res = ApiTestUtils.request("POST", testUrl, null);

        testUrl = "/gameplay/step?player=1&cell=7";
        String step1 = new JSONReader().read(new Step(1L, 1L,5, 1, 7), true);
        String winner =new JSONReader().read(new Player(1, "Oleg", "X"), true);
        res = ApiTestUtils.request("POST", testUrl, null);
        Spark.stop();
    }
}