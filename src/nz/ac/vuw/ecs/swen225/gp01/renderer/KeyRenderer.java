package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import nz.ac.vuw.ecs.swen225.gp01.maze.Maze;

/**
 * A class that defines how a key collectible should be rendered to the screen.
 */
public class KeyRenderer extends CollectibleRenderer {

  // the colours of each type of key
  private static final Color RED_KEY_COLOUR = new Color(0xb13f3a);
  private static final Color GREEN_KEY_COLOUR = new Color(0x47a84f);
  private static final Color BLUE_KEY_COLOUR = new Color(0x2465b2);
  private static final Color YELLOW_KEY_COLOUR = new Color(0xedcd26);

  /**
   * A way to convert between the Maze.Colour enum and the Java colour of a key.
   */
  private static final Map<Maze.Colour, Color> keyColours = new HashMap<>();

  /**
   * A way to convert between the Maze.Colour enum and the image of a key.
   */
  private static final Map<Maze.Colour, BufferedImage> keyImages = new HashMap<>();

  /**
   * Create the KeyRenderer from an existing TileRenderer, copying its position.
   * 
   * @param tileRenderer The TileRenderer to copy
   * @param colour       The colour of the key (what coloured door it can unlock)
   */
  public KeyRenderer(TileRenderer tileRenderer, boolean isDark, Maze.Colour colour) {
    super(tileRenderer, isDark);

    if (keyColours.isEmpty())
      initColourMaps();

    SHADOW_LENGTH = 5;
    SHADOW_OFFSET = getShadowOffset(SHADOW_LENGTH);
    MIN_IMG_SIZE = size * 0.6;
    MAX_IMG_SIZE = size * 0.7;

    image = keyImages.get(colour);
    shadowImage = Renderer.loadImage("img/key.png", MazeRenderer.SHADOW_COLOUR);
  }

  /**
   * Initialise the maps that convert Maze.Colour to colours and images.
   */
  static void initColourMaps() {

    keyColours.put(Maze.Colour.RED, RED_KEY_COLOUR);
    keyColours.put(Maze.Colour.GREEN, GREEN_KEY_COLOUR);
    keyColours.put(Maze.Colour.BLUE, BLUE_KEY_COLOUR);
    keyColours.put(Maze.Colour.YELLOW, YELLOW_KEY_COLOUR);

    for (Map.Entry<Maze.Colour, Color> entry : keyColours.entrySet()) {
      keyImages.put(entry.getKey(), Renderer.loadImage("img/key.png", entry.getValue()));
    }
  }

  /**
   * Get the image corresponding with a given Maze.Colour enum.
   *
   * @param colour The Maze.Colour to get the corresponding image of
   * @return The coloured BufferedImage
   */
  protected static BufferedImage getKeyImage(Maze.Colour colour) {
    return keyImages.get(colour);
  }

  /**
   * Get the Java colour corresponding with a given Maze.Colour enum.
   *
   * @param colour The Maze.Colour to get the corresponding Java colour of
   * @return The colour object
   */
  protected static Color getKeyColour(Maze.Colour colour) {
    return keyColours.get(colour);
  }
}
