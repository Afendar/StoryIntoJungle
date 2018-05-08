package core.gui;

import java.awt.image.BufferedImage;

public class TabItem
{
    private String m_label;
    private BufferedImage m_icon;
    
    public TabItem(String label, BufferedImage icon)
    {
        m_label = label;
        m_icon = icon;
    }
}
