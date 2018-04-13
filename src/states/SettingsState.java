package states;

import audio.Sound;
import core.CustomTextField;
import core.Defines;
import core.I18nManager;
import core.OptionButton;
import core.StateManager;
import core.StateType;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import ld34.profile.Settings;

public class SettingsState extends BaseState
{
    public Font m_font, m_fontS, m_fontL, m_fontU;
    public BufferedImage m_iconPlayer, m_iconSettings, m_iconControls, m_backgroundMenu, m_separatorMenu, m_btnSmallSelected, m_backgroundPlayer,
            m_iconBoy, m_iconGirl, m_iconGP, m_iconRP, m_previewsPandas, m_oldPreview, m_currentPreview, m_soundBar, m_bottomControls, m_jumpingPlayer, m_walkingPlayer,
            m_spritesheetGui2, m_background, m_bgBtn, m_foreground2;
    public CustomTextField m_nameField;
    public int[][] m_btnCoords;
    public int m_selectedItem, m_currentTab, m_posBar, m_timerSlider, m_x2;
    public ArrayList<OptionButton> m_optionButtons = new ArrayList<>();
    public JPanel m_sliderContainer;
    public boolean m_startSlide;
    
    public SettingsState(StateManager stateManager)
    {
        super(stateManager);
        
        try{
            URL url = getClass().getResource("/fonts/kaushanscriptregular.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_font = m_font.deriveFont(Font.PLAIN, 20.0f);
            m_fontS = m_font.deriveFont(Font.PLAIN, 18.0f);
            m_fontL = m_font.deriveFont(Font.PLAIN, 36.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            m_fontU = m_font;
            m_fontU = m_fontU.deriveFont(fontAttributes);
            
            url = getClass().getResource("/previews_pandas.png");
            m_previewsPandas = ImageIO.read(url);
            
            url = getClass().getResource("/gui2.png");
            m_spritesheetGui2 = ImageIO.read(url);
            
            url = getClass().getResource("/background.png");
            m_background = ImageIO.read(url);
            
            url = getClass().getResource("/foreground2.png");
            m_foreground2 = ImageIO.read(url);
            
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }
        
        int [][]coords = {
            {(3*Defines.SCREEN_WIDTH/4) - 80, 455}
        };
        
        OptionButton btn1 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))), 
                "Jump", 
                219, 
                342
        );
        btn1.setFont(m_font);
        m_optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                476, 
                342
        );
        btn2.setFont(m_font);
        m_optionButtons.add(btn2);
        
        m_btnCoords = coords;
        m_selectedItem = 0;
        m_currentTab = 0;
        m_timerSlider = 0;
        m_x2 = 616;
        
        m_startSlide = false;
        
        int volumeVal = Integer.parseInt(Settings.getInstance().getConfigValue("Sound"));
        m_posBar = (int)(200 + (2.4 * volumeVal));
        
        m_iconPlayer = m_spritesheetGui2.getSubimage(679, 187, 50, 50);
        m_iconSettings = m_spritesheetGui2.getSubimage(729, 187, 50, 50); 
        m_iconControls = m_spritesheetGui2.getSubimage(779, 187, 50, 50);
        m_backgroundMenu = m_spritesheetGui2.getSubimage(968, 0, 114, 600);
        m_separatorMenu = m_spritesheetGui2.getSubimage(900, 0, 68, 600);
        m_btnSmallSelected = m_spritesheetGui2.getSubimage(612, 0, 124, 103);
        m_backgroundPlayer = m_spritesheetGui2.getSubimage(235, 127, 217, 216);
        m_iconBoy = m_spritesheetGui2.getSubimage(820, 1, 35, 36);
        m_iconGirl = m_spritesheetGui2.getSubimage(858, 1, 27, 40);
        m_iconGP = m_spritesheetGui2.getSubimage(674, 103, 47, 38);
        m_iconRP = m_spritesheetGui2.getSubimage(676, 147, 49, 38);
        m_soundBar = m_spritesheetGui2.getSubimage(0, 468, 299, 62);
        m_bottomControls = m_spritesheetGui2.getSubimage(680, 255, 212, 88);
        m_jumpingPlayer = m_spritesheetGui2.getSubimage(739, 65, 112, 84);
        m_walkingPlayer = m_spritesheetGui2.getSubimage(737, 1, 79, 64);
        
        int sex = Integer.parseInt(Settings.getInstance().getConfigValue("Sex"));
        int spicies = Integer.parseInt(Settings.getInstance().getConfigValue("Spicies"));
        
        m_currentPreview = m_previewsPandas.getSubimage(128 * (sex + spicies), 0, 128, 128);
        
        m_nameField = new CustomTextField("name", Settings.getInstance().getConfigValue("Name"), 203, 183, 287, 46);
        m_nameField.setFont(m_font);
        
        m_sliderContainer = new JPanel();
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
    public void reloadLocales()
    {
        System.out.println("Reload locales");
    }
    
    @Override
    public void update(double dt) 
    {
        processHover();
        
        if(m_stateManager.getContext().m_inputsListener.e != null)
        {
            processKey(m_stateManager.getContext().m_inputsListener.e);
        }
        
        if(m_stateManager.getContext().m_inputsListener.mousePressed)
        {
            int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
            int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
            switch(m_currentTab)
            {
                case 0:
                    if(m_stateManager.getContext().m_inputsListener.mouseClickCount == 1)
                    {
                        if(mouseX > 199 && mouseX < 319 && mouseY > 266 && mouseY < 365)
                        {
                            //male btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Sex").equals("1"))
                            {
                                Settings.getInstance().setConfigValue("Sex", "0");
                                m_startSlide = true;
                                m_oldPreview = m_currentPreview;
                                m_currentPreview = m_previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Spicies")) * 2), 0, 128, 128);
                            }
                        }
                        else if(mouseX > 369 && mouseX < 489 && mouseY > 266 && mouseY < 365)
                        {
                            //female btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Sex").equals("0"))
                            {
                                Settings.getInstance().setConfigValue("Sex", "1");
                                m_startSlide = true;
                                m_oldPreview = m_currentPreview;
                                m_currentPreview = m_previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Spicies")) * 2 + 1), 0, 128, 128);
                            }
                        }
                        else if(mouseX > 199 && mouseX < 319 && mouseY > 386 && mouseY < 485)
                        {
                            //grand panda btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Spicies").equals("1"))
                            {
                                Settings.getInstance().setConfigValue("Spicies", "0");
                                m_startSlide = true;
                                m_oldPreview = m_currentPreview;
                                m_currentPreview = m_previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Sex"))), 0, 128, 128);
                            }
                        }
                        else if(mouseX > 369 && mouseX < 489 && mouseY > 386 && mouseY < 485)
                        {
                            //panda roux btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Spicies").equals("0"))
                            {
                                Settings.getInstance().setConfigValue("Spicies", "1");
                                m_startSlide = true;
                                m_oldPreview = m_currentPreview;
                                m_currentPreview = m_previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Sex")) + 2), 0, 128, 128);
                            }
                        }
                    }
                    break;
                case 1:
                    if(m_stateManager.getContext().m_inputsListener.mouseClickCount == 1)
                    {
                        if(mouseX > 189 && mouseX < 189 + 120 && mouseY > 166 && mouseY < 166 + 99)
                        {
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "0");
                        }
                        else if(mouseX > 339 && mouseX < 339 + 120 && mouseY > 166 && mouseY < 166 + 99)
                        {
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "2");
                        }
                        else if(mouseX > 479 && mouseX < 479 + 120 && mouseY > 166 && mouseY < 166 + 99)
                        {
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "4");
                        }
                        else if(mouseX > 629 && mouseX < 629 + 120 && mouseY > 166 && mouseY < 166 + 99)
                        {
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "5");
                        }
                        else if(mouseX > 221 && mouseX < 221 + 234 && mouseY > 310 && mouseY < 310 + 100)
                        {
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Lang", "0");
                            m_stateManager.getContext().m_I18nManager.setLanguage(I18nManager.Language.ENGLISH);
                        }
                        else if(mouseX > 481 && mouseX < 481 + 234 && mouseY > 305 && mouseY < 305 + 100)
                        {
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Lang", "1");
                            m_stateManager.getContext().m_I18nManager.setLanguage(I18nManager.Language.FRENCH);
                        }
                    }
                    
                    if(mouseX >= 200 && mouseX <= 440 && mouseY > 475 && mouseY < 489)
                    {
                        m_posBar = mouseX;
                        int newVolume = (int)((200 - m_posBar)/ - 2.4);
                        Settings.getInstance().setConfigValue("Sound", Integer.toString(newVolume));
                    }
                    break;
                case 2:
                    break;
            }
        }
        
        if(m_startSlide)
        {
            m_timerSlider += dt;
            if(m_timerSlider >= 2)
            {
                m_timerSlider = 0;
                m_x2 -= 18;
                if(m_x2 <= 570 - 46 - 128)
                {
                    m_startSlide = false;
                    m_x2 = 616;
                }
            }
        }
        processClick();
    }

    @Override
    public void render(Graphics2D g) 
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(m_background, 0, 0, null);
        
        g.drawImage(m_backgroundMenu, 0, 0, null);

        //draw menu options
        g.setColor(new Color(124, 180, 94));
        switch(m_currentTab)
        {
            case 0:
                g.fillRect(0, 205, 113, 80);
                break;
            case 1:
                g.fillRect(0, 286, 113, 80);
                break;
            case 2:
                g.fillRect(0, 367, 113, 80);
                break;
        }
        
        g.drawImage(m_iconPlayer, 25, 220, null);
        g.drawImage(m_iconSettings, 25, 300, null);
        g.drawImage(m_iconControls, 25, 380, null);
        
        g.drawImage(m_separatorMenu, 89, 0, null);
        
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, Defines.SCREEN_WIDTH, 60);
        
        FontMetrics metrics = g.getFontMetrics(m_fontL);
        g.setFont(m_fontL);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("settings_title");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, Defines.SCREEN_WIDTH/2 - titlewidth/2, 75);
        
        switch(m_currentTab)
        {
            case 0:
                this.renderPlayerSettings(g);
                break;
            case 1:
                this.renderGameSettings(g);
                break;
            case 2:
                this.renderControlsSettings(g);
                break;
            default:
                break;
        }

        //end btn
        if(m_selectedItem == 1)
        {
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            m_bgBtn = m_spritesheetGui2.getSubimage(0, 232, 234, 99);
        }

        if(m_selectedItem == 1){
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 595, 471, null);
        }
        else{
            g.drawImage(m_spritesheetGui2.getSubimage(370, 1, 120, 99), 595, 471, null);
        }
        g.drawImage(m_spritesheetGui2.getSubimage(243, 94, 42, 34), 633, 502, null);
        
        g.drawImage(m_foreground2, 0, 0, null);
    }
    
    public void processHover()
    {
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        if(mouseX > 595 && mouseX < 715 && mouseY > 471 && mouseY < 570)
        {
            //if btn Back
            if(m_selectedItem != 1)
                new Thread(Sound.hover::play).start();
            m_selectedItem = 1;
        }
        else if(mouseX > 0 && mouseX < 113 && mouseY > 205 && mouseY < 285)
        {
            if(m_selectedItem != 2)
                m_selectedItem = 2;
        }
        else if(mouseX > 0 && mouseX < 113 && mouseY > 286 && mouseY < 366)
        {
            if(m_selectedItem != 3)
                m_selectedItem = 3;
        }
        else if(mouseX > 0 && mouseX < 113 && mouseY > 367 && mouseY < 447)
        {
            //if btn controls
            if(m_selectedItem != 4)
                m_selectedItem = 4;
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
        if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount == 1)
        {
            switch(m_currentTab)
            {
                case 0:
                    m_nameField.processClick(m_stateManager.getContext().m_inputsListener.mouseX, m_stateManager.getContext().m_inputsListener.mouseY);
                    break;
                case 2:
                    processButtonsClick();
                    break;
            }
            
            
            switch(m_selectedItem)
            {
                case 1:
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    m_stateManager.switchTo(StateType.MAIN_MENU);
                    break;
                case 2:
                    //player infos tab
                    m_currentTab = 0;
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    break;
                case 3:
                    //settings tab
                    m_currentTab = 1;
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    break;
                case 4:
                    //controls tab
                    m_currentTab = 2;
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    break;
            }
        }
    }
    
    /**
     * 
     * @param g 
     */
    public void renderPlayerSettings(Graphics g){
        
        m_nameField.render(g);
        
        int sex = Integer.parseInt(Settings.getInstance().getConfigValue("Sex"));
        if(sex == 0)
        {
            g.drawImage(m_btnSmallSelected, 199, 266, null);
        }
        else{
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 199, 266, null);
        }
        if(sex == 1)
        {
            g.drawImage(m_btnSmallSelected, 368, 265, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 368, 265, null);
        }
        g.drawImage(m_iconBoy, 239, 297, null);
        g.drawImage(m_iconGirl, 413, 293, null);

        int species = Integer.parseInt(Settings.getInstance().getConfigValue("Spicies"));
        if(species == 0)
        {
            g.drawImage(m_btnSmallSelected, 199, 386, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 199, 386, null);
        }
        if(species == 1)
        {
            g.drawImage(m_btnSmallSelected, 369, 386, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 369, 386, null);
        }
        g.drawImage(m_iconGP, 233, 416, null);
        g.drawImage(m_iconRP, 402, 416, null);

        g.drawImage(m_backgroundPlayer, 570, 190, null);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(0,0,0,25));
        g2d.fillOval(607, 327, 150, 40);
        
        if(m_startSlide && m_x2 <= 570 + 46 && m_x2 > 570 + 46 - 128)
        {
            int w = 128 - ( 570 - m_x2 + 46);
            int w2 = w + 20;
            if(w > 0){
                BufferedImage previewOldPlayer = m_oldPreview.getSubimage(570 - m_x2 + 46, 0, w, 128);
                g.drawImage(previewOldPlayer, 616, 234, null);
            }
            if(128 - w2 > 0 && 128 - w2 < 128 ){
                BufferedImage previewCurrentPlayer = m_currentPreview.getSubimage(0, 0, 128 - w2, 128);
                g.drawImage(previewCurrentPlayer, m_x2 + 128 + 20, 234, null);
            }
        }
        else
        {
            g.drawImage(m_currentPreview, 616, 234, null); 
        }
    }
    
    /**
     * 
     * @param g 
     */
    public void renderGameSettings(Graphics g)
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        
        //difficulty
        g.setColor(Color.BLACK);
        g.setFont(m_fontS);
        FontMetrics metrics = g.getFontMetrics(m_font);
        String difficulty = i18nManager.trans("difficulty");
        g.drawString(difficulty, 170, 128 + metrics.getAscent());
        
        //easy
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 0)
        {
            g.drawImage(m_btnSmallSelected, 189, 166, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 189, 166, null);
        }
        g.drawImage(m_spritesheetGui2.getSubimage(285, 69, 17, 16), 239, 205, null);
        
        //medium
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 2)
        {
            g.drawImage(m_btnSmallSelected, 339, 166, null);
        }else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 339, 166, null);
        }
        g.drawImage(m_spritesheetGui2.getSubimage(325, 69, 35, 16), 380, 206, null);

        //hard
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 4)
        {
            g.drawImage(m_btnSmallSelected, 479, 166, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 479, 166, null);
        }
        g.drawImage(m_spritesheetGui2.getSubimage(285, 89, 33, 32), 521, 197, null);
        
        //hardcore
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 5)
        {
            g.drawImage(m_btnSmallSelected, 629, 166, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(491, 1, 120, 99), 629, 166, null);
        }
        g.drawImage(m_spritesheetGui2.getSubimage(326, 90, 33, 32), 672, 197, null);
        
        //languages
        g.setFont(m_fontS);
        g.setColor(Color.BLACK);
        String language = i18nManager.trans("language");
        g.drawString(language, 170, 278 + metrics.getAscent());
        //english
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 0)
        {
            g.drawImage(m_spritesheetGui2.getSubimage(0, 332, 238, 104), 221, 310, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(0, 132, 234, 100), 221, 310, null);
        }
        g.drawImage(m_spritesheetGui2.getSubimage(238, 344, 42, 28), 256, 345, null);
        g.setFont(m_font);
        String english = i18nManager.trans("english");
        g.drawString(english, 317, 346 + metrics.getAscent()/2 + 8);
        
        //french
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 1)
        {
            g.drawImage(m_spritesheetGui2.getSubimage(0, 332, 238, 104), 481, 305, null);
        }
        else
        {
            g.drawImage(m_spritesheetGui2.getSubimage(0, 132, 234, 100), 481, 305, null);
        }
        g.drawImage(m_spritesheetGui2.getSubimage(238, 372, 42, 28), 517, 342, null);
        g.setFont(m_font);
        String french = i18nManager.trans("french");
        g.drawString(french, 580, 342 + metrics.getAscent()/2 + 8);
        
        //Volume
        g.setFont(m_fontS);
        String volume = i18nManager.trans("volume");
        g.drawString(volume, 170, 424 + metrics.getAscent());
        
        int red = 255;
        int green = 0;
        for(int i=0;i<255;i++)
        {
            g.setColor(new Color(red, green, 0));
            g.fillRect((int)(200 + i), 472, 1, 19);
            red--;
            green++;
        }
        g.drawImage(m_spritesheetGui2.getSubimage(0, 438, 299, 62), 175, 444, null);
        g.setColor(Color.BLACK);
        g.fillRect(m_posBar, 475, 9, 14);
    }
    
    /**
     * 
     * @param g 
     */
    public void renderControlsSettings(Graphics g)
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        
        g.drawImage(m_bottomControls, 218, 345, null);
        g.drawImage(m_bottomControls, 475, 345, null);
        
        g.drawImage(m_backgroundPlayer, 213, 147, null);
        g.drawImage(m_backgroundPlayer, 470, 147, null);
        
        g.drawImage(m_jumpingPlayer, 275, 227, null);
        g.drawImage(m_walkingPlayer, 542, 240, null);
        
        g.setFont(m_font);
        FontMetrics metrics = g.getFontMetrics(m_font);
        String ctrlJump = i18nManager.trans("ctrlJump");
        g.drawString(ctrlJump, 290, 180 + metrics.getAscent());
        String ctrlWalk = i18nManager.trans("ctrlWalk");
        g.drawString(ctrlWalk, 543, 180 + metrics.getAscent());
        
        for(int i=0;i<m_optionButtons.size();i++)
        {
            m_optionButtons.get(i).render(g);
        }
    }

    /**
     * 
     * @param e 
     */
    public void processKey(KeyEvent e)
    {    
        switch(m_currentTab)
        {
            case 0:
                m_nameField.processKey(e);
                break;
            case 2:
                for(int i=0;i<m_optionButtons.size();i++)
                {
                    if(m_optionButtons.get(i).isEditing())
                    {
                        m_optionButtons.get(i).processKey(e);
                    }
                }
                break;
        }
    }
    
    /**
     * 
     */
    public void processButtonsClick()
    {
        for(int i=0;i<m_optionButtons.size();i++)
        {
            if(m_optionButtons.get(i).isEditing())
                return;
        }
        
        for(int i=0;i<m_optionButtons.size();i++)
        {
            m_optionButtons.get(i).processClick(m_stateManager.getContext().m_inputsListener.mouseX, m_stateManager.getContext().m_inputsListener.mouseY);
        }
    }
}
