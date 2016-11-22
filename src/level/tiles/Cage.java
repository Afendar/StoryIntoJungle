package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld34.Defines;
import level.Level;

public class Cage extends Tile {
    
    public BufferedImage topLeftSprite, topRightSprite, bottomLeftSprite, bottomRightSprite;
    public double dt;
    private boolean isBroken;
    
    public Cage(int imgX, int imgY){
        super(imgX, imgY, 11);
        
        this.topLeftSprite = this.tile;
        this.bottomLeftSprite = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, (int)((imgY + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.topRightSprite = this.tileset.getSubimage((int)((imgX + 1) * Defines.TILE_SIZE), imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomRightSprite = this.tileset.getSubimage((int)((imgX + 1) * Defines.TILE_SIZE), (int)((imgY + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
    
        this.dt = 0;
        this.isBroken = false;
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        if(level.getData(x, y) == 2)
            return true;
        return false;
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
        if(level.getData(x, y) == 1){
            this.dt += dt;
            if(this.dt % 2 > 1.5)
            {
                
            }
            
            if(this.dt > 40)
            {
                this.isBroken = true;
                level.setData(x, y, 2);
                level.nbCages--;
            }
        }
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        g.drawImage(this.topLeftSprite, x * Defines.TILE_SIZE, ((y - 1) * Defines.TILE_SIZE - 30) + (3*Defines.TILE_SIZE/4), null);
        g.drawImage(this.topRightSprite, (x+1) * Defines.TILE_SIZE, ((y - 1) * Defines.TILE_SIZE - 30) + (3*Defines.TILE_SIZE/4), null);
    }
    
    @Override
    public void renderTop(Graphics g, int x, int y){
        g.drawImage(this.bottomLeftSprite, x * Defines.TILE_SIZE, (y * Defines.TILE_SIZE - 30) + (3*Defines.TILE_SIZE/4), null);
        g.drawImage(this.bottomRightSprite, (x+1) * Defines.TILE_SIZE, (y * Defines.TILE_SIZE - 30) + (3*Defines.TILE_SIZE/4), null);
    }
}
