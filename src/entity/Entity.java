package entity;

import core.Context;
import core.Game;
import entity.components.EntityComponentBase;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Entity class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public abstract class Entity
{
    protected ArrayList<EntityComponentBase> m_components;
    
    protected Game m_game;
    protected float m_posX, m_posY;
    protected int m_width, m_height;
    protected int m_health;
    protected boolean m_alive;
    protected Rectangle m_bounds;
    
    /** TODO remove when create EntityManager class */
    protected Context m_context;

    /**
     * 
     * @param game
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public Entity(Game game, float x, float y, int width, int height)
    {
        m_game = game;
        m_posX = x;
        m_posY = y;
        m_width = width;
        m_height = height;
        m_health = 100;
        m_alive = true;
        m_bounds = new Rectangle(0, 0, width, height);
    }
    
    /**
     *
     * @param posX
     * @param posY
     * @param context
     */
    public Entity(int posX, int posY, Context context)
    {
        m_posX = posX;
        m_posY = posY;
        m_context = context;
    }
    
    /**
     * 
     * @param amount 
     */
    public void hurt(int amount)
    {
        m_health -= amount;
        if(m_health <= 0)
        {
            m_alive = false;
            die();
        }
    }
    
    /**
     *
     * @param posX
     */
    public void setPosX(int posX)
    {
        m_posX = posX;
    }

    /**
     *
     * @param posY
     */
    public void setPosY(int posY)
    {
        m_posY = posY;
    }

    /**
     *
     * @return
     */
    public float getPosX()
    {
        return m_posX;
    }

    /**
     *
     * @return
     */
    public float getPosY()
    {
        return m_posY;
    }

    /**
     * 
     * @return 
     */
    public int getWidth()
    {
        return m_width;
    }
    
    /**
     * 
     * @param width 
     */
    public void setWidth(int width)
    {
        m_width = width;
    }
    
    /**
     * 
     * @return 
     */
    public int getHeight()
    {
        return m_height;
    }
    
    /**
     * 
     * @param height 
     */
    public void setHeight(int height)
    {
        m_height = height;
    }
    
    /**
     * 
     * @return 
     */
    public int getHealth()
    {
        return m_health;
    }
    
    /**
     * 
     * @param health 
     */
    public void setHealth(int health)
    {
        m_health = health;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isAlive()
    {
        return m_alive;
    }
    
    /**
     * 
     * @param alive 
     */
    public void setAlive(boolean alive)
    {
        m_alive = alive;
    }
    
    /**
     * 
     * @param dt 
     */
    public abstract void update(double dt);
    
    /**
     * 
     * @param g
     * @param debug 
     */
    public abstract void render(Graphics g, Boolean debug);
    
    /**
     *
     * @return
     */
    public abstract Rectangle getBounds();

    /**
     * 
     */
    public abstract void die();

    /**
     *
     * @param g
     */
    public abstract void renderHitbox(Graphics g);
}
