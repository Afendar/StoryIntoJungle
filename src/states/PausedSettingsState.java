package states;

import core.I18nManager;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.gui.GuiComponent;
import core.gui.IconButton;
import core.gui.TabContent;
import core.gui.TabsPanelsContainer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PausedSettingsState extends BaseState
{
    private ArrayList<GuiComponent> m_guiElements;
    
    public PausedSettingsState(StateManager stateManager)
    {
        super(stateManager);
        
        setTransparent(true);
    }

    @Override
    public void onCreate()
    {
        m_guiElements = new ArrayList<>();
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        ResourceManager ressourceManager = m_stateManager.getContext().m_resourceManager;
        BufferedImage spritesheet = ressourceManager.getSpritesheets("spritesheetGui2");
        
        IconButton ib = new IconButton(spritesheet.getSubimage(760, 151, 34, 34), this);
        ib.setPosition((3 * screenWidth / 4), screenHeight - 120);
        ib.addCallback(GuiComponent.Status.CLICKED, "backToMenu", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, spritesheet.getSubimage(491, 1, 120, 99));
        m_guiElements.add(ib);
        
        TabsPanelsContainer tabPanel = new TabsPanelsContainer(this);
        tabPanel.setPosition(100, 120);
        tabPanel.addTab("", spritesheet.getSubimage(475, 554, 110, 93), new TabContent("Lorem ipsum dolor sit amet"));
        tabPanel.setIconSize(0, 55, 46);
        tabPanel.addTab("", spritesheet.getSubimage(590, 552, 68, 74), new TabContent("Little Content"));
        tabPanel.setIconSize(1, 34, 37);
        tabPanel.addTab("", spritesheet.getSubimage(480, 667, 94, 50), new TabContent("Tab content 2"));
        tabPanel.setIconSize(2, 47, 25);
        tabPanel.addTab("", spritesheet.getSubimage(588, 637, 74, 74), new TabContent("Tab content 3"));
        tabPanel.setIconSize(3, 37, 37);
        m_guiElements.add(tabPanel);
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
        
        //int columnSize = (sWidth - 100)/5;
        
        /*
        g.drawImage(spritesheet.getSubimage(475, 554, 110, 93), columnSize + (columnSize - 55) / 2, 150, 55, 46, null);
        g.drawImage(spritesheet.getSubimage(590, 552, 68, 74), 2 * columnSize + (columnSize - 34) / 2, 150, 34, 37, null);
        g.drawImage(spritesheet.getSubimage(480, 667, 94, 50), 3 * columnSize + (columnSize - 47) / 2, 150, 47, 25, null);
        g.drawImage(spritesheet.getSubimage(588, 637, 74, 74), 4 * columnSize + (columnSize - 37) / 2, 150, 37, 37, null);*/
    }
    
    public void backToMenu()
    {
        m_stateManager.remove(StateType.PAUSED_SETTINGS);
        m_stateManager.switchTo(StateType.PAUSED);
    }
}
