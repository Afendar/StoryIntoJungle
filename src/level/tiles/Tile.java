package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import core.Defines;
import level.LevelOld;

/**
 * Tile class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public abstract class Tile {
    
    public int imgX, imgY;
    public final int ID;
    public BufferedImage tileset, tile;
    public int bonus = 0;
    
    /**
     * 
     * @param imgX
     * @param imgY
     * @param ID 
     */
    public Tile(int imgX, int imgY, int ID){
        this.imgX = imgX;
        this.imgY = imgY;
        this.ID = ID;
        
        try{
            URL url = this.getClass().getResource("/tileset2.png");
            this.tileset = ImageIO.read(url);
        }catch(IOException e){
            e.getMessage();
        }
        
        this.tile = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        
        TileAtlas.atlas.add(this);
    }
    
    /**
     * 
     * @param level
     * @param x
     * @param y
     * @return 
     */
    public abstract boolean canPass(LevelOld level, int x, int y);
    
    /**
     * 
     * @param level
     * @param x
     * @param y
     * @param dt 
     */
    public abstract void update(LevelOld level, int x, int y, double dt);
    
    /**
     * 
     * @param g
     * @param x
     * @param y 
     */
    public void render(Graphics g, int x, int y){
        g.drawImage(this.tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
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