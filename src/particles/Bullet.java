package particles;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Bullet class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Bullet extends Particle {
    
    public int x;
    public int y;
    public int dx;
    public int dy;
    public int health;
    
    /**
     * 
     * @param x
     * @param y
     * @param dx
     * @param dy
     * @param health 
     */
    public Bullet(int x, int y, int dx, int dy, int health){
        super();
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.health = health;
    }
    
    @Override
    public void update(double dt){
        this.x += this.dx;
        this.y += this.dy;
        
        this.health--;
        if(this.health <= 0){
            this.dead = true;
        }
    } 
    
    @Override
    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillOval(this.x - 3, this.y - 3, 6, 6);
    }
}
