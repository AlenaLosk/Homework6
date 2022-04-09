package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    private static BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    public static void printMessage(String message, boolean... hasLineBreak) {
        System.out.print(message);
        if (hasLineBreak.length != 0 && hasLineBreak[0]) {
            System.out.println();
        }
    }

    public static String readMessage() {
        String result = null;
        try {
            result = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void close() {
        try {
            inputStreamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
