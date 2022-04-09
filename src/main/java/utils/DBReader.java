package utils;

import jakarta.persistence.Query;
import model.Game;
import model.GameResult;
import model.Player;
import model.Step;
import org.hibernate.*;

import java.io.IOException;
import java.util.List;

public class DBReader implements Reader {
    private SessionFactory factory;

    public DBReader() {
    }

    @Override
    public Game read(String gameplayId) throws IOException {
        Player[] players = readPlayers(gameplayId);
        List<Step> list = readStep(gameplayId);
        GameResult gameResult = readGameResult(gameplayId);
        Game game = new Game();
        game.getGameplay().setPlayers(players);
        game.getGameplay().getSteps().setStepsList(list);
        game.getGameplay().setResult(gameResult);
        return game;
    }

    public Player[] readPlayers(String gameplayId) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        Session session = factory.openSession();
        session.beginTransaction();
        Player[] players = new Player[2];
        List<Player> list = null;
        Query query = session.createNativeQuery("SELECT * FROM PLAYER WHERE gameplayId = " + gameplayId, Player.class);
        list = (List<Player>) query.getResultList();
        if (list != null) {
            players[0] = list.get(0);
            players[1] = list.get(1);
        }
        session.getTransaction().commit();
        session.close();
        return players;
    }

    public List<Step> readStep(String gameplayId) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        Session session = factory.openSession();
        session.beginTransaction();
        List<Step> list = null;
        Query query = session.createNativeQuery("SELECT * FROM STEP WHERE gameplayId = " + gameplayId, Step.class);
        list = (List<Step>) query.getResultList();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public GameResult readGameResult(String gameplayId) {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        Session session = factory.openSession();
        session.beginTransaction();
        Query query = session.createNativeQuery("SELECT * FROM GAMERESULT WHERE gameplayId = " + gameplayId, GameResult.class);
        List<GameResult> list = (List<GameResult>) query.getResultList();
        session.getTransaction().commit();
        session.close();
        return list.get(0);
    }

    public Long readLastGameplayId() {
        if (factory == null) {
            factory = HibernateUtil.getSessionFactory();
        }
        Session session = factory.openSession();
        session.beginTransaction();
        Query query = session.createNativeQuery("SELECT * FROM STEP ORDER BY gameplayId DESC LIMIT 1;", Step.class);
        List<Step> steps = (List<Step>) query.getResultList();
        session.getTransaction().commit();
        session.close();
        if (steps != null) {
            return steps.get(0).getGameplayId();
        }
        return null;
    }
}
