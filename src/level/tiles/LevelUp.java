package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import core.Defines;
import level.Level;

/**
 * LevelUp class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class LevelUp extends Tile{

    public BufferedImage topSprite, bottomSprite;
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public LevelUp(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 7);
        
        this.topSprite = m_tile;
        this.bottomSprite = m_tileset.getSubimage(imgX * Defines.TILE_SIZE, (imgY + 1) * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
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
