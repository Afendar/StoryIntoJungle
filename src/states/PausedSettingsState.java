package states;

import core.CustomTextField;
import core.I18nManager;
import core.OptionButton;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.gui.ButtonGroup;
import core.gui.CheckBox;
import core.gui.GuiComponent;
import core.gui.IconButton;
import core.gui.RadioButton;
import core.gui.RadioIconButton;
import core.gui.Slider;
import core.gui.TabContent;
import core.gui.TabsPanelsContainer;
import entity.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import ld34.profile.Settings;
import level.Level;

public class PausedSettingsState extends BaseState
{
    private ArrayList<GuiComponent> m_guiElements;
    
    public PausedSettingsState(StateManager stateManager)
    {
        super(stateManager);
    }

    @Override
    public void onCreate()
    {
        setTransparent(true);
        
        m_guiElements = new ArrayList<>();
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        BufferedImage spritesheet = resourceManager.getSpritesheets("spritesheetGui2");
        
        IconButton ib = new IconButton(spritesheet.getSubimage(760, 151, 34, 34), this);
        ib.setPosition((3 * screenWidth / 4), screenHeight - 108);
        ib.addCallback(GuiComponent.Status.CLICKED, "backToMenu", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        m_guiElements.add(ib);
        
        TabsPanelsContainer tabPanel = new TabsPanelsContainer(this);
        int panelPosX = (screenWidth - 670) / 2;
        int panelPosY = (screenHeight - 400) / 2;
        int tabHeight = tabPanel.getTabHeight();
        tabPanel.setPosition(panelPosX, panelPosY);
        
        createScreenSettings(tabPanel, spritesheet, panelPosX, panelPosY, tabHeight);
        createPlayerSettings(tabPanel, spritesheet, panelPosX, panelPosY, tabHeight);
        createGameSettings(tabPanel, spritesheet, panelPosX, panelPosY, tabHeight);
        createLanguageSettings(tabPanel, spritesheet, panelPosX, panelPosY, tabHeight);
        
        m_guiElements.add(tabPanel);
    }

    private void createScreenSettings(TabsPanelsContainer tabPanelContainer, BufferedImage spritesheet, int panelPosX, int panelPosY, int tabHeight)
    {
        TabContent content = new TabContent(this);
        CheckBox cb = new CheckBox("Fullscreen", this);
        cb.setPosition(10 + panelPosX + 40, panelPosY + 80 + tabHeight);
        cb.addCallback(GuiComponent.Status.CLICKED, "toggleFullscreen", this);
        cb.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(320, 586, 41, 33));
        cb.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(320, 586, 41, 33));
        cb.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(320, 620, 41, 33));
        content.addGuiComponent(cb);
        
        int res = Integer.parseInt(Settings.getInstance().getConfigValue("resolution"));
        
        RadioButton rb1 = new RadioButton("800 x 600", this);
        rb1.setPosition(15 + panelPosX + 40, 50 + panelPosY + 80 + tabHeight);
        rb1.addCallback(GuiComponent.Status.CLICKED, "changeResolution", this, 800, 600);
        rb1.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(365, 589, 29, 29));
        rb1.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(365, 589, 29, 29));
        rb1.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(365, 624, 29, 29));
        if(res == Screen.RES_1X)
        {
            rb1.setChecked(true);
        }
        
        RadioButton rb2 = new RadioButton("1024 x 768", this);
        rb2.setPosition(15 + panelPosX + 40, 90 + panelPosY + 80 + tabHeight);
        rb2.addCallback(GuiComponent.Status.CLICKED, "changeResolution", this, 1024, 768);
        rb2.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(365, 589, 29, 29));
        rb2.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(365, 589, 29, 29));
        rb2.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(365, 624, 29, 29));
        if(res == Screen.RES_15X)
        {
            rb2.setChecked(true);
        }
        
        RadioButton rb3 = new RadioButton("1280 x 960", this);
        rb3.setPosition(15 + panelPosX + 40, 130 + panelPosY + 80 + tabHeight);
        rb3.addCallback(GuiComponent.Status.CLICKED, "changeResolution", this, 1280, 960);
        rb3.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(365, 589, 29, 29));
        rb3.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(365, 589, 29, 29));
        rb3.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(365, 624, 29, 29));
        if(res == Screen.RES_2X)
        {
            rb3.setChecked(true);
        }
        
        ButtonGroup bg = new ButtonGroup(this);
        bg.add(rb1);
        bg.add(rb2);
        bg.add(rb3);
        content.addGuiComponent(bg);
        
        Slider s = new Slider(this);
        s.setPosition(200 + panelPosX + 40, panelPosY + 80 + tabHeight);
        s.addCallback(GuiComponent.Status.CLICKED, "setSoundVolume", this);
        s.addCallback(GuiComponent.Status.NEUTRAL, "setSoundVolume", this);
        s.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(0, 438, 299, 62));
        s.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(0, 438, 299, 62));
        s.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(0, 438, 299, 62));
        s.setValue(Integer.parseInt(Settings.getInstance().getConfigValue("sound")));
        content.addGuiComponent(s);
        
        s = new Slider(this);
        s.setPosition(200 + panelPosX + 40, 100 + panelPosY + 80 + tabHeight);
        s.addCallback(GuiComponent.Status.CLICKED, "setMusicVolume", this);
        s.addCallback(GuiComponent.Status.NEUTRAL, "setMusicVolume", this);
        s.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(0, 438, 299, 62));
        s.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(0, 438, 299, 62));
        s.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(0, 438, 299, 62));
        s.setValue(Integer.parseInt(Settings.getInstance().getConfigValue("music")));
        content.addGuiComponent(s);
        
        tabPanelContainer.addTab("", spritesheet.getSubimage(475, 554, 110, 93), content);
        tabPanelContainer.setIconSize(0, 46, 38);
    }
    
    private void createPlayerSettings(TabsPanelsContainer tabPanelContainer, BufferedImage spritesheet, int panelPosX, int panelPosY, int tabHeight)
    {
        TabContent content = new TabContent(this);
        
        int sex = Integer.parseInt(Settings.getInstance().getConfigValue("sex"));
        int species = Integer.parseInt(Settings.getInstance().getConfigValue("species"));
        
        ButtonGroup bg = new ButtonGroup(this);
        RadioIconButton rib = new RadioIconButton(spritesheet.getSubimage(820, 1, 35, 36), this);
        rib.setPosition(10 + panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectSex", this, 0);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(sex == Player.SEX_BOY)
            rib.setChecked(true);
        bg.add(rib);
        
        rib = new RadioIconButton(spritesheet.getSubimage(858, 1, 27, 40), this);
        rib.setPosition(150 + panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectSex", this, 1);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(sex == Player.SEX_GIRL)
            rib.setChecked(true);
        bg.add(rib);
        content.addGuiComponent(bg);
        
        bg = new ButtonGroup(this);
        rib = new RadioIconButton(spritesheet.getSubimage(674, 103, 47, 38), this);
        rib.setPosition(10 + panelPosX + 40, 110 + panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectSpecies", this, 0);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(species == Player.SPECIES_PANDA)
            rib.setChecked(true);
        bg.add(rib);
        
        rib = new RadioIconButton(spritesheet.getSubimage(676, 147, 49, 38), this);
        rib.setPosition(150 + panelPosX + 40, 110 + panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectSpecies", this, 1);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(species == Player.SPECIES_REDPANDA)
            rib.setChecked(true);
        bg.add(rib);
        content.addGuiComponent(bg);
        
        tabPanelContainer.addTab("", spritesheet.getSubimage(590, 552, 68, 74), content);
        tabPanelContainer.setIconSize(1, 30, 32);
    }
    
    private void createGameSettings(TabsPanelsContainer tabPanelContainer, BufferedImage spritesheet, int panelPosX, int panelPosY, int tabHeight)
    {
        TabContent content = new TabContent(this);
        
        int difficulty = Integer.parseInt(Settings.getInstance().getConfigValue("difficulty"));
        
        ButtonGroup bg = new ButtonGroup(this);
        RadioIconButton rib = new RadioIconButton(spritesheet.getSubimage(285, 69, 17, 16), this);
        rib.setPosition(10 +panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectDifficulty", this, 0);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(difficulty == Level.DIFFICULTY_EASY)
            rib.setChecked(true);
        bg.add(rib);
        
        rib = new RadioIconButton(spritesheet.getSubimage(325, 69, 35, 16), this);
        rib.setPosition(140 + panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectDifficulty", this, 1);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(difficulty == Level.DIFFICULTY_MEDIUM)
            rib.setChecked(true);
        bg.add(rib);
        
        rib = new RadioIconButton(spritesheet.getSubimage(285, 89, 33, 32), this);
        rib.setPosition(270 + panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectDifficulty", this, 2);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(difficulty == Level.DIFFICULTY_HARD)
            rib.setChecked(true);
        bg.add(rib);
        
        rib = new RadioIconButton(spritesheet.getSubimage(326, 90, 33, 32), this);
        rib.setPosition(400 + panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectDifficulty", this, 3);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(612, 0, 124, 103));
        if(difficulty == Level.DIFFICULTY_HARDCORE)
            rib.setChecked(true);
        bg.add(rib);
        content.addGuiComponent(bg);
        
        tabPanelContainer.addTab("", spritesheet.getSubimage(480, 667, 94, 50), content);
        tabPanelContainer.setIconSize(2, 43, 22);
    }
    
    private void createLanguageSettings(TabsPanelsContainer tabPanelContainer, BufferedImage spritesheet, int panelPosX, int panelPosY, int tabHeight)
    {
        TabContent content = new TabContent(this);
        
        int lang = Integer.parseInt(Settings.getInstance().getConfigValue("lang"));
        
        ButtonGroup bg = new ButtonGroup(this);
        RadioIconButton rib = new RadioIconButton(spritesheet.getSubimage(238, 344, 42, 28), this);
        rib.setPosition(10 +panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectLanguage", this, 0);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(0, 69, 77, 63));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(76, 69, 77, 63));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(0, 69, 77, 63));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(788, 551, 79, 65));
        if(lang == 0)
            rib.setChecked(true);
        bg.add(rib);
        
        rib = new RadioIconButton(spritesheet.getSubimage(238, 372, 42, 28), this);
        rib.setPosition(150 +panelPosX + 40, panelPosY + 80 + tabHeight);
        rib.addCallback(GuiComponent.Status.CLICKED, "selectLanguage", this, 1);
        rib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(0, 69, 77, 63));
        rib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(76, 69, 77, 63));
        rib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(0, 69, 77, 63));
        rib.addApearance(GuiComponent.Status.CHECKED, spritesheet.getSubimage(788, 551, 79, 65));
        if(lang == 1)
            rib.setChecked(true);
        bg.add(rib);
        content.addGuiComponent(bg);
        
        tabPanelContainer.addTab("", spritesheet.getSubimage(588, 637, 74, 74), content);
        tabPanelContainer.setIconSize(3, 33, 33);
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
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        for(GuiComponent element : m_guiElements)
        {
            int[] pos = element.getPosition();
            
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount >= 1)
                {
                    element.onClick(mouseX, mouseY);
                }
                else if(!m_stateManager.getContext().m_inputsListener.mousePressed && element.getStatus() == GuiComponent.Status.CLICKED)
                {
                    element.onRelease();
                }
                
                if(element.getStatus() != GuiComponent.Status.NEUTRAL)
                {
                    continue;
                }

                element.onHover();
            }
            else if(element.getStatus() == GuiComponent.Status.FOCUSED)
            {
                element.onLeave();
            }
            else if(element.getStatus() == GuiComponent.Status.CLICKED)
            {
                element.onRelease();
            }
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        Screen screen = m_stateManager.getContext().m_screen;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        BufferedImage spritesheet = resourceManager.getSpritesheets("spritesheetGui2");
        
        int sWidth = screen.getContentPane().getWidth();
        int sHeight = screen.getContentPane().getHeight();
        double scale = screen.getScale();
        
        g.setColor(new Color(70, 70, 70, 150));
        g.fillRect(0, 0, sWidth, sHeight);
        
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, (int)(800 * scale), 60);
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 36.0f);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("pauseMsg");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, sWidth/2 - titlewidth/2, 75);
        
        for(GuiComponent element : m_guiElements)
        {
            element.render(g);
        }
    }
    
    /**
     * 
     */
    public void backToMenu()
    {
        m_stateManager.remove(StateType.PAUSED_SETTINGS);
        m_stateManager.switchTo(StateType.PAUSED);
    }
    
    /**
     * 
     */
    public void toggleFullscreen()
    {
        System.out.println("toggleFullscreen");
    }
    
    /**
     * 
     * @param width
     * @param height 
     */
    public void changeResolution(Integer width, Integer height)
    {
        System.out.println("changeResolution: " + width + ":" + height);
    }
    
    /**
     * 
     */
    public void setSoundVolume()
    {
        System.out.println("setSoundVolume");
    }
    
    /**
     * 
     */
    public void setMusicVolume()
    {
        System.out.println("setMusicVolume");
    }
    
    public void selectDifficulty(Integer difficulty)
    {
        System.out.println("selectDifficulty: " + difficulty);
    }
    
    public void selectSex(Integer sex)
    {
        System.out.println("selectSex: " + sex);
    }
    
    public void selectSpecies(Integer species)
    {
        System.out.println("selectSpecies: " + species);
    }
}
