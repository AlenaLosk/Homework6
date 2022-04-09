package utils;

import lombok.*;
import model.Game;
import model.GameResult;
import model.Step;
import org.hibernate.*;

import java.io.IOException;
import java.util.List;

public class DBWriter implements Writer {
    private SessionFactory factory;
    @Getter
    @Setter
    private String resource = "src\\main\\resources\\hibernate.cfg.xml";

    public DBWriter() {
    }

    @Override
    public void write(Game game, String resource) throws IOException {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory(resource);
        }
        writePlayers(game, resource);
        writeSteps(game, resource);
        writeGameResult(game, resource);
    }

    public void writePlayers(Game game, String resource) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory(resource);
        }
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(game.getGameplay().getPlayers()[0]);
        session.save(game.getGameplay().getPlayers()[1]);
        session.getTransaction().commit();
        session.close();
    }

    public void writeSteps(Game game, String resource) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory(resource);
        }
        Session session = factory.openSession();
        session.beginTransaction();
        List<Step> steps = game.getGameplay().getSteps().getStepsList();
        for (Step step : steps) {
            session.save(step);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void writeStep(Step step, String resource) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory(resource);
        }
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(step);
        session.getTransaction().commit();
        session.close();
    }

    public void writeGameResult(Game game, String resource) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory(resource);
        }
        Session session = factory.openSession();
        session.beginTransaction();
        GameResult gameResult = game.getGameplay().getResult();
        session.save(gameResult);
        session.getTransaction().commit();
        session.close();
    }
}
