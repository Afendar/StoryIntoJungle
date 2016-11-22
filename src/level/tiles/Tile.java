package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import ld34.Defines;
import level.Level;

public abstract class Tile {
    
    public int imgX, imgY;
    public final int ID;
    public BufferedImage tileset, tile;
    public int bonus = 0;
    
    public Tile(int imgX, int imgY, int ID){
        this.imgX = imgX;
        this.imgY = imgY;
        this.ID = ID;
        
        try{
            URL url = this.getClass().getResource("/tileset2.png");
            this.tileset = ImageIO.read(url);
        }catch(IOException e){
            e.printStackTrace();
        }
        
        this.tile = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        
        TileAtlas.atlas.add(this);
    }
    
    public abstract boolean canPass(Level level, int x, int y);
    public abstract void update(Level level, int x, int y, double dt);
    
    public void render(Graphics g, int x, int y){
        g.drawImage(this.tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
    
    public void renderTop(Graphics g, int x, int y){
        
    }
}
