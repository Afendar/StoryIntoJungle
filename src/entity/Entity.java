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
    protected float posX, posY;

    /**
     *
     * @param posX
     * @param posY
     */
    public Entity(int posX, int posY)
    {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     *
     * @param posX
     */
    public void setPosX(int posX)
    {
        this.posX = posX;
    }

    /**
     *
     * @param posY
     */
    public void setPosY(int posY)
    {
        this.posY = posY;
    }

    /**
     *
     * @return
     */
    public float getPosX()
    {
        return posX;
    }

    /**
     *
     * @return
     */
    public float getPosY()
    {
        return posY;
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
