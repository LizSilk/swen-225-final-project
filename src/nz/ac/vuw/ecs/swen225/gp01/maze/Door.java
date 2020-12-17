package nz.ac.vuw.ecs.swen225.gp01.maze;

/**
 * Stores information about a door in the maze, contains the logic to determine if the player can open the door
 */
public class Door {

    private Maze.Colour doorColour; //only coloured doors will have a colour, exit will have null here

    /**
     * Constructs a new door object of the given colour, or an exit door if given a null parameter
     * @param colour the colour of the door, or null if this will be an exit door
     */
    public Door(Maze.Colour colour){
        doorColour = colour;
    }

    /**
     * Determine if the player has the right collectibles to open this door
     * @param p the player object from the maze, used to check their inventory for the correct keys
     * @param totalTreasures the total treasures in the maze, used to check if the player has collected all the treasures
     * @return true if the player can open the door, false if they can not
     */
    public boolean open(Player p, int totalTreasures){
        assert (totalTreasures>-1);
        if(doorColour == null){ //exit door
            return p.getTreasureCount() == totalTreasures; //compare the players collected treasures to the total treasures in the maze
        }
        for(Collectible c:p.getInventory()){ //check through the player's inventory for the matching key
            if(c instanceof Key){
                Key k = (Key) c;
                if(k.getColour()==doorColour){
                    p.getInventory().remove(k); //remove the key from the players inventory
                    return true;
                }
            }
        }
        return false; //the player can not open this door
    }

    /**
     * Get the colour of this door, colour will be null if it's an exit door
     * @return door colour, or null if exit door
     */
    public Maze.Colour getDoorColour() {
        return doorColour;
    }

    @Override
    public String toString() {
        if(doorColour == Maze.Colour.RED){return "rddr";}
        if(doorColour == Maze.Colour.GREEN){return "gndr";}
        if(doorColour == Maze.Colour.BLUE){return "bldr";}
        if(doorColour == Maze.Colour.YELLOW){return "ywdr";}
        return "xtdr";
    }
}
