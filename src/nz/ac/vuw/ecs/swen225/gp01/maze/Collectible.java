package nz.ac.vuw.ecs.swen225.gp01.maze;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface for collectible objects, implemented by key and treasure classes
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Key.class, name = "key"),
        @JsonSubTypes.Type(value = Treasure.class, name = "treasure"),
})
public interface Collectible {
    /**
     * Will return the colour of this collectible, or null if this is a treasure
     * @return colour of door, or null if treasure
     */
    Maze.Colour getColour();
}
