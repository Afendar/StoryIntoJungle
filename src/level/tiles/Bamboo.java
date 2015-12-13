/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level.tiles;

public class Bamboo extends Tile {
    
    public Bamboo(int imgX, int imgY){
        super(imgX, imgY, 2);
    }

    @Override
    public boolean canPass() {
        return true;
    }

    @Override
    public void update() {
        
    }
    
}
