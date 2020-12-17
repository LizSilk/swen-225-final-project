package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Class to represent how a collectible (key or treasure) is rendered in the
 * game. An abstract class that is implemented in KeyRenderer and
 * TreasureRenderer subclasses.
 */
public class CollectibleRenderer extends FloorRenderer {

  /**
   * The length of the collectible's shadow (roughly how far off the ground it
   * is).
   */
  public int SHADOW_LENGTH;

  /**
   * The shadow's offset from the collectible, based on angle and length of
   * shadow.
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
   * The image that the collectible is displayed as.
   */
  public BufferedImage image;

  /**
   * A recoloured version of the image, used as the collectible's shadow.
   */
  public BufferedImage shadowImage;

  /**
   * Construct a CollectibleRenderer from a given TileRenderer.
   * 
   * @param tileRenderer The TileRenderer that this tile is based on
   * @param isDark       Whether this tile is dark or light in the checkerboard
   *                     pattern
   */
  public CollectibleRenderer(TileRenderer tileRenderer, boolean isDark) {
    super(tileRenderer, isDark);
  }

  /**
   * The collectible itself is rendered on the top layer. Its size changes each
   * frame according to a sine function.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderTopLayer(Graphics2D g) {

    int imageSize = (int) (MIN_IMG_SIZE + MazeRenderer.collectibleImageSize * (MAX_IMG_SIZE - MIN_IMG_SIZE));
    Point imagePos = new Point(centre.x - imageSize / 2, centre.y - imageSize / 2);
    g.drawImage(image, imagePos.x, imagePos.y, imageSize, imageSize, null);
  }

  /**
   * The shadow of the collectible is rendered on the shadow layer The shadow is
   * the same image, but with a different colour and offset. Its size changes each
   * frame. according to a sin function
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderShadow(Graphics2D g) {

    int imageSize = (int) (MIN_IMG_SIZE + MazeRenderer.collectibleImageSize * (MAX_IMG_SIZE - MIN_IMG_SIZE));
    Point imagePos = new Point(centre.x - imageSize / 2 - SHADOW_OFFSET.x, centre.y - imageSize / 2 - SHADOW_OFFSET.y);
    g.drawImage(shadowImage, imagePos.x, imagePos.y, imageSize, imageSize, null);
  }

  /**
   * Collectibles do have an animation.
   * 
   * @return true
   */
  @Override
  protected boolean hasAnimation() {
    return true;
  }
}
