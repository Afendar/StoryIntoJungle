package states;

import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.gui.GuiComponent;
import core.gui.TabContent;
import core.gui.TabsPanelsContainer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TestState extends BaseState
{
    private TabsPanelsContainer m_tabPanel;
    
    public TestState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        ResourceManager ressourceManager = m_stateManager.getContext().m_resourceManager;
        BufferedImage spritesheet = ressourceManager.getSpritesheets("spritesheetGui2");
        Font font = ressourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 14.0f);
        
        m_tabPanel = new TabsPanelsContainer(this);
        m_tabPanel.setPosition(100, 150);
        m_tabPanel.setFont(font);
        m_tabPanel.addTab("", spritesheet.getSubimage(475, 554, 110, 93), new TabContent("Lorem ipsum dolor sit amet"));
        m_tabPanel.setIconSize(0, 55, 46);
        m_tabPanel.addTab("", spritesheet.getSubimage(590, 552, 68, 74), new TabContent("Little Content"));
        m_tabPanel.setIconSize(1, 34, 37);
        m_tabPanel.addTab("", spritesheet.getSubimage(480, 667, 94, 50), new TabContent("Tab content 2"));
        m_tabPanel.setIconSize(2, 47, 25);
        m_tabPanel.addTab("", spritesheet.getSubimage(588, 637, 74, 74), new TabContent("Tab content 3"));
        m_tabPanel.setIconSize(3, 37, 37);
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
        
        m_tabPanel.update(dt);
        
        if(m_tabPanel.isInside(mouseX, mouseY))
        {
            if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount >= 1)
            {
                m_tabPanel.onClick(mouseX, mouseY);
            }
            else if(!m_stateManager.getContext().m_inputsListener.mousePressed && m_tabPanel.getStatus() == GuiComponent.Status.CLICKED)
            {
                m_tabPanel.onRelease();
            }

            if(m_tabPanel.getStatus() != GuiComponent.Status.NEUTRAL)
            {
                return;
            }

            m_tabPanel.onHover();
        }
        else if(m_tabPanel.getStatus() == GuiComponent.Status.FOCUSED)
        {
            m_tabPanel.onLeave();
        }
        else if(m_tabPanel.getStatus() == GuiComponent.Status.CLICKED)
        {
            m_tabPanel.onRelease();
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenWidth, screenHeight);
        
        m_tabPanel.render(g);
    }
}
