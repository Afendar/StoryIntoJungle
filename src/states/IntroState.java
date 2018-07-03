package states;

import core.Screen;
import core.StateManager;
import core.StateType;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class IntroState extends BaseState
{
    public int m_alpha;
    public boolean m_increase, m_first;
    
    public IntroState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        m_alpha = 255;
        m_increase = false;
        m_first = true;
    }

    @Override
    public void onDestroy() 
    {
    }

    @Override
    public void activate()
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
        if(m_first)
        {
            try
            {
                Thread.sleep(500);
                m_first = false;
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        try
        {
            Thread.sleep(5);
            if(m_alpha > 0 && !m_increase)
            {
                m_alpha--;
                if( m_alpha < 0 )
                    m_alpha = 0;
            }
            else
            {
                m_alpha+=2;
                if(m_alpha > 255)
                    m_alpha = 255;
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        
        if(m_alpha == 0)
            m_increase = true;
        
        if(m_alpha == 255 && m_increase)
            m_stateManager.switchTo(StateType.MAIN_MENU);
    }

    @Override
    public void render(Graphics2D g)
    {
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();

        BufferedImage img = m_stateManager.getContext().m_resourceManager.getSpritesheets("afendar");
        g.drawImage(img, screenWidth/2 - img.getWidth()/2, 160 , null);
        
        g.setColor(Color.WHITE);
        Font font = m_stateManager.getContext().m_resourceManager.getFont("kaushanscriptregular").deriveFont(36.0f);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int txt1W = metrics.stringWidth("Afendar games");
        g.drawString("Afendar games", screenWidth/2 - txt1W/2, 390);
        
        font = m_stateManager.getContext().m_resourceManager.getFont("kaushanscriptregular").deriveFont(22.0f);
        g.setFont(font);
        metrics = g.getFontMetrics(font);
        int txt2W = metrics.stringWidth("present");
        g.drawString("present", screenWidth/2 - txt2W/2, 430);
        
        g.setColor(new Color(0, 0, 0, m_alpha));
        g.fillRect(0, 0, screenWidth, screenHeight);
    }
}
