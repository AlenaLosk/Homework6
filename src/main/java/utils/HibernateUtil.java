package utils;

import model.GameResult;
import model.Player;
import model.Step;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {
    private static SessionFactory playerSessionFactory;
    private static SessionFactory stepSessionFactory;
    private static SessionFactory gameResultSessionFactory;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(String resource) {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure(new File(resource)).buildSessionFactory();
        }
        return sessionFactory;
    }

    public static SessionFactory getPlayerSessionFactory(String resource, Class<Player> var) {
        if (playerSessionFactory == null) {
            playerSessionFactory = new Configuration().configure(new File(resource)).addAnnotatedClass(var).buildSessionFactory();
        }
        return playerSessionFactory;
    }

    public static SessionFactory getStepSessionFactory(String resource, Class<Step> var) {
        if (stepSessionFactory == null) {
            stepSessionFactory = new Configuration().configure(new File(resource)).addAnnotatedClass(var).buildSessionFactory();
        }
        return stepSessionFactory;
    }

    public static SessionFactory getGameResultSessionFactory(String resource, Class<GameResult> var) {
        if (gameResultSessionFactory == null) {
            gameResultSessionFactory = new Configuration().configure(new File(resource)).addAnnotatedClass(var).buildSessionFactory();
        }
        return gameResultSessionFactory;
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
