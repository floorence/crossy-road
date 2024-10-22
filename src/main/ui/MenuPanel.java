package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//JPanel that shows a title and buttons to go to CharactersPanel, HighScoresPanel, and WorldPanel
public class MenuPanel extends JPanel {
    JPanel panels;
    CrossyRoad main;

    //EFFECTS: creates a new MenuPanel
    public MenuPanel(JPanel panels, CrossyRoad main) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setAlignmentX(Component.CENTER_ALIGNMENT);
        //setAlignmentY(TOP_ALIGNMENT);
        this.main = main;
        this.panels = panels;
        JLabel title = new JLabel("Crossy Road", JLabel.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        addQuitB();
        add(title);
        addButtons();
    }

    //MODIFIES: this
    //EFFECTS: adds buttons to go to different JPanels
    private void addButtons() {
        JButton playB = new JButton("play");
        playB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) panels.getLayout();
                cardLayout.show(panels, "world");
                main.setPreferredSize(new Dimension(500, 500));
                main.setMinimumSize(new Dimension(500, 500));
            }
        });
        playB.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(playB);
        JButton charactersB = new JButton("characters");
        charactersB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) panels.getLayout();
                cardLayout.show(panels, "characters");
                //main.pack();
            }
        });
        charactersB.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(charactersB);
        addHighScoreButton();
    }

    //MODIFIES: this
    //EFFECTS: adds button to go to HighScoresPanel
    private void addHighScoreButton() {
        JButton highScoresB = new JButton("high scores");
        highScoresB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) panels.getLayout();
                cardLayout.show(panels, "highscores");
                //main.pack();
            }
        });
        add(highScoresB);
        highScoresB.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    //MODIFIES: this
    //EFFECTS: adds button to quit the program
    private void addQuitB() {
        JPanel quitPanel = new JPanel();
        quitPanel.setLayout(new BoxLayout(quitPanel, BoxLayout.X_AXIS));
        JButton quitB = new JButton("quit");
        quitB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.gameOver();
            }
        });
        quitB.setAlignmentX(Component.RIGHT_ALIGNMENT);
        quitPanel.add(Box.createHorizontalGlue());
        quitPanel.add(quitB);
        quitPanel.setPreferredSize(new Dimension(400, 50));
        quitPanel.setMaximumSize(new Dimension(400, 50));
        add(quitPanel);
    }


}
