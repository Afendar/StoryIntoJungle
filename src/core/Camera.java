package core;

import entity.Player;
import level.Level;

/**
 * Camera class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Camera{
    
    public int x, y;
    public int w, h;
    public Level level;
    
    /**
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     * @param level 
     */
    public Camera(int x, int y, int w, int h, Level level){
        this.x = x;
        this.y =y;
        this.w = w;
        this.h = h;
        this.level = level;
    }
    
    /**
     * 
     * @param p 
     */
    public void update(Player p){
        this.x = (int)(p.getPosX() + ( Player.PLAYER_SIZE / 2 ) ) - this.w / 2;
        this.y = (int)(p.getPosY() + ( Player.PLAYER_SIZE / 2) ) - this.h / 2;
        
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
