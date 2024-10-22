package ui;

import model.Character;
import model.CharactersList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//JPanel that shows characters with buttons to show the previous and next character and button to
//add a new character
public class CharactersPanel extends JPanel {
    JPanel panels;
    JPanel characters;
    CrossyRoad main;
    CharactersList list;
    JPanel noCharacters;

    //EFFECTS: creates a new CharactersPanel
    public CharactersPanel(JPanel panels, CharactersList list, CrossyRoad main) {
        this.main = main;
        this.panels = panels;
        this.list = list;
        setLayout(new BorderLayout());
        addTitleAndBackB();
        characters = new JPanel(new CardLayout());
        noCharacters = new JPanel();
        JLabel noLbl = new JLabel("No Characters");
        noCharacters.add(noLbl);
        if (list.getCharacters().isEmpty()) {
            characters.add(noCharacters);
        } else {
            for (model.Character c : list.getCharacters()) {
                CharacterPanel characterPanel = new CharacterPanel(c, characters, this, main);
                characters.add(characterPanel);
            }
        }
        addPrevAndNextBs();
        addAddB(list);
        add(characters, BorderLayout.CENTER);

    }

    //MODIFIES: this
    //EFFECTS: adds a panel with text saying "No Characters", this panel gets added when there
    //are no characters in the list and gets removed when the first character gets added
    public void addNoCharactersPanel() {
        characters.add(noCharacters);

    }

    //MODIFIES: this
    //EFFECTS: adds title JLabel and JButton to go back to menu
    private void addTitleAndBackB() {
        JPanel titleAndBackB = new JPanel();
        titleAndBackB.setLayout(new BoxLayout(titleAndBackB, BoxLayout.LINE_AXIS));
        JLabel title = new JLabel("Characters");
        JButton backB = new JButton("back");
        titleAndBackB.add(title);
        titleAndBackB.add(backB);
        backB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) panels.getLayout();
                cardLayout.first(panels);
            }
        });
        add(titleAndBackB, BorderLayout.PAGE_START);
    }

    //MODIFIES: this
    //EFFECTS: adds buttons to go to the previous and next character
    private void addPrevAndNextBs() {
        JButton prevB = new JButton("<");
        JButton nextB = new JButton(">");
        add(prevB, BorderLayout.LINE_START);
        add(nextB, BorderLayout.LINE_END);
        prevB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) characters.getLayout();
                cardLayout.previous(characters);
            }
        });
        nextB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) characters.getLayout();
                cardLayout.next(characters);
            }
        });
    }

    //MODIFIES: this, list
    //EFFECTS: adds button to add a new character
    private void addAddB(CharactersList list) {
        JButton addB = new JButton("add character");
        addB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeFrame(list);
            }
        });
        add(addB, BorderLayout.PAGE_END);
    }

    //MODIFIES: list
    //EFFECTS: Presents user with a window asking the name and size of the character they want to add
    @SuppressWarnings("methodlength")
    private void makeFrame(CharactersList list) {
        CenterableJFrame makeFrame = new CenterableJFrame();
        makeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel questions = new JPanel();
        questions.setLayout(new BoxLayout(questions, BoxLayout.PAGE_AXIS));
        JTextField nameFld = new JTextField("", 20);
        JLabel enterName = new JLabel("enter name: ");
        JPanel namePanel = new JPanel();
        namePanel.add(enterName);
        namePanel.add(nameFld);
        JTextField sizeFld = new JTextField("", 20);
        JLabel enterSize = new JLabel("enter size: ");
        JPanel sizePanel = new JPanel();
        sizePanel.add(enterSize);
        sizePanel.add(sizeFld);
        JButton addB2 = new JButton("add character");
        questions.add(namePanel);
        questions.add(sizePanel);
        questions.add(addB2);
        addB2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.Character c = new model.Character(nameFld.getText(), Integer.parseInt(sizeFld.getText()));
                //TODO: handle when they enter not an integer for size
                list.addCharacter(c);
                CharacterPanel characterPanel = new CharacterPanel(c, characters, getThis(), main);
                characters.add(characterPanel);
                if (list.getCharacters().size() == 1) {
                    characters.remove(noCharacters);
                }
                makeFrame.setVisible(false);
                makeFrame.dispose();
            }
        });
        makeFrame.add(questions);
        makeFrame.pack();
        makeFrame.centreOnScreen();
        makeFrame.setVisible(true);
    }

    //EFFECTS: returns this since I cant say this in the ActionListener T-T
    private CharactersPanel getThis() {
        return this;
    }

}

