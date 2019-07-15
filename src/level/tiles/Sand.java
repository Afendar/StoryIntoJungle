package level.tiles;

import java.awt.image.BufferedImage;
import level.Level;

/**
 * Sand class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Sand extends Tile {
    
    public boolean startBreak;
    public double dt;
    
    private final boolean isBroken;
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Sand(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 9);
        this.startBreak = false;
        this.dt = 0;
        this.isBroken = false;
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        return level.getData(x, y) == 2;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isBroken(){
        return this.isBroken;
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
    }
}