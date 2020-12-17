package nz.ac.vuw.ecs.swen225.gp01.application;

import nz.ac.vuw.ecs.swen225.gp01.recordAndReplay.Replayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class to create unique KeyListener for Replay playback
 */
public class ReplayController implements KeyListener {

    protected int playPause = KeyEvent.VK_SPACE;
    protected int speedUp = KeyEvent.VK_UP;
    protected int speedDown = KeyEvent.VK_DOWN;
    protected int stepKey = KeyEvent.VK_RIGHT;

    protected Replayer replayer;

    private boolean isAuto = true;

    /**
     * Constructs the ReplayController using the superclass constructor
     */
    public ReplayController() {
        super();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Runs a series of actions on key press, depending on what key is pressed
     * @param e - the KeyEvent of the key pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == playPause) {
            if (replayer.getReplayTimer().isRunning()) {
                replayer.stopTimer();
                isAuto = false;
            } else {
                replayer.autoReplay();
                isAuto = true;
            }
        }

        if (key == stepKey) {
            if (!isAuto) {
                replayer.stepReplay();
            }
        }

        if (key == speedUp) {
            if (replayer.getReplaySpeed() < 8) {
                replayer.setReplaySpeed(replayer.getReplaySpeed() + 1);
            }
        }

        if (key == speedDown) {
            if (replayer.getReplaySpeed() > 1) {
                replayer.setReplaySpeed(replayer.getReplaySpeed() - 1);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
