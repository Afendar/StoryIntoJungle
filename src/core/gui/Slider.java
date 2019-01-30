package core.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import states.BaseState;

public class Slider extends GuiComponent
{
    private static final String COMPONENT_ID = "Slider";

    private int m_value;
    private int[] m_moveMouseLast;

    public Slider(BaseState owner)
    {
        super(owner);

        m_value = 100;
        m_moveMouseLast = new int[2];
    }

    @Override
    public void update(double dt)
    {
        if (getStatus() != Status.CLICKED)
        {
            return;
        }

        int mouseX = m_owner.getStateManager().getContext().m_inputsListener.mouseX;
        int mouseY = m_owner.getStateManager().getContext().m_inputsListener.mouseY;
        int[] pos =
        {
            mouseX, mouseY
        };
        if (Arrays.equals(m_moveMouseLast, pos))
        {
            return;
        }

        m_moveMouseLast = pos;
        setValue((int) ((mouseX - m_position.m_x - 25) / 2.4));
    }

    @Override
    public void onHover()
    {
        setStatus(Status.FOCUSED);
    }

    @Override
    public void onClick(int mouseX, int mouseY)
    {
        setStatus(Status.CLICKED);
        m_moveMouseLast[0] = mouseX;
        m_moveMouseLast[1] = mouseY;
        setValue((int) ((mouseX - m_position.m_x - 25) / 2.4));
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

    @Override
    public void render(Graphics2D g)
    {
        int red = 255;
        int green = 0;
        for (int i = 0; i < 255; i++)
        {
            g.setColor(new Color(red, green, 0));
            g.fillRect((int) (m_position.m_x + 25 + i), m_position.m_y + 28, 1, 19);
            red--;
            green++;
        }

        if (m_appearances.get(m_status) != null)
        {
            g.drawImage(m_appearances.get(m_status), m_position.m_x, m_position.m_y, null);
        }

        g.setColor(Color.BLACK);
        int posBar = (int) (m_position.m_x + 25 + (2.4 * m_value));
        g.fillRect(posBar, m_position.m_y + 31, 9, 14);
    }

    public void setValue(int val)
    {
        if (val > 100 || val < 0)
        {
            return;
        }

        m_value = val;
    }

    public int getValue()
    {
        return m_value;
    }
}
