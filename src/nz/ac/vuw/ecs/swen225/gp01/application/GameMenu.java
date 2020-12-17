package nz.ac.vuw.ecs.swen225.gp01.application;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * This class exists to override JMenus with the formatting wanted for this program, making the GUI class neater
 */
public class GameMenu extends JMenu {

    private Border border = new LineBorder(GUI.ollieGrey);

    /**
     * Constructs the GameMenu
     * @param text - the text to display on the GameMenu
     */
    public GameMenu(String text) {
        super(text);
        this.setBackground(GUI.ollieGrey);
        this.setForeground(Color.WHITE);
        this.setBorderPainted(false);
    }

    /**
     * Controls how the JPopupMenu created on hover is displayed
     * @return - the JPopupMenu created this way
     */
    @Override
    public JPopupMenu getPopupMenu() {
        JPopupMenu menu = super.getPopupMenu();
        menu.setBorder(border);
        menu.setBackground(GUI.ollieGrey);
        return menu;
    }

}
