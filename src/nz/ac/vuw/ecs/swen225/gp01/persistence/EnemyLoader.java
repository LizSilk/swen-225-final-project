package nz.ac.vuw.ecs.swen225.gp01.persistence;

import nz.ac.vuw.ecs.swen225.gp01.maze.EnemyInterface;
import nz.ac.vuw.ecs.swen225.gp01.maze.Tile;
import nz.ac.vuw.ecs.swen225.gp01.renderer.EnemyRendererInterface;
import nz.ac.vuw.ecs.swen225.gp01.renderer.TileRenderer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * Class that loads the Enemy & EnemyRenderer code from the JAR file
 *
 * @author Benjmin Silk 300473874
 */
public class EnemyLoader {
    private final URLClassLoader loader;
    private final String fileName = "levels/level2.jar";//redo this to make it less specific

    /**
     * Constructor for EnemyLoader
     * Creates the UrlClassLoader for the jar file
     *
     * @throws Exception this is thrown if the url is not formatted correctly
     */
    public EnemyLoader() throws Exception {
        File file = new File(fileName);
        loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
    }

    /**
     * This method loads an instance of Enemy from the jar file
     *
     * @param t    the tile the enemy will start at
     * @param inAI the ai type the enemy will be using
     * @param dir  the direction the enemy will face when it starts
     * @return returns an instance of Enemy cast to EnemyInterface
     * @throws ClassNotFoundException    thrown if the URLClassLoader can't find the class in the jar
     * @throws NoSuchMethodException     thrown if the by getConstructor if the class doesn't have a matching constructor
     * @throws InvocationTargetException thrown if there is an issue with the reflection - wraps an exception thrown by an invoked method or constructor.
     * @throws InstantiationException    thrown by newInstance if the class can't be instantiated
     */
    @SuppressWarnings("unchecked")
    public EnemyInterface getEnemyInstance(Tile t, EnemyInterface.AIType inAI, char dir) throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class enemyClass = Class.forName("nz.ac.vuw.ecs.swen225.gp01.maze.Enemy", true, loader);
        Constructor enemyConstructor = enemyClass.getConstructor(Tile.class, EnemyInterface.AIType.class, char.class);
        Object enemy = enemyConstructor.newInstance(t, inAI, dir);
        return (EnemyInterface) enemy;
    }

    /**
     * This method loads an instance of EnemyRenderer from the jar file
     *
     * @param tileRenderer the renderer we are replacing
     * @param isDark       Whether this tile is dark or light in the checkerboard pattern
     * @param direction    the direction the enemy is facing
     * @return an instance of EnemyRenderer cast to EnemyRendererInterface
     * @throws ClassNotFoundException    thrown if the URLClassLoader can't find the class in the jar
     * @throws NoSuchMethodException     thrown if the by getConstructor if the class doesn't have a matching constructor
     * @throws InvocationTargetException thrown if there is an issue with the reflection - wraps an exception thrown by an invoked method or constructor.
     * @throws InstantiationException    thrown by newInstance if the class can't be instantiated
     */
    @SuppressWarnings("unchecked")
    public EnemyRendererInterface getEnemyRenderer(TileRenderer tileRenderer, boolean isDark, char direction) throws
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class enemyClass = Class.forName("nz.ac.vuw.ecs.swen225.gp01.renderer.EnemyRenderer", true, loader);
        Constructor enemyConstructor = enemyClass.getConstructor(TileRenderer.class, boolean.class, char.class);
        Object enemy = enemyConstructor.newInstance(tileRenderer, isDark, direction);
        return (EnemyRendererInterface) enemy;
    }


}
