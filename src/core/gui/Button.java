package core.gui;

import audio.Sound;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Button extends GuiComponent
{   
    private static final String componentID = "Button";
    
    private boolean m_textCenter;
    private int m_paddingTop, m_paddingLeft;
    
    public Button()
    {
        this("");
    }
    
    public Button(String label)
    {
        super();
        
        m_label = label;
        m_textCenter = false;
        m_paddingTop = 0;
        m_paddingLeft = 0;
    }
    
    @Override
    public void update(double dt)
    {
    }
    
    @Override
    public void onHover()
    {
        new Thread(Sound.hover::play).start();
        setStatus(Status.FOCUSED);
    }
    
    @Override
    public void onClick()
    {
        new Thread(Sound.select::play).start();
        setStatus(Status.CLICKED);
    }
    
    @Override
    public void onRelease()
    {
        setStatus(Status.NEUTRAL);
    }
    
    @Override
    public void onLeave()
    {
        setStatus(Status.NEUTRAL);
    }
    
    public void setPadding(int top, int left)
    {
        m_paddingTop = top;
        m_paddingLeft = left;
    }
    
    public void setTextCenter(boolean center)
    {
        m_textCenter = center;
    }
    
    @Override
    public void render(Graphics2D g)
    {   
        if(m_appearances.get(m_status) != null)
        {
            g.drawImage(m_appearances.get(m_status), m_position.m_x, m_position.m_y, null);
        }
        
        g.setColor(m_color);
        g.setFont(m_font);
        FontMetrics fm = g.getFontMetrics(m_font);
        int labelHeight = fm.getHeight();
        int labelWidth = fm.stringWidth(m_label);
        g.drawString(
                m_label, 
                m_textCenter ? 
                        m_position.m_x + (m_width - labelWidth) / 2 + m_paddingLeft : 
                        m_position.m_x + m_paddingLeft, 
                m_textCenter ? 
                        (m_position.m_y + (m_height - labelHeight) / 2) + (labelHeight + m_paddingTop) / 2 : 
                        m_position.m_y + labelHeight + m_paddingTop);
    }
}
