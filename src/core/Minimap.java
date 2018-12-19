package core;

import java.awt.Color;
import java.awt.Graphics;
import level.LevelOld;

/**
 * Minimap class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Minimap {
    
    public int w, h, playerX, playerY;
    public LevelOld lvl;
    
    /**
     * 
     * @param w
     * @param h
     * @param playerX
     * @param playerY
     * @param lvl 
     */
    public Minimap(int w, int h, int playerX, int playerY, LevelOld lvl){
        this.w = w;
        this.h = h;
        this.playerX = ((playerX) / 32) + 50;
        this.playerY = ((playerY * 2) / 32) + 282;
        this.lvl = lvl;
    }
    
    /**
     * 
     * @param playerX
     * @param playerY 
     */
    public void update(int playerX, int playerY){
        this.playerX = ((playerX) / 32) + 50;
        this.playerY = ((playerY * 2) / 32) + 282;
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics g){
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, this.w, this.h);

        for(int i = 0;i<lvl.m_map.length;i++){
            for(int j = 0;j<lvl.m_map[i].length;j++){
                switch(lvl.m_map[i][j]){
                    case 1:
                        g.setColor(Color.WHITE);
                        g.fillRect((i*3) + 50, (j*4) + 282, 3, 3);
                        break;
                    default:
                        break;
                }
            }
        }
        
        g.setColor(Color.RED);
        g.fillRect(this.playerX, this.playerY, 3, 3);
    }
}
