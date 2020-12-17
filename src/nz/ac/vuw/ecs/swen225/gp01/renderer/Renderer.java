package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import nz.ac.vuw.ecs.swen225.gp01.maze.Maze;

/**
 * A class representing the Renderer. Instantiated by the application.
 */
public class Renderer {

  private final MazeRenderer mazeRenderer;
  private final InfoPanelRenderer infoPanelRenderer;

  protected static final int FRAME_RATE = 30;

  protected static double animationCycle = 0; // goes from 0-1 and back again every cycleLength frames
  private static final int cycleLength = 100; // how many frames it takes to go from 0-1

  protected static long currentFrame = 0;

  /**
   * Create a renderer, which consists of a MazeRenderer and InfoPanelRenderer.
   * 
   * @param mazeRenderer      The MazeRenderer (a JComponent)
   * @param infoPanelRenderer The InfoPanelRenderer (a JComponent)
   */
  public Renderer(MazeRenderer mazeRenderer, InfoPanelRenderer infoPanelRenderer) {

    this.mazeRenderer = mazeRenderer;
    this.infoPanelRenderer = infoPanelRenderer;
    initTimer();
  }

  /**
   * Initialize the timer that runs every frame.
   */
  private void initTimer() {

    Timer timer = new Timer(1000 / FRAME_RATE, event -> {

      currentFrame++;

      double increment = 1.0 / cycleLength;
      animationCycle += increment;
      if (animationCycle > 1)
        animationCycle = 0;

      Toolkit.getDefaultToolkit().sync();
      mazeRenderer.repaint();
    });

    timer.setRepeats(true);
    timer.start();
  }

  /**
   * Set all rendering hints to the highest quality. Called throughout the
   * renderer package when a new graphics object is created.
   * 
   * @param g The graphics object to set the rendering hints of
   */
  static void setRenderingQuality(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
  }

  /**
   * Load an image from a given file location.
   * 
   * @param filename The file path of the image
   * @return The image as a BufferedImage
   */
  public static BufferedImage loadImage(String filename) {
    try {
      return ImageIO.read(new File(filename));
    } catch (IOException e) {
      System.out.println("Failed to open image at " + filename);
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Load an image from a given file location, converting all the pixels to a
   * given colour. Used to programmatically specify the colour of an image. Alpha
   * is preserved.
   * 
   * @param filename The file path of the image
   * @param colour   The colour to convert the image to
   * @return The image as a BufferedImage
   */
  public static BufferedImage loadImage(String filename, Color colour) {
    try {
      return recolourImage(ImageIO.read(new File(filename)), colour);
    } catch (IOException e) {
      System.out.println("Failed to open image at " + filename);
      e.printStackTrace();
    }

    return null;
  }

  /**
   * A static method that re-colours a given BufferedImage The colour of each
   * pixel is changed, but the alpha is preserved.
   * 
   * @param image  The image to re-colour
   * @param colour The colour to convert it to
   * @return The re-coloured image
   */
  protected static BufferedImage recolourImage(BufferedImage image, Color colour) {

    int colourInt = colour.getRGB() & 0x00ffffff; // RGB value (minus alpha)
    assert image != null;

    // filter image to convert any colours to the shadow colour, but maintain alpha
    ImageProducer imageProducer = new FilteredImageSource(image.getSource(), new RGBImageFilter() {
      @Override
      public int filterRGB(int x, int y, int rgb) {
        int alphaMask = rgb & 0xff000000;
        if (alphaMask != 0) // if it's not 100% alpha
          return alphaMask | colourInt; // return new color, maintaining alpha

        // otherwise, return 100% alpha
        return 0;
      }
    });

    Image recolouredImg = Toolkit.getDefaultToolkit().createImage(imageProducer);
    BufferedImage output = new BufferedImage(recolouredImg.getWidth(null), recolouredImg.getHeight(null),
        BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = output.createGraphics();
    setRenderingQuality(g);
    g.drawImage(recolouredImg, 0, 0, null);
    g.dispose();

    return output;
  }

  /**
   * Update the MazeRenderer, according to the current state of the maze.
   * 
   * @param maze The current state of the maze
   */
  public void updateMaze(Maze maze) {
    mazeRenderer.update(maze);
  }

  /**
   * Update the InfoPanelRenderer, according to the current state of maze and
   * level.
   * 
   * @param maze                  The current state of the maze
   * @param tenthSecondsRemaining The number of tenths of seconds remaining in the
   *                              level
   * @param levelNumber           The level number
   */
  public void updateInfoPanel(Maze maze, int tenthSecondsRemaining, int levelNumber) {
    infoPanelRenderer.update(maze, tenthSecondsRemaining, levelNumber);
  }

}
