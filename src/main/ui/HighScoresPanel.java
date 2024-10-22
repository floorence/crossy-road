package ui;

import model.HighScoresList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//JPanel that displays the top 5 high scores achieved
public class HighScoresPanel extends JPanel {
    JPanel panels;

    //EFFECTS: creates a new HighScoresPanel with high scores and button to go back to menu
    public HighScoresPanel(JPanel panels, HighScoresList list) {
        this.panels = panels;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton backB = new JButton("back");
        backB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) panels.getLayout();
                cardLayout.first(panels);
            }
        });
        add(backB);
        JLabel title = new JLabel("High scores:");
        add(title);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        for (int score: list.getScores()) {
            JLabel scoreLbl = new JLabel("" + score);
            add(scoreLbl);
        }
    }
}
