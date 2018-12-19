package level.tiles;

import level.LevelOld;

/**
 * Pious class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Pious extends Tile {
    
    /**
     * 
     * @param imgX
     * @param imgY 
     */
    public Pious(int imgX, int imgY){
        super(imgX, imgY, 6);
    }

    @Override
    public boolean canPass(LevelOld level, int x, int y) {
        return true;
    }

    @Override
    public void update(LevelOld level, int x, int y, double dt) {
        
    }
}