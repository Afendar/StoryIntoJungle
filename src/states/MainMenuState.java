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
import java.util.ArrayList;
import javax.imageio.ImageIO;
import ld34.profile.Save;
import particles.Leaf;

public class MainMenuState extends BaseState
{
    public BufferedImage m_spritesheetGui2, m_bgBtn, m_bgBtnSmall, m_foreground, m_background2, m_logo, m_settingsIcon, m_highScoresIcon, m_creditsIcon;
    public Font m_font, m_fontL, m_fontS;
    public int[][] m_btnCoords;
    public int m_selectedItem;
    private ArrayList<Leaf> m_leavesList = new ArrayList<>(5);
    private boolean m_displayLoad;
    
    public MainMenuState(StateManager stateManager)
    {
        super(stateManager);
        
        try
        {
            URL url = getClass().getResource("/gui2.png");
            m_spritesheetGui2 = ImageIO.read(url);
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
            m_bgBtnSmall = m_spritesheetGui2.getSubimage(0, 69, 76, 63);
            m_settingsIcon = m_spritesheetGui2.getSubimage(150, 68, 25, 24);
            m_highScoresIcon = m_spritesheetGui2.getSubimage(175, 69, 35, 23);
            m_creditsIcon = m_spritesheetGui2.getSubimage(154, 92, 16, 24);
            
            url = getClass().getResource("/foreground1.png");
            m_foreground = ImageIO.read(url);
            
            url = getClass().getResource("/fonts/kaushanscriptregular.ttf");
            
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_fontS = m_font.deriveFont(18.0f);
            m_font = m_font.deriveFont(24.0f);
            m_fontL = m_font.deriveFont(52.0f);
            
            url = getClass().getResource("/background.png");
            m_background2 = ImageIO.read(url);
           
            m_logo = m_spritesheetGui2.getSubimage(310, 347, 590, 200);

        }
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }
        m_displayLoad = Save.getInstance().hasSave();
        
        //new game
        if(m_displayLoad)
        {
            int [][]coords = {
                {Defines.SCREEN_WIDTH/2 - 117 - (15*30), 211},
                {Defines.SCREEN_WIDTH/2 - 117 - (17*30), 311},
                {Defines.SCREEN_WIDTH/2 - 117 - (19*30), 411},
                {16, 518},
                {107, 518},
                {703, 518}
            };
            m_btnCoords = coords;
        }
        else
        {
            int [][]coords = {
                {Defines.SCREEN_WIDTH/2 - 107 - 15*30, 235},
                {Defines.SCREEN_WIDTH/2 - 107 - 19*30, 355},
                {16, 518},
                {107, 518},
                {703, 518}
            };
            m_btnCoords = coords;
        }
        
        m_selectedItem = 0;
        
        for(int i = 0;i<5;i++){
            m_leavesList.add(new Leaf(5, 0, 0, Defines.SCREEN_WIDTH, Defines.SCREEN_HEIGHT));
        }
    }
    
    @Override
    public void onCreate()
    {
        System.out.println("onCreate main menu");
    }

    @Override
    public void onDestroy() 
    {
        System.out.println("onDestroy main menu");
    }

    @Override
    public void activate()
    {
        System.out.println("activate main menu");
    }

    @Override
    public void desactivate() 
    {
        System.out.println("desactivate main menu");
    }

    @Override
    public void update(double dt)
    {
        processHover();

        for(int i=0;i<m_btnCoords.length-3;i++)
        {
            if(m_btnCoords[i][0] < Defines.SCREEN_WIDTH/2 - 117){
                m_btnCoords[i][0] += 30;
            }
        }
        
        for (Leaf leaf : m_leavesList)
        {
            if(!leaf.isGenStartX())
            {
                leaf.genRandStartX();
            }
            leaf.update(dt);
        }
        
        processClick();
    }

    @Override
    public void render(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(m_background2, 0, 0, null);

        for(int i=0; i < m_leavesList.size(); i++){
            Leaf leaf = m_leavesList.get(i);
            leaf.render(g);
        }
        
        g.drawImage(m_logo, 99, 20, null);
        
        g.drawImage(m_foreground, 0, 0, null);
        
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        
        String[] labels = new String[2];
        if(m_displayLoad)
        {
            labels = new String[3];
            labels[0] = i18nManager.trans("newGame");
            labels[1] = i18nManager.trans("loadGame");
            labels[2] = i18nManager.trans("quit");
        }
        else
        {
            labels[0] = i18nManager.trans("newGame");
            labels[1] = i18nManager.trans("quit");
        }
        
        //draw btn
        FontMetrics metrics = g.getFontMetrics(m_font);
        for(int i=0; i< m_btnCoords.length;i++)
        {
            if(i < m_btnCoords.length - 3)
            {
                g.setFont(m_font);
                if(m_selectedItem == i + 1)
                {
                    m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
                }
                else
                {
                    m_bgBtn = m_spritesheetGui2.getSubimage(0, 232, 234, 99);
                }
                if(i % 2 == 0)
                {
                    g.drawImage(m_bgBtn, m_btnCoords[i][0], m_btnCoords[i][1], null);
                }
                else
                {
                    g.drawImage(horizontalflip(m_bgBtn), m_btnCoords[i][0], m_btnCoords[i][1], null);
                }
                int labelWidth = metrics.stringWidth(labels[i]);
                g.setColor(new Color(17, 17, 17));
                g.drawString(labels[i], m_btnCoords[i][0] + 117 - labelWidth/2, m_btnCoords[i][1] + metrics.getAscent() + 28);
            }
            else
            {
                if(m_selectedItem == i + 1)
                {
                    m_bgBtnSmall = m_spritesheetGui2.getSubimage(0, 69, 76, 63);
                }
                else
                {
                    m_bgBtnSmall = m_spritesheetGui2.getSubimage(76, 69, 76, 63);
                }
                g.drawImage(m_bgBtnSmall, m_btnCoords[i][0], m_btnCoords[i][1], null);
            }
        }
        
        g.drawImage(m_settingsIcon, 41, 535, null);
        g.drawImage(m_highScoresIcon, 127, 537, null);
        g.drawImage(m_creditsIcon, 733, 535, null);
    }
    
    /**
     * 
     */
    public void processHover()
    {    
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        int oldSelected = m_selectedItem;
        m_selectedItem = 0;
        for(int i=0;i<m_btnCoords.length;i++)
        {
            if(i < m_btnCoords.length - 3){
                if(mouseX > m_btnCoords[i][0] && mouseX < m_btnCoords[i][0] + 234 &&
                        mouseY > m_btnCoords[i][1] && mouseY < m_btnCoords[i][1] + 99){
                    m_selectedItem = i + 1;
                }
            }
            else{
                if(mouseX > m_btnCoords[i][0] && mouseX < m_btnCoords[i][0] + 76 &&
                mouseY > m_btnCoords[i][1] && mouseY < m_btnCoords[i][1] + 63){
                    m_selectedItem = i + 1;
                }
            }
        }
        
        if(m_selectedItem != 0 && m_selectedItem != oldSelected){
            new Thread(Sound.hover::play).start();
        }
    }
    
    /**
     * 
     */
    public void processClick()
    {
        if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount >= 1)
        {
            if(m_displayLoad)
            {
                switch(m_selectedItem)
                {
                    case 1:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.GAME);
                        break;
                    case 2:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.SAVES);
                        break;
                    case 3:
                        new Thread(Sound.select::play).start();
                        System.exit(0);
                        break;
                    case 4:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.SETTINGS);
                        break;
                    case 5:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.HIGHT_SCORES);
                        break;
                    case 6:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.CREDITS);
                        break;
                }
            }
            else
            {
                switch(m_selectedItem)
                {
                    case 1:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.GAME);
                        break;
                    case 2:
                        new Thread(Sound.select::play).start();
                        System.exit(0);
                        break;
                    case 3:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.SETTINGS);
                        break;
                    case 4:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.HIGHT_SCORES);
                        break;
                    case 5:
                        new Thread(Sound.select::play).start();
                        m_stateManager.switchTo(StateType.CREDITS);
                        break;
                }
            }
        }
    }
    
    /**
     * 
     * @param img
     * @return 
     */
    private BufferedImage horizontalflip(BufferedImage img)
    {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);  
        g.dispose();
        return dimg;  
    }
}
