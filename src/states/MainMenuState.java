package states;

import core.Defines;
import core.I18nManager;
import core.ResourceManager;
import core.StateManager;
import core.StateType;
import core.gui.Button;
import core.gui.GuiComponent;
import core.gui.IconButton;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import ld34.profile.Save;
import particles.Leaf;

public class MainMenuState extends BaseState
{
    public BufferedImage m_logo;
    private final ArrayList<Leaf> m_leavesList = new ArrayList<>(5);
    private final ArrayList<GuiComponent> m_guiElements = new ArrayList<>();
    
    /**
     * 
     * @param stateManager 
     */
    public MainMenuState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        System.out.println("Create main menu");
        
        ResourceManager ressourceManager = m_stateManager.getContext().m_resourceManager;
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        
        BufferedImage spritesheet = ressourceManager.getSpritesheets("spritesheetGui2");
        m_logo = spritesheet.getSubimage(310, 347, 590, 200);
        
        BufferedImage[] icons = {
            spritesheet.getSubimage(150, 68, 25, 24),
            spritesheet.getSubimage(175, 69, 35, 23),
            spritesheet.getSubimage(154, 92, 16, 24)
        };
        
        
        
        Integer[][] coords;
        String[] labels;
        String[] methodsToInvoke;
        if(Save.getInstance().hasSave())
        {
            coords = new Integer[][]{
                {Defines.SCREEN_WIDTH/2 - 117 - (15*30), 211},
                {Defines.SCREEN_WIDTH/2 - 117 - (17*30), 311},
                {Defines.SCREEN_WIDTH/2 - 117 - (19*30), 411},
                {16, 518},
                {107, 518},
                {703, 518}
            };
            
            labels = new String[]{
                i18nManager.trans("newGame"),
                i18nManager.trans("loadGame"),
                i18nManager.trans("quit")
            };
            
            methodsToInvoke = new String[]{
                "play",
                "load",
                "quit",
                "settings",
                "highScores",
                "credits"
            };
        }
        else
        {
            coords = new Integer[][]{
                {Defines.SCREEN_WIDTH/2 - 107 - 15*30, 235},
                {Defines.SCREEN_WIDTH/2 - 107 - 19*30, 355},
                {16, 518},
                {107, 518},
                {703, 518}
            };
            
            labels = new String[]{
                i18nManager.trans("newGame"),
                i18nManager.trans("quit")
            };
            
            methodsToInvoke = new String[]{
                "play",
                "quit",
                "settings",
                "highScores",
                "credits"
            };
        }
        
        Font font = ressourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 24.0f);

        for(int i = 0 ; i < coords.length ; i++)
        {
            Button b;
            if(i < 3)
            {
                b = new Button(labels[i]);
                b.setFont(font);
                b.setPadding(4, 0);
                b.setTextCenter(true);            
                b.addApearance(
                        GuiComponent.Status.NEUTRAL, 
                        i % 2 == 0 ? spritesheet.getSubimage(0, 133, 234, 99) : horizontalflip(spritesheet.getSubimage(0, 132, 234, 99)));
                b.addApearance(
                        GuiComponent.Status.FOCUSED, 
                        i % 2 == 0 ? spritesheet.getSubimage(0, 232, 234, 99) : horizontalflip(spritesheet.getSubimage(0, 232, 234, 99)));
            }
            else
            {
                b = new IconButton(icons[i - 3]);
                b.addApearance(GuiComponent.Status.NEUTRAL, spritesheet.getSubimage(0, 69, 76, 63));
                b.addApearance(GuiComponent.Status.FOCUSED, spritesheet.getSubimage(76, 69, 76, 63));
            }
            
            b.setPosition(coords[i][0], coords[i][1]);
            b.addCallback(GuiComponent.Status.CLICKED, methodsToInvoke[i], this);
            
            m_guiElements.add(b);
        }
        
        for(int i = 0;i<5;i++){
            m_leavesList.add(new Leaf(5, 0, 0, Defines.SCREEN_WIDTH, Defines.SCREEN_HEIGHT));
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
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        String[] labels;
        if(Save.getInstance().hasSave())
        {
            labels = new String[]{
                i18nManager.trans("newGame"),
                i18nManager.trans("loadGame"),
                i18nManager.trans("quit")
            };
        }
        else
        {
            labels = new String[]{
                i18nManager.trans("newGame"),
                i18nManager.trans("quit")
            };
        }

        for(int i = 0 ; i < labels.length ; i++)
        {
            m_guiElements.get(i).setLabel(labels[i]);
        }
    }
    
    @Override
    public void update(double dt)
    {
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        int index = 0;
        
        for(GuiComponent element : m_guiElements)
        {
            int[] pos = element.getPosition();
            if(index < 3 && pos[0] < (Defines.SCREEN_WIDTH - element.getWidth()) / 2)
            {
                element.setPosition(pos[0] + 30, pos[1]);
            }
            index++;
            
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
        
        for(Leaf leaf : m_leavesList)
        {
            if(!leaf.isGenStartX())
            {
                leaf.genRandStartX();
            }
            leaf.update(dt);
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(m_stateManager.getContext().m_resourceManager.getSpritesheets("background"), 0, 0, null);

        for (Leaf leaf : m_leavesList)
        {
            leaf.render(g);
        }
        
        g.drawImage(m_logo, 99, 20, null);
        g.drawImage(m_stateManager.getContext().m_resourceManager.getSpritesheets("foreground"), 0, 0, null);
        
        for(GuiComponent element : m_guiElements)
        {
            element.render(g);
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

    public void play()
    {
        m_stateManager.switchTo(StateType.GAME);
    }

    public void load()
    {
        m_stateManager.switchTo(StateType.SAVES);
    }

    public void quit()
    {
        System.exit(0);
    }

    public void settings()
    {
        m_stateManager.switchTo(StateType.SETTINGS);
    }

    public void highScores()
    {
        m_stateManager.switchTo(StateType.HIGHT_SCORES);
    }

    public void credits()
    {
        m_stateManager.switchTo(StateType.CREDITS);
    }
}
