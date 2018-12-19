package states;

import core.I18nManager;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.gui.GuiComponent;
import core.gui.IconButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EndState extends BaseState
{
    public int m_alpha;
    public static Color m_darkGreen = new Color(128, 0, 19);
    private final ArrayList<GuiComponent> m_guiElements = new ArrayList<>();
    
    public EndState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        BufferedImage spritesheet = resourceManager.getSpritesheets("spritesheetGui2");
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();

        IconButton b = new IconButton(spritesheet.getSubimage(243, 94, 42, 34), this);
        b.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(491, 1, 120, 99));
        b.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(370, 1, 120, 99));
        b.setPosition((3 * screenWidth / 4), screenHeight - 120);
        b.addCallback(GuiComponent.Status.CLICKED, "backToMain", this);
        
        m_guiElements.add(b);
        m_alpha = 255;
    }

    @Override
    public void onDestroy()
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
        
        m_guiElements.stream().map((element) ->
        {
            element.update(dt);
            return element;            
        }).forEachOrdered((element) ->
        {
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
                    return;
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
        });
    }

    @Override
    public void render(Graphics2D g)
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        g.drawImage(resourceManager.getSpritesheets("background"), 0, 0, screenWidth, screenHeight, null);
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 22.0f);
        
        g.setFont(font);
        g.setColor(Color.BLACK);
        FontMetrics metrics = g.getFontMetrics(font);
        
        String text1 = i18nManager.trans("end_text1");
        int text1Width = metrics.stringWidth(text1);
        g.drawString(text1, screenWidth/2 - text1Width/2, screenHeight * 240 / Screen.RES_1X_HEIGHT);
        
        String text2 = i18nManager.trans("end_text2");
        int text2Width = metrics.stringWidth(text2);
        g.drawString(text2, screenWidth/2 - text2Width/2, screenHeight * 300 / Screen.RES_1X_HEIGHT);
        
        String text3 = i18nManager.trans("end_text3");
        int text3Width = metrics.stringWidth(text3);
        g.drawString(text3, screenWidth/2 - text3Width/2, screenHeight * 360 / Screen.RES_1X_HEIGHT);
        
        if(m_alpha > 0)
        {
            m_alpha--;
        }
        else
        {
            m_guiElements.forEach((element) ->
            {
                element.render(g);
            });
        }
        
        Font fontS = font.deriveFont(17.0f);
        g.setFont(fontS);
        g.setColor(Color.BLACK);
        metrics = g.getFontMetrics(fontS);
        String text4 = i18nManager.trans("end_text4");
        int text4Width = metrics.stringWidth(text4);
        g.drawString(text4, screenWidth/3 - text4Width/2 + 40, screenHeight * 490 / Screen.RES_1X_HEIGHT);
        
        g.drawImage(resourceManager.getSpritesheets("foreground3"), 0, 0, screenWidth, screenHeight, null);
        
        g.setColor(new Color(0, 0, 0, m_alpha));
        g.fillRect(0, 0, screenWidth, screenWidth);
    }
    
    /**
     * 
     */
    public void backToMain()
    {
        m_stateManager.switchTo(StateType.MAIN_MENU);
    }
}
