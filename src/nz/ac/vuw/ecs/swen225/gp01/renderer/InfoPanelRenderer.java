package nz.ac.vuw.ecs.swen225.gp01.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import nz.ac.vuw.ecs.swen225.gp01.maze.Collectible;
import nz.ac.vuw.ecs.swen225.gp01.maze.Maze;

/**
 * A class to define the information panel drawn to the right of the maze. The
 * information panel is its own JComponent.
 */
public class InfoPanelRenderer extends JComponent {

  private static final Color BACKGROUND_COLOUR = new Color(0x2d2d2d);
  private static final Color PANEL_COLOUR = new Color(0x353535);
  private static final Color QUANTITY_CIRCLE_COLOUR = new Color(0x111111);

  private static final int RECT_RADIUS = 10;

  /**
   * The colour of the panel's shadow at its darkest part (out of 255).
   */
  private static final int SHADOW_DARK = 30;

  /**
   * The colour of the panel's shadow at its lightest part (out of 255).
   */
  private static final int SHADOW_LIGHT = 45;

  private final int width, height;

  private Rectangle topPanel;
  private Rectangle bottomPanel;

  /**
   * The grey background composed of two panels with shadows.
   */
  private static BufferedImage backgroundImage;

  private int totalTreasures = 0;
  private int treasuresCollected = 0;
  private String timeRemaining = "0:00";
  private String levelName = "Loading";
  private final Map<BufferedImage, Integer> playerInventory = new HashMap<>();

  private static final BufferedImage timeIcon = Renderer.loadImage("img/time-icon.png");
  private static final BufferedImage treasuresIcon = Renderer.loadImage("img/treasure.png", Color.WHITE);

  private static final Font font = new Font("SansSerif", Font.PLAIN, 56);

  /**
   * Create an InfoPanelRenderer with a given width and height Called from outside
   * the package (by the application).
   * 
   * @param width  The width of the info panel
   * @param height The height of the info panel
   */
  public InfoPanelRenderer(int width, int height) {
    super();
    setPreferredSize(new Dimension(width, height));

    this.width = width;
    this.height = height;

    initPanels();
    initBackground();
  }

  /**
   * Determine the bounds of the two inner panels, based on padding and a given
   * height ratio.
   */
  private void initPanels() {
    int padding = 30;
    int cardWidth = width - 2 * padding;
    float topCardPercentage = 0.5f;
    int heightAfterPadding = height - 3 * padding;
    int topCardHeight = (int) (topCardPercentage * heightAfterPadding);
    int bottomCardHeight = (int) ((1 - topCardPercentage) * heightAfterPadding);

    topPanel = new Rectangle(padding, padding, cardWidth, topCardHeight);
    bottomPanel = new Rectangle(padding, 2 * padding + topCardHeight, cardWidth, bottomCardHeight);
  }

  /**
   * Create the background image, consisting of a grey background, and two panels
   * with shadows. This BufferedImage can then be reused each time the InfoPanel
   * is redrawn.
   */
  private void initBackground() {
    backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) backgroundImage.getGraphics();
    Renderer.setRenderingQuality(g);

    // draw background
    g.setColor(BACKGROUND_COLOUR);
    g.fillRect(0, 0, width, height);

    // draw panels
    g.setColor(PANEL_COLOUR);
    g.fillRoundRect(topPanel.x, topPanel.y, topPanel.width, topPanel.height, 10, 10);
    g.fillRoundRect(bottomPanel.x, bottomPanel.y, bottomPanel.width, bottomPanel.height, 10, 10);
    drawRectShadow(topPanel, g);
    drawRectShadow(bottomPanel, g);
  }

  /**
   * Draw the shadow of rounded rectangle.
   *
   * @param rect The rectangle whose shadow to draw
   * @param g    The graphics object to draw to
   */
  private void drawRectShadow(Rectangle rect, Graphics2D g) {

    int grey = SHADOW_DARK;
    int i = 1;
    while (grey < SHADOW_LIGHT) {
      g.setColor(new Color(grey, grey, grey));
      final int w = rect.width + 2 * i;
      final int h = rect.height + 2 * i;
      g.drawRoundRect(rect.x - i, rect.y - i, w, h, RECT_RADIUS, RECT_RADIUS);

      // increment grey level by 2 each time the shadow extends by 1 pixel
      grey += 2;
      i++;
    }
  }

  /**
   * Convert the remaining tenthSeconds into a minutes and seconds format, as a
   * string. e.g. 650 tenth seconds becomes "1:05".
   * 
   * @param tenthSeconds The number of tenths of seconds remaining
   * @return The number of minutes and seconds as a string
   */
  private static String getTimeAsString(int tenthSeconds) {

    tenthSeconds = Math.max(tenthSeconds, 0);

    final int mins = tenthSeconds / 600;
    final int seconds = (tenthSeconds / 10) % 60;
    return mins + ":" + (seconds < 10 ? "0" + seconds : seconds);
  }

  /**
   * Update values and redraw the info panel Called when the application updates
   * the renderer.
   * 
   * @param maze                  The maze being rendered
   * @param tenthSecondsRemaining The number of tenths of seconds remaining in the
   *                              level
   * @param levelNumber           The current level number
   */
  protected void update(Maze maze, int tenthSecondsRemaining, int levelNumber) {
    treasuresCollected = maze.getPlayer().getTreasureCount();
    totalTreasures = maze.getTotalTreasures();
    timeRemaining = getTimeAsString(tenthSecondsRemaining);
    levelName = "Level " + levelNumber;
    updateInventory(maze);

    repaint();
  }

  /**
   * Update the player's inventory. Converts the maze's list of collectibles into
   * a map of unique items and the quantity of each.
   * 
   * @param maze The maze, where the players inventory is stored as a list
   */
  private void updateInventory(Maze maze) {
    playerInventory.clear();

    for (Collectible collectible : maze.getPlayer().getInventory()) {
      BufferedImage image = null;

      if (collectible.getColour() != null)
        image = KeyRenderer.getKeyImage(collectible.getColour());

      image = image == null ? treasuresIcon : image;

      int numItems = playerInventory.getOrDefault(image, 0);
      playerInventory.put(image, numItems + 1);
    }
  }

  /**
   * Defines how the info panel is drawn.
   *
   * @param graphics The graphics object to draw to
   */
  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D g = (Graphics2D) graphics;
    Renderer.setRenderingQuality(g);

    // draw background
    g.drawImage(backgroundImage, 0, 0, null);

    // draw top panel text
    renderTopPanel(g);

    // draw player's inventory
    renderInventory(g);
  }

  /**
   * Render the information on the top panel. This includes the level name, time
   * remaining, and number of treasures collected.
   * 
   * @param g The graphics object to draw to
   */
  private void renderTopPanel(Graphics2D g) {

    // draw the level number
    g.setColor(Color.WHITE);
    g.setFont(font);
    FontMetrics fontMetrics = g.getFontMetrics(font);
    int leftMargin = topPanel.width / 2 - fontMetrics.stringWidth(levelName) / 2;
    int levelTextY = topPanel.y + topPanel.height / 5 + 18;
    g.drawString(levelName, topPanel.x + leftMargin, levelTextY);

    // draw the remaining time
    int timeIconY = topPanel.y + topPanel.height / 2 - 28;
    g.drawImage(timeIcon, topPanel.x + leftMargin, timeIconY, 56, 56, null);
    g.drawString(timeRemaining, topPanel.x + topPanel.width / 2 - 12, timeIconY + 46);

    // draw the number of treasures found
    int treasureIconY = topPanel.y + 4 * topPanel.height / 5 - 28;
    g.drawImage(treasuresIcon, topPanel.x + leftMargin, treasureIconY, 56, 56, null);
    String treasureString = treasuresCollected + "/" + totalTreasures;
    g.drawString(treasureString, topPanel.x + topPanel.width / 2 - 12, treasureIconY + 46);
  }

  /**
   * Render the player's inventory in the bottom panel.
   * 
   * @param g The graphic object to draw to.
   */
  private void renderInventory(Graphics2D g) {

    int rows = 2;
    int cols = 2;

    // if there's not enough space with current rows and cols, increase them
    while (rows * cols < playerInventory.size()) {
      if (rows < cols)
        rows++;
      else
        cols++;
    }

    int paddingX = 10 + bottomPanel.width / (8 * cols);
    int diameter = (bottomPanel.width - (cols + 1) * paddingX) / cols;
    int radius = diameter / 2;
    int paddingY = (bottomPanel.height - rows * diameter) / (rows + 1);
    int imageSize = (int) (diameter * 0.7);
    int imageOffset = radius - imageSize / 2;

    ArrayList<BufferedImage> itemIcons = new ArrayList<>(playerInventory.keySet());

    int row = 0;
    int col = 0;
    for (int i = 0; i < playerInventory.size(); i++) {
      int x = bottomPanel.x + paddingX + col * (paddingX + diameter);
      int y = bottomPanel.y + paddingY + row * (paddingY + diameter);

      // draw big circle
      g.setColor(Color.WHITE);
      g.fillOval(x, y, diameter, diameter);

      // draw icon
      g.drawImage(itemIcons.get(i), x + imageOffset, y + imageOffset, imageSize, imageSize, null);

      // draw quantity if there are more than one of an item
      int quantity = playerInventory.get(itemIcons.get(i));
      if (quantity > 1)
        renderQuantityCircle(quantity, x, y, radius, g);

      row++;
      if (row >= rows) {
        col++;
        row = 0;
      }
      if (col >= cols)
        break;
    }
  }

  /**
   * Render the quantity of an item in the player's inventory. Draws a number in a
   * circle, and is only called if the player has >1 of an item
   * 
   * @param quantity         The number to display
   * @param x                The x position of centre of the circle
   * @param y                The y position of the centre of the circle
   * @param iconCircleRadius The radius of the circle
   * @param g                The graphics object to draw to
   */
  private void renderQuantityCircle(int quantity, int x, int y, int iconCircleRadius, Graphics2D g) {

    int quantityCircleOffsetX = iconCircleRadius + (int) (iconCircleRadius * Math.cos(0.85));
    int quantityCircleOffsetY = iconCircleRadius + (int) (iconCircleRadius * Math.sin(0.85));
    int quantityCircleRadius = (int) (iconCircleRadius * 0.35);

    // draw quantity indicator circle
    g.setColor(QUANTITY_CIRCLE_COLOUR);
    int quantityCircleX = x + quantityCircleOffsetX - quantityCircleRadius;
    int quantityCircleY = y + quantityCircleOffsetY - quantityCircleRadius;
    g.fillOval(quantityCircleX, quantityCircleY, quantityCircleRadius * 2, quantityCircleRadius * 2);

    // draw quantity number
    String number = String.valueOf(quantity);
    Font font = new Font("SansSerif", Font.BOLD, (int) (quantityCircleRadius * 1.5));
    Rectangle2D fontBounds = g.getFontMetrics(font).getStringBounds(number, g);
    int textX = quantityCircleX + quantityCircleRadius - (int) fontBounds.getWidth() / 2;
    int textY = quantityCircleY + quantityCircleRadius + quantityCircleRadius / 2;
    g.setFont(font);
    g.setColor(Color.WHITE);
    g.drawString(number, textX, textY);
  }
}
