package nz.ac.vuw.ecs.swen225.gp01.maze;

import com.google.common.base.Preconditions;

/**
 * Acts as the cells that make up the maze, stores information about what each cell contains
 */

public class Tile {

    private int row,col;
    private Floor floor; //wall, free, info field, exit
    private Door door; //coloured door, exit lock,
    private Actor actor; //enemy or player
    private Collectible collectible; //key or treasure

    /**
     * Creates a new tile that knows what row and column of the 2d arraylist of tiles it's in
     * @param inRow the row the tile is in
     * @param inCol the column the tile is in
     */
    public Tile(int inRow, int inCol){
        Preconditions.checkArgument(inRow>-1&&inCol>-1);
        row = inRow;
        col = inCol;
        floor = new Floor(Floor.Type.FREE); //all tiles are free by default
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    public void setType(Floor.Type t){
        floor.setType(t);
    }
    public Floor.Type getType(){return floor.getType();}

    public void setInfo(String s){floor.setInfo(s);}
    public boolean isInfo(){return floor.getType()==Floor.Type.INFO;}
    public String getInfo(){return floor.getInfo();}

    public void setActor(Actor a){actor = a;}
    public Actor getActor(){return actor;}
    public void removeActor(){actor = null;}

    public void setCollectible(Collectible c) {collectible = c;}
    public boolean hasCollectible(){return collectible!=null;}
    public Collectible getCollectible(){return collectible;}

    public void setDoor(Door door) {this.door = door;}
    public boolean hasDoor(){return door!=null;}
    public Door getDoor(){return door;}

    /**
     * Checks whether the player can open the door on this tile
     * @param p the player
     * @param totalTreasures the total number of treasures in the maze, used to check if an exit door can be opened
     * @return true if the door was opened, false if it was not
     */
    public boolean openDoor(Player p, int totalTreasures){
        if(door==null){throw new Error("No door on this tile");}
        if(door.open(p,totalTreasures)){ //check if the player can open the door
            door = null; //remove the door from this tile once opened
            return true; //inform the method calling this the door was opened successfully
        }
        return false; //inform the method calling this the door couldn't be opened
    }

    /**
     * Method to determine if this tile can have a new actor, collectible, or door in it
     * @return false if this tile has an actor or has a collectible or isn't a floor tile or has a door, otherwise true
     */
    public boolean isNotOccupied(){return actor == null && collectible == null && floor.getType() == Floor.Type.FREE && door == null;}

    /**
     * Check if an enemy can move into this tile
     * @return true if an enemy could, false if it could not
     */
    public boolean enemyCanMoveinto(){
        //enemy can move only into free, no door, no collectible, into player or no actor
        return (floor.getType()== Floor.Type.FREE)&&(door==null)&&(collectible==null)&&(actor instanceof Player||actor==null);
    }

    @Override
    public String toString() {
        if(collectible!=null){return "{"+collectible+"}";}
        if(actor!=null){return "{"+actor+"}";}
        if(door!=null){return "{"+door+"}";}
        return "{"+floor+"}";
    }
}
