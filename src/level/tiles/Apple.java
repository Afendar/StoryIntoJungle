package level.tiles;

import java.awt.image.BufferedImage;
import level.Level;

/**
 * Apple class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Apple extends Tile {
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Apple(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 4);
        m_bonus = 10;
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
}