package nz.ac.vuw.ecs.swen225.gp01.maze;

/**
 * Stores the information about a key, implements the collectible interface
 */
public class Key implements Collectible {

    private Maze.Colour colour;

    /**
     * Construct a key of the given colour
     * @param inColour the colour this key will be
     */
    public Key(Maze.Colour inColour){
        colour = inColour;
    }

    @Override
    public Maze.Colour getColour(){return colour;}

    @Override
    public String toString() {
        if(colour == Maze.Colour.RED){return "rdky";}
        if(colour == Maze.Colour.GREEN){return "gnky";}
        if(colour == Maze.Colour.BLUE){return "blky";}
        if(colour == Maze.Colour.YELLOW){return "ywky";}
        throw new Error("Unrecognised key colour");
    }
}
