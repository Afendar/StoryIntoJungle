package entity;

import java.awt.Graphics;

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
    
    public abstract void update();
    public abstract void render(Graphics g);
    
}
