package particles;

import java.awt.Graphics;

public class Particle {
    
    protected boolean dead;
    
    public Particle(){
        this.dead = false;
    }
    
    public void update(double dt){
    }
    
    public void render(Graphics g){
    }
    
    public boolean isDead(){
        return this.dead;
    }
}
