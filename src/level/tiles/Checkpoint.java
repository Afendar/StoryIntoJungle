package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import core.Defines;
import core.TimerThread;
import level.Level;

/**
 * Checkpoint class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Checkpoint extends Tile{
    
    public BufferedImage tile2, tile3;
    public int pos, timeAnim, counter;
    public boolean animated;
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Checkpoint(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 8);
        this.pos = 0;
        this.tile2 = m_tileset.getSubimage((imgX + 1) * Defines.TILE_SIZE , imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.tile3 = m_tileset.getSubimage((imgX + 2) * Defines.TILE_SIZE , imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.animated = false;
        this.timeAnim = TimerThread.MILLI;
        this.counter = 0;
    }
    
    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        this.animated = true;
        this.timeAnim = TimerThread.MILLI;
        this.counter = 0;
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        switch(this.pos){
            case 0:
                g.drawImage(m_tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
                break;
            case 1:
                g.drawImage(this.tile2, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
                break;
            case 2:
                g.drawImage(this.tile3, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
                break;
        }
        
        if(this.animated && (TimerThread.MILLI - this.timeAnim) > 500 && this.counter < 10){
            this.pos++;
            this.timeAnim = TimerThread.MILLI;
            this.counter++;
            if(this.pos > 2)
                this.pos = 0;
        }
        else if(this.counter > 9){
            this.pos = 0;
            this.counter = 0;
            this.animated = false;
        }
    }
}