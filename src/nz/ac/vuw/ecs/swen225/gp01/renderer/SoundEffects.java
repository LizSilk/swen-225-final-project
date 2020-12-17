package nz.ac.vuw.ecs.swen225.gp01.renderer;

import javax.sound.sampled.Clip;

/**
 * Public class, used for playing a given sound effect.
 */
public class SoundEffects {

  /**
   * Static method to play a given sound effect.
   * 
   * @param soundEffect The sound effect to play
   */
  public static void play(SFX soundEffect) {

    Clip clip = soundEffect.getClip();
    if (clip != null) {
      clip.stop();
      clip.setFramePosition(0);
      clip.start();
    }
  }
}
