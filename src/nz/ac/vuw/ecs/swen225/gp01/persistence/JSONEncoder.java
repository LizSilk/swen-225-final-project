package nz.ac.vuw.ecs.swen225.gp01.persistence;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import nz.ac.vuw.ecs.swen225.gp01.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp01.maze.Tile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a helper class I made to let me convert the array of tiles in nz.ac.vuw.ecs.swen225.gp01.maze to JSON when making levels
 * I only saved the tiles from the nz.ac.vuw.ecs.swen225.gp01.maze, as the rest of the fields could be worked out from these
 * This actually helped to refer to the same object
 *
 * @author Benjmin Silk 300473874
 */
public class JSONEncoder {

    /**
     * Static method that encodes a level to JSON
     * @param maze the arraylist to encode
     * @param time the time a user has to complete a level
     * @param fileName the filepath of the file to save to
     */
    public static void encodeLevel(Maze maze, int time, String fileName){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File outputFile = new File("levels/"+fileName + ".json");

        Map<String,Object> levelMap = new HashMap<String,Object>();
        levelMap.put("tiles", mapper.valueToTree(maze.getTiles()));
        levelMap.put("time", time);

        try {
            outputFile.createNewFile();
            mapper.writeValue(outputFile, levelMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

