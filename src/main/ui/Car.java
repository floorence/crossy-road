package ui;

import java.awt.*;

//represents a car which is a rectangle with a direction and colour
public class Car extends Rectangle {
    private int direction;
    private Color colour;

    //EFFECTS: creates a new car with given direction
    public Car(int dir) {
        super();
        direction = dir;
    }

    public int getDirection() {
        return direction;
    }

    public void setColour(Color c) {
        colour = c;
    }

    public Color getColour() {
        return colour;
    }
}
