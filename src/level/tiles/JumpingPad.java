/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

import level.Level;

public class JumpingPad extends Tile {
    
    public JumpingPad(int imgX, int imgY){
        super(imgX, imgY, 10);
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        return false;
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
        
    }
    
}
