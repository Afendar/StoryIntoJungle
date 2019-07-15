package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import core.Defines;
import level.Level;

/**
 * Bush class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Bush extends Tile {
    
    public BufferedImage topSprite, bottomSprite;
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Bush(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 11);
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
        g.drawImage(m_tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE + 16, null);
    }
}
