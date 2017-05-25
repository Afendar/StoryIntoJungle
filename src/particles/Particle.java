package particles;

import java.awt.Graphics;

/**
 * Particle class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Particle {
    
    protected boolean dead;
    
    /**
     * 
     */
    public Particle(){
        this.dead = false;
    }
    
    /**
     * 
     * @param dt 
     */
    public void update(double dt){
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics g){
    }
    
    /**
     * 
     * @return 
     */
    public boolean isDead(){
        return this.dead;
    }
}
