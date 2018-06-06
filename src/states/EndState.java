package states;

import core.I18nManager;
import core.Screen;
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

public class EndState extends BaseState
{
    public Font m_font, m_fontS;
    public BufferedImage m_background, m_foreground3, m_spritesheetGui2, m_bgBtn;
    public int[][] m_btnCoords;
    public int m_selectedItem;
    public int m_alpha;
    public static Color m_darkGreen = new Color(128, 0, 19);
    
    public EndState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        
        try{
            URL url = getClass().getResource("/fonts/kaushanscriptregular.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_font = m_font.deriveFont(Font.PLAIN, 22.0f);
            m_fontS = m_font.deriveFont(Font.PLAIN, 17.0f);
            
            url = getClass().getResource("/background.png");
            m_background = ImageIO.read(url);
            
            url = getClass().getResource("/foreground3.png");
            m_foreground3 = ImageIO.read(url);
            
            url = getClass().getResource("/gui2.png");
            m_spritesheetGui2 = ImageIO.read(url);
            
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }

        int [][]coords = {
            {(3 * screenWidth / 4) - 80, 455}
        };
        m_btnCoords = coords;
        m_selectedItem = 0;
        m_alpha = 255;
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
        System.out.println("Reload locales");
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
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        
        g.drawImage(m_background, 0, 0, null);
        
        g.setFont(m_font);
        g.setColor(Color.BLACK);
        FontMetrics metrics = g.getFontMetrics(m_font);
        
        String text1 = i18nManager.trans("end_text1");
        int text1Width = metrics.stringWidth(text1);
        g.drawString(text1, screenWidth/2 - text1Width/2, 240);
        
        String text2 = i18nManager.trans("end_text2");
        int text2Width = metrics.stringWidth(text2);
        g.drawString(text2, screenWidth/2 - text2Width/2, 300);
        
        String text3 = i18nManager.trans("end_text3");
        int text3Width = metrics.stringWidth(text3);
        g.drawString(text3, screenWidth/2 - text3Width/2, 360);
        
        if(m_alpha > 0){
            m_alpha--;
        }else
        {
            String backLabel = i18nManager.trans("backToMain");
            int backWidth = metrics.stringWidth(backLabel);
            g.drawImage(m_bgBtn, m_btnCoords[0][0], m_btnCoords[0][1], null);
            if(m_selectedItem == 1){
                g.rotate(-0.1, (3*screenWidth/4)+25, 475);
                g.setColor(m_darkGreen);
                g.drawString(backLabel, (3*screenWidth/4) + 25 - backWidth/2, 495);
                g.rotate(0.1, (3*screenWidth/4)+25, 475);
            }
            else{
                g.setColor(Color.BLACK);
                g.drawString(backLabel, (3*screenWidth/4) + 25 - backWidth/2, 495);
            }
        }
        
        g.setFont(m_fontS);
        g.setColor(Color.BLACK);
        metrics = g.getFontMetrics(m_fontS);
        String text4 = i18nManager.trans("end_text4");
        int text4Width = metrics.stringWidth(text4);
        g.drawString(text4, screenWidth/3 - text4Width/2 + 40, 490);
        
        g.drawImage(m_foreground3, 0, 0, null);
        
        g.setColor(new Color(0, 0, 0, m_alpha));
        g.fillRect(0, 0, screenWidth, screenWidth);
    }
    
    public void processHover()
    {
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        if(mouseX > m_btnCoords[0][0] && mouseX < m_btnCoords[0][0] + 214 &&
                mouseY > m_btnCoords[0][1] && mouseY < m_btnCoords[0][1] + 70){
            //if btn Back
            m_selectedItem = 1;
        }
        else{
            m_selectedItem = 0;
        }
    }
    
    public void processClick()
    {        
        if(m_stateManager.getContext().m_inputsListener.mousePressed)
        {
            switch(m_selectedItem)
            {
                case 1:
                    m_stateManager.switchTo(StateType.MAIN_MENU);
                    break;
            }
        }
    }
}
