package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * A class that defines how an information tile should be rendered to the
 * screen.
 */
public class InfoRenderer extends FloorRenderer {

  private final int imageSize;
  private static final BufferedImage infoImage = Renderer.loadImage("img/info.png");

  /**
   * Create the InfoRenderer from an existing TileRenderer, copying its position.
   * 
   * @param tileRenderer The TileRenderer to copy
   * @param isDark       Whether this tile is dark or light in the checkerboard
   *                     pattern
   */
  public InfoRenderer(TileRenderer tileRenderer, boolean isDark) {
    super(tileRenderer, isDark);

    imageSize = (int) (0.7 * size);
  }

  /**
   * Info tiles are drawn on the bottom layer.
   * 
   * @param g The graphics object to draw to
   */
  @Override
  protected void renderBottomLayer(Graphics2D g) {
    super.renderBottomLayer(g);

    Point tileCentre = new Point(position.x + size / 2, position.y + size / 2);
    Point imagePos = new Point(tileCentre.x - imageSize / 2, tileCentre.y - imageSize / 2);
    g.drawImage(infoImage, imagePos.x, imagePos.y, imageSize, imageSize, null);
  }

  /**
   * Info tiles do not have an animation.
   * 
   * @return false
   */
  @Override
  protected boolean hasAnimation() {
    return false;
  }
}
