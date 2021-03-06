package level.tiles;

import java.awt.Graphics;
import java.awt.Graphics2D;
import core.Defines;
import java.awt.image.BufferedImage;
import level.Level;

/**
 * Bridge class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Bridge extends Tile {
    
    int timeanim = 15;
    float rotation;
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Bridge(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 3);
    }

    @Override
    public boolean canPass(Level level, int x, int y) {
        return false;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
    }
    
    /**
     * 
     * @param g
     * @param x
     * @param y
     * @param onBridge 
     */
    public void render(Graphics g, int x, int y, boolean onBridge){
        Graphics2D g2d = (Graphics2D) g;
        if(onBridge){
            if(this.timeanim == 0){
                if(this.rotation > 0){
                    this.rotation = (-0.07f);
                }
                else{
                    this.rotation = 0.07f;
                }
                this.timeanim = 15;
            }

            if(this.timeanim > 0){
                this.timeanim--;
            }
            g2d.rotate(-this.rotation, (x * Defines.TILE_SIZE) + 16, (y * Defines.TILE_SIZE) + 16);
        }
        g.drawImage(m_tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
        if(onBridge){
            g2d.rotate(this.rotation, (x * Defines.TILE_SIZE) + 16, (y * Defines.TILE_SIZE) + 16);
        }
    }
}
