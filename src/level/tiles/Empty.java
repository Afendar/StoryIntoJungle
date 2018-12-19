package level.tiles;

import java.awt.Graphics;
import level.LevelOld;

/**
 * Empty class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Empty extends Tile {
    
    /**
     * 
     * @param imgX
     * @param imgY 
     */
    public Empty(int imgX, int imgY){
        super(imgX, imgY, 0);
    }

    @Override
    public boolean canPass(LevelOld level, int x, int y) {
        return true;
    }

    @Override
    public void update(LevelOld level, int x, int y, double dt) {
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        
    }
}