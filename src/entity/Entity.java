package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Entity class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public abstract class Entity
{
    protected float m_posX, m_posY;

    /**
     *
     * @param posX
     * @param posY
     */
    public Entity(int posX, int posY)
    {
        m_posX = posX;
        m_posY = posY;
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
    public abstract Rectangle getBounds();

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
     * @param g
     */
    public abstract void renderHitbox(Graphics g);
}
