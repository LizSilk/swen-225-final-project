package nz.ac.vuw.ecs.swen225.gp01.application;

import nz.ac.vuw.ecs.swen225.gp01.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp01.recordAndReplay.Move;
import nz.ac.vuw.ecs.swen225.gp01.renderer.SFX;
import nz.ac.vuw.ecs.swen225.gp01.renderer.SoundEffects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Controls the game through a modified KeyListener
 */
public class GameController implements KeyListener {
    public ArrayList<Move> history = new ArrayList<>(); //Array list to keep track of moves made by actors

    // keys to control movement
    protected int rightKey = KeyEvent.VK_RIGHT;
    protected int upKey = KeyEvent.VK_UP;
    protected int downKey = KeyEvent.VK_DOWN;
    protected int leftKey = KeyEvent.VK_LEFT;

    private Maze maze;
    private GUI gui;

    /**
     * Constructs the GameController object
     */
    public GameController(GUI gui) {
        super();
        this.gui = gui;
    }

    /**
     * Determines what key has been pressed and carries out the appropriate action (moving the player).
     * @param e - the KeyEvent of the key pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        int invSize = maze.getPlayer().getInventory().size();
        int treasureCount = maze.getPlayer().getTreasureCount();
        boolean moveIntoDoor = false;
        boolean collectsCollectible = false;

        if (key == KeyEvent.VK_SPACE) {
            Main.main.stopAnyTimers();
            gui.pauseDialog();
        }

        if (key == rightKey) {
            if (maze.getTile(maze.getPlayer().getRow(), maze.getPlayer().getCol()+1).hasDoor()) {
                moveIntoDoor = true;
            }
            if (maze.getTile(maze.getPlayer().getRow(), maze.getPlayer().getCol()+1).isInfo()) {
                SoundEffects.play(SFX.DISPLAY_INFO);
            }
            if (maze.getTile(maze.getPlayer().getRow(), maze.getPlayer().getCol()+1).hasCollectible()) {
                collectsCollectible = true;
            }
            if (maze.move(maze.getPlayer(), 'r')){
                playSFX(moveIntoDoor, invSize, treasureCount);
                if (collectsCollectible || moveIntoDoor) {
                    gui.updateInfo(maze);
                }
            } else {
                SoundEffects.play(SFX.DENIED);
            }
            Move move = new Move("player", Main.main.tenthSecondsTime,'r', Main.main.currentLevel);
            history.add(move);

            gui.updateMaze(maze);
        }

        if (key == upKey) {
            if (maze.getTile(maze.getPlayer().getRow()-1, maze.getPlayer().getCol()).hasDoor()) {
                moveIntoDoor = true;
            }
            if (maze.getTile(maze.getPlayer().getRow()-1, maze.getPlayer().getCol()).isInfo()) {
                SoundEffects.play(SFX.DISPLAY_INFO);
            }
            if (maze.getTile(maze.getPlayer().getRow()-1, maze.getPlayer().getCol()).hasCollectible()) {
                collectsCollectible = true;
            }
            if (maze.move(maze.getPlayer(), 'u')){
                playSFX(moveIntoDoor, invSize, treasureCount);
                if (collectsCollectible || moveIntoDoor) {
                    gui.updateInfo(maze);
                }
            } else {
                SoundEffects.play(SFX.DENIED);
            }
            Move move = new Move("player", Main.main.tenthSecondsTime, 'u', Main.main.currentLevel);
            history.add(move);

            gui.updateMaze(maze);
        }

        if (key == downKey) {
            if (maze.getTile(maze.getPlayer().getRow()+1, maze.getPlayer().getCol()).hasDoor()) {
                moveIntoDoor = true;
            }
            if (maze.getTile(maze.getPlayer().getRow()+1, maze.getPlayer().getCol()).isInfo()) {
                SoundEffects.play(SFX.DISPLAY_INFO);
            }
            if (maze.getTile(maze.getPlayer().getRow()+1, maze.getPlayer().getCol()).hasCollectible()) {
                collectsCollectible = true;
            }
            if (maze.move(maze.getPlayer(), 'd')){
                playSFX(moveIntoDoor, invSize, treasureCount);
                if (collectsCollectible || moveIntoDoor) {
                    gui.updateInfo(maze);
                }
            } else {
                SoundEffects.play(SFX.DENIED);
            }
            Move move = new Move("player", Main.main.tenthSecondsTime, 'd', Main.main.currentLevel);
            history.add(move);

            gui.updateMaze(maze);
        }

        if (key == leftKey) {
            if (maze.getTile(maze.getPlayer().getRow(), maze.getPlayer().getCol()-1).hasDoor()) {
                moveIntoDoor = true;
            }
            if (maze.getTile(maze.getPlayer().getRow(), maze.getPlayer().getCol()-1).isInfo()) {
                SoundEffects.play(SFX.DISPLAY_INFO);
            }
            if (maze.getTile(maze.getPlayer().getRow(), maze.getPlayer().getCol()-1).hasCollectible()) {
                collectsCollectible = true;
            }
            if (maze.move(maze.getPlayer(), 'l')){
                playSFX(moveIntoDoor, invSize, treasureCount);
                if (collectsCollectible || moveIntoDoor) {
                    gui.updateInfo(maze);
                }
            } else {
                SoundEffects.play(SFX.DENIED);
            }
            Move move = new Move("player", Main.main.tenthSecondsTime, 'l', Main.main.currentLevel);
            history.add(move);

            gui.updateMaze(maze);
        }

    }

    /**
     * Helper method to process the sounds to be made on a given move command.
     * @param moveIntoDoor - boolean for whether the move is into a door
     * @param invSize - the current player's inventory size
     * @param treasureCount - the current player's treasure count
     */
    private void playSFX(boolean moveIntoDoor, int invSize, int treasureCount) {
        SoundEffects.play(SFX.MOVE);
        if (maze.getPlayer().getInventory().size() > invSize ||
                maze.getPlayer().getTreasureCount() > treasureCount) {
            SoundEffects.play(SFX.PICKUP);
        }
        if (moveIntoDoor) {
            SoundEffects.play(SFX.UNLOCK);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Helper method to reset the maze the game controller is operating on
     * @param maze - the maze to use
     */
    protected void resetGameController(Maze maze) {
        this.maze = maze;
    }
}
