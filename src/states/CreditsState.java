package states;

import core.Defines;
import core.I18nManager;
import core.ResourceManager;
import core.StateManager;
import core.StateType;
import core.gui.GuiComponent;
import core.gui.IconButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CreditsState extends BaseState
{   
    private final ArrayList<GuiComponent> m_guiElements = new ArrayList<>();
    
    public CreditsState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        BufferedImage spritesheet = resourceManager.getSpritesheets("spritesheetGui2");
        
        IconButton b = new IconButton(spritesheet.getSubimage(243, 94, 42, 34));
        b.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        b.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(370, 1, 120, 99));
        b.setPosition((3*Defines.SCREEN_WIDTH/4), Defines.SCREEN_HEIGHT - 120);
        b.addCallback(GuiComponent.Status.CLICKED, "backToMain", this);
        
        m_guiElements.add(b);
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
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount >= 1)
                {
                    element.onClick();
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
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(resourceManager.getSpritesheets("background"), 0, 0, null);
        
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, Defines.SCREEN_WIDTH, 60);
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 36.0f);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("credits_title");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, Defines.SCREEN_WIDTH/2 - titlewidth/2, 75);
        
        font = font.deriveFont(Font.PLAIN, 22.0f);
        g.setFont(font);
        metrics = g.getFontMetrics(font);
        
        String text1 = i18nManager.trans("credits_text1");
        int text1Width = metrics.stringWidth(text1);
        g.drawString(text1, Defines.SCREEN_WIDTH/2 - text1Width/2, 200);
        
        String text2 = i18nManager.trans("credits_text2");
        int text2Width = metrics.stringWidth(text2);
        g.drawString(text2, Defines.SCREEN_WIDTH/2 - text2Width/2, 250);
        
        String text3 = i18nManager.trans("credits_text3");
        int text3Width = metrics.stringWidth(text3);
        g.drawString(text3, Defines.SCREEN_WIDTH/2 - text3Width/2, 300);
        
        for(GuiComponent element : m_guiElements)
        {
            element.render(g);
        }
        
        g.drawImage(resourceManager.getSpritesheets("foreground2"), 0, 0, null);
    }
    
    public void backToMain()
    {
        m_stateManager.switchTo(StateType.MAIN_MENU);
    }
}