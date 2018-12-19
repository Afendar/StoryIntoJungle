package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import core.Defines;
import level.LevelOld;

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
     * @param imgX
     * @param imgY 
     */
    public Bush(int imgX, int imgY){
        super(imgX, imgY, 11);
    }

    @Override
    public boolean canPass(LevelOld level, int x, int y) {
        return true;
    }

    @Override
    public void update(LevelOld level, int x, int y, double dt) {
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        g.drawImage(this.tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE + 16, null);
    }
}
