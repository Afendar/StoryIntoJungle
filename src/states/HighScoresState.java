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
import ld34.profile.BestScores;
import org.json.simple.JSONArray;

public class HighScoresState extends BaseState
{
    private final ArrayList<GuiComponent> m_guiElements = new ArrayList<>();
    
    private JSONArray m_bestScores;
    
    public HighScoresState(StateManager stateManager)
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
        
        m_bestScores = BestScores.getInstance().getBestScores();
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
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        for(GuiComponent element : m_guiElements)
        {
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
    public void reloadLocales()
    {
        System.out.println("Reload locales");
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
        
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, screenWidth, 60);
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 36.0f);
         FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("highscores_title");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, screenWidth/2 - titlewidth/2, 75);
        
        font = font.deriveFont(Font.PLAIN, 22.0f);
        g.setFont(font);
        g.setColor(Color.BLACK);
        
        for(int i=0;i<m_bestScores.size();i++)
        {
            if(m_bestScores.get(i) instanceof JSONArray)
            {
                JSONArray array = (JSONArray) m_bestScores.get(i);
                if(array.size() == 2)
                {
                    String name = (String) array.get(0);
                    String score = (String) array.get(1);
                    g.drawString((i + 1) +". " + name + " : " + score, 200, (i * 50) + 170 );
                }
            }
        }
        
        for(GuiComponent element : m_guiElements)
        {
            element.render(g);
        }
        
        g.drawImage(resourceManager.getSpritesheets("foreground2"), 0, 0, screenWidth, screenHeight, null);
    }
    
    public void backToMain()
    {
        m_stateManager.switchTo(StateType.MAIN_MENU);
    }
}
