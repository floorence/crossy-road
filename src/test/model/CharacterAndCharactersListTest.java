package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterAndCharactersListTest {
    CharactersList list;
    Character dog;
    Character cat;
    Character bunny;


    @BeforeEach
    public void setup() {
        list = new CharactersList();
        dog = new Character("dog");
        cat = new Character("cat", 5);
        bunny = new Character("bunny", 9);
    }

    @Test
    public void testCharacterConstructor() {
        assertEquals(cat.getName(), "cat");
        assertEquals(cat.getSize(), 5);
        assertEquals(dog.getSize(), 10);
        assertEquals(dog.getX(), 4);
        assertEquals(cat.getY(), 2);

    }

    @Test
    public void testCharactersListConstructor() {
        assertTrue(list.getCharacters().isEmpty());
    }

    @Test
    public void testCharacterMove() {
        dog.move(0);
        assertEquals(dog.getX(), 4);
        assertEquals(dog.getY(), 3);
        dog.move(1);
        assertEquals(dog.getX(), 5);
        assertEquals(dog.getY(), 3);
        dog.move(2);
        assertEquals(dog.getX(), 5);
        assertEquals(dog.getY(), 2);
        dog.move(3);
        assertEquals(dog.getX(), 4);
        assertEquals(dog.getY(), 2);
        dog.move(4);
        assertEquals(dog.getX(), 4);
        assertEquals(dog.getY(), 2);
    }

    @Test
    public void testAddAndGetCharacter() {
        list.addCharacter(dog);
        assertEquals(list.getCharacters().size(), 1);
        list.addCharacter(cat);
        assertEquals(list.getCharacters().size(), 2);
        list.addCharacter(bunny);
        assertEquals(list.getCharacters().size(), 3);
        try {
            assertEquals(list.getCharacter("dog"), dog);
            assertEquals(list.getCharacter("cat"), cat);
            assertEquals(list.getCharacter("bunny"), bunny);
        } catch (NoSuchObjectException e) {
            fail("NoSuchObjectException was thrown when expecting no exception");
        }
        try {
            list.getCharacter("fish");
            fail("No exception was thrown when expecting NoSuchObjectException");
        } catch (NoSuchObjectException e) {
            //pass
        }
    }

    @Test
    public void testGetAndSetCharacters() {
        ArrayList<Character> list2 = new ArrayList<>();
        CharactersList list3 = new CharactersList();
        list2.add(cat);
        list2.add(bunny);
        list3.addCharacter(cat);
        list3.addCharacter(bunny);
        list.setCharacters(list2);
        assertEquals(list.toString(), list3.toString());
    }

    @Test
    public void testGetAndSetImage() {
        cat.setImage('C');
        assertEquals(cat.getImage(), 'C');
    }

    @Test
    public void testGetAndSetName() {
        bunny.setName("rabbit");
        assertEquals(bunny.getName(), "rabbit");
    }

    @Test
    public void testGetAndSetSize() {
        dog.setSize(8);
        assertEquals(dog.getSize(), 8);
    }

    @Test
    public void testToString() {
        list.addCharacter(cat);
        assertEquals(list.toString(), "\nCHARACTERS:\nName: cat\nSize: 5\n\n\n");
    }

    @Test
    public void testListEquals() {
        CharactersList list2 = new CharactersList();
        assertTrue(list.equals(list2));
        list2.addCharacter(cat);
        assertFalse(list.equals(list2));
        list.addCharacter(dog);
        assertFalse(list.equals(list2));
        list2 = new CharactersList();
        list2.addCharacter(dog);
        assertTrue(list.equals(list2));
    }

    @Test
    public void testCharacterEquals() {
        Character bunny2 = new Character("bunny", 9);
        assertTrue(bunny.equals(bunny2));
        bunny.setImage('B');
        assertFalse(bunny.equals(bunny2));
        Character rabbit = new Character("rabbit", 9);
        assertFalse(bunny.equals(rabbit));
        bunny2.setImage('B');
        bunny2.setSize(10);
        assertFalse(bunny.equals(bunny2));
    }
}
