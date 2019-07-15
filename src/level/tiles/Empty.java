package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import level.Level;

/**
 * Empty class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Empty extends Tile {
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Empty(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 0);
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        
    }
}