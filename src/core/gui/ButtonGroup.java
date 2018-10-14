package core.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import states.BaseState;

public class ButtonGroup extends GuiComponent
{
    protected ArrayList<Button> m_buttons;
    protected int m_mouseX, m_mouseY;
    
    public ButtonGroup(BaseState owner)
    {
        super(owner);
        m_buttons = new ArrayList<>();
    }
    
    public void add(Button b)
    {
        if(b == null)
        {
            return;
        }
        
        m_buttons.add(b);
        b.setGroup(this);
    }
    
    public void remove(Button b)
    {
        if(b == null)
        {
            return;
        }
        
        m_buttons.remove(b);
        b.setGroup(null);
    }
    
    public ArrayList<Button> getButtons()
    {
        return m_buttons;
    }
    
    public int size()
    {
        return m_buttons.size();
    }
    
    public Button getButton(int index)
    {
        return m_buttons.get(index);
    }
    
    @Override
    public boolean isInside(int mouseX, int mouseY)
    {
        for(Button b : m_buttons)
        {
            if(b.isInside(mouseX, mouseY))
            {
                m_mouseX = mouseX;
                m_mouseY = mouseY;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onRelease(){}
    
    @Override
    public void onHover(){}
    
    @Override
    public void onLeave(){}
    
    @Override
    public void onClick(int mouseX, int mouseY)
    {
        m_buttons.forEach((b) ->
        {
            if(b.isInside(m_mouseX, m_mouseY))
            {
                b.onClick(mouseX, mouseY);
            }
            else
            {
                if(b instanceof RadioButton)
                {
                    ((RadioButton)b).setChecked(false);
                }
                b.setStatus(Status.NEUTRAL);
            }
        });
    }
    
    @Override
    public void render(Graphics2D g)
    {
        m_buttons.forEach((b) ->
        {
            b.render(g);
        });
    }

    @Override
    public void update(double dt)
    {
        
    }
}
