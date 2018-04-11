package core;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ResourceManager
{
    private Map<String, BufferedImage> m_spritesheets = new HashMap<>();
    private Map<String, Font> m_fonts = new HashMap<>();
    
    private static ResourceManager INSTANCE = null;
    
    public Map getSpritesheets()
    {
        return m_spritesheets;
    }
    
    public Map getFonts()
    {
        return m_fonts;
    }
    
    private ResourceManager()
    {
        try
        {
            m_spritesheets.put("spritesheetGui", ImageIO.read(getClass().getResource("/gui.png")));
            m_spritesheets.put("spritesheetGui2", ImageIO.read(getClass().getResource("/gui2.png")));
            m_spritesheets.put("background", ImageIO.read(getClass().getResource("/background.png")));
            m_spritesheets.put("foreground", ImageIO.read(getClass().getResource("/foreground1.png")));
            m_spritesheets.put("foreground2", ImageIO.read(getClass().getResource("/foreground2.png")));
            m_spritesheets.put("foreground3", ImageIO.read(getClass().getResource("/foreground3.png")));
        }
        catch(IOException e)
        {
            e.getMessage();
        }
    }
    
    public static ResourceManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ResourceManager();
        }
        return INSTANCE;
    }
}
