package ui;

import model.Character;
import model.CharactersList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//JPanel that shows one character with its name, image, size, and buttons to remove and select it
public class CharacterPanel extends JPanel {

    //EFFECTS: creates a new CharacterPanel
    public CharacterPanel(model.Character c, JPanel characters, CharactersPanel charactersPanel, CrossyRoad main) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel nameLabel = new JLabel(c.getName());
        ImageIcon image = new ImageIcon(c.getImgSource());
        JLabel imgLabel = new JLabel(image);
        JLabel sizeLabel = new JLabel("" + c.getSize());
        JButton select = new JButton("select");
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);
        add(imgLabel);
        bottomPanel.add(sizeLabel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(select);
        select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                main.selectCharacter(c);
            }
        });
        addRemoveB(bottomPanel, main, charactersPanel, characters, c);
        add(bottomPanel);
    }

    //MODIFIES: characters
    //EFFECTS: removes this CharacterPanel from the CharactersPanel
    private void removeFromPanels(JPanel characters) {
        characters.remove(this);
    }

    //MODIFIES: this, botPanel, main, chPanel, characters
    //EFFECTS: adds the button to remove this character
    private void addRemoveB(JPanel botPanel, CrossyRoad main, CharactersPanel chPanel, JPanel characters, Character c) {
        JButton remove = new JButton("remove");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (main.removeCharacter(c)) {
                    chPanel.addNoCharactersPanel();
                }
                removeFromPanels(characters);
                CardLayout cardLayout = (CardLayout) characters.getLayout();
                cardLayout.next(characters);

            }
        });
        botPanel.add(remove);
    }

}
