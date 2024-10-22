package ui;

import java.awt.*;

//represents a character hitbox which is a rectangle that is never drawn to detect collision in game
public class CharacterHitBox extends Rectangle {

    //EFFECTS: creates a new CharacterHitBox with given width and height
    public CharacterHitBox(int width, int height) {
        super(width, height);
    }
}
