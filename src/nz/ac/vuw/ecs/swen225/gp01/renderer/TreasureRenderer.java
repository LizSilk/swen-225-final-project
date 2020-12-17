package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Color;

/**
 * A class that defines how a treasure collectible should be rendered to the
 * screen.
 */
public class TreasureRenderer extends CollectibleRenderer {

  protected static final Color TREASURE_COLOUR = new Color(0xf5933f);

  /**
   * Create the TreasureRenderer from an existing TileRenderer, copying its
   * position.
   * 
   * @param tileRenderer The TileRenderer to copy
   * @param isDark       Whether the tile is dark or light in the checkerboard
   */
  public TreasureRenderer(TileRenderer tileRenderer, boolean isDark) {
    super(tileRenderer, isDark);

    SHADOW_LENGTH = 8;
    SHADOW_OFFSET = getShadowOffset(SHADOW_LENGTH);
    MIN_IMG_SIZE = size * 0.6;
    MAX_IMG_SIZE = size * 0.7;

    image = Renderer.loadImage("img/treasure.png", TREASURE_COLOUR);
    shadowImage = Renderer.loadImage("img/treasure.png", MazeRenderer.SHADOW_COLOUR);
  }
}
