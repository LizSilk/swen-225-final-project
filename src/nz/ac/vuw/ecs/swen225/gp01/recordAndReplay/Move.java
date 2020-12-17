package nz.ac.vuw.ecs.swen225.gp01.recordAndReplay;

/**
 * Move Objects that allow game history to be saved.
 **/
public class Move {

    String actor; //Object/Actor that has made the move (player, AI, or collectible).
    int time; // timer move was made.
    char direction; //the direction of the move (where they moved to).
    int level; //Level move made in.
    int last; //indicator of last level

    /**
     * Move Objects that allow game history to be saved.
     * @param a the actor that is moving.
     * @param t the time of the move.
     * @param d the direction of the move (where they moved to).
     * @param l the level.
     **/
    public Move (String a, int t, char d, int l){
        this.actor = a;
        this.time = t;
        this.direction = d;
        this.level = l;
        this.last = 0;
    }

    /**
     * Returns the actor that made the move.
     **/
    public String getActor() {
        return actor;
    }

    /**
     * Returns the direction of the move.
     **/
    public char getDirection() {
        return direction;
    }

    /**
     * Returns the time of the move.
     **/
    public int getTime() {
        return time;
    }

    /**
     * Returns the level of the move.
     **/
    public int getLevel() {
        return level;
    }

    /**
     * setter for the last move
     **/
    public void setLast() {
        this.last = 1;
    }

    /**
     * getter for last move
     **/
    public int getLast() {
        return this.last;
    }

    /**
     * To string method.
     **/
    @Override
    public String toString() {
        return "Move{" +
                "actor='" + actor + '\'' +
                ", time=" + time +
                ", direction=" + direction +
                ", level=" + level +
                '}';
    }


}
