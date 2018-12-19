package level.tiles;

import java.awt.Graphics;
import core.Defines;
import level.LevelOld;

/**
 * Floor class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Floor extends Tile {
    
    /**
     * 
     * @param imgX
     * @param imgY 
     */
    public Floor(int imgX, int imgY){
        super(imgX, imgY, 1);
    }
    
    @Override
    public boolean canPass(LevelOld level, int x, int y){
        return false;
    }
    
    @Override
    public void update(LevelOld level, int x, int y, double dt){
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        
    }
    
    /**
     * 
     * @param g
     * @param x
     * @param y
     * @param left
     * @param right 
     */
    public void render(Graphics g, int x, int y, boolean left, boolean right){
        if(left && right){
            this.tile = this.tileset.getSubimage((this.imgX * Defines.TILE_SIZE) + (Defines.TILE_SIZE/2), this.imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else if(left){
            this.tile = this.tileset.getSubimage((this.imgX + 1) * Defines.TILE_SIZE, this.imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else if(right){
            this.tile = this.tileset.getSubimage((this.imgX * Defines.TILE_SIZE), this.imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else{
            this.tile = this.tileset.getSubimage(0, 3 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        
        g.drawImage(this.tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
}
