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
    
    public int m_x, m_y;
    public int m_width, m_height;
    public Level m_level;
    
    /**
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     * @param level 
     */
    public Camera(int x, int y, int w, int h, Level level){
        m_x = x;
        m_y =y;
        m_width = w;
        m_height = h;
        m_level = level;
    }
    
    /**
     * 
     * @param p 
     */
    public void update(Player p){
        m_x = (int)(p.getPosX() + ( Player.PLAYER_SIZE / 2 ) ) - m_width / 2;
        m_y = (int)(p.getPosY() + ( Player.PLAYER_SIZE / 2) ) - m_height / 2;
        
        if(m_x < 0)
            m_x = 0;
        if(m_y < 0)
            m_y = 0;
        if(m_y + m_height - 24 > m_level.getHeight())
            m_y = m_level.getHeight() - m_height + 24;
        if(m_x + m_width > m_level.getWidth())
            m_x = m_level.getWidth() - m_width;
    }
}
