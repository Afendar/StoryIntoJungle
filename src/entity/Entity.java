package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
    
    protected float posX, posY;
    
    public Entity(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }
    
    public abstract Rectangle getBounds();
    public abstract void update(double dt);
    public abstract void render(Graphics g, Boolean debug);
    public abstract void renderHitbox(Graphics g);
}
