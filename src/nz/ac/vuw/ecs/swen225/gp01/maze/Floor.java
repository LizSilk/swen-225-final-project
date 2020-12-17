package nz.ac.vuw.ecs.swen225.gp01.maze;

import com.google.common.base.Preconditions;

/**
 * Stores information about what type of floor a cell in the maze contains
 */
public class Floor {
    /**
     * Enum of the types of floor a cell can contain
     * FREE represents an an empty tile that the player can move into
     * WALL represents a wall in the maze a player can't move through
     * INFO represents an information tile, that displays a message when the player steps on it
     * EXIT represents an exit tile, that will mark the maze as complete once the player steps on it
     */
    public enum Type{
        FREE,
        WALL,
        INFO,
        EXIT
    }

    private Type type;
    private String information; //used by INFO tile, null for all other tile types

    /**
     * Construct a floor object of the specified type
     * @param t the type this floor will be
     */
    public Floor(Type t){
        Preconditions.checkNotNull(t);
        type = t;
    }

    /**
     * Set the information to be displayed when this tile is stepped on, will only be called when constructing info tiles
     * @param info the string to be displayed when the player occupies the tile this floor is associated with
     */
    public void setInfo(String info){
        assert (type==Type.INFO && info!=null);
        information = info;
    }
    public String getInfo() {
        return information;
    }

    public void setType(Type inType) {
        assert (type!=null);
        type = inType;
    }
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        if(type==Type.FREE){return "    ";}
        return ""+type;
    }
}
