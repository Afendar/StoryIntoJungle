package core;

import audio.Sound;
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
    private Map<String, Sound> m_sounds = new HashMap<>();
    
    private static ResourceManager INSTANCE = null;
    
    /**
     * 
     * @param name
     * @return 
     */
    public BufferedImage getSpritesheets(String name)
    {
        return m_spritesheets.get(name);
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public Font getFont(String name)
    {
        return m_fonts.get(name);
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public Sound getSound(String name)
    {
        return m_sounds.get(name);
    }
    
    /**
     * 
     */
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
            
            m_sounds.put("bonus", new Sound("/bonus.wav"));
            m_sounds.put("death", new Sound("/death.wav"));
            m_sounds.put("jump", new Sound("/jump.wav"));
            m_sounds.put("levelup", new Sound("/levelup.wav"));
            m_sounds.put("hover", new Sound("/hover3.wav"));
            m_sounds.put("select", new Sound("/select3.wav"));
            m_sounds.put("explosion", new Sound("/explosion.wav"));
            
            m_sounds.put("jungle1", new Sound("/jungle01.wav"));
            m_sounds.put("jungle2", new Sound("/jungle02.wav"));
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
    
    /**
     * 
     * @return 
     */
    public static ResourceManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ResourceManager();
        }
        return INSTANCE;
    }
}