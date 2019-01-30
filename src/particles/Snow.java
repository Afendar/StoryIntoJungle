package particles;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Snow class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Snow extends Particle
{
    private int m_x, m_y, m_startX, m_startY, m_winH, m_winW;
    private double m_dx, m_dy, m_perturb, m_influence, m_speed, m_dt;
    private boolean m_genStartX, m_dead;
    private Random m_rnd = new Random();
    private BufferedImage m_sprite;
    
    public Snow(int size, int startX, int startY , int winW, int winH){
        try{
            URL url = getClass().getResource("/snowflake.png");
            m_sprite = ImageIO.read(url);
        }catch(IOException e){
            e.getMessage();
        }
        
        m_winW = winW;
        m_winH = winH;
        m_dt = 0;
        m_genStartX = false;
        m_startX = startX;
        m_startY = startY;
        genRandStartX();
    }
    
    public boolean isGenStartX(){
        return m_genStartX;
    }
    
    public final void genRandStartX(){
        m_x = m_rnd.nextInt((m_startX + m_winW) - m_startX) + m_startX;
        m_y = m_startY - m_sprite.getHeight();
        m_speed = Math.random() * 4 + 1;
        m_genStartX = true;
        m_perturb = 0;
        m_influence = Math.random() * 0.06 + 0.05;
    }
    
    @Override
    public void update(double dt)
    {
        m_dt += dt;
        if(m_dt > 1.5)
        {
            m_dt = 0;
            m_dy = m_speed * Math.sin(90 * Math.PI / 180);
            m_dx = m_speed * Math.cos(m_perturb);
            m_x += m_dx;
            if (m_y <= m_startY + m_winH - m_sprite.getHeight()) {
                m_y += m_dy;
            } else {
                m_genStartX = false;
                m_dead = true;
            }
            m_perturb += m_influence;
        }
    }
    
    @Override
    public boolean isDead(){
        return m_dead;
    }
    
    @Override
    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(m_sprite, (int)m_x - (m_sprite.getWidth() / 2), (int)m_y - (m_sprite.getHeight() / 2), null);
    }
}
