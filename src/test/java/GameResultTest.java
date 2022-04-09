import model.GameResult;
import model.Player;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameResultTest {
    GameResult gameResult = new GameResult();

    @ParameterizedTest
    @MethodSource("playerProvider")
    void WinnerTest1(Player player) {
        gameResult.setWinner(player);
        assertEquals(player, gameResult.getWinner());
    }

    static Stream<Player> playerProvider() {
        return Stream.of(new Player(1L,1L,0, null, ""),
                new Player(1398265L, 999999999L, 15, "_&0981?", "127"),
                new Player(0L, 180095L,18000000, "Котик", "}"));
    }

}