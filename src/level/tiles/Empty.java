package level.tiles;

import java.awt.Graphics;
import level.Level;

public class Empty extends Tile {
    
    public Empty(int imgX, int imgY){
        super(imgX, imgY, 0);
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
