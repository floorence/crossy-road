package persistence;

import model.*;
import model.Character;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            World world = new World();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("No exception thrown when expecting IOException");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterDefaultWorld() {
        try {
            World world = new World();
            JsonWriter writer = new JsonWriter("./data/testWriteDefaultWorld.json");
            writer.open();
            writer.write(world);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteDefaultWorld.json");
            World world2 = reader.readWorld();
            assertEquals(world.getScore(), 0);
            assertEquals(world.getWidth(), 9);
            assertEquals(world.getHeight(), 9);
            assertEquals(world.getCharacter().getX(), 4);
            assertEquals(world.getCharacter().getY(), 2);
            assertEquals(world, world2);
        } catch (IOException e) {
            fail("IOException thrown when expecting no exception");
        }
    }

    @Test
    void testWriterNotEmptyWorld() {
        try {
            World world = new World();
            world.newRow();
            world.newRow();
            JsonWriter writer = new JsonWriter("./data/testWriteNotEmptyWorld.json");
            writer.open();
            writer.write(world);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteNotEmptyWorld.json");
            World world2 = reader.readWorld();
            assertEquals(world.getCharacter().getX(), world2.getCharacter().getX());
            assertEquals(world.getCharacter().getY(), world2.getCharacter().getY());
            assertEquals(world.getScore(), world2.getScore());
            assertEquals(world, world2);

        } catch (IOException e) {
            fail("IOException thrown when expecting no exception");
        }
    }

    @Test
    void testWriterEmptyCharactersList() {
        try {
            CharactersList list = new CharactersList();
            JsonWriter writer = new JsonWriter("./data/testWriteEmptyCharactersList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteEmptyCharactersList.json");
            CharactersList list2 = reader.readList();
            assertEquals(list, list2);

        } catch (IOException e) {
            fail("IOException thrown when expecting no exception");
        }
    }

    @Test
    void testWriterNotEmptyCharactersList() {
        try {
            CharactersList list = new CharactersList();
            list.addCharacter(new Character("Gregor", 'G'));
            list.addCharacter(new Character("Karina", 'K'));
            list.addCharacter(new Character("Felix", 'F'));
            JsonWriter writer = new JsonWriter("./data/testWriteEmptyCharactersList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteEmptyCharactersList.json");
            CharactersList list2 = reader.readList();
            assertEquals(list, list2);

        } catch (IOException e) {
            fail("IOException thrown when expecting no exception");
        }
    }

    @Test
    void testWriterEmptyHighScoresList() {
        try {
            HighScoresList list = new HighScoresList();
            JsonWriter writer = new JsonWriter("./data/testWriteEmptyHighScores.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteEmptyHighScores.json");
            HighScoresList list2 = reader.readScores();
            assertEquals(list, list2);

        } catch (IOException e) {
            fail("IOException thrown when expecting no exception");
        }
    }

    @Test
    void testWriterNotEmptyHighScoresList() {
        try {
            HighScoresList list = new HighScoresList();
            list.setScore(0, 100);
            list.setScore(1, 91);
            list.setScore(2, 60);
            list.setScore(3, 50);
            list.setScore(4, 32);

            JsonWriter writer = new JsonWriter("./data/testWriteNotEmptyHighScores.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteNotEmptyHighScores.json");
            HighScoresList list2 = reader.readScores();
            assertEquals(list, list2);

        } catch (IOException e) {
            fail("IOException thrown when expecting no exception");
        }
    }
}