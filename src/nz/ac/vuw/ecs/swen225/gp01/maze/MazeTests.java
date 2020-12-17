package nz.ac.vuw.ecs.swen225.gp01.maze;

import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;

import nz.ac.vuw.ecs.swen225.gp01.persistence.EnemyLoader;

import static org.junit.Assert.*;

/**
 * Unit testing for this package
 */

public class MazeTests {
    /**
     * Tests construction of a basic small maze
     */
    @Test
    public void test_01(){
        Maze actual = new Maze(5,5);
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Tests construction of a basic moderate size maze
     */
    @Test
    public void test_02(){
        Maze actual = new Maze(10,10);
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test construction of a simple maze with player, treasures, an exit door, and an exit
     */
    @Test
    public void test_03(){
        Maze actual = buildBasicMaze();
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{plyu}{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{trsr}{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{trsr}{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{trsr}{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player movement onto free tile
     */
    @Test
    public void test_04(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'r');
        assertEquals('r',actual.getPlayer().getDirection());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{plyr}{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{trsr}{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{trsr}{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{trsr}{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player movement onto free tile again
     */
    @Test
    public void test_05(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'d');
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{plyd}{trsr}{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{trsr}{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{trsr}{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player trying to move onto a wall tile
     */
    @Test
    public void test_06(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'u');
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{plyu}{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{trsr}{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{trsr}{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{trsr}{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player collecting a treasure
     */
    @Test
    public void test_07(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{plyr}{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{trsr}{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{trsr}{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
        assertEquals(1,actual.getPlayer().getTreasureCount());
        int treasureCount = 0;
        for(Collectible c:actual.getCollectibles()){
            if(c instanceof Treasure){
                treasureCount++;
            }
        }
        assertEquals(2,treasureCount);
    }

    /**
     * Test player collecting 2 treasures
     */
    @Test
    public void test_08(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{plyd}{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{trsr}{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
        assertEquals(2,actual.getPlayer().getTreasureCount());
        int treasureCount = 0;
        for(Collectible c:actual.getCollectibles()){
            if(c instanceof Treasure){
                treasureCount++;
            }
        }
        assertEquals(1,treasureCount);
    }

    /**
     * Test player collecting 2 treasures and trying to open the exit door
     */
    @Test
    public void test_09(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        assertFalse(actual.move(actual.getPlayer(), 'd')); //move will return false when the player tries to open the door
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{trsr}{    }{plyd}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
        assertEquals(2,actual.getPlayer().getTreasureCount());
        int treasureCount = 0;
        for(Collectible c:actual.getCollectibles()){
            if(c instanceof Treasure){
                treasureCount++;
            }
        }
        assertEquals(1,treasureCount);
    }

    /**
     * Test player collecting all treasures and trying to open the exit door
     */
    @Test
    public void test_10(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        assertTrue(actual.move(actual.getPlayer(), 'd')); //move will return true when the player tries to open the door
        actual.move(actual.getPlayer(),'u');
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{plyu}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
        assertEquals(3,actual.getPlayer().getTreasureCount());
        int treasureCount = 0;
        for(Collectible c:actual.getCollectibles()){
            if(c instanceof Treasure){
                treasureCount++;
            }
        }
        assertEquals(0,treasureCount);
        assertFalse(actual.getTile(5,6).hasDoor());
    }

    /**
     * Test player collecting all treasures, opening the exit door, and finishing the maze
     */
    @Test
    public void test_11(){
        Maze actual = buildBasicMaze();
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        assertTrue(actual.move(actual.getPlayer(), 'd')); //move will return true when the player tries to open the door
        actual.move(actual.getPlayer(),'d');
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{plyd}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
        assertEquals(actual.getPlayer().getTreasureCount(),3);
        int treasureCount = 0;
        for(Collectible c:actual.getCollectibles()){
            if(c instanceof Treasure){
                treasureCount++;
            }
        }
        assertEquals(0,treasureCount);
        assertFalse(actual.getTile(5,6).hasDoor());
        assertTrue(actual.isWon());
    }

    /**
     * Test construction of a more advanced maze with keys and coloured doors
     */
    @Test
    public void test_12(){
        Maze actual = buildKeyMaze();
        String expected = "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{rdky}{WALL}{trsr}{    }{gndr}{    }{WALL}\n" +
                "{WALL}{bldr}{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{trsr}{WALL}{ywdr}{WALL}\n" +
                "{WALL}{    }{ywky}{    }{    }{WALL}{trsr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{rddr}{WALL}{xtdr}{WALL}\n" +
                "{WALL}{plyu}{blky}{WALL}{gnky}{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player collecting a blue key and opening blue door
     */
    @Test
    public void test_13(){
        Maze actual = buildKeyMaze();
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertTrue(actual.move(actual.getPlayer(),'u'));
        String expected = "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{rdky}{WALL}{trsr}{    }{gndr}{    }{WALL}\n" +
                "{WALL}{plyu}{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{trsr}{WALL}{ywdr}{WALL}\n" +
                "{WALL}{    }{ywky}{    }{    }{WALL}{trsr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{rddr}{WALL}{xtdr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{gnky}{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player collecting a blue key and failing to open red door
     */
    @Test
    public void test_14(){
        Maze actual = buildKeyMaze();
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        assertFalse(actual.move(actual.getPlayer(),'d'));
        String expected = "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{rdky}{WALL}{trsr}{    }{gndr}{    }{WALL}\n" +
                "{WALL}{bldr}{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{trsr}{WALL}{ywdr}{WALL}\n" +
                "{WALL}{    }{ywky}{    }{plyd}{WALL}{trsr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{rddr}{WALL}{xtdr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{gnky}{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player collecting a blue key, opening the blue door, collecting red key, open red door
     */
    @Test
    public void test_15(){
        Maze actual = buildKeyMaze();
        actual.move(actual.getPlayer(),'r');
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertTrue(actual.move(actual.getPlayer(),'u'));
        assertEquals(0, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        assertTrue(actual.move(actual.getPlayer(),'d'));
        assertEquals(0, actual.getPlayer().getInventory().size());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{WALL}{trsr}{    }{gndr}{    }{WALL}\n" +
                "{WALL}{    }{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{trsr}{WALL}{ywdr}{WALL}\n" +
                "{WALL}{    }{ywky}{    }{    }{WALL}{trsr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{plyd}{WALL}{xtdr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{gnky}{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player collecting a blue key and yellow key, then opening blue door
     */
    @Test
    public void test_16(){
        Maze actual = buildKeyMaze();
        actual.move(actual.getPlayer(),'r');
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        assertTrue(actual.move(actual.getPlayer(),'u'));
        assertEquals(1, actual.getPlayer().getInventory().size());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{rdky}{WALL}{trsr}{    }{gndr}{    }{WALL}\n" +
                "{WALL}{plyu}{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{trsr}{WALL}{ywdr}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{WALL}{trsr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{rddr}{WALL}{xtdr}{WALL}\n" +
                "{WALL}{    }{    }{WALL}{gnky}{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player collecting all the keys, then opening all the doors
     */
    @Test
    public void test_17(){
        Maze actual = buildKeyMaze();
        actual.move(actual.getPlayer(),'r');
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        assertTrue(actual.move(actual.getPlayer(),'u'));
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        assertEquals(1,actual.getPlayer().getTreasureCount());
        actual.move(actual.getPlayer(),'d');
        assertTrue(actual.move(actual.getPlayer(),'d'));
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'d');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertTrue(actual.move(actual.getPlayer(),'r'));
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        assertTrue(actual.move(actual.getPlayer(),'d'));
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{WALL}{trsr}{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{plyd}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{trsr}{WALL}\n" +
                        "{WALL}{    }{    }{WALL}{    }{WALL}{xtdr}{WALL}\n" +
                        "{WALL}{    }{    }{WALL}{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test player collecting all the keys, then opening all the doors, then failing to open the exit door
     */
    @Test
    public void test_18(){
        Maze actual = buildKeyMaze();
        actual.move(actual.getPlayer(),'r');
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        assertTrue(actual.move(actual.getPlayer(),'u'));
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        assertEquals(1,actual.getPlayer().getTreasureCount());
        actual.move(actual.getPlayer(),'d');
        assertTrue(actual.move(actual.getPlayer(),'d'));
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'d');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertTrue(actual.move(actual.getPlayer(),'r'));
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        assertTrue(actual.move(actual.getPlayer(),'d'));
        actual.move(actual.getPlayer(),'d');
        assertEquals(2,actual.getPlayer().getTreasureCount());
        assertFalse(actual.move(actual.getPlayer(),'d'));
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{WALL}{trsr}{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{plyd}{WALL}\n" +
                        "{WALL}{    }{    }{WALL}{    }{WALL}{xtdr}{WALL}\n" +
                        "{WALL}{    }{    }{WALL}{    }{WALL}{EXIT}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test the player completing the maze
     */
    @Test
    public void test_19(){
        Maze actual = buildKeyMaze();
        actual.move(actual.getPlayer(),'r');
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        assertTrue(actual.move(actual.getPlayer(),'u'));
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'d');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        assertEquals(1,actual.getPlayer().getTreasureCount());
        actual.move(actual.getPlayer(),'d');
        assertTrue(actual.move(actual.getPlayer(),'d'));
        assertEquals(1, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'d');
        assertEquals(2, actual.getPlayer().getInventory().size());
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'u');
        actual.move(actual.getPlayer(),'l');
        actual.move(actual.getPlayer(),'r');
        assertTrue(actual.move(actual.getPlayer(),'r'));
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'d');
        assertTrue(actual.move(actual.getPlayer(),'d'));
        actual.move(actual.getPlayer(),'d');
        assertEquals(3,actual.getPlayer().getTreasureCount());
        assertTrue(actual.move(actual.getPlayer(),'d'));
        actual.move(actual.getPlayer(),'d');
        assertTrue(actual.isWon());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{WALL}{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{WALL}{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{WALL}{    }{WALL}{    }{WALL}\n" +
                        "{WALL}{    }{    }{WALL}{    }{WALL}{plyd}{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Testing info maze construction
     */
    @Test
    public void test_20(){
        Maze actual = buildInfoMaze();
        assertTrue(actual.getTile(1,4).isInfo());
        assertEquals("Testing info",actual.getTile(1,4).getInfo());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{plyu}{    }{    }{INFO}{    }{    }{WALL}\n" +
                "{WALL}{    }{trsr}{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{trsr}{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{WALL}{xtdr}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{WALL}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }


    /**
     * Testing maze construction with enemy
     */
    @Test
    public void test_21(){
        Maze actual = buildBasicEnemyMaze();
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{nmLd}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{plyu}{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Testing enemy movement
     */
    @Test
    public void test_22(){
        Maze actual = buildBasicEnemyMaze();
        ArrayList<EnemyInterface> enemies = actual.getEnemies();
        for (EnemyInterface e:enemies){
            actual.move(e,' ');
        }
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{nmLd}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{plyu}{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Testing enemy turning when it reaches a corner
     */
    @Test
    public void test_23(){
        Maze actual = buildBasicEnemyMaze();
        ArrayList<EnemyInterface> enemies = actual.getEnemies();
        for(int i=0;i<7;i++) {
            for (EnemyInterface e : enemies) {
                actual.move(e, ' ');
            }
        }
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{plyu}{    }{    }{nmLl}{    }{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Testing enemy killing the player
     */
    @Test
    public void test_24(){
        Maze actual = buildBasicEnemyMaze();
        ArrayList<EnemyInterface> enemies = actual.getEnemies();
        for(int i=0;i<9;i++) {
            actual.move(enemies.get(0),' ');
        }
        actual.move(enemies.get(0),' ');
        assertTrue(actual.isLost());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{nmLl}{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Testing player moving into enemy and dying
     */
    @Test
    public void test_25(){
        Maze actual = buildBasicEnemyMaze();
        ArrayList<EnemyInterface> enemies = actual.getEnemies();
        for(int i=0;i<7;i++) {
            actual.move(enemies.get(0),' ');
        }
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        actual.move(actual.getPlayer(),'r');
        assertTrue(actual.isLost());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                        "{WALL}{    }{    }{    }{nmLl}{    }{    }{WALL}\n" +
                        "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Testing construction of a maze with multiple enemies
     */
    @Test
    public void test_26(){
        Maze actual = buildMultipleEnemyMaze();
        String expected = "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}{nmLd}{WALL}\n" +
                "{WALL}{nmRu}{WALL}{    }{    }{    }{    }{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{trsr}{nmRl}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{trsr}{trsr}{WALL}\n" +
                "{WALL}{    }{plyu}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{nmRl}{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Move all the enemies in the multiple enemy maze 2 steps
     */
    @Test
    public void test_27(){
        Maze actual = buildMultipleEnemyMaze();
        ArrayList<EnemyInterface> enemies = actual.getEnemies();
        for(int i=0;i<2;i++) {
            for (EnemyInterface e : enemies) {
                actual.move(e, ' ');
            }
        }
        String expected = "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{nmRr}{    }{    }{    }{    }{WALL}{nmLu}{WALL}\n" +
                "{WALL}{    }{WALL}{    }{    }{    }{    }{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{trsr}{nmRr}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{trsr}{trsr}{WALL}\n" +
                "{WALL}{    }{plyu}{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{nmRr}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Player collects a treasure, releasing an enemy that kills them
     */
    @Test
    public void test_28(){
        Maze actual = buildMultipleEnemyMaze();
        ArrayList<EnemyInterface> enemies = actual.getEnemies();
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        actual.move(actual.getPlayer(),'u');
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        actual.move(actual.getPlayer(),'r');
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        actual.move(actual.getPlayer(),'r');
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        actual.move(actual.getPlayer(),'u');
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        actual.move(actual.getPlayer(),'r');
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        actual.move(actual.getPlayer(),'r');
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        actual.move(actual.getPlayer(),'r');
        for (EnemyInterface e : enemies) {
            actual.move(e, ' ');
        }
        assertTrue(actual.isLost());
        String expected = "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{WALL}{nmLd}{WALL}\n" +
                "{WALL}{nmRu}{WALL}{    }{    }{    }{    }{WALL}{WALL}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{nmRl}{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{trsr}{trsr}{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{    }{    }{    }{    }{nmRl}{    }{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Test all level construction methods
     */
    @Test
    public void test_29(){
        Maze actual = new Maze(6,6);
        actual.setWall(2,2);
        actual.addPlayer(1,1);
        actual.addEnemy(1,4, EnemyInterface.AIType.LEFT,'u');
        actual.addTreasure(3,3);
        actual.addInfo(3,4,"Testing");
        actual.addDoor(4,3, Maze.Colour.GREEN);
        actual.addKey(4,1, Maze.Colour.RED);
        actual.setExit(4,4);
        assertEquals(Maze.Colour.GREEN,actual.getTile(4, 3).getDoor().getDoorColour());
        String expected =
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n" +
                "{WALL}{plyu}{    }{    }{nmLu}{WALL}\n" +
                "{WALL}{    }{WALL}{    }{    }{WALL}\n" +
                "{WALL}{    }{    }{trsr}{INFO}{WALL}\n" +
                "{WALL}{rdky}{    }{gndr}{EXIT}{WALL}\n" +
                "{WALL}{WALL}{WALL}{WALL}{WALL}{WALL}\n";
        assertEquals(expected,actual.toString());
    }

    /**
     * Try to create a maze with negative parameters, should fail
     */
    @Test
    public void test_30(){
        try {
            new Maze(-1,0);
            fail();
        }catch (IllegalArgumentException e){
            //test passed
        }
    }

    /**
     * Try to place a collectible outside the map
     */
    @Test
    public void test_31(){
        try {
            Maze m = new Maze(3,3);
            m.addKey(10,10, Maze.Colour.RED);
            fail();
        }catch (AssertionError error){
            //test passed
        }
    }

    /**
     * Try to create a player with a null tile
     */
    @Test
    public void test_32(){
        try {
            new Player(null);
            fail();
        }catch (IllegalArgumentException error){
            //test passed
        }
    }

    /**
     * Try to create a maze with null collectibles
     */
    @Test
    public void test_33(){
        try{
            ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
            tiles.add(new ArrayList<>());
            tiles.add(new ArrayList<>());
            new Maze(3,3,2,tiles,null,new Player(new Tile(0,0)));
            fail();
        }catch (IllegalArgumentException error){
            //test passed
        }
    }

    /**
     * Try to call the getColour method on Treasure, will throw an error
     */
    @Test
    public void test_34(){
        try{
            Treasure t = new Treasure();
            t.getColour();
            fail();
        }catch (IllegalCallerException error){
            //test passed
        }
    }

    /**
     * Constructs a basic maze with a player, some walls, 3 treasures, an exit door, and an exit
     * @return a basic maze with a player, some walls, 3 treasures, an exit door, and an exit
     */
    private Maze buildBasicMaze(){
        Maze temp = new Maze(8,8);
        int boardWidth = 7;
        int boardHeight = 7;
        int totalTreasures = 3;
        ArrayList<ArrayList<Tile>> tiles = temp.getTiles();
        Treasure a = new Treasure();
        Treasure b = new Treasure();
        Treasure c = new Treasure();
        ArrayList<Collectible> collectibles = new ArrayList<>(Arrays.asList(a, b, c));
        Player p = new Player(tiles.get(1).get(1));
        tiles.get(1).get(1).setActor(p);
        tiles.get(2).get(2).setCollectible(a);
        tiles.get(3).get(3).setCollectible(b);
        tiles.get(4).get(4).setCollectible(c);
        tiles.get(6).get(6).setType(Floor.Type.EXIT);
        tiles.get(5).get(5).setType(Floor.Type.WALL);
        tiles.get(6).get(5).setType(Floor.Type.WALL);
        Door d = new Door(null);
        tiles.get(5).get(6).setDoor(d);
        return new Maze(boardWidth,boardHeight,totalTreasures,tiles,collectibles,p);
    }

    /**
     * Constructs a basic maze with an info field
     * @return a basic maze with an info field
     */
    private Maze buildInfoMaze(){
        Maze temp = new Maze(8,8);
        int boardWidth = 7;
        int boardHeight = 7;
        int totalTreasures = 2;
        ArrayList<ArrayList<Tile>> tiles = temp.getTiles();
        Treasure a = new Treasure();
        Treasure b = new Treasure();
        ArrayList<Collectible> collectibles = new ArrayList<>(Arrays.asList(a, b));
        Player p = new Player(tiles.get(1).get(1));
        tiles.get(1).get(1).setActor(p);
        tiles.get(2).get(2).setCollectible(a);
        tiles.get(3).get(3).setCollectible(b);
        tiles.get(6).get(6).setType(Floor.Type.EXIT);
        tiles.get(5).get(5).setType(Floor.Type.WALL);
        tiles.get(6).get(5).setType(Floor.Type.WALL);
        tiles.get(1).get(4).setType(Floor.Type.INFO);
        tiles.get(1).get(4).setInfo("Testing info");
        Door d = new Door(null);
        tiles.get(5).get(6).setDoor(d);
        return new Maze(boardWidth,boardHeight,totalTreasures,tiles,collectibles,p);
    }

    /**
     * Constructs a more advanced maze with a number of coloured keys and doors
     * @return a maze with coloured keys and doors
     */
    private Maze buildKeyMaze(){
        Maze temp = new Maze(8,8);
        int boardWidth = 7;
        int boardHeight = 7;
        int totalTreasures = 3;
        ArrayList<ArrayList<Tile>> tiles = temp.getTiles();
        Treasure a = new Treasure();
        Treasure b = new Treasure();
        Treasure c = new Treasure();
        Key bk = new Key(Maze.Colour.BLUE);
        Key rk = new Key(Maze.Colour.RED);
        Key gk = new Key(Maze.Colour.GREEN);
        Key yk = new Key(Maze.Colour.YELLOW);
        Door bd = new Door(Maze.Colour.BLUE);
        Door rd = new Door(Maze.Colour.RED);
        Door gd = new Door(Maze.Colour.GREEN);
        Door yd = new Door(Maze.Colour.YELLOW);
        ArrayList<Collectible> collectibles = new ArrayList<>(Arrays.asList(a, b, c,bk,rk,gk,yk));
        Player p = new Player(tiles.get(6).get(1));
        tiles.get(6).get(1).setActor(p);
        Door exit = new Door(null);
        tiles.get(5).get(6).setDoor(exit);
        tiles.get(3).get(6).setDoor(yd);
        tiles.get(6).get(6).setType(Floor.Type.EXIT);
        tiles.get(2).get(5).setType(Floor.Type.WALL);
        tiles.get(3).get(5).setType(Floor.Type.WALL);
        tiles.get(4).get(5).setType(Floor.Type.WALL);
        tiles.get(5).get(5).setType(Floor.Type.WALL);
        tiles.get(6).get(5).setType(Floor.Type.WALL);
        tiles.get(1).get(5).setDoor(gd);
        tiles.get(5).get(4).setDoor(rd);
        tiles.get(5).get(3).setType(Floor.Type.WALL);
        tiles.get(6).get(3).setType(Floor.Type.WALL);
        tiles.get(6).get(4).setCollectible(gk);
        tiles.get(2).get(2).setType(Floor.Type.WALL);
        tiles.get(1).get(2).setType(Floor.Type.WALL);
        tiles.get(2).get(1).setDoor(bd);
        tiles.get(1).get(1).setCollectible(rk);
        tiles.get(6).get(2).setCollectible(bk);
        tiles.get(4).get(2).setCollectible(yk);
        tiles.get(4).get(6).setCollectible(c);
        tiles.get(1).get(3).setCollectible(b);
        tiles.get(3).get(4).setCollectible(a);
        return new Maze(boardWidth,boardHeight,totalTreasures,tiles,collectibles,p);
    }

    /**
     * Constructs a basic maze with a player and an actor
     * @return a basic maze with a player and an actor
     */
    private Maze buildBasicEnemyMaze(){
        Maze temp = new Maze(8,8);
        int boardWidth = 7;
        int boardHeight = 7;
        int totalTreasures = 0;
        ArrayList<ArrayList<Tile>> tiles = temp.getTiles();
        Player p = new Player(tiles.get(6).get(1));
        tiles.get(6).get(1).setActor(p);
        ArrayList<EnemyInterface> enemies = new ArrayList<>();
        try {
            EnemyInterface e = new EnemyLoader().getEnemyInstance(tiles.get(1).get(6), EnemyInterface.AIType.LEFT, 'd');
            enemies.add(e);
            tiles.get(1).get(6).setActor(e);
        }
        catch (Exception e){System.out.println(e);}
        return new Maze(boardWidth,boardHeight,totalTreasures,tiles,new ArrayList<Collectible>(),enemies,p);
    }

    /**
     * Constructs a maze with a player and multiple enemies in various situations
     * One enemy is trapped in walls so will spin around
     * Another is trapped behind collectibles, so will kill the player when they're collected
     * Another will spin in a corner
     * One orbits a single wall
     * @return a maze with a player and multiple enemies in various situations
     */
    private Maze buildMultipleEnemyMaze(){
        Maze temp = new Maze(10,10);
        int boardWidth = 9;
        int boardHeight = 9;
        int totalTreasures = 3;
        ArrayList<ArrayList<Tile>> tiles = temp.getTiles();
        Player p = new Player(tiles.get(5).get(2));
        tiles.get(5).get(2).setActor(p);
        ArrayList<EnemyInterface> enemies = new ArrayList<>();
        Treasure a = new Treasure();
        Treasure b = new Treasure();
        Treasure c = new Treasure();
        ArrayList<Collectible> collectibles = new ArrayList<>(Arrays.asList(a, b, c));
        tiles.get(3).get(7).setCollectible(a);
        tiles.get(4).get(7).setCollectible(b);
        tiles.get(4).get(8).setCollectible(c);
        tiles.get(2).get(2).setType(Floor.Type.WALL);
        tiles.get(1).get(7).setType(Floor.Type.WALL);
        tiles.get(2).get(7).setType(Floor.Type.WALL);
        tiles.get(2).get(8).setType(Floor.Type.WALL);
        try {
            EnemyInterface e1 = new EnemyLoader().getEnemyInstance(tiles.get(1).get(8), EnemyInterface.AIType.LEFT, 'd');
            enemies.add(e1);
            EnemyInterface e2 = new EnemyLoader().getEnemyInstance(tiles.get(3).get(8), EnemyInterface.AIType.RIGHT, 'l');
            enemies.add(e2);
            EnemyInterface e3 = new EnemyLoader().getEnemyInstance(tiles.get(2).get(1), EnemyInterface.AIType.RIGHT, 'u');
            enemies.add(e3);
            EnemyInterface e4 = new EnemyLoader().getEnemyInstance(tiles.get(8).get(7), EnemyInterface.AIType.RIGHT, 'l');
            enemies.add(e4);
            tiles.get(1).get(8).setActor(e1);
            tiles.get(3).get(8).setActor(e2);
            tiles.get(2).get(1).setActor(e3);
            tiles.get(8).get(7).setActor(e4);
        }
        catch (Exception e){System.out.println(e);}
        return new Maze(boardWidth,boardHeight,totalTreasures,tiles,collectibles,enemies,p);
    }
}
