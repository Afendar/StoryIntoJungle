package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld34.Defines;
import level.Level;

public class LevelUp extends Tile{

    public BufferedImage topSprite, bottomSprite;
    
    public LevelUp(int imgX, int imgY){
        super(imgX, imgY, 7);
        
        this.topSprite = this.tile;
        this.bottomSprite = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, (imgY + 1) * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
    }
    
    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
    
    public void render(Graphics g, int x, int y){
        g.drawImage(this.topSprite, x * Defines.TILE_SIZE, (y-1) * Defines.TILE_SIZE, null);
        g.drawImage(this.bottomSprite, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
}
