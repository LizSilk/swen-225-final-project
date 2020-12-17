package nz.ac.vuw.ecs.swen225.gp01.application;

import nz.ac.vuw.ecs.swen225.gp01.maze.*;
import nz.ac.vuw.ecs.swen225.gp01.persistence.JSONEncoder;
import nz.ac.vuw.ecs.swen225.gp01.renderer.*;
import nz.ac.vuw.ecs.swen225.gp01.renderer.Renderer;   //idk why I have to import this separately? but otherwise it throws a hissyfit

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.regex.Pattern;

import static java.awt.GridBagConstraints.BOTH;

/**
 * Initialises all the display elements using swing
 */
public class GUI {

    public static final JFrame frame = new JFrame("Chap's Challenge");
    private JPanel panel;
    private Renderer renderer;
    public static final Color ollieGrey = new Color(0x222222);

    private MazeRenderer mazeRenderer;
    private InfoPanelRenderer infoPanelRenderer;

    protected JDialog pauseDialog;

    /**
     * Creates the GUI.
     * This involves initialising a JFrame and adding JComponents and a JMenuBar to it, and making it visible.
     */
    public GUI() {
        panel = new JPanel(new GridBagLayout());
        panel.setSize(960, 640);

        mazeRenderer = new MazeRenderer(640, 640);
        infoPanelRenderer = new InfoPanelRenderer(320, 640);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 2f/3;

        panel.add(mazeRenderer, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1f/3;

        panel.add(infoPanelRenderer, gbc);

        frame.add(panel);
        frame.setIconImage(Renderer.loadImage("img//player.png"));

        setMenuBar();

        renderer = new Renderer(mazeRenderer, infoPanelRenderer);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveGameState();
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * Method for adding the JMenuBar and all contained JMenu and JMenuItem objects to it.
     */
    private void setMenuBar() {

        // creates menu bar
        JMenuBar menuBar = new JMenuBar();

        // creates drop-down menus
        GameMenu fileMenu = new GameMenu("File");
        GameMenu optionsMenu = new GameMenu("Options");
        GameMenu levelMenu = new GameMenu("Level");
        GameMenu helpMenu = new GameMenu("Help");
        GameMenu randrMenu = new GameMenu("Record and Replay");

        // adds menus to the bar
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(levelMenu);
        menuBar.add(helpMenu);
        menuBar.add(randrMenu);

        // creates and adds menu items to menus, and adds their functionality
        GameMenuItem startGame = new GameMenuItem("Start Game");
        fileMenu.add(startGame);
        startGame.addActionListener(e -> Main.main.changeLevel(1));
        startGame.setAccelerator(KeyStroke.getKeyStroke('1', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        GameMenuItem exitWithoutSaving = new GameMenuItem("Exit Without Saving");
        fileMenu.add(exitWithoutSaving);
        exitWithoutSaving.addActionListener(e -> {
            File file = new File("levels/lastSession.json");
            if (file.isFile()) {
                file.delete();
            }
            System.exit(0);
        });
        exitWithoutSaving.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        GameMenuItem exitWithSaving = new GameMenuItem("Save & Exit");
        fileMenu.add(exitWithSaving);
        exitWithSaving.addActionListener(e -> {
            saveGameState();
            System.exit(0);
        });
        exitWithSaving.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        //functionality never added to this
        GameMenuItem changeControls = new GameMenuItem("Controls");
        optionsMenu.add(changeControls);

        GameMenuItem selectLevel = new GameMenuItem("Select Level");
        levelMenu.add(selectLevel);
        selectLevel.addActionListener(e -> {
            chooseLevelFromFilechooser();
            if (!Arrays.asList(frame.getKeyListeners()).contains(Main.gameController)) {
                frame.addKeyListener(Main.gameController);
            }
        });
        GameMenuItem skipLevel = new GameMenuItem("Skip Level");
        levelMenu.add(skipLevel);
        skipLevel.addActionListener(e -> {
            loadNextLevel();
            if (!Arrays.asList(frame.getKeyListeners()).contains(Main.gameController)) {
                frame.addKeyListener(Main.gameController);
            }
        });
        GameMenuItem previousLevel = new GameMenuItem("Previous Level");
        levelMenu.add(previousLevel);
        previousLevel.addActionListener(e -> {
            loadPreviousLevel();
            if (!Arrays.asList(frame.getKeyListeners()).contains(Main.gameController)) {
                frame.addKeyListener(Main.gameController);
            }
        });

        GameMenuItem readme = new GameMenuItem("Readme");
        helpMenu.add(readme);
        readme.addActionListener(e -> displayTextDialog("Help!", frame));

        GameMenuItem saveRecording = new GameMenuItem("Save Recording");
        randrMenu.add(saveRecording);
        saveRecording.addActionListener(e -> {
            Main.main.saveReplay();
            Main.main.startTimer();
        });
        GameMenuItem playRecording = new GameMenuItem("Play Back Recording");
        randrMenu.add(playRecording);
        playRecording.addActionListener(e -> runReplay());
        GameMenuItem stopPlayback = new GameMenuItem("Exit Playback Mode");
        randrMenu.add(stopPlayback);
        stopPlayback.setEnabled(false);
        stopPlayback.addActionListener(e -> stopReplay());

        // formats the menuBar, then adds it to the frame
        menuBar.setBackground(ollieGrey);
        menuBar.setOpaque(true);
        menuBar.setBorderPainted(false);

        frame.setJMenuBar(menuBar);
    }

    /**
     * Loads the previous level (if there is one)
     */
    private void loadPreviousLevel() {
        if (new File("levels//level" + (Main.main.currentLevel - 1) + ".json").isFile()) {
            Main.main.changeLevel(Main.main.currentLevel - 1);
        }
    }

    /**
     * Loads the next level (if there is one)
     */
    private void loadNextLevel() {
        if (new File("levels//level" + (Main.main.currentLevel + 1) + ".json").isFile()) {
            Main.main.changeLevel(Main.main.currentLevel + 1);
        }
    }

    /**
     * Runs a JFileChooser to select a level file to load.
     */
    private void chooseLevelFromFilechooser() {
        JFileChooser chooser = new JFileChooser("levels");
        chooser.setDialogTitle("Load a Level");
        FileFilter filter = new FileNameExtensionFilter("JSON files", "json");
        chooser.setFileFilter(filter);
        chooser.addChoosableFileFilter(filter);
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String file = chooser.getSelectedFile().getName().split("\\.")[0];
            file = "levels//" + file;
            Main.main.changeLevelFromFilename(file);
        }
    }

    /**
     * Switches the application into "Record and Replay" mode by changing the keyListener for it.
     * Pops up a JFileChooser dialog to select the replay to play.
     */
    private void runReplay() {
        Main.main.stopAnyTimers();
        JFileChooser chooser = new JFileChooser("replays");
        chooser.setDialogTitle("Load a Replay");
        FileFilter filter = new FileNameExtensionFilter("JSON files", "json");
        chooser.setFileFilter(filter);
        chooser.addChoosableFileFilter(filter);
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            frame.removeKeyListener(Main.gameController);
            frame.addKeyListener(Main.replayController);
            String file = chooser.getSelectedFile().getName().split("\\.")[0];
            Main.main.loadReplay(file);
            frame.getJMenuBar().getMenu(4).getItem(0).setEnabled(false);    // disables saveRecording JMenuItem
            frame.getJMenuBar().getMenu(4).getItem(1).setEnabled(false);    // disables playRecording JMenuItem
            frame.getJMenuBar().getMenu(4).getItem(2).setEnabled(true);     // enables stopPlayback JMenuItem
        } else {
            Main.main.startTimer();
        }

    }

    /**
     * Switches the application out of "Record and Replay" mode, making the game playable again
     */
    private void stopReplay() {
        frame.removeKeyListener(Main.replayController);
        Main.replayController.replayer.stopTimer();
        frame.addKeyListener(Main.gameController);
        frame.getJMenuBar().getMenu(4).getItem(0).setEnabled(true);     // enables saveRecording JMenuItem
        frame.getJMenuBar().getMenu(4).getItem(1).setEnabled(true);     // enables playRecording JMenuItem
        frame.getJMenuBar().getMenu(4).getItem(2).setEnabled(false);    // disables stopPlayback JMenuItem
        chooseLevelFromFilechooser();   // loads in a new level
    }

    /**
     * Returns a string of a filename.
     * This filename is selected from a JFileChooser SaveDialog, and is used to save
     * @return the saving filename, as a String
     */
    protected String saveReplay() {
        JFileChooser chooser = new JFileChooser("replays");
        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getName();
        }
        return null;    // should only reach here if no file is selected for saving
    }

    /**
     * Creates a dialog for winning/losing the game, with buttons to save a replay or move to the next level/restart
     * @param won - whether the player won or lost the level
     */
    protected void levelEndDialog(boolean won) {
        //setup JDialog
        JDialog endDialog = new JDialog(frame, "", true);
        endDialog.setSize(400, 250);
        endDialog.setLocationRelativeTo(frame);
        endDialog.setResizable(false);
        endDialog.getContentPane().setBackground(new Color(0x2d2d2d));

        //setup title
        JLabel title = new JLabel();
        title.setFont(new Font("SansSerif", Font.PLAIN, 40));
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        title.setForeground(Color.WHITE);

        //setup button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(400, 120));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        //setup buttons
        JButton saveButton = new GameDialogButton("Save Replay");
        saveButton.addActionListener(e -> {
            Main.main.saveReplay();
            endDialog.setVisible(false);
        });

        JButton changeButton = new GameDialogButton();

        //make changes for won/lost modes
        if (won) {
            endDialog.setTitle("Level Complete!");
            title.setText("Level Complete!");
            changeButton.setText("Next Level");
            changeButton.addActionListener(e -> {
                loadNextLevel();
                endDialog.setVisible(false);
            });
        } else {
            endDialog.setTitle("Level Failed >:(");
            title.setText("Level Failed >:(");
            changeButton.setText("Retry Level");
            changeButton.addActionListener(e -> {
                Main.main.changeLevel(Main.main.currentLevel);
                endDialog.setVisible(false);
            });
        }

        buttonPanel.add(changeButton);
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(saveButton);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.NORTH);
        endDialog.add(title);
        endDialog.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        endDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Main.main.stopAnyTimers();
                frame.removeKeyListener(Main.gameController);
            }
        });

        endDialog.setVisible(true);
    }

    /**
     * Creates a dialog box for when the game is paused.
     */
    protected void pauseDialog() {
        pauseDialog = new JDialog(frame, "Paused", true);
        pauseDialog.setResizable(false);
        pauseDialog.setSize(200, 100);
        pauseDialog.getContentPane().setBackground(new Color(0x2d2d2d));
        pauseDialog.setLocationRelativeTo(frame);
        pauseDialog.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pauseDialog.setVisible(false);
                    Main.main.startTimer();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) { }
        });

        JLabel pauseText = new JLabel("Paused");
        pauseText.setFont(new Font("SansSerif", Font.PLAIN, 40));
        pauseText.setForeground(Color.WHITE);
        pauseText.setHorizontalAlignment(SwingConstants.CENTER);
        pauseText.setVerticalAlignment(SwingConstants.CENTER);
        pauseDialog.add(pauseText);

        pauseDialog.setVisible(true);
    }

    /**
     * Saves the current game state for reloading purposes.
     */
    private void saveGameState() {
        File directory = new File("levels/");
        for (int i = 0; i < directory.list().length; i++) {
            File file = new File("levels/lastSession" + i + ".json");
            if (file.isFile()) {
                file.delete();
            }
        }
        JSONEncoder.encodeLevel(Main.main.getMaze(), Main.main.tenthSecondsTime, "lastSession" + Main.main.currentLevel);
    }

    /**
     * Updates all parts of the renderer.
     * @param maze - the maze to update with
     * @param main - the Main game to grab the time and level from
     */
    protected void updateAll(Maze maze, Main main) {
        updateMaze(maze);
        renderer.updateInfoPanel(maze, main.displayTime, main.currentLevel);
    }

    /**
     * Updates just the maze component in renderer
     * @param maze - the maze to update with
     */
    protected void updateMaze(Maze maze) {
        renderer.updateMaze(maze);
        if (maze.isLost()) {
            Main.main.loseGame();
        } else if (maze.isWon()) {
            Main.main.winLevel();
        }
    }

    /**
     * A generic method to make a dialog box filled with scrollable text.
     *
     * @param title       - Title for dialog window
     * @param parentFrame - Main frame of the program
     */
    public void displayTextDialog(String title, JFrame parentFrame) {
        // Sets up dialog box
        JDialog rules = new JDialog(parentFrame, title, true);
        rules.setSize(400, 400);

        // Formatting for Text Box
        JTextArea textBox = new JTextArea();
        try {
            textBox.read(new BufferedReader(new FileReader(new File("help"))), "Help");
        } catch (IOException e) {
            e.printStackTrace();
        }
        textBox.setLineWrap(true);
        textBox.setWrapStyleWord(true);
        textBox.setEditable(false);
        textBox.setBackground(ollieGrey);
        textBox.setForeground(Color.WHITE);

        // Scroll Bar Implementation & Formatting
        JScrollPane scroll = new JScrollPane(textBox);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add scrolling pane to dialog box & display box
        rules.add(scroll);
        rules.setVisible(true);
    }

    /**
     * Updates just the info panel component in renderer
     * @param maze - the maze to update with
     */
    protected void updateInfo(Maze maze) {
        renderer.updateInfoPanel(maze, Main.main.displayTime, Main.main.currentLevel);
    }
}
