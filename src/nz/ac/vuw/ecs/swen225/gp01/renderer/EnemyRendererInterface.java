package nz.ac.vuw.ecs.swen225.gp01.renderer;

/**
 * A class to represent how an enemy is rendered. Used by the persistence
 * package to load enemy rendering data from a .jar file.
 */
public abstract class EnemyRendererInterface extends ActorRenderer {

  /**
   * Construct an EnemyRenderer from a given TileRenderer.
   * 
   * @param tileRenderer The TileRenderer this enemy tile is based on
   * @param isDark       Whether this tile is dark or light in the checkerboard
   *                     pattern
   * @param direction    The direction that the enemy is facing (u, d, l, r)
   */
  protected EnemyRendererInterface(TileRenderer tileRenderer, boolean isDark, char direction) {
    super(tileRenderer, isDark, direction);
  }
}
