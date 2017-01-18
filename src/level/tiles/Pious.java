package level.tiles;

import level.Level;

public class Pious extends Tile {
    
    public Pious(int imgX, int imgY){
        super(imgX, imgY, 6);
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
}
