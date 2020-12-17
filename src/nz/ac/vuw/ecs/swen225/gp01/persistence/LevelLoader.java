package nz.ac.vuw.ecs.swen225.gp01.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp01.maze.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that decodes the level files to a Maze object
 * Used by nz.ac.vuw.ecs.swen225.gp01.application to load the level files
 * All the methods are static as I saw no reason to make it so you had to create an object to use it
 *
 * @author Benjmin Silk 300473874
 */
public class LevelLoader {
    private static ArrayList<Collectible> collectibles;
    private static ArrayList<EnemyInterface> enemies;
    private static Player player;
    private static int treasureCount;

    /**
     * Main method to load a file
     * Offloads the actual construction of the array of tiles onto loadMazeTiles
     * works out the other fields and constructs the maze
     *
     * @param fileName The fileName of the level to load
     * @return a maze object containing the level
     */
    public static Maze loadLevel(String fileName) {
        collectibles = new ArrayList<>();
        player = null;
        treasureCount = 0;
        enemies = new ArrayList<>();

        File file = new File(fileName + ".json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode fileNode = null;
        try {
            fileNode = mapper.readTree(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<Tile>> tiles = loadMazeTiles(fileNode.get("tiles"));
        int rows = tiles.size();
        int cols = tiles.get(0).size();

        //subtract 1 from rows and cols as Maze counts from 0 for some reason
        Maze maze = new Maze(rows - 1, cols - 1, treasureCount, tiles, collectibles, enemies, player);

        collectibles = null;
        enemies = null;
        player = null;
        treasureCount = 0;
        return maze;
    }

    /**
     * Main method to load a file
     * Loads the time a player has to complete a level
     *
     * @param fileName The filename of the level to load
     * @return an int representing the time the player has to complete the level
     */
    public static int loadTime(String fileName) {
        File file = new File(fileName + ".json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode fileNode = null;
        try {
            fileNode = mapper.readTree(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNode.get("time").asInt();
    }

    /**
     * Helper method that loads the maze itself
     *
     * @param mazeNode A JSONNode that contains the information about maze tiles
     * @return returns a 2d array containing the tiles of the arrays
     */
    private static ArrayList<ArrayList<Tile>> loadMazeTiles(JsonNode mazeNode) {

        ArrayList<ArrayList<Tile>> outPutList = new ArrayList<ArrayList<Tile>>();
        if (mazeNode.isArray()) {
            for (JsonNode arrayNode : mazeNode) {
                ArrayList row = new ArrayList<Tile>();
                if (arrayNode.isArray()) {
                    for (JsonNode jsonNode : arrayNode) {
                        Tile tile = new Tile(jsonNode.get("row").intValue(), jsonNode.get("col").intValue());
                        loadActor(jsonNode, tile);
                        loadFloor(jsonNode, tile);
                        loadDoor(jsonNode, tile);
                        loadCollectible(jsonNode, tile);
                        row.add(tile);
                    }
                }
                outPutList.add(row);
            }
        }

        return outPutList;
    }

    /**
     * Helper method to load classes that inherit from the Actor interface - constructs the Actor and adds it to
     * the relevant tile
     *
     * @param jsonNode The jsonNode object that contains the information about the Actor
     * @param tile     The tile the Actor is in
     */
    private static void loadActor(JsonNode jsonNode, Tile tile) {
        if (!jsonNode.get("actor").isNull()) {
            JsonNode actorNode = jsonNode.get("actor");
            if (actorNode.get("type").asText().equals("player")) {
                Player player = new Player((tile));
                tile.setActor(player);
                LevelLoader.player = player;
                if (actorNode.get("inventory").isArray()) {
                    for (JsonNode keyNode : actorNode.get("inventory")) {
                        if (keyNode.hasNonNull("colour")) {
                            switch (keyNode.get("colour").asText()) {
                                case "BLUE":
                                    player.addToInventory(new Key(Maze.Colour.BLUE));
                                    break;
                                case "RED":
                                    player.addToInventory(new Key(Maze.Colour.RED));

                                    break;
                                case "GREEN":
                                    player.addToInventory(new Key(Maze.Colour.GREEN));

                                    break;
                                case "YELLOW":
                                    player.addToInventory(new Key(Maze.Colour.YELLOW));
                                    ;
                            }
                        }

                    }
                }

            }
            if (actorNode.get("type").asText().equals("Enemy")) {
                char direction = (char) actorNode.get("direction").asText().charAt(0);
                EnemyInterface.AIType ai = EnemyInterface.AIType.LEFT;
                switch (actorNode.get("ai").asText()) {
                    case "LEFT":
                        ai = EnemyInterface.AIType.LEFT;
                        break;
                    case "RIGHT":
                        ai = EnemyInterface.AIType.RIGHT;
                        break;
                }
                try {
                    EnemyInterface enemy = new EnemyLoader().getEnemyInstance(tile, ai, direction);
                    tile.setActor(enemy);
                    enemies.add(enemy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Helper method to load a Floor object - constructs it and adds it to the relevant tile
     *
     * @param jsonNode The jsonNode object that contains the information about the Floor
     * @param tile     The tile the Floor is in
     */
    private static void loadFloor(JsonNode jsonNode, Tile tile) {
        JsonNode floorNode = jsonNode.get("floor");
        switch (floorNode.get("type").asText()) {
            case "FREE":
                tile.setType(Floor.Type.FREE);
                break;
            case "WALL":
                tile.setType(Floor.Type.WALL);
                break;
            case "INFO":
                tile.setType(Floor.Type.INFO);
                break;
            case "EXIT":
                tile.setType(Floor.Type.EXIT);
        }
        if (!floorNode.get("information").isNull()) {
            tile.setInfo(floorNode.get("information").asText());
        }
    }

    /**
     * Helper method to load a Door object - constructs it and adds it to the relevant tile
     *
     * @param jsonNode The jsonNode object that contains the information about the Door
     * @param tile     The tile the Door is in
     */
    private static void loadDoor(JsonNode jsonNode, Tile tile) {
        if (!jsonNode.get("door").isNull()) {
            switch (jsonNode.get("door").get("doorColour").asText()) {
                case "BLUE":
                    tile.setDoor(new Door(Maze.Colour.BLUE));
                    break;
                case "RED":
                    tile.setDoor(new Door(Maze.Colour.RED));
                    break;
                case "GREEN":
                    tile.setDoor(new Door(Maze.Colour.GREEN));
                    break;
                case "YELLOW":
                    tile.setDoor(new Door(Maze.Colour.YELLOW));
                    break;
                default:
                    tile.setDoor(new Door(null));
            }
        }
    }

    /**
     * Helper method to load classes that inherit from the Collectible interface - constructs the Collectible and adds it to
     * the relevant tile
     *
     * @param jsonNode The jsonNode object that contains the information about the Collectible
     * @param tile     The tile the Collectible is in
     */
    private static void loadCollectible(JsonNode jsonNode, Tile tile) {
        if (!jsonNode.get("collectible").isNull()) {
            JsonNode collectibleNode = jsonNode.get("collectible");
            if (collectibleNode.get("type").asText().equals("key")) {
                Key key = null;
                switch (jsonNode.get("collectible").get("colour").asText()) {
                    case "BLUE":
                        key = new Key(Maze.Colour.BLUE);
                        tile.setCollectible(key);
                        LevelLoader.collectibles.add(key);
                        break;
                    case "RED":
                        key = new Key(Maze.Colour.RED);
                        tile.setCollectible(key);
                        LevelLoader.collectibles.add(key);
                        break;
                    case "GREEN":
                        key = new Key(Maze.Colour.GREEN);
                        tile.setCollectible(key);
                        LevelLoader.collectibles.add(key);
                        break;
                    case "YELLOW":
                        key = new Key(Maze.Colour.YELLOW);
                        tile.setCollectible(key);
                        LevelLoader.collectibles.add(key);
                        ;
                }
            }
            if (collectibleNode.get("type").asText().equals("treasure")) {
                Treasure treasure = new Treasure();
                tile.setCollectible(treasure);
                treasureCount += 1;
                LevelLoader.collectibles.add(treasure);
            }
        }
    }
}
