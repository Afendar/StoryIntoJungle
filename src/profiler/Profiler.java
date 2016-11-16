package profiler;

import java.awt.Graphics;

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
