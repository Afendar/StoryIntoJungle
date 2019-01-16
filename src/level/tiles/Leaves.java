package level.tiles;

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
     * @param imgX
     * @param imgY 
     */
    public Leaves(int imgX, int imgY){
        super(imgX, imgY, 5);
        this.bonus = 100;
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
}