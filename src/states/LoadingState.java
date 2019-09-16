package states;

import core.Screen;
import core.StateManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * MainMenuState class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class LoadingState extends BaseState
{
    private float m_time;
    private int m_tick, m_alpha, m_alphaTxt;
    
    public LoadingState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        m_alpha = m_alphaTxt = 255;
        m_time = m_tick = 0;
    }

    @Override
    public void onDestroy()
    {
        
    }

    @Override
    public void desactivate()
    {
        
    }

    @Override
    public void reloadLocales()
    {
        
    }

    @Override
    public void update(double dt)
    {
        m_time += dt;
        if(m_time >= 1.5){
            m_time -= 1.5;
            m_tick++;
        }
        m_alphaTxt = (m_tick / 20) % 2 == 0 ? 255 : 135;
    }

    @Override
    public void render(Graphics2D g)
    {
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        BufferedImage img = m_stateManager.getContext().m_resourceManager.getSpritesheets("background-fall-loading");
        g.drawImage(img, screenWidth/2 - img.getWidth()/2, screenHeight/2 - img.getHeight()/2 - 50, null);
        
        img = m_stateManager.getContext().m_resourceManager.getSpritesheets("loading-border");
        g.drawImage(img, screenWidth/2 - img.getWidth()/2, screenHeight/2 - img.getHeight()/2 - 50, null);
        
        g.setColor(new Color(255, 255, 255, m_alphaTxt));
        Font font = m_stateManager.getContext().m_resourceManager.getFont("kaushanscriptregular").deriveFont(36.0f);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int txt1W = metrics.stringWidth("Loading ...");
        g.drawString("Loading ...", screenWidth/2 - txt1W/2, screenHeight - 100);
    }
}
