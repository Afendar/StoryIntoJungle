/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package profiler;

import java.awt.Graphics;

/**
 * @author Mickaël
 */
public class Profiler {
    
    public boolean visible;
    public int fps, playerX, playerY, useRam, useCpu;
    
    public Profiler()
    {
        this.visible = false;
    }
    
    public void render(Graphics g){
        
    }
    
    public void toggleVisible(){
        this.visible = !this.visible;
    }
}
