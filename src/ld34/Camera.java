package ld34;

import entity.Player;
import level.Level;

public class Camera{
    
    public int x, y;
    public int w, h;
    public Level level;
    
    public Camera(int x, int y, int w, int h, Level level){
        this.x = x;
        this.y =y;
        this.w = w;
        this.h = h;
        this.level = level;
    }
    
    public void update(Player p){
        this.x = (int)(p.getPosX() + ( p.PLAYER_SIZE / 2 ) ) - this.w / 2;
        this.y = (int)(p.getPosY() + ( p.PLAYER_SIZE / 2) ) - this.h / 2;
        
        if(this.x < 0)
            this.x = 0;
        if(this.y < 0)
            this.y = 0;
        if(this.y + this.h - 24 > this.level.h)
            this.y = this.level.h - this.h + 24;
        if(this.x + this.w > this.level.w)
            this.x = this.level.w - this.w;
    }
}
