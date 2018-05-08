package core;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Screen class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Screen extends JFrame 
{   
    private GraphicsDevice m_device;
    
    private DisplayMode m_displayMode;
    private DisplayMode m_oldDisplayMode;
    
    private DisplayMode[] m_availableModes;
    
    protected boolean m_fullscreen;
    protected double m_oldWidth, m_newWidth;
    protected double m_oldHeight, m_newHeight;
    
    protected Game m_game;
    
    /**
     * 
     */
    public Screen(Game game)
    {
        super();
        
        m_device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        m_oldDisplayMode = m_device.getDisplayMode();
        DisplayMode[] suportedDisplayModes = m_device.getDisplayModes();
        
        DisplayMode[] desiredModes = {
            new DisplayMode(800, 600, 32, 60),
            new DisplayMode(1024, 768, 32, 60),
            new DisplayMode(1280, 960, 32, 60)
        };
        
        for (DisplayMode dm : suportedDisplayModes)
        {
            for(DisplayMode desiredMode : desiredModes)
            {
                if(dm.equals(desiredMode))
                {
                    
                }
            }
        }
        
        m_oldWidth = Defines.SCREEN_WIDTH;
        m_newWidth = Defines.SCREEN_WIDTH;
        m_oldHeight = Defines.SCREEN_HEIGHT;
        m_newHeight = Defines.SCREEN_HEIGHT;
        
        m_game = game;
        m_displayMode = new DisplayMode(Defines.SCREEN_WIDTH, Defines.SCREEN_HEIGHT, 32, 60);
        
        init();
    }
    
    /**
     * 
     */
    private void init()
    {       
        setTitle("Story Into Jungle - v" + Defines.VERSION);
        add(m_game);
        getContentPane().setPreferredSize(new Dimension(Defines.SCREEN_WIDTH, Defines.SCREEN_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFullscreen(false);
        setLayout(null);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(getClass().getResource("/cursor.png")).getImage(), new Point(0,0), "cursorSIJ"));
        URL iconUrl = getClass().getResource("/storyintojungle.png");
        ImageIcon icon = new ImageIcon(iconUrl);
        setIconImage(icon.getImage());
    }
    
    public void setFullscreen(boolean fullscreen)
    {
        if(m_fullscreen == fullscreen)
        {
            return;
        }
        
        m_fullscreen = fullscreen;
        if(!m_fullscreen)
        {
            //windowed mode
            m_device.setDisplayMode(m_oldDisplayMode);
            setVisible(false);
            dispose();
            setUndecorated(false);
            m_device.setFullScreenWindow(null);
            setSize(getWidth(), getHeight());
            setLocationRelativeTo(null);
            setVisible(true);
        }
        else
        {
            //fullscreen mode
            setVisible(false);
            dispose();
            setExtendedState(JFrame.MAXIMIZED_BOTH); 
            setUndecorated(true);
            m_device.setFullScreenWindow(this);
            m_device.setDisplayMode(m_displayMode);
            setVisible(true);
        }
        repaint();
    }
    
    public boolean isFullscreen()
    {
        return m_fullscreen;
    }
    
    public void setResolution(int width, int height)
    {
        m_oldWidth = getWidth();
        m_newWidth = width;
        
        m_game.setMinimumSize(new Dimension(width, height));
        m_game.setMaximumSize(new Dimension(width, height));
        m_game.setPreferredSize(new Dimension(width, height));
        m_game.setSize(new Dimension(width, height));
        getContentPane().setPreferredSize(new Dimension(width, height));
        m_displayMode = new DisplayMode(width, height, 32, 60);
        pack();
        revalidate();        
        repaint();
    }
    
    public double getScale()
    {
        return m_newWidth / 800.0;
    }
    
    public double getRatio()
    {
        if(m_newWidth > m_oldWidth)
        {
            System.out.println(Math.floor((m_newWidth / m_oldWidth) * 10.0) / 10.0);
            return Math.floor((m_newWidth / m_oldWidth) * 10.0) / 10.0;
        }
        System.out.println(Math.round((m_newWidth / m_oldWidth) * 10.0) / 10.0);
        return Math.round((m_newWidth / m_oldWidth) * 10.0) / 10.0;
    }
}
