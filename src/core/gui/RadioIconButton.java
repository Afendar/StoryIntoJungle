package core.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import states.BaseState;

public class RadioIconButton extends RadioButton
{
    private static final String COMPONENT_ID = "RadioIconButton";

    private final BufferedImage m_icon;

    public RadioIconButton(BufferedImage icon, BaseState owner)
    {
        super("", owner);
        m_icon = icon;
    }

    @Override
    public void render(Graphics2D g)
    {
        if (m_appearances.get(m_status) != null)
        {
            g.drawImage(m_appearances.get(m_status), m_position.m_x, m_position.m_y, null);
        }

        g.drawImage(
                m_icon,
                m_position.m_x + (m_width - m_icon.getWidth()) / 2,
                m_position.m_y + (m_height - m_icon.getHeight()) / 2,
                null
        );
    }
}
