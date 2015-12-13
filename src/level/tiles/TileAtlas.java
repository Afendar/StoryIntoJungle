package level.tiles;

import java.util.ArrayList;

public class TileAtlas {
    
    public static ArrayList<Tile> atlas = new ArrayList<>();
    
    public static Empty empty = new Empty(0, 0);
    public static Floor floor = new Floor(1, 0);
    public static Bamboo bamboo = new Bamboo(2, 0);
    public static Bridge bridge = new Bridge(3, 0);
    public static Apple apple = new Apple(4, 0);
    public static Leaves leaves = new Leaves(5, 0);
    public static Pious pious = new Pious(6, 0);
    public static LevelUp levelup = new LevelUp(7, 0);
}
