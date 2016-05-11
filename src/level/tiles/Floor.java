/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

import java.awt.Graphics;
import ld34.Defines;


public class Floor extends Tile {
    
    public Floor(int imgX, int imgY){
        super(imgX, imgY, 1);
    }
    
    @Override
    public boolean canPass(){
        return false;
    }
    
    @Override
    public void update(){
        
    }
    
    public void render(Graphics g, int x, int y){
        
    }
    
    public void render(Graphics g, int x, int y, boolean left, boolean right){
        if(left && right){
            this.tile = this.tileset.getSubimage((this.imgX * Defines.TILE_SIZE) + (Defines.TILE_SIZE/2), this.imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else if(left){
            this.tile = this.tileset.getSubimage((this.imgX + 1) * Defines.TILE_SIZE, this.imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        else if(right){
            this.tile = this.tileset.getSubimage((this.imgX * Defines.TILE_SIZE), this.imgY * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        
        g.drawImage(this.tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
}
