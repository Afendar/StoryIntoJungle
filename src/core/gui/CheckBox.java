package core.gui;

import audio.Sound;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import states.BaseState;

/**
 * 
 */
public class CheckBox extends Button
{
    private static final String COMPONENT_ID = "CheckBox";

    protected boolean m_checked;

    /**
     * 
     * @param owner 
     */
    public CheckBox(BaseState owner)
    {
        this("", owner);
    }
    
    /**
     * 
     * @param label
     * @param owner 
     */
    public CheckBox(String label, BaseState owner)
    {
        super(label, owner);
        setChecked(false);
    }

    /**
     * 
     * @param checked 
     */
    public void setChecked(boolean checked)
    {
        setStatus(checked ? Status.CHECKED : Status.NEUTRAL);
        m_checked = checked;
    }

    public boolean isChecked()
    {
        return m_checked;
    }

    @Override
    public void update(double dt)
    {
    }

    @Override
    public void onClick(int mouseX, int mouseY)
    {
        Sound select = m_owner.getStateManager().getContext().m_resourceManager.getSound("select");
        new Thread(select::play).start();
        setStatus(Status.CLICKED);
        setChecked(!isChecked());
    }

    @Override
    public void onHover()
    {
        setStatus(Status.FOCUSED);
    }

    @Override
    public void onRelease()
    {
        if (isChecked())
        {
            setStatus(Status.CHECKED);
            return;
        }
        setStatus(Status.NEUTRAL);
    }

    @Override
    public void onLeave()
    {
        if (isChecked())
        {
            setStatus(Status.CHECKED);
            return;
        }
        setStatus(Status.NEUTRAL);
    }

    @Override
    public void render(Graphics2D g)
    {
        g.setColor(Color.BLACK);
        g.setFont(m_font);
        int w = 0;
        int h = 0;
        if (m_appearances.get(m_status) != null)
        {
            w = m_appearances.get(m_status).getWidth();
            h = m_appearances.get(m_status).getHeight();
            g.drawImage(m_appearances.get(m_status), m_position.m_x, m_position.m_y, null);
        }

        FontMetrics fm = g.getFontMetrics(m_font);
        g.drawString(m_label, m_position.m_x + w + 10, m_position.m_y + (fm.getAscent() + h) / 2);
    }
}
