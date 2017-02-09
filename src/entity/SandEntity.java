package entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import level.Level;

public class SandEntity extends Entity {
    
    public double dt;
    public Level level;
    
    public SandEntity(Level level, int posX, int posY){
        super(posX, posY);
        this.level = level;
    }
    
    @Override
    public void update(double dt) {
        int data = level.getData((int)this.posX, (int)this.posY);
        switch(data){
            case 1:
                this.dt += dt;            
                if(this.dt > 90)
                {
                    level.setData((int)this.posX, (int)this.posY, 2);
                    this.dt = 0;
                }
                break;
            case 2:
                this.dt += dt;
                if(this.dt > 270){
                    level.setData((int)this.posX, (int)this.posY, 0);
                    this.dt = 0;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle((int)this.posX, (int)this.posY, 10, 10);
    }
    
    @Override
    public void render(Graphics g, Boolean debug) {
    }
    
    @Override
    public void renderHitbox(Graphics g){
        
    }
}
