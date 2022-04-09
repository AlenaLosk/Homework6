package utils;

import model.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLReader implements Reader {

    @Override
    public Game read(String file) throws IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Game game = new Game();
        Gameplay gameplay = new Gameplay(game.getId());
        game.setGameplay(gameplay);
        GameResult gameResult = new GameResult(game.getId(), gameplay.getGameplayId());
        game.getGameplay().setResult(gameResult);

        List<Step> steps = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            XMLEventReader reader = factory.createXMLEventReader(inputStream);
            int attributeId = 0;
            String attributeName = "";
            String attributeSymbol = "";
            int attributePlId = 0;
            int cell = 0;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals("model.Player")) {
                        attributeId = Integer.parseInt(startElement.getAttributeByName(new QName("id")).getValue());
                        attributeName = startElement.getAttributeByName(new QName("name")).getValue();
                        attributeSymbol = startElement.getAttributeByName(new QName("symbol")).getValue();
                        players.add(new Player(game.getId(), game.getGameplay().getGameplayId(), attributeId, attributeName, attributeSymbol));
                    } else if (startElement.getName().getLocalPart().equals("model.Step")) {
                        attributeId = Integer.parseInt(startElement.getAttributeByName(new QName("num")).getValue());
                        attributePlId = Integer.parseInt(startElement.getAttributeByName(new QName("playerId")).getValue());
                        event = reader.nextEvent();
                        cell = getCellAddress(event.asCharacters().getData());
                        steps.add(new Step(game.getId(), game.getGameplay().getGameplayId(), attributeId, attributePlId, cell));
                    } else if (startElement.getName().getLocalPart().equals("model.GameResult")) {
                        event = reader.nextEvent();
                        if (event.isStartElement()) {
                            startElement = event.asStartElement();
                            attributeId = Integer.parseInt(startElement.getAttributeByName(new QName("id")).getValue());
                            attributeName = startElement.getAttributeByName(new QName("name")).getValue();
                            attributeSymbol = startElement.getAttributeByName(new QName("symbol")).getValue();
                            game.getGameplay().getResult().setWinner(new Player(game.getId(), game.getGameplay().getGameplayId(), attributeId, attributeName, attributeSymbol));
                        }
                    }
                }
            }
            if (players.size() == 2) {
                game.getGameplay().setPlayers(new Player[]{players.get(0), players.get(1)});
            } else {
                ConsoleHelper.printMessage("The count of players isn't two!", true);
            }
            game.getGameplay().getSteps().setStepsList(steps);
        } catch (XMLStreamException ignore) {
        }
        return game;
    }

    public Integer getCellAddress(String value) {
        Integer cell = -1;
        try {
            cell = Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            String[] cellAdr = value.trim().split(" ");
            if (cellAdr.length == 2) {
                try {
                    cell = 3 * (Integer.parseInt(cellAdr[0]) - 1) + (Integer.parseInt(cellAdr[1]));
                } catch (Exception e) {
                    ConsoleHelper.printMessage("Unidentified format of coordinates! Use only two digits with space (from '1 1' to '3 3') or one digit (from 1 to 9)");
                }
            }
        }
        return cell;
    }
}
