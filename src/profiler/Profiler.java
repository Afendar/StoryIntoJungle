package profiler;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import core.Defines;
import core.Game;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Profiler class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Profiler
{
    private String[] m_labels;
    private Map<String, String> m_data;
    private boolean m_visible;
    private Game m_game;
    private Font m_fontD;
    private Map<Object, String[]> m_debugObjects;
    private Logger m_logger;
    
    private static Profiler m_instance = null;
    
    /**
     * 
     * @return 
     */
    public static Profiler getInstance()
    {
        if(m_instance == null)
        {
            m_instance = new Profiler();
        }
        return m_instance;
    }
    
    /**
     * 
     */
    private Profiler()
    {
        m_data = new HashMap<>();
        m_visible = false;
        
        try
        {
            URL url = getClass().getResource("/fonts/arial.ttf");
            m_fontD = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_fontD = m_fontD.deriveFont(Font.PLAIN, 18.0f);
        }
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }
        
        m_debugObjects = new HashMap<>();
    }
    
    /**
     * 
     * @param game 
     * @param logger 
     */
    public void init(Game game, Logger logger)
    {
        m_game = game;
        m_logger = logger;
    }
    
    public void addObjectToDebug(Object o, String[] methodsName)
    {
        m_debugObjects.put(o, methodsName);
    }
    
    /**
     * 
     * @param frames
     * @param memory 
     */
    public void update(String frames, String memory)
    {
        m_data.put("fps", frames);
        m_data.put("memory", memory);
        
        m_debugObjects.entrySet().forEach((e) ->
        {
            String[] methods = e.getValue();
            Object o = e.getKey();
            for(String methodName : methods)
            {
                try
                {
                    Method m = o.getClass().getMethod(methodName);
                    Class<?> enclosingClass = o.getClass().getEnclosingClass();
                    String className = o.getClass().getSimpleName();
                    m_data.put(className + "." + methodName.replace("get", "").toLowerCase(), m.invoke(o).toString());
                }
                catch (NoSuchMethodException | SecurityException ex)
                {
                    m_logger.log(Level.WARNING, ex.getMessage());
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
                {
                    m_logger.log(Level.SEVERE, ex.getMessage());
                }
            }
        });
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics g)
    {
        renderGlobalDebug(g);
    }
    
    /**
     * 
     * @param g 
     */
    public void renderGlobalDebug(Graphics g)
    {
        FontMetrics fm = g.getFontMetrics(m_fontD);
        g.setFont(m_fontD);
        
        /*String text = "FPS : ";
        text += m_data.get("fps");
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 30 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 30);
        
        text = "Memory : " + m_data.get("memory") + "Mo";
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 57 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 60);*/
        
        String text = "Java : " + System.getProperty("java.version") + "  x" + System.getProperty("sun.arch.data.model") + " bit";
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(Defines.DEFAULT_SCREEN_WIDTH - (int)rect.getWidth() - 40, 30 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, Defines.DEFAULT_SCREEN_WIDTH - (int)rect.getWidth() - 30, 30);
                
        text = "Story Into Jungle : v" + Defines.VERSION;
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(Defines.DEFAULT_SCREEN_WIDTH - (int)rect.getWidth() - 40, 60 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, Defines.DEFAULT_SCREEN_WIDTH - (int)rect.getWidth() - 30, 60);
        
        int i = 0;
        for (Map.Entry<String, String> e : m_data.entrySet())
        {
            text = e.getKey() + ": " + e.getValue();
            rect = fm.getStringBounds(text, g);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 10 + i * 30, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
            g.setColor(Color.WHITE);
            g.drawString(text, 30, 10 + i * 30 + fm.getAscent() + 3);
            i++;
        }
    }
    
    /**
     * 
     * @return 
     */
    public boolean isVisible()
    {
        return m_visible;
    }
    
    /**
     * 
     */
    public void toggleVisible()
    {
        m_visible = !m_visible;
    }
}