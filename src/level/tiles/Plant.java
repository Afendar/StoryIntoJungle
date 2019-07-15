package level.tiles;

import core.Defines;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import level.Level;

/**
 * Plant class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Plant extends Tile {
    
    public static final int HELICONIA = 0;
    
    public int size;
    
    /**
     * 
     * @param tileset
     * @param size
     * @param imgX
     * @param imgY 
     */
    public Plant(BufferedImage tileset, int size, int imgX, int imgY){
        super(tileset, imgX, imgY, 14);
        m_tile = m_tileset.getSubimage(imgX * Defines.TILE_SIZE, imgY * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE);
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        return true;
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        g.drawImage(m_tile, x * Defines.TILE_SIZE, (y * Defines.TILE_SIZE) - Defines.TILE_SIZE, null);
    }
}