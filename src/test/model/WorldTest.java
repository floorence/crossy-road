package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    World world;
    Character c;
    int width = 9;

    @BeforeEach
    public void setup() {
        c = new Character("duck");
        world = new World(c);
    }

    @Test
    public void testConstructors() {
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width);
        assertEquals(world.getCharacter(), c);
        world = new World();
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width);
        assertEquals(world.getCharacter().getName(), "chicken");
        world = new World(false);
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width);
        assertEquals(world.getCharacter().getName(), "chicken");
        world = new World(true);
        assertTrue(world.getWorld().isEmpty());
        assertEquals(world.getCharacter().getName(), "chicken");


    }

    @Test
    public void getAndSetTile() {
        world.setTile(2, 3, 2);
        assertEquals(world.getTile(2, 3), 2);
    }

    @Test
    public void testGetAndSetCharacter() {
        Character bob = new Character("bob");
        world.setCharacter(bob);
        assertEquals(world.getCharacter(), bob);
    }

    @Test
    public void testGetAndSetScore() {
        world.setScore(20);
        assertEquals(world.getScore(), 20);
    }

    @Test
    public void testNewRow() {
        world.newRow();
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width + 1);
        assertEquals(world.getScore(), 1);
        world.newRow();
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width + 2);
        assertEquals(world.getScore(), 2);
        world.newRow();
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width + 3);
        assertEquals(world.getScore(), 3);
        world.newRow();
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width + 4);
        assertEquals(world.getScore(), 4);
        world.newRow();
        assertEquals(world.getWidth(), width);
        assertEquals(world.getHeight(), width + 5);
        assertEquals(world.getScore(), 5);


    }

    @Test
    public void testShouldAddNewRow() {
        assertFalse(world.shouldAddNewRow(1));
        assertFalse(world.shouldAddNewRow(0));
        c.setY(3);
        assertFalse(world.shouldAddNewRow(1));
        assertTrue(world.shouldAddNewRow(0));

    }

    @Test
    public void testMoveCharacterNoExceptions() {
        world.setTile(4,3,0);
        try {
            world.moveCharacter(0);
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException thrown when expecting no exception");
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting no exception");
        }
        assertEquals(c.getX(), 4);
        assertEquals(c.getY(), 3);
        c.setX(width - 1);
        world.setTile(width - 1,2,0);
        try {
            world.moveCharacter(2);
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException thrown when expecting no exception");
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting no exception");
        }
        assertEquals(c.getX(), width - 1);
        assertEquals(c.getY(), 2);
        c.setX(0);
        world.setTile(0,3,0);
        try {
            world.moveCharacter(0);
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException thrown when expecting no exception");
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting no exception");
        }
        assertEquals(c.getX(), 0);
        assertEquals(c.getY(), 3);
        c.setY(0);
        world.setTile(1,0,0);
        try {
            world.moveCharacter(1);
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException thrown when expecting no exception");
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting no exception");
        }
        assertEquals(c.getX(), 1);
        assertEquals(c.getY(), 0);c.setY(0);
        world.setTile(0,0,0);
        try {
            world.moveCharacter(3);
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException thrown when expecting no exception");
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting no exception");
        }
        assertEquals(c.getX(), 0);
        assertEquals(c.getY(), 0);
    }

    @Test
    public void testMoveCharacterInvalidMove() {
        c.setX(width - 1);
        try {
            world.moveCharacter(1);
            fail("No exception thrown when expecting InvalidMoveException");
        } catch (InvalidMoveException e) {
            //pass
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting InvalidMoveException");
        }
        assertEquals(c.getX(), width - 1);
        assertEquals(c.getY(), 2);
        c.setX(0);
        try {
            world.moveCharacter(3);
            fail("No exception thrown when expecting InvalidMoveException");
        } catch (InvalidMoveException e) {
            //pass
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting InvalidMoveException");
        }
        assertEquals(c.getX(), 0);
        assertEquals(c.getY(), 2);
    }

    @Test
    public void testMoveCharacterGameOver() {
        c.setY(0);
        try {
            world.moveCharacter(2);
            fail("No exception thrown when expecting GameOverException");
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException thrown when expecting GameOverException");
        } catch (GameOverException e) {
            //pass
        }
        assertEquals(c.getX(), 4);
        assertEquals(c.getY(), 0);
    }

    @Test
    public void testMoveCharacterIntoTree() {
        world.setTile(4,3,1);
        try {
            world.moveCharacter(0);
            fail("No exception thrown when expecting InvalidMoveException");
        } catch (InvalidMoveException e) {
            //pass
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting InvalidMoveException");
        }
        assertEquals(c.getX(), 4);
        assertEquals(c.getY(), 2);
        world.setTile(4,3,0);
        world.setTile(5,2,1);
        try {
            world.moveCharacter(1);
            fail("No exception thrown when expecting InvalidMoveException");
        } catch (InvalidMoveException e) {
            //pass
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting InvalidMoveException");
        }
        assertEquals(c.getX(), 4);
        assertEquals(c.getY(), 2);
        world.setTile(5,2,0);
        world.setTile(4,1,1);
        try {
            world.moveCharacter(2);
            fail("No exception thrown when expecting InvalidMoveException");
        } catch (InvalidMoveException e) {
            //pass
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting InvalidMoveException");
        }
        assertEquals(c.getX(), 4);
        assertEquals(c.getY(), 2);
        world.setTile(4,1,0);
        world.setTile(3,2,1);
        try {
            world.moveCharacter(3);
            fail("No exception thrown when expecting InvalidMoveException");
        } catch (InvalidMoveException e) {
            //pass
        } catch (GameOverException e) {
            fail("GameOverException thrown when expecting InvalidMoveException");
        }
        assertEquals(c.getX(), 4);
        assertEquals(c.getY(), 2);
    }

    @Test
    public void testToString() {
        world.newRow();
        world.newRow();
        world.newRow();
        world.newRow();
        world.newRow();
        c.setY(7);
        assertEquals(world.toString().length(), (width * 2) * width + width);
        assertTrue(world.toString().contains(c.getImage() + ""));
    }

    @Test
    public void testEquals() {
        World world2 = new World();
        world2.newRow();
        assertFalse(world.equals(world2));

        world = world2;

        assertTrue(world.equals(world2));

        world2 = new World(true);
        world = new World(true);

        assertTrue(world.equals(world2));
    }

}
