package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Class to represent how an actor (player or enemy) is rendered in the game. An
 * abstract class that is implemented in PlayerRenderer and EnemyRenderer
 * subclasses.
 */
public abstract class ActorRenderer extends FloorRenderer {

  /**
   * The length of the actor's shadow (roughly how far off the ground they are).
   */
  public int SHADOW_LENGTH;

  /**
   * The shadow's offset from the actor, based on angle and length of shadow.
   */
  public Point SHADOW_OFFSET;

  /**
   * The maximum size of the image in the growing/shrinking animation.
   */
  public double MAX_IMG_SIZE;

  /**
   * The minimum size of the image in the growing/shrinking animation.
   */
  public double MIN_IMG_SIZE;

  /**
   * The image that the actor is displayed as.
   */
  public BufferedImage image;

  /**
   * A recoloured version of the image, used as the actor's shadow.
   */
  public BufferedImage shadowImage;

  private final char direction;
  public double angle;

  /**
   * Create an actor from a given TileRenderer. Actors face in one of four
   * directions (u, d, l, r).
   * 
   * @param tileRenderer The TileRenderer we're replacing
   * @param isDark       Whether this tile is dark or light in the checkerboard
   *                     pattern
   * @param dir          The direction the actor is facing
   */
  public ActorRenderer(TileRenderer tileRenderer, boolean isDark, char dir) {
    super(tileRenderer, isDark);

    assert (dir == 'l' || dir == 'r' || dir == 'u' || dir == 'd');

    this.direction = dir;
    this.angle = getAngle();
  }

  /**
   * Convert the char angle (up, down, left, or right) into an angle in radians.
   * 
   * @return The angle in radians, where facing down means no rotation.
   */
  private double getAngle() {
    if (direction == 'r')
      return (3f / 2) * Math.PI;
    if (direction == 'l')
      return Math.PI / 2;
    if (direction == 'u')
      return Math.PI;
    return 0;
  }

  /**
   * Determine the size that the actor should be drawn at each frame. This varies
   * according to whether the actor is doing their "jumping" animation.
   * 
   * @return The size of the image this frame
   */
  private int getImageSize() {
    if (doIdleAnimation()) {
      angle = getAngle();
      return (int) (MIN_IMG_SIZE + MazeRenderer.actorImageSize * (MAX_IMG_SIZE - MIN_IMG_SIZE));
    }

    return (int) ((MIN_IMG_SIZE + MAX_IMG_SIZE) / 2);
  }

  /**
   * Render the shadow of this actor onto the shadow layer.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderShadow(Graphics2D g) {
    super.renderShadow(g);

    int imageSize = getImageSize();

    AffineTransform transform = g.getTransform();
    g.translate(centre.x - SHADOW_OFFSET.x, centre.y - SHADOW_OFFSET.y);
    g.rotate(angle);
    g.drawImage(shadowImage, -imageSize / 2, -imageSize / 2, imageSize, imageSize, null);
    g.setTransform(transform);
  }

  /**
   * Render this actor onto the top layer.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderTopLayer(Graphics2D g) {
    super.renderTopLayer(g);

    int imageSize = getImageSize();

    AffineTransform transform = g.getTransform();
    g.translate(centre.x, centre.y);
    g.rotate(angle);
    g.drawImage(image, -imageSize / 2, -imageSize / 2, imageSize, imageSize, null);
    g.setTransform(transform);
  }

  /**
   * Actors do have an animation, so are redrawn each frame.
   * 
   * @return true
   */
  @Override
  protected boolean hasAnimation() {
    return true;
  }

  /**
   * Subclasses return a boolean to determine whether they are currently in their
   * idle animation.
   * 
   * @return True if they should do their idle animation, otherwise false
   */
  protected abstract boolean doIdleAnimation();
}
