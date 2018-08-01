package states;

import core.I18nManager;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.gui.Button;
import core.gui.GuiComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PausedState extends BaseState
{
    private ArrayList<GuiComponent> m_guiElements;
    
    public PausedState(StateManager stateManager)
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
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        BufferedImage spritesheet = ressourceManager.getSpritesheets("spritesheetGui2");
        
        Integer[][] coords = {
            {screenWidth/2 - 117, (screenHeight / 6)},
            {screenWidth/2 - 117, 2 * (screenHeight / 6)},
            {screenWidth/2 - 117, 3 * (screenHeight / 6)},
            {screenWidth/2 - 117, 4 * (screenHeight / 6)}
        };
            
        String[] labels = {
            i18nManager.trans("continue"),
            i18nManager.trans("save"),
            i18nManager.trans("settings"),
            i18nManager.trans("backToMain")
        };

        String[] methodsToInvoke = new String[]{
            "dispose",
            "save",
            "settings",
            "backToMain"
        };
        
        Font font = ressourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 24.0f);
        
        for(int i = 0 ; i < 4 ; i++)
        {
            Button b = new Button(labels[i], this);
            b.setFont(font);
            b.setPadding(4, 0);
            b.setTextCenter(true);
            b.addApearance(
                GuiComponent.Status.NEUTRAL, 
                i % 2 == 0 ? spritesheet.getSubimage(0, 133, 234, 99) : horizontalflip(spritesheet.getSubimage(0, 132, 234, 99)));
            b.addApearance(
                GuiComponent.Status.FOCUSED, 
                i % 2 == 0 ? spritesheet.getSubimage(0, 232, 234, 99) : horizontalflip(spritesheet.getSubimage(0, 232, 234, 99)));
            b.setPosition(coords[i][0], coords[i][1]);
            b.addCallback(GuiComponent.Status.CLICKED, methodsToInvoke[i], this);
            m_guiElements.add(b);
        }
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
        
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        g.setColor(new Color(127, 127, 127, 150));
        g.fillRect(0, 0, screenWidth, screenHeight);
        
        for(GuiComponent element : m_guiElements)
        {
            element.render(g);
        }
    }
    
    public void dispose()
    {
        m_stateManager.switchTo(StateType.GAME);
    }
    
    public void save()
    {
        GameState gs = (GameState)m_stateManager.getState(StateType.GAME);
        if(gs == null)
        {
            System.out.println("game scene not found");
            return;
        }
        
        if(gs.save())
        {
            System.out.println("Save completed");
        }
        else
        {
            System.out.println("Error during save");
        }
    }
    
    public void settings()
    {
        
    }
    
    public void backToMain()
    {
        m_stateManager.switchTo(StateType.MAIN_MENU);
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
