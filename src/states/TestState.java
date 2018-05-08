package states;

import core.StateManager;
import core.gui.ButtonGroup;
import core.gui.CheckBox;
import core.gui.GuiComponent;
import core.gui.RadioButton;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class TestState extends BaseState
{
    private ArrayList<GuiComponent> m_guiElements;
    private BufferedImage m_spritesheetGui2;
    
    public TestState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        try
        {
            URL url = getClass().getResource("/gui2.png");
            m_spritesheetGui2 = ImageIO.read(url);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        m_guiElements = new ArrayList<>();
        
        CheckBox cb = new CheckBox("Mon super Label");
        cb.setPosition(150, 150);
        cb.addApearance(GuiComponent.Status.NEUTRAL, m_spritesheetGui2.getSubimage(320, 586, 41, 33));
        cb.addApearance(GuiComponent.Status.FOCUSED, m_spritesheetGui2.getSubimage(320, 586, 41, 33));
        cb.addApearance(GuiComponent.Status.CHECKED, m_spritesheetGui2.getSubimage(320, 620, 41, 33));
        m_guiElements.add(cb);
        
        RadioButton rb = new RadioButton("Choix 1");
        rb.setPosition(150, 200);
        rb.addApearance(GuiComponent.Status.NEUTRAL, m_spritesheetGui2.getSubimage(365, 589, 29, 29));
        rb.addApearance(GuiComponent.Status.FOCUSED, m_spritesheetGui2.getSubimage(365, 589, 29, 29));
        rb.addApearance(GuiComponent.Status.CHECKED, m_spritesheetGui2.getSubimage(365, 624, 29, 29));
        
        RadioButton rb2 = new RadioButton("Choix 2");
        rb2.setPosition(150, 250);
        rb2.addApearance(GuiComponent.Status.NEUTRAL, m_spritesheetGui2.getSubimage(365, 589, 29, 29));
        rb2.addApearance(GuiComponent.Status.FOCUSED, m_spritesheetGui2.getSubimage(365, 589, 29, 29));
        rb2.addApearance(GuiComponent.Status.CHECKED, m_spritesheetGui2.getSubimage(365, 624, 29, 29));
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rb);
        bg.add(rb2);
        
        m_guiElements.add(bg);
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
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);
        
        for(GuiComponent gc : m_guiElements)
        {
            gc.render(g);
        }
    }
}
