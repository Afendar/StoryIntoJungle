package level.tiles;

import java.awt.image.BufferedImage;
import level.Level;

/**
 * Pious class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Pious extends Tile {
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Pious(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 6);
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
}