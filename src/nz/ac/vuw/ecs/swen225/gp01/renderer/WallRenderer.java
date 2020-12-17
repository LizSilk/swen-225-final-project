package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * A class that defines how a wall should be rendered to the screen.
 */
public class WallRenderer extends TileRenderer {

  private static final Color WALL_COLOUR_DARK = new Color(0x6fe0a6);
  private static final Color WALL_COLOUR_LIGHT = new Color(0x70ecb1);

  private static final int shadowLength = 20;
  private static BufferedImage shadow;

  /**
   * Create the WallRenderer from an existing TileRenderer, copying its position.
   * 
   * @param tileRenderer The TileRenderer to copy
   * @param isDark       Whether the tile is dark or light in the checkerboard
   *                     pattern
   */
  public WallRenderer(TileRenderer tileRenderer, boolean isDark) {
    super(tileRenderer.position, tileRenderer.size, isDark);

    if (shadow == null)
      shadow = initShadow();
  }

  /**
   * Create the walls' shadow as a BufferedImage, common to all WallRenderers.
   * 
   * @return The shadow as a BufferedImage
   */
  private BufferedImage initShadow() {

    final int length = shadowLength;
    final double angle = MazeRenderer.SHADOW_ANGLE;

    int shadowWidth = size + (int) Math.abs(length * Math.cos(angle));
    int shadowHeight = size + (int) Math.abs(length * Math.sin(angle));
    BufferedImage shadow = new BufferedImage(shadowWidth, shadowHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) shadow.getGraphics();
    Renderer.setRenderingQuality(g);

    int tileX = (int) Math.max(length * Math.cos(angle), 0);
    int tileY = (int) Math.max(length * Math.sin(angle), 0);
    Rectangle tile = new Rectangle(tileX, tileY, size, size);
    Point[] rectVerts = new Point[4];
    rectVerts[0] = new Point(tile.x, tile.y);
    rectVerts[1] = new Point(tile.x + tile.width, tile.y);
    rectVerts[2] = new Point(tile.x + tile.width, tile.y + tile.height);
    rectVerts[3] = new Point(tile.x, tile.y + tile.height);

    int shadowStartIndex = 0;
    Point[] shadowVerts = new Point[4];
    for (int i = 0; i < 4; i++) {
      int x = rectVerts[i].x - (int) (length * Math.cos(angle));
      int y = rectVerts[i].y - (int) (length * Math.sin(angle));
      shadowVerts[i] = new Point(x, y);
      if (tile.contains(shadowVerts[i]))
        shadowStartIndex = i + 1 >= 4 ? 0 : i + 1;
    }

    int[] shadowPointsX = new int[6];
    int[] shadowPointsY = new int[6];

    for (int i = 0; i < 3; i++) {
      shadowPointsX[i] = shadowVerts[shadowStartIndex].x;
      shadowPointsY[i] = shadowVerts[shadowStartIndex].y;
      shadowPointsX[5 - i] = rectVerts[shadowStartIndex].x;
      shadowPointsY[5 - i] = rectVerts[shadowStartIndex].y;

      shadowStartIndex = shadowStartIndex + 1 >= 4 ? 0 : shadowStartIndex + 1;
    }

    g.setColor(MazeRenderer.SHADOW_COLOUR);
    g.fillPolygon(shadowPointsX, shadowPointsY, 6);

    return shadow;
  }

  /**
   * Walls are drawn on the top layer The colour of the wall depends on the
   * checkerboard pattern.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderTopLayer(Graphics2D g) {
    g.setColor(isDark ? WALL_COLOUR_DARK : WALL_COLOUR_LIGHT);
    g.fillRect(position.x, position.y, size, size);
  }

  /**
   * The shadow of a wall is rendered to the shadow layer.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderShadow(Graphics2D g) {
    g.drawImage(shadow, position.x, position.y, null);
  }

  /**
   * Walls do not draw anything on the bottom layer by default Although this could
   * be overwritten in subclasses.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderBottomLayer(Graphics2D g) {
  }

  /**
   * Walls do not have an animation.
   * 
   * @return false
   */
  @Override
  protected boolean hasAnimation() {
    return false;
  }
}
