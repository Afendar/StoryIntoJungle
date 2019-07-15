package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import core.Defines;
import level.Level;

/**
 * Tile class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public abstract class Tile {
    
    public int m_imgX, m_imgY;
    public final int ID;
    public BufferedImage m_tileset, m_tile;
    public int m_bonus = 0;
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY
     * @param ID 
     */
    public Tile(BufferedImage tileset, int imgX, int imgY, int ID){
        m_imgX = imgX;
        m_imgY = imgY;
        this.ID = ID;
        m_tileset = tileset;
        m_tile = m_tileset.getSubimage(imgX * Defines.TILE_SIZE, imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        
        TileAtlas.atlas.add(this);
    }
    
    /**
     * 
     * @param level
     * @param x
     * @param y
     * @return 
     */
    public abstract boolean canPass(Level level, int x, int y);
    
    /**
     * 
     * @param level
     * @param x
     * @param y
     * @param dt 
     */
    public abstract void update(Level level, int x, int y, double dt);
    
    /**
     * 
     * @param g
     * @param x
     * @param y 
     */
    public void render(Graphics g, int x, int y){
        g.drawImage(m_tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
    
    /**
     * 
     * @param g
     * @param x
     * @param y 
     */
    public void renderTop(Graphics g, int x, int y){
        
    }
}