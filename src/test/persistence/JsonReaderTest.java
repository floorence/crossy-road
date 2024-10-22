package persistence;

import model.CharactersList;
import model.HighScoresList;
import model.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Class modeled from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
class JsonReaderTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            World wr = reader.readWorld();
            fail("No exception thrown when IOException expected");
        } catch (IOException e) {
            // pass
        }
        try {
            CharactersList list = reader.readList();
            fail("No exception thrown when IOException expected");
        } catch (IOException e) {
            // pass
        }
        try {
            HighScoresList list = reader.readScores();
            fail("No exception thrown when IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorld() {
        JsonReader reader = new JsonReader("./data/testEmptyWorld.json");
        try {
            World world = reader.readWorld();
            assertEquals(world.getScore(), 0);
            assertEquals(world.getWidth(), 0);
            assertEquals(world.getHeight(), 0);
            assertEquals(world.getCharacter().getX(), 4);
            assertEquals(world.getCharacter().getY(), 2);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderDefaultWorld() {
        JsonReader reader = new JsonReader("./data/testDefaultWorld.json");
        try {
            World world = reader.readWorld();
            assertEquals(world.getScore(), 0);
            assertEquals(world.getWidth(), 9);
            assertEquals(world.getHeight(), 9);
            assertEquals(world.getCharacter().getX(), 4);
            assertEquals(world.getCharacter().getY(), 2);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderNotEmptyWorld() {
        JsonReader reader = new JsonReader("./data/testNotEmptyWorld.json");
        try {
            World world = reader.readWorld();
            assertEquals(world.getScore(), 3);
            assertEquals(world.getWidth(), 9);
            assertEquals(world.getHeight(), 12);
            assertEquals(world.getCharacter().getX(), 2);
            assertEquals(world.getCharacter().getY(), 5);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderEmptyCharactersList() {
        JsonReader reader = new JsonReader("./data/testEmptyCharactersList.json");
        try {
            CharactersList list = reader.readList();
            assertTrue(list.getCharacters().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderNotEmptyCharactersList() {
        JsonReader reader = new JsonReader("./data/testNotEmptyCharactersList.json");
        try {
            CharactersList list = reader.readList();
            assertEquals(list.getCharacters().size(), 3);
            ArrayList<model.Character> check = list.getCharacters();
            assertEquals(check.get(0).getName(), "Gregor");
            assertEquals(check.get(1).getName(), "Karina");
            assertEquals(check.get(2).getName(), "Felix");
            assertEquals(check.get(0).getImage(), 'G');
            assertEquals(check.get(1).getImage(), 'K');
            assertEquals(check.get(2).getImage(), 'F');
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderEmptyHighScores() {
        JsonReader reader = new JsonReader("./data/testEmptyHighScores.json");
        try {
            HighScoresList list = reader.readScores();
            assertEquals(list.getScores().length, 5);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderNotEmptyHighScores() {
        JsonReader reader = new JsonReader("./data/testNotEmptyHighScores.json");
        try {
            HighScoresList list = reader.readScores();
            assertEquals(list.getScores().length, 5);
            assertEquals(list.getScores()[0], 100);
            assertEquals(list.getScores()[1], 91);
            assertEquals(list.getScores()[2], 60);
            assertEquals(list.getScores()[3], 50);
            assertEquals(list.getScores()[4], 32);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}