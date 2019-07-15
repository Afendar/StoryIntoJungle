package level.tiles;

import java.awt.Graphics;
import core.Defines;
import java.awt.image.BufferedImage;
import level.Level;

/**
 * Floor class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Floor extends Tile {
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Floor(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 1);
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        return false;
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
        
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
            m_tile = m_tileset.getSubimage((m_imgX * Defines.TILE_SIZE) + (Defines.TILE_SIZE/2), m_imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else if(left){
            m_tile = m_tileset.getSubimage((m_imgX + 1) * Defines.TILE_SIZE, m_imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else if(right){
            m_tile = m_tileset.getSubimage((m_imgX * Defines.TILE_SIZE), m_imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else{
            m_tile = m_tileset.getSubimage(0, 3 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        
        g.drawImage(m_tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
}
