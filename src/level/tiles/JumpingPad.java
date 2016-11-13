/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

/**
 *
 * @author Mickaël
 */
public class JumpingPad extends Tile {
    
    public JumpingPad(int imgX, int imgY){
        super(imgX, imgY, 10);
    }
    
    @Override
    public boolean canPass(){
        return false;
    }
    
    @Override
    public void update(double dt){
        
    }
    
}
