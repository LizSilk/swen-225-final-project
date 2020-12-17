package nz.ac.vuw.ecs.swen225.gp01.application;

import nz.ac.vuw.ecs.swen225.gp01.maze.EnemyInterface;
import nz.ac.vuw.ecs.swen225.gp01.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp01.persistence.LevelLoader;
import nz.ac.vuw.ecs.swen225.gp01.recordAndReplay.LoadReplay;
import nz.ac.vuw.ecs.swen225.gp01.recordAndReplay.Move;
import nz.ac.vuw.ecs.swen225.gp01.recordAndReplay.Replayer;
import nz.ac.vuw.ecs.swen225.gp01.recordAndReplay.SaveReplay;
import nz.ac.vuw.ecs.swen225.gp01.renderer.SFX;
import nz.ac.vuw.ecs.swen225.gp01.renderer.SoundEffects;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main class for the game's functionality
 * Runs the game/recordAndReplay playback, and controls the updating of the display
 */
public class Main {

    public static Main main;

    private GUI gui;
    private Maze maze;
    private final String levelFilepath = "levels//level";
    private Timer gameTimer;        //timer for the ticks of the gameplay itself

    protected int tenthSecondsTime;     //time in a given level (currently in tenths of a second)
    protected int displayTime;          //time to display to the InfoPanel (only updates every second)
    protected int currentLevel = 1;     //can be used to update the level

    protected static GameController gameController;
    protected static ReplayController replayController = new ReplayController();

    /**
     * Constructor for the App class
     * Creates a new application which runs the game's interface
     * Loads first level, constructs & updates GUI, and initialises the game controller.jl.
     */
    public Main() {
        gui = new GUI();

        SoundEffects.play(SFX.PICKUP);
        File directory = new File("levels/");
        for (int i = 0; i < directory.list().length; i++) {
            if (new File("levels/lastSession" + i + ".json").isFile()) {
                maze = LevelLoader.loadLevel("levels/lastSession" + i);
                tenthSecondsTime = LevelLoader.loadTime("levels/lastSession" + i);
                currentLevel = i;
                break;
            } else {
                maze = LevelLoader.loadLevel(levelFilepath + currentLevel);
                tenthSecondsTime = LevelLoader.loadTime(levelFilepath + currentLevel);
            }
        }
        displayTime = tenthSecondsTime;
        gameController = new GameController(gui);
        gameController.resetGameController(maze);
        GUI.frame.addKeyListener(gameController);
        gui.updateAll(maze, this);

        startTimer();

    }

    /**
     * Main method for the game
     * Constructs a new App
     * @param args
     */
    public static void main(String[] args) {
        main = new Main();
    }

    /**
     * Helper method to select and load in a given level
     * @param level - the number of the level to load
     */
    protected void changeLevel(int level) {
        changeLevelFromFilename(levelFilepath + level);
    }

    /**
     * Returns the current state of the maze
     * @return - a Maze object
     */
    protected Maze getMaze() {
        return maze;
    }

    /**
     * Loads a level from a given filename
     * @param filename - the file of the level to load
     */
    protected void changeLevelFromFilename(String filename) {
        stopAnyTimers();
        gameController.history.clear();
        String levelNumber = filename.replaceAll("[\\D.]", "");
        if (levelNumber.length() > 0) {
            // prevents crashing when loading a level saved with no level number/no numbers in the filename
            currentLevel = Integer.parseInt(levelNumber);
        }
        maze = LevelLoader.loadLevel(filename);
        tenthSecondsTime = LevelLoader.loadTime(filename);
        displayTime = tenthSecondsTime;
        gameController.resetGameController(maze);
        startTimer();
        gui.updateAll(maze, this);
    }

    /**
     * Loads a replay from a replay file and starts running it
     * @param filename - the filename of the replay file being loaded
     */
    protected void loadReplay(String filename) {
        try {
            stopAnyTimers();
            if (LoadReplay.decodeList(filename) != null) {
                replayController.replayer = new Replayer(LoadReplay.decodeList(filename));
                maze = LevelLoader.loadLevel(levelFilepath + replayController.replayer.getLevel());
                replayController.replayer.setupTimes(LevelLoader.loadTime(levelFilepath +
                        replayController.replayer.getLevel()));
                replayController.replayer.autoReplay();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for the Record and Replay package to update the GUI when making a move
     * @param time - the time in the replay the move was made (to be displayed on the InfoPanel)
     */
    public void updateAll(int time) {
        this.displayTime = time;
        gui.updateAll(maze, main);
    }

    /**
     * Method for the Record and Replay package to move an actor when replaying
     * @param actor - the name of the actor to be moved
     * @param dir - the direction to move the actor
     */
    public void replayMove(String actor, char dir) {
        if (actor.equals("player")) {
            maze.move(maze.getPlayer(), dir);
        } else if (actor.contains("enemy")) {
            int enemyNumber = Integer.parseInt(actor.replace("enemy", ""));
            maze.move(maze.getEnemies().get(enemyNumber-1), dir);
        }
    }

    /**
     * Method for the Record and Replay package to update the maze back to the start of the replay.
     */
    public void updateMaze() {
        maze = LevelLoader.loadLevel(levelFilepath + replayController.replayer.getLevel());
    }

    /**
     * Starts the main game's timer.
     * Runs for as long as specified in tenthSecondsTime for a level, end of the timer results in game being lost.
     */
    protected void startTimer() {
        gameTimer = new Timer(100, event -> {
            tenthSecondsTime--;

            // updates the info panel each time the timer ticks over
            if (tenthSecondsTime % 10 == 0) {
                displayTime -= 10;
                gui.updateInfo(maze);

                // handles updating positions for all enemies on a given board.
                int loops = 1;
                for (EnemyInterface e : maze.getEnemies()) {
                    maze.move(e, 'u');
                    gameController.history.add(new Move("enemy" + loops, tenthSecondsTime, 'u', currentLevel));
                    gui.updateMaze(maze);
                    loops++;
                }
            }

            // lose the game when the time runs out
            if (tenthSecondsTime < 0 ) {
                loseGame();
            }
        });

        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    /**
     * Runs the game win sequence
     */
    public void winLevel() {
        if (Arrays.asList(GUI.frame.getKeyListeners()).contains(gameController)) {
            // will only run the game win sequence if the game is being played, not if its being replayed.
            gameTimer.stop();
            SoundEffects.play(SFX.WIN);
            gui.levelEndDialog(true);
        }
    }

    /**
     * Runs the lose game sequence for the game
     */
    public void loseGame() {
        if (Arrays.asList(GUI.frame.getKeyListeners()).contains(gameController)) {
            gameTimer.stop();
            SoundEffects.play(SFX.DEATH);
            gui.levelEndDialog(false);
        }
    }

    /**
     * Saves a replay by opening up a JFileChooser dialog to create a file, and giving the game history and filename to
     * the recordAndReplay.SaveReplay constructor.
     */
    protected void saveReplay() {
        try {
            stopAnyTimers();
            String filename = gui.saveReplay();
            if (filename != null) { // won't save the game if no file is selected
                if (filename.contains(".json")) {
                    filename = filename.replace(".json", "");
                }
                new SaveReplay(gameController.history, filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops any timers currently running
     */
    protected void stopAnyTimers() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

}
