package level.tiles;

import core.Defines;
import java.awt.Graphics;
import level.Level;

public class Plant extends Tile {
    
    public static final int HELICONIA = 0;
    
    public int size;
    
    public Plant(int size, int imgX, int imgY){
        super(imgX, imgY, 14);
        this.tile = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, imgY * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE);
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
        g.drawImage(this.tile, x * Defines.TILE_SIZE, (y * Defines.TILE_SIZE) - Defines.TILE_SIZE, null);
    }
}
