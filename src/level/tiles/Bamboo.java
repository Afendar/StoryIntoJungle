package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import core.Defines;
import level.Level;

public class Bamboo extends Tile {
    
    public BufferedImage topSprite, bottomSprite;
    
    public Bamboo(int imgX, int imgY){
        super(imgX, imgY, 2);
        
        this.topSprite = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, (imgY - 1) * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomSprite = this.tile;
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
        g.drawImage(this.topSprite, x * Defines.TILE_SIZE, (y-1) * Defines.TILE_SIZE, null);
        g.drawImage(this.bottomSprite, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
}
