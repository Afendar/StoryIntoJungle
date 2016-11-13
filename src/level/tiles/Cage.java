package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld34.Defines;

public class Cage extends Tile {
    
    public BufferedImage topLeftSprite, topRightSprite, bottomLeftSprite, bottomRightSprite;
    
    public Cage(int imgX, int imgY){
        super(imgX, imgY, 11);
        
        this.topLeftSprite = this.tile;
        this.bottomLeftSprite = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, (int)((imgY + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.topRightSprite = this.tileset.getSubimage((int)((imgX + 1) * Defines.TILE_SIZE), imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomRightSprite = this.tileset.getSubimage((int)((imgX + 1) * Defines.TILE_SIZE), (int)((imgY + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
    }
    
    public boolean canPass(){
        return false;
    }
    
    public void update(double dt){
        
    }
    
    public void render(Graphics g, int x, int y){
        g.drawImage(this.topLeftSprite, x * Defines.TILE_SIZE, ((y-1) * Defines.TILE_SIZE) + (3*Defines.TILE_SIZE/4), null);
        g.drawImage(this.topRightSprite, (x+1) * Defines.TILE_SIZE, ((y-1) * Defines.TILE_SIZE) + (3*Defines.TILE_SIZE/4), null);
        g.drawImage(this.bottomLeftSprite, x * Defines.TILE_SIZE, (y * Defines.TILE_SIZE) + (3*Defines.TILE_SIZE/4), null);
        g.drawImage(this.bottomRightSprite, (x+1) * Defines.TILE_SIZE, (y * Defines.TILE_SIZE) + (3*Defines.TILE_SIZE/4), null);
    }
}
