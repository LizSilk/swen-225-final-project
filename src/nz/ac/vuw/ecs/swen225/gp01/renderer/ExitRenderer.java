package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A class that defines how an exit should be rendered to the screen.
 */
public class ExitRenderer extends FloorRenderer {

  private static final BufferedImage exitImage = Renderer.loadImage("img/exit.png");
  private final int imageSize;

  /**
   * Create the ExitRenderer from an existing TileRenderer, copying its position.
   * 
   * @param tileRenderer The TileRenderer to copy
   * @param isDark       Whether this tile is dark or light in the checkerboard
   *                     pattern
   */
  public ExitRenderer(TileRenderer tileRenderer, boolean isDark) {
    super(tileRenderer, isDark);

    imageSize = (int) (size * 0.8);
  }

  /**
   * Exits are drawn on the bottom layer, and are rotated each frame.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderBottomLayer(Graphics2D g) {
    super.renderBottomLayer(g);

    AffineTransform transform = g.getTransform();
    g.translate(centre.x, centre.y);
    g.rotate(Renderer.animationCycle * 2 * Math.PI);
    g.drawImage(exitImage, -imageSize / 2, -imageSize / 2, imageSize, imageSize, null);
    g.setTransform(transform);
  }

  /**
   * Exits do have an animation.
   * 
   * @return true
   */
  @Override
  protected boolean hasAnimation() {
    return true;
  }
}
