package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml")).addAnnotatedClass(model.Player.class).
                    addAnnotatedClass(model.Step.class).addAnnotatedClass(model.GameResult.class).buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
