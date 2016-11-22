package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld34.Defines;
import level.Level;

public class Bush extends Tile {
    public BufferedImage topSprite, bottomSprite;
    
    public Bush(int imgX, int imgY){
        super(imgX, imgY, 12);
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
        g.drawImage(this.tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE + 16, null);
    }
}
