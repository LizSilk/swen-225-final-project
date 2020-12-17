package nz.ac.vuw.ecs.swen225.gp01.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

/**
 * Stores the player and all relevant information, including their inventory and the number of treasures they've collected
 */

public class Player implements Actor {

    private int treasureCount;
    @JsonIgnore //ignore the location to prevent infinite loop
    private Tile location;
    private ArrayList<Collectible> inventory;
    private char direction = 'u'; //player starts facing up

    /**
     * Creates a new player in the maze at the location specified
     * New player's have an empty inventory and the amount of treasures they've collected is 0
     * @param t the tile the player will be starting at
     */
    public Player(Tile t){
        Preconditions.checkArgument(t!=null);
        treasureCount = 0;
        location = t;
        inventory = new ArrayList<>();
    }

    /**
     * Add the given collectible to the players inventory, or if the collectible is a treasure, increment the players collected treasure count
     * @param c the collectible to add to the player inventory
     */
    public void addToInventory(Collectible c){
        assert (c!=null);
        if(c instanceof Treasure){treasureCount++;} //increment player's number of treasures if a treasure is collected
        else{inventory.add(c);} //else add this item to their inventory
    }

    public ArrayList<Collectible> getInventory() {
        return inventory;
    }
    public int getTreasureCount() {
        return treasureCount;
    }
    @Override
    public void setDirection(char dir) {
        assert (dir=='u'||dir=='r'||dir=='d'||dir=='l');
        direction = dir;
    }

    @Override
    public char getDirection() {return direction;}

    @Override
    public int getRow() {
        return location.getRow();
    }
    @Override
    public int getCol() {
        return location.getCol();
    }
    @Override
    public Tile getLocation() {return location;}
    @Override
    public void move(Tile moveTile) {
        location = moveTile;
    }

    @Override
    public String toString() {
        return "ply"+direction;
    }
}
