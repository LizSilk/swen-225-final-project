package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * A class that defines how a floor tile should be rendered to the screen.
 */
public class FloorRenderer extends TileRenderer {

  private static final Color COLOUR_DARK = new Color(0x698cd9);
  private static final Color COLOUR_LIGHT = new Color(0x84a5f5);

  /**
   * Create the FloorRenderer at a given point on the screen.
   * 
   * @param position The top/left coordinate of the tile
   * @param size     The size (with and height) of the tile
   * @param isDark   Whether this tile is dark or light in the checkerboard
   *                 pattern
   */
  public FloorRenderer(Point position, int size, boolean isDark) {
    super(position, size, isDark);
  }

  /**
   * Create the FloorRenderer from an existing TileRenderer, copying its position.
   * 
   * @param tileRenderer The TileRenderer to copy
   * @param isDark       Whether this tile is dark or light according to the
   *                     checkerboard pattern
   */
  public FloorRenderer(TileRenderer tileRenderer, boolean isDark) {
    super(tileRenderer.position, tileRenderer.size, isDark);
  }

  /**
   * Floors don't draw anything on the top layer.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderTopLayer(Graphics2D g) {
  }

  /**
   * Floors don't have a shadow either.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderShadow(Graphics2D g) {
  }

  /**
   * Floors are drawn on the bottom layer The colour depends on the checkerboard
   * pattern.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderBottomLayer(Graphics2D g) {
    g.setColor(isDark ? COLOUR_DARK : COLOUR_LIGHT);
    g.fillRect(position.x, position.y, size, size);
  }

  /**
   * Floors do not have an animation.
   * 
   * @return false
   */
  @Override
  protected boolean hasAnimation() {
    return false;
  }
}
