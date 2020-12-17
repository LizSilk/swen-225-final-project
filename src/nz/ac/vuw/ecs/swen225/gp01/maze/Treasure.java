package nz.ac.vuw.ecs.swen225.gp01.maze;

/**
 * Acts as the treasure in the maze, implements collectible, doesn't store any information, only needs to exist in the maze, other classes handle the logic
 */

public class Treasure implements Collectible {

    @Override
    public String toString() {
        return "trsr";
    }

    @Override
    public Maze.Colour getColour() {
        throw new IllegalCallerException();
    }
}
