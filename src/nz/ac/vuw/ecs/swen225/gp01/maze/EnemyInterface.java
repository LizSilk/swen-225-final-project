package nz.ac.vuw.ecs.swen225.gp01.maze;

/**
 * Interface to allow enemies to be loaded from the .jar file for each level and still be interacted with by the existing code
 * This was a nightmare to implement for everyone involved, we dedicated an entire branch to getting it to work
 * Note to future self, switches and enums are fine by themselves, don't ever use them together
 */

public interface EnemyInterface extends Actor {

    /**
     * Determines what direction the AI will first look when trying to move forward
     */
    enum AIType {
        LEFT, //AI will first look at the tile to it's left then clockwise from there to determine it's next move
        //e.g. enemy can move left, moves left
        //OR enemy can't move left, can move forward, so moves forward
        //OR enemy can't move left or forward, can move right, so moves right
        //OR enemy can't move left, forward, right, can move back, so moves back
        RIGHT; //AI will first look at the tile to it's right then anticlockwise from there to determine it's next move
        //similar to left, but starting from the right side and moving anticlockwise
    }

    AIType getAi();

    int getRow();

    int getCol();

    Tile getLocation();

    char getDirection();

    void setDirection(char dir);

    void move(Tile moveTile);

    String toString();

    /**
     * Contains the logic that the enemy will use to determine which of the given tiles to move into
     * @param frontCell the tile in front of the enemy
     * @param leftCell the tile to the left of the enemy
     * @param rightCell the tile to the right of the enemy
     * @param backCell the tile behind the enemy
     * @return the tile the enemy has determined it can move into
     */
    Tile moveLogic(Tile frontCell, Tile leftCell, Tile rightCell, Tile backCell);

}


