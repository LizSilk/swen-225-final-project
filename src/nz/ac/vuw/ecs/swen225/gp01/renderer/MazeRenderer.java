package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JComponent;

import nz.ac.vuw.ecs.swen225.gp01.maze.*;
import nz.ac.vuw.ecs.swen225.gp01.persistence.EnemyLoader;

/**
 * A class to define the maze JComponent. Draws all the tiles in the maze.
 */
public class MazeRenderer extends JComponent {

  /**
   * The minimum number of tiles wide/high maze to be should be. Should be an odd
   * number so the player is in the middle.
   */
  private static final int MIN_TILES = 7;

  private static final float SHADOW_OPACITY = 0.7f;
  static final double SHADOW_ANGLE = 4;
  static double collectibleImageSize;
  static double actorImageSize;

  public static final Color SHADOW_COLOUR = new Color(0x2f53a8);

  private int rows;
  private int cols;
  private final int tileSize;

  private TileRenderer[][] tileRenderers;
  private final RescaleOp shadowOpacityRescale;
  private BufferedImage bottomLayer;
  private BufferedImage shadowLayer;
  private BufferedImage topLayer;

  private String infoMessage = "";

  private final int width;
  private final int height;

  /**
   * Create an InfoPanelRenderer with a given width and height. Called from
   * outside the package (by the application).
   * 
   * @param width  The width of the maze in pixels
   * @param height The height of the maze in pixels
   */
  public MazeRenderer(int width, int height) {
    super();

    this.width = width;
    this.height = height;
    setPreferredSize(new Dimension(width, height));

    shadowOpacityRescale = new RescaleOp(new float[] { 1f, 1f, 1f, SHADOW_OPACITY }, new float[4], null);

    // calculate the size of tiles and number of rows/cols
    if (width < height) {
      tileSize = width / MIN_TILES;
      rows = height / tileSize + 2;
      rows = rows % 2 == 0 ? rows + 1 : rows;
      cols = MIN_TILES + 2;
    } else {
      tileSize = height / MIN_TILES;
      rows = MIN_TILES + 2;
      cols = width / tileSize + 2;
      cols = cols % 2 == 0 ? cols + 1 : cols;
    }

    initBoard();
    updateStaticTiles();
    KeyRenderer.initColourMaps();
  }

  /**
   * Initialise the TileRenderers in the board. All the TileRenderers are
   * initialised as FloorRenderers.
   */
  private void initBoard() {
    // initialize the board as all floors
    tileRenderers = new TileRenderer[rows][cols];
    int tileX, tileY;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        tileX = width / 2 - (cols / 2) * tileSize + col * tileSize - tileSize / 2;
        tileY = height / 2 - (rows / 2) * tileSize + row * tileSize - tileSize / 2;
        Point tilePos = new Point(tileX, tileY);
        tileRenderers[row][col] = new FloorRenderer(tilePos, tileSize, row % 2 == col % 2);
      }
    }
  }

  /**
   * Called each time the maze updates. Draws all the non-animated tiles to
   * BufferedImage layer. This reduces the number of things that have to be
   * recalculated each frame, improving performance.
   */
  private void updateStaticTiles() {
    bottomLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    shadowLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    topLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D bottomLayerGraphics = bottomLayer.createGraphics();
    Graphics2D shadowLayerGraphics = shadowLayer.createGraphics();
    Graphics2D topLayerGraphics = topLayer.createGraphics();

    Renderer.setRenderingQuality(bottomLayerGraphics);
    Renderer.setRenderingQuality(shadowLayerGraphics);
    Renderer.setRenderingQuality(topLayerGraphics);

    for (TileRenderer[] row : tileRenderers) {
      for (TileRenderer tileRenderer : row) {
        if (!tileRenderer.hasAnimation()) {
          tileRenderer.renderBottomLayer(bottomLayerGraphics);
          tileRenderer.renderShadow(shadowLayerGraphics);
          tileRenderer.renderTopLayer(topLayerGraphics);
        }
      }
    }
  }

  /**
   * Generate a TileRenderer from a given position on the maze.
   * 
   * @param currentTileRenderer The previous TileRenderer in this position
   * @param position            The position of the tile in the maze
   * @param maze                The maze to get the tile from
   * @return A new TileRenderer generated from that position
   */
  private TileRenderer updateTileRenderer(TileRenderer currentTileRenderer, Point position, Maze maze) {

    boolean isDark = Math.abs(position.x) % 2 == Math.abs(position.y) % 2;

    if (maze.isOnBoard(position.y, position.x)) {

      Tile tile = maze.getTile(position.y, position.x);

      if (tile.getType() == Floor.Type.WALL)
        return new WallRenderer(currentTileRenderer, isDark);
      if (tile.getActor() != null && tile.getActor() instanceof Player)
        return new PlayerRenderer(currentTileRenderer, isDark, tile.getActor().getDirection());
      if (tile.getActor() != null && tile.getActor() instanceof EnemyInterface) {
        try {
          return new EnemyLoader().getEnemyRenderer(currentTileRenderer, isDark, tile.getActor().getDirection());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (tile.getType() == Floor.Type.EXIT)
        return new ExitRenderer(currentTileRenderer, isDark);
      if (tile.isInfo())
        return new InfoRenderer(currentTileRenderer, isDark);
      if (tile.hasDoor())
        return new DoorRenderer(currentTileRenderer, isDark, tile.getDoor().getDoorColour());
      if (tile.hasCollectible() && tile.getCollectible() instanceof Key)
        return new KeyRenderer(currentTileRenderer, isDark, tile.getCollectible().getColour());
      if (tile.hasCollectible() && tile.getCollectible() instanceof Treasure)
        return new TreasureRenderer(currentTileRenderer, isDark);
    }

    return new FloorRenderer(currentTileRenderer, isDark);
  }

  /**
   * A method called each time the renderer is updated.
   * Updates all the TileRenderers to reflect the current state of the maze.
   * 
   * @param maze The current state of the maze
   */
  protected void update(Maze maze) {

    // calculate the difference in (x,y) coordinates between the maze and renderer
    Point playerPos = new Point(maze.getPlayer().getCol(), maze.getPlayer().getRow());
    Point offset = new Point(playerPos.x - cols / 2, playerPos.y - cols / 2);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Point tileIndex = new Point(col + offset.x, row + offset.y);
        TileRenderer currentTileRenderer = tileRenderers[row][col];
        tileRenderers[row][col] = updateTileRenderer(currentTileRenderer, tileIndex, maze);
      }
    }

    updateStaticTiles();

    if (maze.getTile(playerPos.y, playerPos.x).isInfo())
      infoMessage = maze.getTile(playerPos.y, playerPos.x).getInfo();
    else
      infoMessage = "";
  }

  /**
   * Draw the info message that is displayed when the player steps on an info
   * tile.
   * 
   * @param g The graphics object to draw to
   */
  private void drawInfoMessage(Graphics2D g) {
    int padding = 30;

    // remove quotes from string (that the level loader adds), and make the \n's actual newlines
    if (infoMessage.charAt(0) == '\"' && infoMessage.charAt(infoMessage.length() - 1) == '\"')
      infoMessage = infoMessage.substring(1, infoMessage.length() - 1);
    infoMessage = infoMessage.replace("\\n", "\n");

    String[] lines = infoMessage.split("\n");

    Font font = new Font("SansSerif", Font.PLAIN, 28);
    FontMetrics fontMetrics = g.getFontMetrics(font);
    g.setFont(font);

    int textHeight = fontMetrics.getHeight() * lines.length + 10;
    int textBoxHeight = textHeight + fontMetrics.getHeight();
    Rectangle box = new Rectangle(padding, height - padding - textBoxHeight, width - 2 * padding, textBoxHeight);

    // draw the text box
    g.setColor(new Color(0x2f53a8));
    g.fillRoundRect(box.x, box.y, box.width, box.height, 10, 10);

    // draw each line of text
    int textY = box.y + box.height / 2 - textHeight / 2;
    for (String line : lines) {
      textY += fontMetrics.getHeight();

      int textX = box.x + box.width / 2 - fontMetrics.stringWidth(line) / 2;
      g.setColor(Color.WHITE);
      g.drawString(line, textX, textY);
    }
  }

  /**
   * Defines how the maze panel is drawn. Updated every frame by the Renderer.
   * 
   * @param graphics The graphics object to draw to
   */
  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D g = (Graphics2D) graphics;
    Renderer.setRenderingQuality(g);

    collectibleImageSize = 0.5 + 0.5 * Math.sin(4 * Math.PI * Renderer.animationCycle);
    actorImageSize = Math.max(Math.sin(8 * Math.PI * Renderer.animationCycle), 0);

    // create image for shadows to be drawn onto
    BufferedImage shadowImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D s = shadowImage.createGraphics();
    Renderer.setRenderingQuality(s);

    // draw the bottom layer
    g.drawImage(bottomLayer, 0, 0, null);
    for (TileRenderer[] row : tileRenderers)
      for (TileRenderer tileRenderer : row)
        if (tileRenderer.hasAnimation())
          tileRenderer.renderBottomLayer(g);

    // draw the shadows layer (at a lower opacity)
    s.drawImage(shadowLayer, 0, 0, null);
    for (TileRenderer[] row : tileRenderers)
      for (TileRenderer tileRenderer : row)
        if (tileRenderer.hasAnimation())
          tileRenderer.renderShadow(s);
    g.drawImage(shadowImage, shadowOpacityRescale, 0, 0);

    // draw the top layer
    g.drawImage(topLayer, 0, 0, null);
    for (TileRenderer[] row : tileRenderers)
      for (TileRenderer tileRenderer : row)
        if (tileRenderer.hasAnimation())
          tileRenderer.renderTopLayer(g);

    if (!infoMessage.equals(""))
      drawInfoMessage(g);
  }
}
