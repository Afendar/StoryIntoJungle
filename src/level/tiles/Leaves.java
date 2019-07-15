package level.tiles;

import java.awt.image.BufferedImage;
import level.Level;

/**
 * Leaves class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Leaves extends Tile {
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Leaves(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 5);
        m_bonus = 100;
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
}