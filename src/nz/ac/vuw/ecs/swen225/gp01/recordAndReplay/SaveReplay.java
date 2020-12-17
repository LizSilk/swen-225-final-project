package nz.ac.vuw.ecs.swen225.gp01.recordAndReplay;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that saves a replay into a JSON file based on move history.
 **/
public class SaveReplay {
    ArrayList<Move> moves; //Moves made in the game
    String filename; //filename of the created JSON file

    /**
     *Constructor of the saveReplay object which initialises the fields so
     * moves can be encoded.
     *
     * @param history list of the moves made that game.
     * @param filename name of the file to encode/save to.
     * @throws IOException if the encode doesn't work due to not finding the file path.
     **/
    public SaveReplay(ArrayList<Move> history, String filename) throws IOException {
        this.moves = history;
        this.filename = filename;
        encode(this.moves, this.filename);
    }

    /**
     * This method encodes the list of moves and saves the file based
     * on the filename.
     *
     * @param history list of the moves made that game.
     * @param filename name of the file to encode/save to.
     * @throws IOException if the encode doesn't work due to not finding the file path.
     **/
    public static void encode(ArrayList<Move> history, String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

        File outputFile = new File("replays/" + filename + ".json");

        try {
            outputFile.createNewFile();
            mapper.writeValue(outputFile, history);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



