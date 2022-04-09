import controller.ConsoleUserInterface;
import controller.Server;
import utils.ApiTestUtils;
import utils.HibernateUtil;
import utils.JSONReader;
import utils.JSONWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {
    public static void main(String[] args) throws Exception{
        //new Server(); // использовать для запуска серверного варианта
        ConsoleUserInterface.start(); // использовать для работы с консольным вариантом (реализовано хранение в БД через Hibernate)
    }
}
