package utils;

import model.Game;
import model.Gameplay;
import model.Player;
import model.Step;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileOutputStream;

public class XMLWriter implements Writer {
    @Override
    public void write(Game game, String file) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        Gameplay gameplay = game.getGameplay();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            XMLEventWriter writer = factory.createXMLEventWriter(outputStream, "windows-1251");
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent tab = eventFactory.createDTD("\t");
            writer.add(eventFactory.createStartDocument("windows-1251", "1.0"));
            writer.add(end);
            writer.add(eventFactory.createStartElement("", "", "model.Gameplay"));
            writer.add(end);
            for (Player player : gameplay.getPlayers()) {
                writer.add(tab);
                writePlayer(writer, player);
                writer.add(end);
            }
            writer.add(tab);
            writer.add(eventFactory.createStartElement("", "", "model.Game"));
            writer.add(end);
            for (Step step : gameplay.getSteps().getStepsList()) {
                writer.add(tab);
                writer.add(tab);
                writeStep(writer, step);
                writer.add(end);
            }
            writer.add(tab);
            writer.add(eventFactory.createEndElement("", "", "model.Game"));
            writer.add(end);
            writer.add(tab);
            writeGameResult(writer, gameplay.getResult().getWinner());
            writer.add(end);
            writer.add(eventFactory.createEndElement("", "", "model.Gameplay"));
            writer.add(end);
            writer.add(eventFactory.createEndDocument());
            writer.close();
        } catch (Exception e) {
            ConsoleHelper.printMessage("The file for writing game steps wasn't found!" + System.lineSeparator(), true);
        }
    }

    private void writeStep(XMLEventWriter eventWriter, Step step) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        eventWriter.add(eventFactory.createStartElement("", "", "model.Step"));
        eventWriter.add(eventFactory.createAttribute("num", String.valueOf(step.getNum())));
        eventWriter.add(eventFactory.createAttribute("playerId", String.valueOf(step.getPlayerId())));
        eventWriter.add(eventFactory.createCharacters(String.valueOf(step.getCell())));
        eventWriter.add(eventFactory.createEndElement("", "", "model.Step"));
    }

    private void writePlayer(XMLEventWriter eventWriter, Player player) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        eventWriter.add(eventFactory.createStartElement("", "", "model.Player"));
        eventWriter.add(eventFactory.createAttribute("id", String.valueOf(player.getNum())));
        eventWriter.add(eventFactory.createAttribute("name", player.getName()));
        eventWriter.add(eventFactory.createAttribute("symbol", String.valueOf(player.getSymbol())));
        eventWriter.add(eventFactory.createEndElement("", "", "model.Player"));
    }

    private void writeGameResult(XMLEventWriter eventWriter, Player winner) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        eventWriter.add(eventFactory.createStartElement("", "", "model.GameResult"));
        if (winner != null) {
            eventWriter.add(eventFactory.createStartElement("", "", "model.Player"));
            eventWriter.add(eventFactory.createAttribute("id", String.valueOf(winner.getNum())));
            eventWriter.add(eventFactory.createAttribute("name", winner.getName()));
            eventWriter.add(eventFactory.createAttribute("symbol", String.valueOf(winner.getSymbol())));
            eventWriter.add(eventFactory.createEndElement("", "", "model.Player"));
        } else {
            eventWriter.add(eventFactory.createCharacters("Draw!"));
        }
        eventWriter.add(eventFactory.createEndElement("", "", "model.GameResult"));
    }

}
