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
    private Map<String, BufferedImage> m_backgrounds = new HashMap<>();
    private Map<String, Font> m_fonts = new HashMap<>();
    private Map<String, Sound> m_sounds = new HashMap<>();
    private Map<String, Sound> m_musics = new HashMap<>();
    
    private static ResourceManager INSTANCE = null;
    
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
            m_spritesheets.put("backgroundpause", ImageIO.read(getClass().getResource("/backgroundpause.png")));
            m_spritesheets.put("foreground", ImageIO.read(getClass().getResource("/foreground1.png")));
            m_spritesheets.put("foreground2", ImageIO.read(getClass().getResource("/foreground2.png")));
            m_spritesheets.put("foreground3", ImageIO.read(getClass().getResource("/foreground3.png")));
            m_spritesheets.put("littles_pandas", ImageIO.read(getClass().getResource("/littles_pandas.png")));
            m_spritesheets.put("bgPause", ImageIO.read(getClass().getResource("/gui_pausesettings.png")));
            
            m_backgrounds.put("background-fall", ImageIO.read(getClass().getResource("/backgrounds/background-fall.png")));
            m_backgrounds.put("background-lagoon", ImageIO.read(getClass().getResource("/backgrounds/background-lagoon.png")));
            m_backgrounds.put("background-spring", ImageIO.read(getClass().getResource("/backgrounds/background-spring.png")));
            m_backgrounds.put("background-temple", ImageIO.read(getClass().getResource("/backgrounds/background-temple.png")));
            m_backgrounds.put("background-summer", ImageIO.read(getClass().getResource("/backgrounds/background-summer.png")));
            m_backgrounds.put("background-swamp", ImageIO.read(getClass().getResource("/backgrounds/background-swamp.png")));
            m_backgrounds.put("background-winter", ImageIO.read(getClass().getResource("/backgrounds/background-winter.png")));
            
            m_fonts.put("kaushanscriptregular", Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("/fonts/kaushanscriptregular.ttf").openStream()));
            
            m_sounds.put("bonus", new Sound("/bonus.wav"));
            m_sounds.put("death", new Sound("/death.wav"));
            m_sounds.put("jump", new Sound("/jump.wav"));
            m_sounds.put("levelup", new Sound("/levelup.wav"));
            m_sounds.put("hover", new Sound("/hover3.wav"));
            m_sounds.put("select", new Sound("/select3.wav"));
            m_sounds.put("explosion", new Sound("/explosion.wav"));
            
            m_musics.put("jungle1", new Sound("/jungle01.wav"));
            m_musics.put("jungle2", new Sound("/jungle02.wav"));
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
    public BufferedImage getBackground(String name){
        return m_backgrounds.get(name);
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
     * @param name
     * @return 
     */
    public Sound getMusic(String name)
    {
        return m_musics.get(name);
    }
    
    /**
     * 
     * @param volume 
     */
    public void setSoundVolume(int volume)
    {
        m_sounds.entrySet().forEach((e) ->
        {
            e.getValue().setVolume(volume);
        });
    }
    
    /**
     * 
     * @param volume 
     */
    public void setMusicVolume(int volume)
    {
        m_musics.entrySet().forEach((e) ->
        {
            e.getValue().setVolume(volume);
        });
    }
}
