/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

import java.awt.Graphics;

public class Empty extends Tile {
    
    public Empty(int imgX, int imgY){
        super(imgX, imgY, 0);
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
        
    }

}
