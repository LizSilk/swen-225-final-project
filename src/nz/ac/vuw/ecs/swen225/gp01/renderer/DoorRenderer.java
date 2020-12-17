package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import nz.ac.vuw.ecs.swen225.gp01.maze.Maze;

/**
 * A class that defines how a door should be rendered to the screen. This
 * includes coloured doors and exit doors.
 */
public class DoorRenderer extends WallRenderer {

  /**
   * The thickness of the coloured outline around the door.
   */
  private static final int STROKE_WEIGHT = 8;

  /**
   * The bounds of the door's coloured outline.
   */
  private final Rectangle outline;

  private static final BufferedImage padlockImage = Renderer.loadImage("img/padlock.png");
  private static final BufferedImage exitPacklockImage = Renderer.loadImage("img/exit-padlock.png");

  private final Color colour;
  private final BufferedImage image;

  /**
   * Create the DoorRenderer from an existing TileRenderer, copying its position.
   * The colour of key that can open the door is specified (null for exit door).
   * 
   * @param tileRenderer The TileRenderer to copy
   * @param isDark       Whether the tile is dark or light according to the
   *                     checkerboard pattern
   * @param colour       The colour of the door (what coloured key can unlock it),
   *                     null for exit door
   */
  public DoorRenderer(TileRenderer tileRenderer, boolean isDark, Maze.Colour colour) {
    super(tileRenderer, isDark);

    boolean isExitDoor = colour == null;
    this.colour = isExitDoor ? TreasureRenderer.TREASURE_COLOUR : KeyRenderer.getKeyColour(colour);
    this.image = isExitDoor ? exitPacklockImage : padlockImage;

    outline = new Rectangle(tileRenderer.position.x + STROKE_WEIGHT / 2, tileRenderer.position.y + STROKE_WEIGHT / 2,
        size - STROKE_WEIGHT, size - STROKE_WEIGHT);
  }

  /**
   * Doors are rendered on the top layer The underlying wall is drawn first.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderTopLayer(Graphics2D g) {
    super.renderTopLayer(g);

    int imageSize = (int) (0.55 * size);
    Point imagePos = new Point(centre.x - imageSize / 2, centre.y - imageSize / 2);
    g.drawImage(image, imagePos.x, imagePos.y, imageSize, imageSize, null);

    g.setColor(colour);
    g.setStroke(new BasicStroke(STROKE_WEIGHT));
    g.drawRect(outline.x, outline.y, outline.width, outline.height);
  }

  /**
   * Doors do not have an animation.
   * 
   * @return false
   */
  @Override
  protected boolean hasAnimation() {
    return false;
  }
}