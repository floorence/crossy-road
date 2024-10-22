package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.lang.Math;

//represents the game world, a 2d array with each position in the array representing a
//tile in the game; each tile can have a tree, grass, or road.
public class World implements Writable {
    //Each tile is an integer where 0=grass, 1=tree, 2=road
    private ArrayList<int[]> world; //world width is constant, world height increases throughout game
    private Character character;
    private final int width = 9;
    private int score;


    //EFFECTS: Initializes a new World object with default character "chicken"
    public World() {
        world = new ArrayList<>();
        character = new Character("chicken");
        makeWorld();
        score = 0;
    }

    //EFFECTS: Initializes a new World object with given character
    public World(Character c) {
        world = new ArrayList<>();
        character = c;
        makeWorld();
        score = 0;
    }

    //EFFECTS: Initializes a new World object with empty world array (for json and testing)
    public World(boolean empty) {
        world = new ArrayList<>();
        character = new Character("chicken");
        score = 0;
        if (!empty) {
            makeWorld();
        }
    }

    //REQUIRES: world is not filled in yet
    //EFFECTS: fills in the world as a 9x9 of grass and trees
    private void makeWorld() {
        for (int i = 0; i < width; i++) {
            world.add(new int[width]);
            for (int j = 0; j < width; j++) {
                int num = (int) (Math.random() * 10);
                if (num == 0) {
                    world.get(i)[j] = 1;
                } else {
                    world.get(i)[j] = 0;
                }

            }
        }
    }

    public ArrayList<int[]> getWorld() {
        return world;
    }

    //REQUIRES: x and y coordinate is within bounds of the world
    //EFFECTS: gets the int value of the tile at give x and y
    public int getTile(int x, int y) {
        return world.get(y)[x];

    }

    //REQUIRES: x and y coordinate is within bounds of the world
    //MODIFIES: this
    //EFFECTS: sets the int value of the tile at given x and y
    public void setTile(int x, int y, int tile) {
        world.get(y)[x] = tile;
    }

    public int getWidth() {
        if (world.isEmpty()) {
            return 0;
        } else {
            return world.get(0).length;
        }
    }

    public int getHeight() {
        return world.size();
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character c) {
        character = c;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //MODIFIES: this
    //EFFECTS: adds a new row to the end of the world list. Each new row has a 1/4th chance of
    //being grass and trees and a 3/4th chance of being road. Increases score by 1.
    public void newRow() {
        int[] row = new int[width];
        int num = (int) (Math.random() * 4);
        if (num == 0) {
            for (int i = 0; i < row.length; i++) {
                int num2 = (int)(Math.random() * 5);
                if (num2 == 0) {
                    row[i] = 1;
                } else {
                    row[i] = 0;
                }
            }
        } else {
            for (int i = 0; i < row.length; i++) {
                row[i] = 2;
            }
        }
        world.add(row);
        score++;
    }

    //EFFECTS: Returns whether a new row should be added to the world. A new row should be added
    //if character's Y value is 6 tiles or fewer from the top of the world and character just
    // moved in the forward direction.
    public boolean shouldAddNewRow(int direction) {
        return (direction == 0 && world.size() - character.getY() <= 6);

    }

    //MODIFIES: this
    //EFFECTS: adds the given row to the end of world
    public void addRow(int[] row) {
        world.add(row);
    }

    //REQUIRES: 0 <= direction <= 3
    //MODIFIES: this
    //EFFECTS: Moves the character one tile in the given direction. If character is at an edge of
    //the screen and will move out of the world on the left or right side, throws InvalidMoveException.
    //If character is at the bottom of the world and will move down, throws GameOverException. If
    //tile in which character will move into is a tree, throws InvalidMoveException.
    public void moveCharacter(int direction) throws InvalidMoveException, GameOverException {
        InvalidMoveException e = new InvalidMoveException();
        if (character.getX() == world.get(0).length - 1 && direction == 1) {
            throw e;
        } else if (character.getX() == 0 && direction == 3) {
            throw e;
        } else if (world.size() - character.getY() >= width && direction == 2) {
            throw new GameOverException();
        } else if ((direction == 0 && getTile(character.getX(), character.getY() + 1) == 1)
                || (direction == 1 && getTile(character.getX() + 1, character.getY()) == 1)
                || (direction == 2 && getTile(character.getX(), character.getY() - 1) == 1)
                || (direction == 3 && getTile(character.getX() - 1, character.getY()) == 1)) {
            throw e;
        } else {
            character.move(direction);
        }
    }

    @Override
    public boolean equals(Object w) {
        World world2 = (World) w;
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < width; j++) {
                if (!(getTile(j, i) == world2.getTile(j, i))) {
                    return false;
                }
            }
        }
        return true;
    }

    //EFFECTS: Returns a String representation of the last 9 rows of the world with the end of
    //the world list at the front of the string
    @Override
    public String toString() {
        String s = "";
        for (int i = world.size() - 1; i >= world.size() - width; i--) {
            for (int j = 0; j < world.get(0).length; j++) {
                if (character.getX() == j && character.getY() == i) {
                    s = s + character.getImage() + " ";
                } else if (world.get(i)[j] == 0) {
                    s = s + "= ";
                } else if (world.get(i)[j] == 1) {
                    s = s + "T ";
                } else {
                    s = s + "- ";
                }
            }
            s = s + "\n";
        }
        return s;
    }

    //Method modeled from WorkRoom toJson method in the JsonSerialization Demo
    //EFFECTS: Returns a JSON representation of the world
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("score", score);
        json.put("character", character.toJson());
        json.put("world", rowsToJson());
        return json;
    }

    //Method modeled from WorkRoom thingiesToJson method in the JsonSerialization Demo
    //EFFECTS: Returns JSON representation of rows in the world
    private JSONArray rowsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (int[] row : world) {
            jsonArray.put(rowToJson(row));
        }
        return jsonArray;
    }

    //Method modeled from WorkRoom thingiesToJson method in the JsonSerialization Demo
    //EFFECTS: Returns JSON representation of tiles in the row
    private JSONObject rowToJson(int[] row) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int tile : row) {
            JSONObject json = new JSONObject();
            json.put("tile", tile);
            jsonArray.put(json);
        }
        jsonObject.put("row", jsonArray);
        return jsonObject;
    }


}
