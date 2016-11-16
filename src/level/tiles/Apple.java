package level.tiles;

import level.Level;

public class Apple extends Tile {
    
    public Apple(int imgX, int imgY){
        super(imgX, imgY, 4);
        this.bonus = 10;
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
}
