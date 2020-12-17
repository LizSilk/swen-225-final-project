package nz.ac.vuw.ecs.swen225.gp01.recordAndReplay;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that loads a replay JSON file and extracts the move
 **/
public class LoadReplay {

    /**
     * This method opens the JSON file and extracts the individual moves
     * from JsonNode and adds them to a list to then be constructed into
     * a map which is return.
     *
     * @param filename name of the file to open
     * @throws FileNotFoundException if file not found
     **/
    public static Map<Integer, ArrayList<Move>> decodeList(String filename) throws FileNotFoundException {
        File file = new File( "replays/" + filename + ".json");
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Move> moves = new ArrayList<Move>();

        JsonNode arrayNode = null;
        try {
            arrayNode = mapper.readTree(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (arrayNode.isArray()) {
            for (JsonNode moveNode : arrayNode) {
                String actor = moveNode.get("actor").asText();
                int time = moveNode.get("time").asInt();
                String direction = moveNode.get("direction").asText();
                int level = moveNode.get("level").asInt();
                moves.add(new Move(actor, time, direction.charAt(0), level));

            }
            if(moves.isEmpty()){
                return null;
            }
            return buildMap(moves);

        }
        return null;
    }

    /**
     * This method is a helper method for the decodeList method.
     * Takes in a list of the moves extracted from the JSON file,
     * marks the final move, and constructs a map of the moves based on the time the moves
     * were made.
     *
     * @param list list of the moves that have been extracted from the JSON file
     **/
    public static Map<Integer, ArrayList<Move>> buildMap(ArrayList<Move> list){
        Map<Integer, ArrayList<Move>> map = new HashMap<Integer, ArrayList<Move>>();
        list.get(list.size()-1).setLast();
        for(Move move : list){
            if(map.keySet().isEmpty() || !map.containsKey(move.time)){
                map.put(move.time, new ArrayList<Move>());
            }
            map.get(move.time).add(move);

        }

        return map;

    }

}
