/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;


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
}
