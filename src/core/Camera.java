package core;

import entity.Player;
import level.LevelOld;

/**
 * Camera class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Camera{
    
    public int x, y;
    public int w, h;
    public LevelOld level;
    
    /**
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     * @param level 
     */
    public Camera(int x, int y, int w, int h, LevelOld level){
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
        if(this.y + this.h - 24 > this.level.m_h)
            this.y = this.level.m_h - this.h + 24;
        if(this.x + this.w > this.level.m_w)
            this.x = this.level.m_w - this.w;
    }
}
