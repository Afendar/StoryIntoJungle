package core.gui;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import states.BaseState;

public class Label extends GuiComponent
{
    public Label(String text, BaseState owner)
    {
        super(owner);
        m_label = text;
    }

    @Override
    public void update(double dt)
    {

    }

    @Override
    public void render(Graphics2D g)
    {
        g.setColor(m_color);
        g.setFont(m_font);
        FontMetrics fm = g.getFontMetrics(m_font);

        int labelHeight = fm.getHeight();
        g.drawString(m_label, m_position.m_x, m_position.m_y + labelHeight);
    }
}
