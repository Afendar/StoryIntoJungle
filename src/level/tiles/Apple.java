package level.tiles;

import level.LevelOld;

/**
 * Apple class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Apple extends Tile {
    
    /**
     * 
     * @param imgX
     * @param imgY 
     */
    public Apple(int imgX, int imgY){
        super(imgX, imgY, 4);
        this.bonus = 10;
    }

    @Override
    public boolean canPass(LevelOld level, int x, int y) {
        return true;
    }

    @Override
    public void update(LevelOld level, int x, int y, double dt) {
        
    }
}