package core;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ResourceManager
{
    private Map<String, BufferedImage> m_spritesheets = new HashMap<>();
    private Map<String, Font> m_fonts = new HashMap<>();
    
    private static ResourceManager INSTANCE = null;
    
    public BufferedImage getSpritesheets(String name)
    {
        return m_spritesheets.get(name);
    }
    
    public Font getFont(String name)
    {
        return m_fonts.get(name);
    }
    
    private ResourceManager()
    {
        try
        {
            m_spritesheets.put("afendar", ImageIO.read(getClass().getResource("/afendar.png")));
            m_spritesheets.put("spritesheetGui", ImageIO.read(getClass().getResource("/gui.png")));
            m_spritesheets.put("spritesheetGui2", ImageIO.read(getClass().getResource("/gui2.png")));
            m_spritesheets.put("background", ImageIO.read(getClass().getResource("/background.png")));
            m_spritesheets.put("foreground", ImageIO.read(getClass().getResource("/foreground1.png")));
            m_spritesheets.put("foreground2", ImageIO.read(getClass().getResource("/foreground2.png")));
            m_spritesheets.put("foreground3", ImageIO.read(getClass().getResource("/foreground3.png")));
            
            m_fonts.put("kaushanscriptregular", Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("/fonts/kaushanscriptregular.ttf").openStream()));
        }
        catch(IOException e)
        {
            e.getMessage();
        }
        catch (FontFormatException ex)
        {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
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
