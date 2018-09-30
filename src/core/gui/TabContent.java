package core.gui;

import java.awt.Graphics2D;

public class TabContent
{
    private String m_tabContent;
    
    public TabContent(String content)
    {
        m_tabContent = content;
    }
    
    public void render(Graphics2D g, int x, int y)
    {
        g.drawString(m_tabContent, x, y);
    }
}
