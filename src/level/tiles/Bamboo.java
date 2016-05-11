/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ld34.Defines;

public class Bamboo extends Tile {
    
    public BufferedImage topSprite, bottomSprite;
    
    public Bamboo(int imgX, int imgY){
        super(imgX, imgY, 2);
        
        this.topSprite = this.tileset.getSubimage(imgX * Defines.TILE_SIZE, (imgY - 1) * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomSprite = this.tile;
    }

    @Override
    public boolean canPass() {
        return true;
    }

    @Override
    public void update() {
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        g.drawImage(this.topSprite, x * Defines.TILE_SIZE, (y-1) * Defines.TILE_SIZE, null);
        g.drawImage(this.bottomSprite, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
    }
}
