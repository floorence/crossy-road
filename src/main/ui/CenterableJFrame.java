package ui;

import javax.swing.*;
import java.awt.*;

//class that extends JFrame, but you are able to easily centre it on screen by calling centreOnScreen
public class CenterableJFrame extends JFrame {

    //EFFECTS: creates a new CenterableJFrame with empty title
    public CenterableJFrame() {
        super("");
    }

    //EFFECTS: creates a new CenterableJFrame with given title
    public CenterableJFrame(String title) {
        super(title);
    }

    // Centres frame on desktop
    // modifies: this
    // effects:  location of frame is set so frame is centred on desktop
    // Method taken from SpaceInvaders: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git
    protected void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }
}
