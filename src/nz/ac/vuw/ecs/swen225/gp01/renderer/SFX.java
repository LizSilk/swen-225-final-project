package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Public enumerator defining sound effects. Each sound effect is linked to a
 * file.
 */
public enum SFX {
  MOVE("sfx/move.wav"), DENIED("sfx/denied.wav"), PICKUP("sfx/pickup.wav"), UNLOCK("sfx/unlock.wav"),
  DEATH("sfx/death.wav"), WIN("sfx/win.wav"), DISPLAY_INFO("sfx/display-info.wav");

  private Clip clip;

  /**
   * Create an SFX object, linked to a given file name.
   * 
   * @param filename The location of the sound effect file
   */
  SFX(String filename) {

    // load clip from filename
    try {
      File file = new File(filename);
      clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(file));
    } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
      System.out.println("Error getting audio clip");
      e.printStackTrace();
    }
  }

  /**
   * Get the clip associated with a given sound effect.
   * 
   * @return The clip
   */
  protected Clip getClip() {
    return clip;
  }
}
