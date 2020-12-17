package nz.ac.vuw.ecs.swen225.gp01.application;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Helper class to format JButtons used in dialogs
 */
public class GameDialogButton extends JButton {

    /**
     * Constructs the GameDialogButton with no text displayed in it
     */
    public GameDialogButton() {
        super();
        setFormatting();
    }

    /**
     * Constructs the GameDialogButton with text in it
     * @param text - the text to be displayed in the GameDialogButton
     */
    public GameDialogButton(String text) {
        super(text);
        setFormatting();
    }

    /**
     * Helper method to help set the formatting of the GameDialogButton
     */
    private void setFormatting() {
        this.setBackground(new Color(0x353535));
        this.setForeground(Color.WHITE);
        this.setBorder(new LineBorder(GUI.ollieGrey, 4, true));
        this.setMinimumSize(new Dimension(120, 80));
        this.setMaximumSize(new Dimension(120, 80));
        this.setPreferredSize(new Dimension(120, 80));
    }


}


