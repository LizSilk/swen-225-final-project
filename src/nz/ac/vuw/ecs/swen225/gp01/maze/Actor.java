package nz.ac.vuw.ecs.swen225.gp01.maze;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface for actor objects. Implemented by both player and enemy classes
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Player.class, name = "player"),
        @JsonSubTypes.Type(value = EnemyInterface.class, name = "enemy"),
})
public interface Actor {

    /**
     * Gets the row the actor is currently in
     * @return the row the actor is in
     */
    int getRow();

    /**
     * Gets the column the actor is currently in
     * @return the column the actor is in
     */
    int getCol();

    /**
     * Gets the current location of the actor
     * @return the tile the actor is on
     */
    Tile getLocation();

    /**
     * Gets the current direction the actor is facing as a character
     * 'u' for up, 'l' for left, 'r' for right, 'd' for down
     * @return char representing the direction that actor is facing
     */
    char getDirection();

    /**
     * Set the direction of the current actor
     * @param dir the direction the actor will face
     */
    void setDirection(char dir);

    /**
     * Tells the actor that the maze has moved it
     * @param moveTile the tile the actor is moving to
     */
    void move(Tile moveTile);
}
