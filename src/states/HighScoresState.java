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
import ld34.profile.BestScores;
import org.json.simple.JSONArray;

public class HighScoresState extends BaseState
{
    public BufferedImage m_background, m_foreground2, m_spritesheetGui2, m_bgBtn;
    public Font m_font, m_fontS, m_fontM, m_fontXS;
    public int[][] m_btnCoords;
    public int m_selectedItem;
    
    private final JSONArray m_bestScores;
    
    public HighScoresState(StateManager stateManager)
    {
        super(stateManager);
        
        try
        {
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_fontXS = m_font.deriveFont(18.0f);
            m_fontS = m_font.deriveFont(22.0f);
            m_font = m_font.deriveFont(24.0f);
            m_fontM = m_font.deriveFont(36.0f);

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
        
        m_bestScores = BestScores.getInstance().getBestScores();
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
    public void reloadLocales()
    {
        System.out.println("Reload locales");
    }
    
    @Override
    public void render(Graphics2D g)
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(m_background, 0, 0, null);
        
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, Defines.SCREEN_WIDTH, 60);
        
        FontMetrics metrics = g.getFontMetrics(m_fontM);
        g.setFont(m_fontM);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("highscores_title");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, Defines.SCREEN_WIDTH/2 - titlewidth/2, 75);
        
        g.setFont(m_font);
        g.setColor(Color.BLACK);
        
        for(int i=0;i<m_bestScores.size();i++){
            if(m_bestScores.get(i) instanceof JSONArray){
                JSONArray array = (JSONArray) m_bestScores.get(i);
                if(array.size() == 2)
                {
                    String name = (String) array.get(0);
                    String score = (String) array.get(1);
                    g.drawString((i + 1) +". " + name + " : " + score, 200, (i * 50) + 170 );
                }
            }
        }
        
        metrics = g.getFontMetrics(m_font);
        g.setFont(m_font);
        String backLabel = i18nManager.trans("backToMain");
        int backWidth = metrics.stringWidth(backLabel);
        
        if(m_selectedItem == 1){
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 232, 234, 99);
        }
        g.setColor(new Color(17, 17, 17));
        g.drawImage(m_bgBtn, m_btnCoords[0][0], m_btnCoords[0][1], null);
        g.drawString(backLabel, (3*Defines.SCREEN_WIDTH/4) + 35 - backWidth/2, 510);
        
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
        if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount == 1){
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
