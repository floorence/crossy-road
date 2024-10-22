package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HighScoresListTest {
    HighScoresList list;

    @BeforeEach
    public void setup() {
        list = new HighScoresList();
    }

    @Test
    public void testConstructor() {
        assertEquals(list.getScores().length, 5);

    }

    @Test
    public void testSetAndGetScore() {
        list.setScore(3, 32);
        assertEquals(list.getScore(3), 32);
    }

    @Test
    public void testIsHighScore() {
        list.setScore(0, 100);
        list.setScore(1, 91);
        list.setScore(2, 60);
        list.setScore(3, 50);
        list.setScore(4, 32);
        assertFalse(list.isHighScore(30));
        assertFalse(list.isHighScore(32));
        assertTrue(list.isHighScore(60));
        assertEquals(list.getScore(3), 60);
        assertTrue(list.isHighScore(101));
        assertEquals(list.getScore(0), 101);
        assertTrue(list.isHighScore(70));
        assertEquals(list.getScore(3), 70);
        assertEquals(list.getScore(4), 60);
    }

    @Test
    public void testToString() {
        list.setScore(0, 100);
        list.setScore(1, 91);
        list.setScore(2, 60);
        list.setScore(3, 50);
        list.setScore(4, 32);
        assertEquals(list.toString(), "\nHigh Scores: \n100\n91\n60\n50\n32\n");
    }

    @Test
    public void testEquals() {
        HighScoresList list2 = new HighScoresList();
        assertTrue(list.equals(list2));
        list.setScore(0, 100);
        list.setScore(1, 91);
        list.setScore(2, 60);
        list.setScore(3, 50);
        list.setScore(4, 32);
        assertFalse(list.equals(list2));
        list2.setScore(0, 100);
        list2.setScore(1, 91);
        list2.setScore(2, 60);
        list2.setScore(3, 50);
        list2.setScore(4, 32);
        assertTrue(list.equals(list2));

    }

}
