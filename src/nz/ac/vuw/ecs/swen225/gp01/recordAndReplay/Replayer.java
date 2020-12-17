package nz.ac.vuw.ecs.swen225.gp01.recordAndReplay;

import nz.ac.vuw.ecs.swen225.gp01.application.Main;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class that the application package uses to play replays.
 **/
public class Replayer {
    private Map<Integer, ArrayList<Move>> moveMap; //Map of moves made in the level based on time
    int replayTime; //Maximum time in the replay
    int levelTime; //Maximum time in the level
    int replaySpeed = 1; //tick speed
    int currentTime; //Current ticks left (1 tick = 1/10 of a second)
    Timer replayTimer; //Replay timer

    /**
     * Constructor for the replayer.
     *
     * @param moves map of moves.
     **/
    public Replayer(Map<Integer, ArrayList<Move>> moves){
        this.moveMap = moves;
    }

    /**
     * Helper method that will get the level for time setup.
     **/
    public int getLevel(){
     for(Integer key : this.moveMap.keySet()){
         for(Move move : moveMap.get(key)){
             return move.getLevel();
         }
     }
     return 0;
    }

    /**
     * Helper method that will setup the initial times
     * by getting the time of the last move.
     **/
    public void setupTimes(int time){
        for(Integer key : this.moveMap.keySet()){
            for(Move move : this.moveMap.get(key)){
                if(move.getLast() == 1){
                    this.currentTime = time;
                    this.replayTime = move.getTime();
                    this.levelTime = time;
                    return;
                }
            }
        }
    }

    /**
     * Method that allows auto replaying.
     **/
    public void autoReplay() {
        replayTimer = new Timer(100, event -> {
            int before = currentTime;
            int end = currentTime - this.replaySpeed;

            for(int i = before; i > end; i-- ){
                ArrayList<Move> moves = this.getMoves(i);
                if(moves != null){
                    for(Move move : moves){
                        Main.main.replayMove(move.getActor(), move.getDirection());
                    }
                }

            }

            this.currentTime = end;
            if(this.currentTime <= this.replayTime){
                this.currentTime = this.levelTime;
                Main.main.updateMaze();
            }
            Main.main.updateAll(this.currentTime);

        });

        replayTimer.setRepeats(true);
        replayTimer.start();
    }

    /**
     * Method that allows step-by-step replaying.
     **/
    public void stepReplay(){
        if(this.currentTime < this.replayTime){
            this.currentTime = this.levelTime;
            Main.main.updateMaze();
            Main.main.updateAll(this.currentTime);
            return;
        }

        int before = this.currentTime;
        int end = this.currentTime - 1;

        for(int i = before; i > end; i-- ){
            ArrayList<Move> moves = this.getMoves(i);
            if(moves != null){
                for(Move move : moves){
                    Main.main.replayMove(move.getActor(), move.getDirection());
                }
            } else{
                end--;
            }

        }
        this.currentTime = end;
        Main.main.updateAll(this.currentTime);

    }

    /**
     * Setter for replaySpeed.
     *
     * @param replaySpeed what replaySpeed will be set to.
     **/
    public void setReplaySpeed(int replaySpeed) {
        this.replaySpeed = replaySpeed;
    }

    /**
     * Getter for replaySpeed.
     **/
    public int getReplaySpeed() {
        return this.replaySpeed;
    }

    /**
     * Timer stopper.
     **/
    public void stopTimer(){
        this.replayTimer.stop();
    }


    /**
     * Getter for replayTimer.
     **/
    public Timer getReplayTimer() {
        return replayTimer;
    }

    /**
     * Helper method that gets all the moves at the given time.
     *
     * @param time time to filter moves by.
     **/
    public ArrayList<Move> getMoves(int time){
            return this.moveMap.get(time);
    }
}
