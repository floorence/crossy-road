package ui;

import model.*;
import model.Character;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CrossyRoad extends CenterableJFrame {
    private CharactersList characters;
    private Character character;
    private HighScoresList highScores;
    private JPanel panels;
    private World world;
    HighScoresPanel scoresPanel;

    private static final String WORLD_STORE = "./data/world.json";
    private static final String LIST_STORE = "./data/characterslist.json";
    private static final String SCORES_STORE = "./data/highscores.json";

    //EFFECTS: initializes a new Crossy Road game
    public CrossyRoad() {
        super("Crossy Road");
        //setPreferredSize(new Dimension(500, 200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (hasWorldStore() || hasCharactersStore() || hasHighScoresStore()) {
            askLoadEverything();
        }
        EventLog.getInstance().clear();
    }

    //MODIFIES: this
    //EFFECTS: adds panels for the game, characters list, and high scores
    private void addAllPanels() {
        panels = new JPanel(new CardLayout());
        MenuPanel menuPanel = new MenuPanel(panels, this);
        CharactersPanel charPanel = new CharactersPanel(panels, characters, this);
        scoresPanel = new HighScoresPanel(panels, highScores);
        WorldPanel worldPanel = new WorldPanel(panels, world, this);
        panels.add(menuPanel, "menu");
        panels.add(charPanel, "characters");
        panels.add(scoresPanel, "highscores");
        panels.add(worldPanel, "world");
        /*
        menuPanel.setPreferredSize(new Dimension(400, 200));
        menuPanel.setMaximumSize(new Dimension(400, 200));
        charPanel.setPreferredSize(new Dimension(400, 200));
        scoresPanel.setPreferredSize(new Dimension(400, 200));
        worldPanel.setPreferredSize(new Dimension(500, 500));
        worldPanel.setMinimumSize(new Dimension(500, 500));
        */
        add(panels);
        pack();
        centreOnScreen();
        setVisible(true);
        addWindowL(this);
    }

    //MODIFIES: this
    //EFFECTS: ends game by closing the window and asking user to save
    public void gameOver() {
        askSaveEverything();
        dispose();

    }

    //MODIFIES: this
    //EFFECTS: selects given character to be the one in the game
    public void selectCharacter(Character c) {
        character = c;
        world.setCharacter(c);
    }

    //MODIFIES: this
    //EFFECTS: removes given character from list of characters and returns true if list is empty after removing
    public boolean removeCharacter(Character c) {
        characters.removeCharacter(c);
        if (characters.getCharacters().isEmpty()) {
            return true;
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: Presents user with checkboxes that they can click to select which elements of the game to save
    private void askSaveEverything() {
        CenterableJFrame saveFrame = new CenterableJFrame();
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel("Select everything you want to save: ");
        JCheckBox saveWorld = new JCheckBox("world");
        JCheckBox saveCharacters = new JCheckBox("characters");
        JCheckBox saveHighScores = new JCheckBox("high scores");
        saveWorld.setSelected(true);
        saveCharacters.setSelected(true);
        saveHighScores.setSelected(true);
        savePanel.add(title);
        savePanel.add(saveWorld);
        savePanel.add(saveCharacters);
        savePanel.add(saveHighScores);
        savePanel.add(saveButton(saveWorld, saveCharacters, saveHighScores, saveFrame));
        saveFrame.add(savePanel);
        saveFrame.pack();
        saveFrame.centreOnScreen();
        saveFrame.setVisible(true);
    }

    private void addWindowL(JFrame frame) {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                printLogs();
            }
        });
    }

    private void printLogs() {
        String str = "";
        for (model.Event event: EventLog.getInstance()) {
            str += event.getDescription() + "\n";
        }
        System.out.println(str);
    }

    //MODIFIES: this, saveFrame
    //EFFECTS: returns a save button which, when pressed, saves the elements of the game that the
    //user selected
    private JButton saveButton(JCheckBox saveW, JCheckBox saveC, JCheckBox saveS, CenterableJFrame saveFrame) {
        JButton enter = new JButton("save");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveW.isSelected()) {
                    saveWorld();
                }
                if (saveC.isSelected()) {
                    saveCharacters();
                }
                if (saveS.isSelected()) {
                    saveHighScores();
                }
                saveFrame.dispose();
                printLogs();
                System.exit(0);
            }
        });
        return enter;
    }

    //MODIFIES: this
    //EFFECTS: Presents user with checkboxes that they can click to select which elements of the game to load
    @SuppressWarnings("methodlength")
    private void askLoadEverything() {
        CenterableJFrame loadFrame = new CenterableJFrame();
        JPanel loadPanel = new JPanel();
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel("Select everything you want to load: ");
        JCheckBox loadWorld = new JCheckBox("world");
        JCheckBox loadCharacters = new JCheckBox("characters");
        JCheckBox loadHighScores = new JCheckBox("high scores");
        loadPanel.add(title);
        if (hasWorldStore()) {
            loadWorld.setSelected(true);
            loadPanel.add(loadWorld);
        } else {
            this.world = character == null ? new World() : new World(character);
        }
        if (hasCharactersStore()) {
            loadCharacters.setSelected(true);
            loadPanel.add(loadCharacters);
        } else {
            characters = new CharactersList();
        }
        if (hasHighScoresStore()) {
            loadHighScores.setSelected(true);
            loadPanel.add(loadHighScores);
        } else {
            highScores = new HighScoresList();
        }
        loadPanel.add(loadButton(loadWorld, loadCharacters, loadHighScores, loadFrame));
        loadFrame.add(loadPanel);
        loadFrame.pack();
        loadFrame.centreOnScreen();
        loadFrame.setVisible(true);

    }

    //MODIFIES: this, loadFrame
    //EFFECTS: returns a load button which, when pressed, loads the elements of the game that the
    //user selected
    private JButton loadButton(JCheckBox loadW, JCheckBox loadC, JCheckBox loadS, CenterableJFrame loadFrame) {
        JButton enter = new JButton("load");
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (loadW.isSelected()) {
                    loadWorld();
                } else {
                    world = character == null ? new World() : new World(character);
                }
                if (loadC.isSelected()) {
                    loadCharacters();
                } else {
                    characters = new CharactersList();
                }
                if (loadS.isSelected()) {
                    loadHighScores();
                } else {
                    highScores = new HighScoresList();
                }
                addAllPanels();
                loadFrame.dispose();
            }
        });
        return enter;
    }

    //EFFECTS: returns true if the world saved to file is not empty
    private boolean hasWorldStore() {
        JsonReader jsonReader = new JsonReader(WORLD_STORE);
        try {
            World worldRead = jsonReader.readWorld();
            if (worldRead.getWorld().isEmpty()) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //EFFECTS: returns true if the CharactersList saved to file is not empty
    private boolean hasCharactersStore() {
        JsonReader jsonReader = new JsonReader(LIST_STORE);
        try {
            CharactersList list = jsonReader.readList();
            if (list.getCharacters().isEmpty()) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //EFFECTS: returns true if the HighScoresList saved to file is not empty
    private boolean hasHighScoresStore() {
        JsonReader jsonReader = new JsonReader(SCORES_STORE);
        try {
            HighScoresList list = jsonReader.readScores();
            if (list.getScores().length == 0) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //MODIFIES: this
    //EFFECTS: loads world from file
    private void loadWorld() {
        JsonReader jsonReader = new JsonReader(WORLD_STORE);
        try {
            world = jsonReader.readWorld();
        } catch (IOException e) {
            JDialog error = new JDialog();
            JLabel errorLbl = new JLabel("Error loading: File not found");
            error.add(errorLbl);
            error.setVisible(true);
        }
    }

    //MODIFIES: this
    //EFFECTS: loads CharactersList from file
    private void loadCharacters() {
        JsonReader jsonReader = new JsonReader(LIST_STORE);
        try {
            characters = jsonReader.readList();
        } catch (IOException e) {
            JDialog error = new JDialog();
            JLabel errorLbl = new JLabel("Error loading: File not found");
            error.add(errorLbl);
            error.setVisible(true);
        }
    }

    //MODIFIES: this
    //EFFECTS: loads HighScoresList from file
    private void loadHighScores() {
        JsonReader jsonReader = new JsonReader(SCORES_STORE);
        try {
            highScores = jsonReader.readScores();
        } catch (IOException e) {
            JDialog error = new JDialog();
            JLabel errorLbl = new JLabel("Error loading: File not found");
            error.add(errorLbl);
            error.setVisible(true);
        }
    }

    //EFFECTS: saves characters to file
    private void saveCharacters() {
        JsonWriter jsonWriter = new JsonWriter(LIST_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(characters);
            jsonWriter.close();
            //System.out.println("Saved successfully!");
        } catch (FileNotFoundException e) {
            JDialog error = new JDialog();
            JLabel errorLbl = new JLabel("Error saving: File not found");
            error.add(errorLbl);
            error.setVisible(true);
        }
    }

    //EFFECTS: saves world to file
    private void saveWorld() {
        JsonWriter jsonWriter = new JsonWriter(WORLD_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(world);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            JDialog error = new JDialog();
            JLabel errorLbl = new JLabel("Error saving: File not found");
            error.add(errorLbl);
            error.setVisible(true);
        }
    }

    //EFFECTS: saves high scores to file
    private void saveHighScores() {
        JsonWriter jsonWriter = new JsonWriter(SCORES_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(highScores);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            JDialog error = new JDialog();
            JLabel errorLbl = new JLabel("Error saving: File not found");
            error.add(errorLbl);
            error.setVisible(true);
        }
    }

    //EFFECTS: returns true if given score is a high score. If so, resets HighScoresPanel
    public boolean isHighScore(int score) {
        if (highScores.isHighScore(score)) {
            panels.remove(scoresPanel);
            scoresPanel = new HighScoresPanel(panels, highScores);
            panels.add(scoresPanel, "highscores");
            return true;
        }
        return false;
    }

}
