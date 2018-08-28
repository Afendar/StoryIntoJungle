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

/**
 * Profiler class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Profiler
{
    private String[] m_labels;
    private String[] m_datas;
    private boolean m_visible;
    private Game m_game;
    private Font m_fontD;
    
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
        m_labels = new String[]{"fps", "memory", "x", "y", "jump", "fall"};
        m_datas = new String[]{"0", "0", "0", "0", "true", "true"};
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
    }
    
    /**
     * 
     * @param game 
     */
    public void addGame(Game game)
    {
        m_game = game;
    }
    
    /**
     * 
     * @param frames
     * @param memory 
     */
    public void update(String frames, String memory)
    {
        m_datas[0] = frames;
        m_datas[1] = memory;
        
        /*if(this.game.gs instanceof GameScene){
            GameScene gs = (GameScene)this.game.gs;
            this.datas[2] = Float.toString(gs.player.getPosX());
            this.datas[3] = Float.toString(gs.player.getPosY());
            this.datas[4] = Boolean.toString(gs.player.isJumping());
            this.datas[5] = Boolean.toString(gs.player.isFalling());
        }*/
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics g)
    {
        renderGlobalDebug(g);

        /*if(this.game.gs instanceof GameScene){
            this.renderGameDebug(g);
        }*/
    }
    
    /**
     * 
     * @param g 
     */
    public void renderGlobalDebug(Graphics g)
    {
        FontMetrics fm = g.getFontMetrics(m_fontD);
        String text = "FPS : ";
        text += m_datas[0];
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setFont(m_fontD);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 30 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 30);
        
        text = "Memory : " + m_datas[1] + "Mo";
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 57 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 60);
        
        text = "Java : " + System.getProperty("java.version") + "  x" + System.getProperty("sun.arch.data.model") + " bit";
        rect = fm.getStringBounds(text, g);
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
    }
    
    /**
     * 
     * @param g 
     */
    public void renderGameDebug(Graphics g)
    {
        FontMetrics fm = g.getFontMetrics(m_fontD);
        String text = "X : " + m_datas[2];
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 84 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 87);

        text = "Y : " + m_datas[3];
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 111 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 114);

        text = "Jump : " + m_datas[4];
        rect = fm.getStringBounds(text, g);
        text = "Jump : ";
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 138 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 141);
        
        if(m_datas[4].equals("true"))
        {
            g.setColor(Color.GREEN);
        }
        else
        {
            g.setColor(Color.RED);
        }
        
        g.drawString(m_datas[4], 30 + fm.stringWidth(text), 141);
        
        text = "Fall : " + m_datas[5];
        rect = fm.getStringBounds(text, g);
        text = "Fall : ";
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 165 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 168);
        
        if(m_datas[5].equals("true"))
        {
            g.setColor(Color.GREEN);
        }
        else
        {
            g.setColor(Color.RED);
        }
        
        g.drawString(m_datas[5], 30 + fm.stringWidth(text), 168);
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