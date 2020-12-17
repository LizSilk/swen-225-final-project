package nz.ac.vuw.ecs.swen225.gp01.maze;

import java.util.*;
import com.google.common.base.Preconditions;
import nz.ac.vuw.ecs.swen225.gp01.persistence.EnemyLoader;

/**
 * The maze that chap must move through, contains the majority of the logic for this package
 * This is the main class of this package
 */

public class Maze {
    /**
     * Enum of the possible colours for doors and keys
     */
    public enum Colour{
        BLUE,
        RED,
        GREEN,
        YELLOW
    }
    private final int boardWidth, boardHeight;
    private int totalTreasures;
    private ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
    private ArrayList<Collectible> collectibles = new ArrayList<>();
    private ArrayList<EnemyInterface> enemies = new ArrayList<>();
    private Player player; //will only ever be one player character
    private boolean hasWon = false, hasLost = false;

    /**
     * Constructor used for building an empty maze for level design and construction
     * @param rows the number of rows the maze will have
     * @param cols the number of columns the maze will have
     */
    public Maze(int rows, int cols){
        Preconditions.checkArgument(rows>0&&cols>0);
        boardHeight = rows-1; //-1 so that building a 4x4 maze doesn't give you a 5x5
        boardWidth = cols-1;
        totalTreasures = 0;
        buildBoard();
    }

    /**
     * Constructor used by the JSON level decoder to build loaded maze
     * @param inBoardWidth the amount of columns the maze has, ended up being redundant because my logic was bad
     * @param inBoardHeight the amount of rows the maze has, ended up being redundant because my logic was bad
     * @param inTotalTreasures the total number of treasures in the maze, ended up being redundant because my logic was bad
     * @param inTiles the arraylist of arraylists of tiles
     * @param inCollectibles the arraylist of tiles
     * @param inPlayer the player object
     */
    public Maze(int inBoardWidth, int inBoardHeight, int inTotalTreasures, ArrayList<ArrayList<Tile>> inTiles,
                ArrayList<Collectible> inCollectibles, Player inPlayer){
        Preconditions.checkArgument((inBoardWidth>0) && (inBoardHeight>0) && (inTotalTreasures>-1));
        Preconditions.checkArgument((inTiles!=null) && (inCollectibles!=null) && (inPlayer!=null));
        boardWidth = inTiles.get(0).size();
        boardHeight = inTiles.size();
        totalTreasures = countTreasures(inCollectibles);
        tiles = inTiles;
        collectibles = inCollectibles;
        player = inPlayer;
    }

    /**
     * Constructor used by the JSON level decoder to build loaded maze, exactly the same as above constructor but allows decoder to pass in enemies where necessary
     * @param inBoardWidth the amount of columns the maze has, ended up being redundant because my logic was bad
     * @param inBoardHeight the amount of rows the maze has, ended up being redundant because my logic was bad
     * @param inTotalTreasures the total number of treasures in the maze, ended up being redundant because my logic was bad
     * @param inTiles the arraylist of arraylists of tiles
     * @param inCollectibles the arraylist of tiles
     * @param inEnemies the arraylist of enemies
     * @param inPlayer the player object
     */
    public Maze(int inBoardWidth, int inBoardHeight, int inTotalTreasures, ArrayList<ArrayList<Tile>> inTiles,
                ArrayList<Collectible> inCollectibles,ArrayList<EnemyInterface> inEnemies, Player inPlayer){
        Preconditions.checkArgument((inBoardWidth>0) && (inBoardHeight>0) && (inTotalTreasures>-1));
        Preconditions.checkArgument((inTiles!=null) && (inCollectibles!=null) && (inEnemies!=null) && (inPlayer!=null)); //check the initial arguments of the constructor
        boardWidth = inTiles.get(0).size();
        boardHeight = inTiles.size();
        totalTreasures = countTreasures(inCollectibles);
        tiles = inTiles;
        collectibles = inCollectibles;
        enemies = inEnemies;
        player = inPlayer;
    }

    /**
     * Builds an empty board and sets the external tiles to walls
     */
    private void buildBoard(){
        //add the required amount of tiles to the board
        for(int row=0;row<=boardHeight;row++){
            tiles.add(new ArrayList<>());
            ArrayList<Tile> thisRow = tiles.get(row);
            for(int col=0;col<=boardWidth;col++){
                thisRow.add(new Tile(row,col));
            }
        }
        //surround the outside with walls
        for(int row=0;row<=boardHeight;row++){
            for(int col=0;col<=boardWidth;col++){
                if(col==0||row==0||row==boardHeight||col==boardWidth){
                    getTile(row,col).setType(Floor.Type.WALL);
                }
            }
        }
    }

    /**
     * Check the treasure count is equal to the actual treasure count
     * @param inCollectibles the arraylist of collectibles
     * @return true if the counts are the same
     */
    public int countTreasures(ArrayList<Collectible> inCollectibles){
        int count = 0;
        for(Collectible c:inCollectibles){
            if(c instanceof Treasure){
                count++;
            }
        }
        return count;
    }

    /**
     * Add a player at the specified location, there can only be one player on the board
     * @param row the row to add it to
     * @param col the column to add it to
     */
    public void addPlayer(int row, int col){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        assert (player==null); //make sure a player doesn't already exist, only one player can exist at any given time
        Tile t =getTile(row,col);
        assert (t.isNotOccupied()); //check a player can be placed on this tile
        player = new Player(t);
        getTile(row,col).setActor(player);
    }

    /**
     * Add an enemy at the specified location and set its ai type and starting direction
     * @param row the row to add it to
     * @param col the column to add it to
     * @param ai the ai type the enemy will lose
     * @param dir the direction the enemy will be facing
     */
    public void addEnemy(int row, int col, EnemyInterface.AIType ai, char dir){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        Tile t =getTile(row,col);
        assert (t.isNotOccupied()); //check an enemy can be placed on this tile
        try {
            EnemyInterface e = new EnemyLoader().getEnemyInstance(t, ai, dir);
            enemies.add(e);
            getTile(row,col).setActor(e);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Add a wall to the board at the specified location
     * @param row the row to add it to
     * @param col the column to add it to
     */
    public void setWall(int row, int col){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        Tile t = getTile(row,col);
        assert (t.isNotOccupied()); //check a wall can be placed on this tile
        t.setType(Floor.Type.WALL);
    }

    /**
     * Add a key to the board at the specified location and set it's colour
     * @param row the row to add it to
     * @param col the column to add it to
     * @param colour the colour of the key
     */
    public void addKey(int row,int col,Colour colour){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        Tile t = getTile(row,col);
        assert (t.isNotOccupied()); //check a key can be placed on this tile
        Key k = new Key(colour);
        collectibles.add(k);
        t.setCollectible(k);
    }

    /**
     * Add a treasure to the board at the specified location
     * @param row the row to add it to
     * @param col the column to add it to
     */
    public void addTreasure(int row, int col){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        Tile t = getTile(row,col);
        assert (t.isNotOccupied()); //check a treasure can be placed on this tile
        Treasure treasure = new Treasure();
        collectibles.add(treasure); //store the treasure in the list
        t.setCollectible(treasure); //set the tiles content to this treasure
        totalTreasures++; //increment the counter of treasures
    }

    /**
     * Add a door to the board at the specified location and set it's colour
     * @param row the row to add it to
     * @param col the column to add it to
     * @param c the colour of the door
     */
    public void addDoor(int row, int col, Colour c){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        Tile t = getTile(row,col);
        assert (t.isNotOccupied()); //check a wall can be placed on this tile
        Door d = new Door(c);
        t.setDoor(d);
    }

    /**
     * Add an information tile to the board at the specified location and set the information it will display
     * @param row the row to add it to
     * @param col the column to add it to
     * @param info the information that will be displayed when this tile is stepped on
     */
    public void addInfo(int row, int col, String info){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        Tile t = getTile(row,col);
        assert (t.isNotOccupied()); //check a wall can be placed on this tile
        t.setType(Floor.Type.INFO);
        t.setInfo(info);
    }

    /**
     * Add an exit tile at the specified location to the current board
     * @param row the row to add it to
     * @param col the column to add it to
     */
    public void setExit(int row, int col){
        assert (isOnBoard(row,col)); //make sure the row and column refer to a tile on the board
        Tile t = getTile(row,col);
        assert (t.isNotOccupied()); //check a wall can be placed on this tile
        t.setType(Floor.Type.EXIT);
    }

    /**
     * public method for moving an actor, called by other classes
     * determines which internal method to call based on the type of actor
     * @param a the actor to be moved
     * @param dir the direction to move the actor, not used by enemy movement
     * @return true if the move could be made, false if it could not
     */
    public boolean move(Actor a, char dir) {
        assert (a!=null);
        if(a instanceof Player){
            return movePlayer(dir);
        }
        else if(a instanceof EnemyInterface){
            moveEnemy((EnemyInterface) a);
            return true;
        }
        throw new Error("Unknown actor type");
    }

    /**
     * Move the player one tile in the given direction
     * contains the logic for when a player moves onto a tile with something in it
     * @param dir the direction to move the player in
     * @return true if the move could be made, false if it could not
     */
    private boolean movePlayer(char dir){
        Tile prev = player.getLocation();
        int currentRow = prev.getRow();
        int currentCol = prev.getCol();
        Tile target;
        switch (dir){
            case 'u':
                if(currentRow==0){return false;} //stop the player trying to leave the board causing an outofbounds error
                target = getTile(currentRow-1,currentCol);
                break;
            case 'd':
                if(currentRow==boardHeight){return false;} //stop the player trying to leave the board causing an outofbounds error
                target = getTile(currentRow+1,currentCol);
                break;
            case 'l':
                if(currentCol==0){return false;} //stop the player trying to leave the board causing an outofbounds error
                target = getTile(currentRow,currentCol-1);
                break;
            case 'r':
                if(currentCol==boardWidth){return false;} //stop the player trying to leave the board causing an outofbounds error
                target = getTile(currentRow,currentCol+1);
                break;
            default: throw new Error("Bad direction");
        }
        player.setDirection(dir); //if a player tries to move have them face that way
        if(target.getType()==Floor.Type.WALL){return false;} //player can't move onto a wall tile
        //if the target tile has a collectible, collect it and remove it from the tile and collectible list
        if(target.hasCollectible()){
            Collectible c = target.getCollectible();
            player.addToInventory(c);
            target.setCollectible(null);
            collectibles.remove(c);
        }
        //try to unlock the door
        else if(target.hasDoor()){
            if(target.openDoor(player,totalTreasures)){
                moveActor(player,prev,target);
                return true;
            }
            return false;
        }
        if(target.getActor()!=null){ //if player moves into an enemy, they lose
            hasLost = true;
            player.getLocation().removeActor(); //delete the player when they move into the enemy
            return false;
        }
        //default move
        moveActor(player,prev,target);
        //if the player moves onto the exit, mark the game as won
        if(target.getType()== Floor.Type.EXIT){
            hasWon = true;
            //System.out.println("Player has reached the exit");
        }
        return true;
    }

    /**
     * Advance the current enemy by one tile
     * Contains all the AI for the enemies
     * Enemies determine which cell they will move into based on the cells immediately surrounding them
     * @param e the enemy to be moved
     */
    private void moveEnemy(EnemyInterface e){
        Tile target, leftCell, rightCell, frontCell, backCell;
        Tile current = e.getLocation();
        char direction = e.getDirection();
        switch (direction){ //determine the cells surrounding the enemy based on it's current direction
            case 'u':
                leftCell = getTile(e.getRow(),e.getCol()-1);
                rightCell = getTile(e.getRow(),e.getCol()+1);
                frontCell = getTile(e.getRow()-1,e.getCol());
                backCell = getTile(e.getRow()+1,e.getCol());
                break;
            case 'd':
                leftCell = getTile(e.getRow(),e.getCol()+1);
                rightCell = getTile(e.getRow(),e.getCol()-1);
                frontCell = getTile(e.getRow()+1,e.getCol());
                backCell = getTile(e.getRow()-1,e.getCol());
                break;
            case 'r':
                leftCell = getTile(e.getRow()-1,e.getCol());
                rightCell = getTile(e.getRow()+1,e.getCol());
                frontCell = getTile(e.getRow(),e.getCol()+1);
                backCell = getTile(e.getRow(),e.getCol()-1);
                break;
            case 'l':
                leftCell = getTile(e.getRow()+1,e.getCol());
                rightCell = getTile(e.getRow()-1,e.getCol());
                frontCell = getTile(e.getRow(),e.getCol()-1);
                backCell = getTile(e.getRow(),e.getCol()+1);
                break;
            default: throw new Error("Bad direction");
        }
        target = e.moveLogic(frontCell,leftCell,rightCell,backCell); //gives the enemy the tiles around it and gets it to determine which it can move into
        if(target.getActor()!=null&&target.getActor().equals(player)){ //if the enemy moves onto a tile with the player, the maze is lost
            hasLost = true;
            //System.out.println("Enemy has collided with player");
        }
        if(!current.equals(target)) { //if the enemy doesn't move, don't run the moveActor method because it deletes them
            moveActor(e, current, target);
        }
    }

    /**
     * Move the given actor from one tile to another by adding them to the target tile and removing them from the previous tile
     * Will only be called once other methods have checked the parameters
     * @param actor the actor to be moved
     * @param prev the current tile the actor is in
     * @param target the tile the actor will be moved to
     */
    private void moveActor(Actor actor,Tile prev , Tile target){
        target.setActor(actor);
        prev.removeActor();
        actor.move(target); //update the actor with their new tile
    }



    //**********************************Helper, Getter, and Setter Methods******************************************************//
    /**
     * Determines whether the given position is within the maze
     * @param row the row of the position
     * @param col the column of the position
     * @return true if the position is within the maze's boundaries, otherwise false
     */
    public boolean isOnBoard(int row, int col){
        return row>-1&&col>-1&&row<boardHeight&&col<boardWidth;
    }

    public Player getPlayer() {return player;}

    public ArrayList<ArrayList<Tile>> getTiles() {return tiles;}

    public Tile getTile(int row, int col){
        return tiles.get(row).get(col);
    }

    public ArrayList<Collectible> getCollectibles() {return collectibles;}

    public int getBoardHeight() {return boardHeight;}

    public int getBoardWidth() {return boardWidth;}

    public int getTotalTreasures() {return totalTreasures;}
    
    public boolean isWon(){return hasWon;}

    public boolean isLost(){return hasLost;}

    public ArrayList<EnemyInterface> getEnemies() {
        return enemies;
    }

    //*****************************************************************************************************//

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        //s.append("Width = ").append(boardWidth+1).append(" Height = ").append(boardHeight+1).append("\n"); //easier to add +1 in the print statement than -1 in the rest of the code
        for(ArrayList<Tile> row:tiles){
            for(Tile t:row) {
                s.append(t);
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Constructs a simple maze, functioned as the default maze for most of development, used by persistence tests
     */
    public void buildTestMaze(){
        addPlayer(1,1);
        setWall(1,3);
        setWall(2,3);
        setWall(3,3);
        addKey(6,3,Colour.BLUE);
        addInfo(4,2,"Testing info");
        setExit(1,6);
        addDoor(3,6,null); //add exit door
        setWall(1,5);
        setWall(2,5);
        setWall(3,5);
        setWall(4,5);
        setWall(5,5);
        addDoor(6,5,Colour.BLUE);
        addTreasure(5,6);
        addTreasure(4,3);
    }
}
