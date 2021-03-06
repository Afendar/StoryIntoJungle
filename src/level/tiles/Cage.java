package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import core.Defines;
import entity.CageEntity;
import level.Level;

/**
 * Cage class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Cage extends Tile {
    
    public BufferedImage topLeftSprite, topRightSprite, bottomLeftSprite, bottomRightSprite;
    public double dt;
    
    /**
     * 
     * @param tileset
     * @param imgX
     * @param imgY 
     */
    public Cage(BufferedImage tileset, int imgX, int imgY){
        super(tileset, imgX, imgY, 10);
        
        this.topLeftSprite = m_tile;
        this.bottomLeftSprite = m_tileset.getSubimage(imgX * Defines.TILE_SIZE, (int)((imgY + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.topRightSprite = m_tileset.getSubimage((int)((imgX + 1) * Defines.TILE_SIZE), imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomRightSprite = m_tileset.getSubimage((int)((imgX + 1) * Defines.TILE_SIZE), (int)((imgY + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
    
        this.dt = 0;
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        CageEntity entity = level.getCageEntity(x, y);
        return entity != null && entity.isBreak();
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        g.drawImage(this.topLeftSprite, x * Defines.TILE_SIZE, ((y - 1) * Defines.TILE_SIZE - 25) + (3*Defines.TILE_SIZE/4), null);
        g.drawImage(this.topRightSprite, (x+1) * Defines.TILE_SIZE, ((y - 1) * Defines.TILE_SIZE - 25) + (3*Defines.TILE_SIZE/4), null);
    }
    
    @Override
    public void renderTop(Graphics g, int x, int y){
        g.drawImage(this.bottomLeftSprite, x * Defines.TILE_SIZE, (y * Defines.TILE_SIZE - 25) + (3*Defines.TILE_SIZE/4), null);
        g.drawImage(this.bottomRightSprite, (x+1) * Defines.TILE_SIZE, (y * Defines.TILE_SIZE - 25) + (3*Defines.TILE_SIZE/4), null);
    }
}