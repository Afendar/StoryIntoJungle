package core.gui;

import core.InputsListeners;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class GuiConsole
{
    private boolean m_visible;
    private int m_x;
    private int m_y;
    private int m_width;
    private int m_height;
    
    private Rectangle CONSOLE_BOX;
    private Rectangle MESSAGE_BOX;
    
    private String m_command;
    
    private Font m_font;
    
    private Color BACKGROUND_COLOR = new Color(0, 0, 0, 100);
    private Color MESSAGE_BOX_COLOR = new Color(0, 0, 0, 150);
    private Color TEXT_COLOR = new Color(255, 255, 255, 170);
    
    private InputsListeners m_inputListeners;
    
    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @param inputsListeners 
     */
    public GuiConsole(int x, int y, int width, int height, InputsListeners inputsListeners)
    {
        m_visible = false;
        m_x = x;
        m_y = y;
        m_width = width;
        m_height = height;
        
        m_command = "";
        
        m_font = new Font("Arial", Font.PLAIN, 18);
        
        CONSOLE_BOX = new Rectangle(m_x, m_y, m_width, m_height - 30);
        MESSAGE_BOX = new Rectangle(m_x, m_y + m_height - 30, m_width, 30);
        
        m_inputListeners = inputsListeners;
    }
    
    /**
     * 
     * @param dt 
     */
    public void update(double dt)
    {
        if(!m_visible)
        {
            return;
        }
        
        if(m_inputListeners.e != null)
        {
            KeyEvent e = m_inputListeners.e;
            m_command += e.getKeyChar();
        }
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics2D g)
    {
        if(!m_visible)
        {
            return;
        }
        
        g.setColor(BACKGROUND_COLOR);
        g.fill(CONSOLE_BOX);
        g.setColor(MESSAGE_BOX_COLOR);
        g.fill(MESSAGE_BOX);
        
        g.setColor(TEXT_COLOR);
        g.setFont(m_font);
        g.drawString(m_command, m_x + 5, m_y + m_height - 30);
    }
    
    /**
     * 
     */
    public void toggleVisible()
    {
        m_visible = !m_visible;
    }
}
