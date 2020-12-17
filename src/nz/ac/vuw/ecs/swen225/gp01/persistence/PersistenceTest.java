package nz.ac.vuw.ecs.swen225.gp01.persistence;

import nz.ac.vuw.ecs.swen225.gp01.maze.*;
import nz.ac.vuw.ecs.swen225.gp01.persistence.EnemyLoader;
import nz.ac.vuw.ecs.swen225.gp01.persistence.JSONEncoder;
import nz.ac.vuw.ecs.swen225.gp01.persistence.LevelLoader;
import nz.ac.vuw.ecs.swen225.gp01.renderer.EnemyRendererInterface;
import nz.ac.vuw.ecs.swen225.gp01.renderer.FloorRenderer;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * tests for the nz.ac.vuw.ecs.swen225.gp01.persistence module
 *
 * @author Benjmin Silk 300473874
 */
public class PersistenceTest {

    /**
     * Tests that the loadLevel method works for level1.json
     */
    @Test
    public void test_01() {
        Maze maze = generateLevel1();


        Maze output = LevelLoader.loadLevel("levels/level1");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests that the loadTime method works for level1.json
     */
    @Test
    public void test_02() {
        assertEquals(600, LevelLoader.loadTime("levels/level1"));
    }


    /**
     * Tests that the EnemyLoader can load an instance of Enemy from level2.jar
     */
    @Test
    public void test_04() {
        Maze output = LevelLoader.loadLevel("levels/level1");
        try {
            EnemyInterface enemy = new EnemyLoader().getEnemyInstance(output.getTile(2, 3), EnemyInterface.AIType.LEFT, 'u');
            assertEquals(enemy.getAi(), EnemyInterface.AIType.LEFT);
            assertEquals(enemy.getDirection(), 'u');
            assertEquals(enemy.getRow(), 2);
            assertEquals(enemy.getCol(), 3);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests that EnemyLoader can load an EnemyRenderer without catastrophic failure
     */
    @Test
    public void test_05() {
        try {
            EnemyRendererInterface rendererInterface = new EnemyLoader().getEnemyRenderer(new FloorRenderer(new Point(2, 3), 1, true), true, 'd');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests loading blue keys
     */
    @Test
    public void test_06_a() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addEnemy(1, 4, EnemyInterface.AIType.LEFT, 'd');
        maze.getPlayer().addToInventory(new Key(Maze.Colour.BLUE));
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests loading red keys
     */
    @Test
    public void test_06_b() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addEnemy(1, 4, EnemyInterface.AIType.LEFT, 'd');
        maze.getPlayer().addToInventory(new Key(Maze.Colour.RED));
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests loading green keys
     */
    @Test
    public void test_06_c() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addEnemy(1, 4, EnemyInterface.AIType.LEFT, 'd');
        maze.getPlayer().addToInventory(new Key(Maze.Colour.GREEN));
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests loading yellow keys
     */
    @Test
    public void test_06_d() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addEnemy(1, 4, EnemyInterface.AIType.LEFT, 'd');
        maze.getPlayer().addToInventory(new Key(Maze.Colour.YELLOW));
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests JSONEncoder.encodeLevel and LevelLoader.loadTime
     * Stores a time into the JSON file
     * Loads it and checks that it's the same
     */
    @Test
    public void test_07() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        // maze.addEnemy(1, 4, EnemyInterface.AIType.LEFT,'d');
        JSONEncoder.encodeLevel(maze, 1200, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());
        assertEquals(1200, LevelLoader.loadTime("levels/levelTest"));

    }

    /**
     * Tests that LevelLoader can load ai with AIType RIGHT
     */
    @Test
    public void test_08() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addEnemy(1, 4, EnemyInterface.AIType.RIGHT, 'd');
        System.out.println(maze);
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests that LevelLoader can load green doors and keys
     */
    @Test
    public void test_09() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addDoor(3, 4, Maze.Colour.GREEN);
        maze.addKey(4, 4, Maze.Colour.GREEN);
        System.out.println(maze);
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests that LevelLoader can load red doors and keys
     */
    @Test
    public void test_10() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addDoor(3, 4, Maze.Colour.RED);
        maze.addKey(4, 4, Maze.Colour.RED);
        System.out.println(maze);
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Tests that LevelLoader can load yellow doors and keys
     */
    @Test
    public void test_11() {
        Maze maze = new Maze(8, 8);
        maze.buildTestMaze();
        maze.addDoor(3, 4, Maze.Colour.YELLOW);
        maze.addKey(4, 4, Maze.Colour.YELLOW);
        System.out.println(maze);
        JSONEncoder.encodeLevel(maze, 0, "levelTest");

        Maze output = LevelLoader.loadLevel("levels/levelTest");
        assertEquals(maze.toString(), output.toString());

    }

    /**
     * Generates level2 and tests if it will load out
     */
    @Test
    public void test_12() {
        Maze maze = generateLevel2();
        System.out.println(maze);
        JSONEncoder.encodeLevel(maze, 600, "level2");

        Maze output = LevelLoader.loadLevel("levels/level2");
        assertEquals(maze.toString(), output.toString());

    }

    private Maze generateLevel1() {
        Maze maze = new Maze(13, 13);
        maze.addPlayer(1, 1);
        maze.addInfo(2, 2, "Collect all the treasures to get to the\nexit. Use keys to open doors");

        maze.setWall(1, 5);
        maze.setWall(3, 5);
        maze.setWall(4, 5);
        maze.setWall(5, 5);
        maze.setWall(7, 5);
        maze.setWall(8, 5);
        maze.setWall(4, 4);
        maze.setWall(4, 3);
        maze.setWall(4, 2);
        maze.setWall(8, 1);
        maze.setWall(8, 2);
        maze.setWall(8, 4);
        maze.setWall(9, 1);
        maze.setWall(9, 2);
        maze.setWall(10, 1);
        maze.setWall(11, 1);
        maze.setWall(9, 4);
        maze.setWall(9, 5);
        maze.setWall(10, 5);
        maze.setWall(11, 5);
        maze.setWall(4, 6);
        maze.setWall(4, 7);
        maze.setWall(4, 8);
        maze.setWall(4, 9);
        maze.setWall(4, 10);
        maze.setWall(4, 11);
        maze.setWall(3, 8);
        maze.setWall(1, 8);
        maze.setWall(8, 7);
        maze.setWall(8, 8);
        maze.setWall(8, 9);
        maze.setWall(8, 10);
        maze.setWall(8, 11);
        maze.setWall(6, 9);
        maze.setWall(7, 9);
        maze.setWall(9, 9);
        maze.setWall(10, 9);


        maze.addDoor(2, 8, null);
        maze.addKey(3, 7, Maze.Colour.RED);
        maze.addDoor(4, 1, Maze.Colour.RED);
        maze.addKey(5, 4, Maze.Colour.GREEN);
        maze.addDoor(8, 3, Maze.Colour.GREEN);
        maze.addKey(11, 2, Maze.Colour.YELLOW);
        maze.addDoor(6, 5, Maze.Colour.YELLOW);
        maze.addKey(7, 11, Maze.Colour.BLUE);
        maze.addDoor(8, 6, Maze.Colour.BLUE);

        maze.addTreasure(11, 4);
        maze.addTreasure(1, 7);
        maze.addTreasure(5, 11);
        maze.addTreasure(9, 11);
        maze.addTreasure(10, 7);

        maze.setExit(2, 11);

        return maze;
    }

    private Maze generateLevel2() {
        Maze maze = new Maze(9, 13);
        maze.addPlayer(6, 11);
        maze.addInfo(6, 10, "Watch out for bugs! And I don't\n just mean the ones in our code!");

        maze.setWall(1, 9);
        maze.setWall(2, 9);
        maze.setWall(3, 9);
        maze.setWall(4, 9);
        maze.setWall(4, 10);
        maze.setWall(3, 6);
        maze.setWall(4, 6);
        maze.setWall(5, 6);
        maze.setWall(7, 4);
        maze.setWall(5, 4);
        maze.setWall(4, 4);
        maze.setWall(3, 4);
        maze.setWall(1, 4);
        maze.setWall(4, 3);
        maze.setWall(4, 2);
        maze.setWall(4, 1);
        maze.setWall(6,2);

        maze.addDoor(4, 11, null);
        maze.addKey(7,5, Maze.Colour.RED);
        maze.addDoor(6,4, Maze.Colour.RED);
        maze.addKey(5,1, Maze.Colour.GREEN);
        maze.addDoor(2,4, Maze.Colour.GREEN);

        maze.addTreasure(7,1);
        maze.addTreasure(1,1);
        maze.addTreasure(3,1);
        maze.addTreasure(1,5);

        maze.addEnemy(2,6, EnemyInterface.AIType.LEFT,'l');
        maze.addEnemy(6,6, EnemyInterface.AIType.LEFT,'r');
        maze.addEnemy(6,3, EnemyInterface.AIType.LEFT,'u');
        maze.setExit(1, 10);

        return maze;
    }

}
