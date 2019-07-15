package entity;

import core.Context;
import java.awt.Graphics;
import java.awt.Rectangle;
import level.Level;

/**
 * SandEntity class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class SandEntity extends Entity
{
    public double dt;
    public Level level;

    /**
     *
     * @param level
     * @param posX
     * @param posY
     * @param context
     */
    public SandEntity(Level level, int posX, int posY, Context context)
    {
        super(posX, posY, context);
        this.level = level;
    }

    @Override
    public void update(double dt)
    {
        int data = level.getData((int) m_posX, (int) m_posY);
        switch (data)
        {
            case 1:
                this.dt += dt;
                if (this.dt > 90)
                {
                    level.setData((int) m_posX, (int) m_posY, 2);
                    this.dt = 0;
                }
                break;
            case 2:
                this.dt += dt;
                if (this.dt > 270)
                {
                    level.setData((int) m_posX, (int) m_posY, 0);
                    this.dt = 0;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) m_posX, (int) m_posY, 10, 10);
    }

    @Override
    public void render(Graphics g, Boolean debug)
    {
    }

    @Override
    public void renderHitbox(Graphics g)
    {

    }
    
    @Override
    public void die()
    {
        
    }
}
