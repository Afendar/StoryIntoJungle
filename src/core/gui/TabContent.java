package core.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import states.BaseState;

public class TabContent
{
    
    private BaseState m_owner;
    
    /**
     * 
     */
    private final ArrayList<GuiComponent> m_guiComponents;
    
    /**
     * 
     * @param owner 
     */
    public TabContent(BaseState owner)
    {
        m_owner = owner;
        m_guiComponents = new ArrayList<>();
    }
    
    /**
     * 
     * @param guiComponent 
     */
    public void addGuiComponent(GuiComponent guiComponent)
    {
        m_guiComponents.add(guiComponent);
    }
    
    /**
     * 
     * @param dt 
     */
    public void update(double dt)
    {
        int mouseX = m_owner.getStateManager().getContext().m_inputsListener.mouseX;
        int mouseY = m_owner.getStateManager().getContext().m_inputsListener.mouseY;
        
        for(GuiComponent element : m_guiComponents)
        {
            int[] pos = element.getPosition();
            
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_owner.getStateManager().getContext().m_inputsListener.mousePressed && m_owner.getStateManager().getContext().m_inputsListener.mouseClickCount >= 1)
                {
                    element.onClick(mouseX, mouseY);
                }
                else if(!m_owner.getStateManager().getContext().m_inputsListener.mousePressed && element.getStatus() == GuiComponent.Status.CLICKED)
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
    
    /**
     * 
     * @param g
     */
    public void render(Graphics2D g)
    {
        for(GuiComponent gc : m_guiComponents)
        {
            gc.render(g);
        }
    }
}
