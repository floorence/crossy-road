package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonWriter;
import persistence.Writable;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;

//a list of character objects
public class CharactersList implements Writable {
    private ArrayList<Character> characters;

    //EFFECTS: Initializes a new CharactersList with empty characters list
    public CharactersList() {
        characters = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: adds a character to the end of the list
    public void addCharacter(Character c) {
        characters.add(c);
        EventLog.getInstance().logEvent(new Event("character added: " + c.getName()));
    }

    //MODIFIES: this
    //EFFECTS: removes the given character from the CharactersList
    public void removeCharacter(Character c) {
        characters.remove(c);
        EventLog.getInstance().logEvent(new Event("character removed: " + c.getName()));
    }

    //EFFECTS: gets the character with the given name; if character is not in list
    //throws NoSuchObjectException
    public Character getCharacter(String name) throws NoSuchObjectException {
        for (Character character : characters) {
            if (character.getName().equals(name)) {
                return character;
            }
        }
        NoSuchObjectException e = new NoSuchObjectException("character not in list");
        throw e;

    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }


    //EFFECTS: Returns a string representation of the list of characters
    @Override
    public String toString() {
        String s = "\nCHARACTERS:\n";
        for (Character c: characters) {
            s = s + "Name: " + c.getName()
                    +
                    //"\n" + "Speed: " + c.getSpeed() +
                    "\n" + "Size: " + c.getSize() + "\n\n\n";
        }
        return s;
    }

    //EFFECTS: returns true if all characters in the list have the same name, size, and image
    @Override
    public boolean equals(Object o) {
        CharactersList list = (CharactersList) o;
        if (characters.size() != list.getCharacters().size()) {
            return false;
        }
        for (int i = 0; i < characters.size(); i++) {
            if (!characters.get(i).equals(((CharactersList) o).getCharacters().get(i))) {
                return false;
            }
        }
        return true;
    }

    //Method modeled from WorkRoom toJson method in the JsonSerialization Demo
    //EFFECTS: Returns a JSON representation of the list of characters
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("characters", charactersToJson());
        return json;
    }

    //Method modeled from WorkRoom thingiesToJson method in the JsonSerialization Demo
    //EFFECTS: Returns JSON representation of characters
    private JSONArray charactersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Character c : characters) {
            JSONObject json = new JSONObject();
            json.put("name", c.getName());
            json.put("image", java.lang.Character.toString(c.getImage()));
            json.put("size", c.getSize());
            jsonArray.put(json);
        }
        return jsonArray;
    }
}
