import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.View;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @ParameterizedTest
    @MethodSource("gameFieldProvider")
    void givenSystemOutRedirection_whenInvokePrintln_thenOutputCaptorSuccess(String[][] gameField) {
        View.refresh(gameField);
        assertEquals(Arrays.deepToString(gameField).replace("[[", "|").replace("]]", "|")
                        .replace("], ", "|" + System.lineSeparator() + "|")
                        .replace("[", "").replace(",", "")
                        .replace(" ", "|"),
                outputStreamCaptor.toString().trim());
    }

    static Stream<String[][]> gameFieldProvider() {
        return Stream.of(new String[][]{{"145", "-", "145"}, {"-", "-", "145"}, {"145", "-", "-"}},
                new String[][]{{"O", "X", "X"}, {"-", "-", "O"}, {"X", "-", "-"}},
                new String[][]{{"}", "O", "{"}, {"!", "-", "{"}, {"", "-", "-"}});
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

}