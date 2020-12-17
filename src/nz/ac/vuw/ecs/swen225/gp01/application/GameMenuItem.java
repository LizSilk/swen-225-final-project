package nz.ac.vuw.ecs.swen225.gp01.application;

import javax.swing.*;
import java.awt.*;

/**
 * This class exists to override JMenuItems with the formatting wanted for this program, making the GUI class neater
 */
public class GameMenuItem extends JMenuItem {

    /**
     * Constructs the GameMenuItem
     * @param text - the text to display on the GameMenuItem
     */
    public GameMenuItem(String text) {
        super(text);
        this.setBackground(GUI.ollieGrey);
        this.setForeground(Color.WHITE);
        this.setBorderPainted(false);
    }

}
