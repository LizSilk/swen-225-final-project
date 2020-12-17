package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Abstract class defining the template methods/fields of a TileRenderer.
 */
public abstract class TileRenderer {

  protected final Point position;
  protected final Point centre;
  protected final int size;
  protected final boolean isDark;

  /**
   * Create a TileRenderer at a given position.
   * 
   * @param position The (x,y) position of the top/left corner of the tile
   * @param size     The size of the tile
   * @param isDark   Whether the tile is dark or light in the checkerboard pattern
   */
  public TileRenderer(Point position, int size, boolean isDark) {
    this.position = position;
    this.size = size;
    this.isDark = isDark;

    this.centre = new Point(position.x + size / 2, position.y + size / 2);
  }

  /**
   * Get the (x,y) offset of a shadow, based on the length and angle of the shadow.
   * 
   * @param shadowLength The length of the shadow (how far off the ground the
   *                     object is)
   * @return The offset
   */
  public static Point getShadowOffset(int shadowLength) {
    int xOffSet = (int) (shadowLength * Math.cos(MazeRenderer.SHADOW_ANGLE));
    int yOffset = (int) (shadowLength * Math.sin(MazeRenderer.SHADOW_ANGLE));

    return new Point(xOffSet, yOffset);
  }

  /**
   * Method to render tiles on the bottom layer (below shadows).
   * 
   * @param g The graphics object to draw to
   */
  protected abstract void renderBottomLayer(Graphics2D g);

  /**
   * Method to render tiles to the (middle) shadow layer. The shadow layer is
   * drawn with transparency.
   * 
   * @param g The graphics object to draw to
   */
  protected abstract void renderShadow(Graphics2D g);

  /**
   * Method to render tiles on the top layer (above shadows).
   * 
   * @param g The graphics object to draw to
   */
  protected abstract void renderTopLayer(Graphics2D g);

  /**
   * Determine whether a tile needs to be drawn each frame, or just each time the
   * maze is updated.
   * 
   * @return True if the player needs to be animated each frame, otherwise false
   */
  protected abstract boolean hasAnimation();
}