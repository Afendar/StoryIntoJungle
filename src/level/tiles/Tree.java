/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

import java.awt.Graphics;
import ld34.Defines;
import level.Level;

public class Tree extends Tile{
    
    public static final int TALL = 1;
    public static final int SMALL = 2;
    public int size;
    
    public Tree(int size, int imgX, int imgY){
        super(imgX, imgY, (size == Tree.TALL) ? 13 : 14);
        this.size = size;
        if(size == Tree.TALL)
            this.tile = this.tileset.getSubimage(4 * Defines.TILE_SIZE, 0, 5 * Defines.TILE_SIZE, 4 * Defines.TILE_SIZE);
        else
            this.tile = this.tileset.getSubimage(3 * Defines.TILE_SIZE, 4 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE);
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

        this.render(g, x, y, Tree.TALL);
    }
    
    public void render(Graphics g, int x, int y, int size){
        switch(size){
            case Tree.SMALL:
                g.drawImage(this.tile, x * Defines.TILE_SIZE, (y * Defines.TILE_SIZE) - Defines.TILE_SIZE, null);
                break;
            default:
                g.drawImage(this.tile, (x * Defines.TILE_SIZE) - Defines.TILE_SIZE - 5, (y * Defines.TILE_SIZE) - (3 * Defines.TILE_SIZE - 1 ), null);
                break;
        }
    }
}
