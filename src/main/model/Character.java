package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;

//A character with a name, size, and image, and it's x and y coordinates during a game
public class Character implements Writable {
    private String name;
    private int size;
    private char image; // for now, image is just a character which will represent the Character in the console game
    private int xpos;
    private int ypos;
    private String imgSource;
    //private int imgNum;

    //EFFECTS: Initializes a new Character with given name
    public Character(String name) {
        this.name = name;
        size = 10;
        xpos = 4;
        ypos = 2;
        image = 'O';
        imgSource = "images/bunny.png";
    }

    //EFFECTS: Initializes a new Character with given name and image
    public Character(String name, char image) {
        this.name = name;
        size = 10;
        xpos = 4;
        ypos = 2;
        this.image = image;
        imgSource = "images/bunny.png";
    }

    //EFFECTS: Initializes a new Character with given name and size
    public Character(String name, int size) {
        this.name = name;
        this.size = size;
        xpos = 4;
        ypos = 2;
        image = 'O';
        imgSource = "images/bunny.png";
    }

    public Character(String name, int size, String imgSource) {
        this.name = name;
        this.size = size;
        xpos = 4;
        ypos = 2;
        image = 'O';
        this.imgSource = imgSource;
    }

    //REQUIRES: character won't move out of bounds
    //MODIFIES: this
    //EFFECTS: moves one tile in given direction
    public void move(int direction) {
        if (direction == 0) { //forward
            ypos++;
        } else if (direction == 1) { //right
            xpos++;
        } else if (direction == 2) { //down
            ypos--;
        } else if (direction == 3) { //left
            xpos--;
        }
    }

    public void setImage(char i) {
        image = i;
    }

    public char getImage() {
        return image;
    }

    public String getImgSource() {
        return imgSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getX() {
        return xpos;
    }

    public void setX(int x) {
        this.xpos = x;
    }

    public int getY() {
        return ypos;
    }

    public void setY(int y) {
        this.ypos = y;
    }

    //EFFECTS: Returns a JSON representation of the character with its x and y values
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", xpos);
        jsonObject.put("y", ypos);
        return jsonObject;
    }

    //EFFECTS: Returns true if both characters have the same name, size and image
    @Override
    public boolean equals(Object o) {
        Character c = (Character) o;
        return (c.getName().equals(name)) && (c.getSize() == size) && (c.getImage() == image);
    }
}
