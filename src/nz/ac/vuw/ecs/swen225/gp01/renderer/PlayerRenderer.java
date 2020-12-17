package nz.ac.vuw.ecs.swen225.gp01.renderer;

/**
 * Class that determines how the player is drawn to the screen.
 */
public class PlayerRenderer extends ActorRenderer {

  private final long timeSinceLastMove;

  /**
   * Create a new PlayerRenderer, based on an existing TileRenderer.
   * 
   * @param tileRenderer The TileRenderer that this player is based on
   * @param isDark       Whether this tile is dark or light in the checkerboard
   *                     pattern
   * @param direction    The direction that the player is facing (u, d, l, r)
   */
  public PlayerRenderer(TileRenderer tileRenderer, boolean isDark, char direction) {
    super(tileRenderer, isDark, direction);

    SHADOW_LENGTH = 12;
    SHADOW_OFFSET = getShadowOffset(SHADOW_LENGTH);
    MAX_IMG_SIZE = size * 0.93;
    MIN_IMG_SIZE = size * 0.87;
    image = Renderer.loadImage("img/player.png");
    shadowImage = Renderer.loadImage("img/player.png", MazeRenderer.SHADOW_COLOUR);

    timeSinceLastMove = Renderer.currentFrame;
    angle += (isDark ? 1 : -1) * 0.15;
  }

  /**
   * Determine whether the player should play their idle animation. After not
   * moving for 1 second, the player starts jumping up and down.
   * 
   * @return True if the player has not moved in the last second, otherwise false
   */
  @Override
  protected boolean doIdleAnimation() {
    return Renderer.currentFrame - timeSinceLastMove > Renderer.FRAME_RATE;
  }
}