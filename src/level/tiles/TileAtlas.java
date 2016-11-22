package level.tiles;

import java.util.ArrayList;

public class TileAtlas {
    
    public static ArrayList<Tile> atlas = new ArrayList<>();
    
    public static Empty empty = new Empty(0, 0);
    public static Floor floor = new Floor(0, 0);
    public static Bamboo bamboo = new Bamboo(2, 1);
    public static Bridge bridge = new Bridge(3, 0);
    public static Apple apple = new Apple(0, 1);
    public static Leaves leaves = new Leaves(1, 1);
    public static Pious pious = new Pious(3, 3);
    public static LevelUp levelup = new LevelUp(3, 1);
    public static Checkpoint checkpoint = new Checkpoint(0, 2);
    public static Sand sand = new Sand(1, 3);
    public static JumpingPad jumpingPad = new JumpingPad(2, 3);
    public static Cage cage = new Cage(0, 4);
    public static Bush bush = new Bush(2, 4);
    public static Tree tallTree = new Tree(Tree.TALL, 5, 3);
    public static Tree smallTree = new Tree(Tree.SMALL, 3, 5);
}
