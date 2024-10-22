package persistence;

import model.*;
import model.Character;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Class modeled from the JsonSerialization Demo
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads CharactersList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public CharactersList readList() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        CharactersList list = parseList(jsonObject);
        return list;
    }

    // EFFECTS: reads World from file and returns it;
    // throws IOException if an error occurs reading data from file
    public World readWorld() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorld(jsonObject);
    }

    // EFFECTS: reads HighScoresList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public HighScoresList readScores() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseScores(jsonObject);
    }


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // parses HighScoresList from JSON object and returns it
    private HighScoresList parseScores(JSONObject jsonObject) {
        HighScoresList list = new HighScoresList();
        JSONArray jsonArray = jsonObject.getJSONArray("high scores");
        int i = 0;
        for (Object json : jsonArray) {
            JSONObject nextScore = (JSONObject) json;
            int score = nextScore.getInt("score");
            list.setScore(i, score);
            i++;
        }
        return list;
    }

    // EFFECTS: parses CharactersList from JSON object and returns it
    private CharactersList parseList(JSONObject jsonObject) {
        CharactersList list = new CharactersList();
        addCharacters(list, jsonObject);
        return list;
    }

    // EFFECTS: parses World from JSON object and returns it
    private World parseWorld(JSONObject jsonObject) {
        World world = new World(true);
        addRows(world, jsonObject);
        JSONObject characterJson = jsonObject.getJSONObject("character");
        int x = characterJson.getInt("x");
        int y = characterJson.getInt("y");
        Character c = new Character("chicken");
        c.setX(x);
        c.setY(y);
        world.setCharacter(c);
        int score = jsonObject.getInt("score");
        world.setScore(score);
        return world;
    }

    //MODIFIES: world
    //EFFECTS: parses rows from JSON object and adds them to world
    private void addRows(World world, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("world");
        for (Object json : jsonArray) {
            JSONObject nextRow = (JSONObject) json;
            addRow(world, nextRow);
        }
    }

    //MODIFIES: world
    //EFFECTS: parses row from JSON object and adds them to world
    private void addRow(World world, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("row");
        int[] row = new int[9];
        int i = 0;
        for (Object json : jsonArray) {
            JSONObject nextTile = (JSONObject) json;
            addTile(row, nextTile, i);
            i++;
        }
        world.addRow(row);
    }

    //MODIFIES: row
    //EFFECTS: parses tile from JSON object and adds them to row at given index
    private void addTile(int[] row, JSONObject jsonObject, int index) {
        int tile = jsonObject.getInt("tile");
        row[index] = tile;
    }

    // MODIFIES: list
    // EFFECTS: parses characters from JSON object and adds them to CharactersList
    private void addCharacters(CharactersList list, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("characters");
        for (Object json : jsonArray) {
            JSONObject nextCharacter = (JSONObject) json;
            addCharacter(list, nextCharacter);
        }
    }

    // MODIFIES: list
    // EFFECTS: parses character from JSON object and adds it to CharactersList
    private void addCharacter(CharactersList list, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String image = jsonObject.getString("image");
        int size = jsonObject.getInt("size");
        Character c = new Character(name);
        c.setImage(image.charAt(0));
        c.setSize(size);
        list.addCharacter(c);
    }
}
