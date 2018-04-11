package states;

import core.Defines;
import core.StateManager;
import core.StateType;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class IntroState extends BaseState
{
    public Font m_font, m_fontSm;
    public BufferedImage m_logo;
    public int m_alpha;
    public boolean m_increase, m_first;
    
    public IntroState(StateManager stateManager)
    {
        super(stateManager);
        
        m_alpha = 255;
        m_increase = false;
        m_first = true;
        
        try{
            URL url = getClass().getResource("/fonts/kaushanscriptregular.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_font = m_font.deriveFont(Font.PLAIN, 36.0f);
            m_fontSm = m_font.deriveFont(Font.PLAIN, 22.0f);
            
            url = getClass().getResource("/afendar.png");
            m_logo = ImageIO.read(url);
        }catch(FontFormatException | IOException e){
            e.getMessage();
        }
    }
    
    @Override
    public void onCreate()
    {
        System.out.println("onCreate intro");
    }

    @Override
    public void onDestroy() 
    {
        System.out.println("onDestroy intro");
    }

    @Override
    public void activate()
    {
        System.out.println("activate intro");
    }

    @Override
    public void desactivate() 
    {
        System.out.println("desactivate intro");
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
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
        g.drawImage(m_logo, Defines.SCREEN_WIDTH/2 - m_logo.getWidth()/2, 160 , null);
        
        g.setColor(Color.WHITE);
        g.setFont(m_font);
        FontMetrics metrics = g.getFontMetrics(m_font);
        int txt1W = metrics.stringWidth("Afendar games");
        g.drawString("Afendar games", Defines.SCREEN_WIDTH/2 - txt1W/2, 390);
        
        g.setFont(m_fontSm);
        metrics = g.getFontMetrics(m_fontSm);
        int txt2W = metrics.stringWidth("present");
        g.drawString("present", Defines.SCREEN_WIDTH/2 - txt2W/2, 430);
        
        g.setColor(new Color(0, 0, 0, m_alpha));
        g.fillRect(0, 0, Defines.SCREEN_WIDTH, Defines.SCREEN_HEIGHT);
    }
}
