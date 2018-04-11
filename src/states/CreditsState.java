package states;

import audio.Sound;
import core.Defines;
import core.I18nManager;
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

public class CreditsState extends BaseState
{
    public BufferedImage m_background, m_foreground2, m_spritesheetGui2, m_bgBtn;
    public Font m_font, m_fontM, m_fontL;
    public int[][] m_btnCoords;
    public int m_selectedItem;
    
    public CreditsState(StateManager stateManager)
    {
        super(stateManager);
        
        try
        {
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_font = m_font.deriveFont(Font.PLAIN, 18.0f);
            m_fontM = m_font.deriveFont(Font.PLAIN, 22.0f);
            m_fontL = m_font.deriveFont(Font.PLAIN, 36.0f);
            
            url = getClass().getResource("/foreground2.png");
            m_foreground2 = ImageIO.read(url);
            url = getClass().getResource("/gui2.png");
            m_spritesheetGui2 = ImageIO.read(url);
            url = getClass().getResource("/background.png");
            m_background = ImageIO.read(url);
            
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
            
        }
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }
        
        int [][]coords = {
            {(3*Defines.SCREEN_WIDTH/4) - 80, 455}
        };
        m_btnCoords = coords;
        m_selectedItem = 0;
    }
    
    @Override
    public void onCreate()
    {
        
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
    public void update(double dt)
    {
        processHover();
        processClick();
    }

    @Override
    public void render(Graphics2D g)
    {
        Graphics2D g2d = (Graphics2D) g;
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(m_background, 0, 0, null);
        
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, Defines.SCREEN_WIDTH, 60);
        
        FontMetrics metrics = g.getFontMetrics(m_fontL);
        g.setFont(m_fontL);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("credits_title");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, Defines.SCREEN_WIDTH/2 - titlewidth/2, 75);
        
        g.setFont(m_fontM);
        metrics = g.getFontMetrics(m_fontM);
        
        String text1 = i18nManager.trans("credits_text1");
        int text1Width = metrics.stringWidth(text1);
        g.drawString(text1, Defines.SCREEN_WIDTH/2 - text1Width/2, 200);
        
        String text2 = i18nManager.trans("credits_text2");
        int text2Width = metrics.stringWidth(text2);
        g.drawString(text2, Defines.SCREEN_WIDTH/2 - text2Width/2, 250);
        
        String text3 = i18nManager.trans("credits_text3");
        int text3Width = metrics.stringWidth(text3);
        g.drawString(text3, Defines.SCREEN_WIDTH/2 - text3Width/2, 300);
        
        String backLabel = i18nManager.trans("backToMain");
        int backWidth = metrics.stringWidth(backLabel);
        if(m_selectedItem == 1)
        {
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else
        {
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 232, 234, 99);
        }
        g.drawImage(m_bgBtn, m_btnCoords[0][0], m_btnCoords[0][1], null);
        g.setColor(new Color(17, 17, 17));
        g.drawString(backLabel, (3 * Defines.SCREEN_WIDTH/4)+32 - backWidth/2, 510);
        
        g.drawImage(m_foreground2, 0, 0, null);
    }
    
    /**
     * 
     */
    public void processHover(){
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        if(mouseX > m_btnCoords[0][0] && mouseX < m_btnCoords[0][0] + 214 &&
                mouseY > m_btnCoords[0][1] && mouseY < m_btnCoords[0][1] + 70)
        {
            //if btn Back
            if(m_selectedItem != 1)
                new Thread(Sound.hover::play).start();
            m_selectedItem = 1;
        }
        else
        {
            m_selectedItem = 0;
        }
    }
    
    /**
     * 
     */
    public void processClick()
    {  
        if(m_stateManager.getContext().m_inputsListener.mousePressed){
            switch(m_selectedItem)
            {
                case 1:
                    new Thread(Sound.select::play).start();
                    m_stateManager.switchTo(StateType.MAIN_MENU);
                    break;
            }
        }
    }
}
