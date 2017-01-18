package level.tiles;

import level.Level;

public class Leaves extends Tile {
    
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
