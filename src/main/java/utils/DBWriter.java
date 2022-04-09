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

    public DBWriter() {
    }

    @Override
    public void write(Game game) throws IOException {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        writePlayers(game);
        writeSteps(game);
        writeGameResult(game);
    }

    public void writePlayers(Game game) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(game.getGameplay().getPlayers()[0]);
        session.save(game.getGameplay().getPlayers()[1]);
        session.getTransaction().commit();
        session.close();
    }

    public void writeSteps(Game game) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
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

    public void writeStep(Step step) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(step);
        session.getTransaction().commit();
        session.close();
    }

    public void writeGameResult(Game game) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        Session session = factory.openSession();
        session.beginTransaction();
        GameResult gameResult = game.getGameplay().getResult();
        session.save(gameResult);
        session.getTransaction().commit();
        session.close();
    }
}
