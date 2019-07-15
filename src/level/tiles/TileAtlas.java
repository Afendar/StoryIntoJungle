package level.tiles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * TileAtlas class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class TileAtlas {
    
    public static ArrayList<Tile> atlas = new ArrayList<>();
    
    public static Empty empty;
    public static Floor floor;
    public static Bamboo bamboo;
    public static Bridge bridge;
    public static Apple apple;
    public static Leaves leaves;
    public static Pious pious;
    public static LevelUp levelup;
    public static Checkpoint checkpoint;
    public static Sand sand;
    public static Cage cage;
    public static Bush bush;
    public static Tree tallTree;
    public static Tree smallTree;
    public static Plant plant;
    
    public static void load(BufferedImage tileset){
        TileAtlas.empty = new Empty(tileset, 0, 0);
        TileAtlas.floor = new Floor(tileset, 0, 0);
        TileAtlas.bamboo = new Bamboo(tileset, 2, 1);
        TileAtlas.bridge = new Bridge(tileset, 3, 0);
        TileAtlas.apple = new Apple(tileset, 0, 1);
        TileAtlas.leaves = new Leaves(tileset, 1, 1);
        TileAtlas.pious = new Pious(tileset, 3, 3);
        TileAtlas.levelup = new LevelUp(tileset, 3, 1);
        TileAtlas.checkpoint = new Checkpoint(tileset, 0, 2);
        TileAtlas.sand = new Sand(tileset, 1, 3);
        TileAtlas.cage = new Cage(tileset, 0, 6);
        TileAtlas.bush = new Bush(tileset, 0, 4);
        TileAtlas.tallTree = new Tree(tileset, Tree.TALL, 5, 3);
        TileAtlas.smallTree = new Tree(tileset, Tree.SMALL, 3, 5);
        TileAtlas.plant = new Plant(tileset, Plant.HELICONIA, 9, 2);
    }
}
