package core.gui;

import core.I18nManager;
import core.ResourceManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import states.BaseState;

/**
 * 
 */
public class SaveSlot extends RadioButton
{
    private int m_pandaSex, m_pandaSpecies, m_score, m_freeCages, m_percentage, m_difficulty, m_levelNumber;
    private String m_name;
    private boolean m_empty;
    
    /**
     * 
     * @param owner 
     */
    public SaveSlot(BaseState owner)
    {
        this(owner, true);
    }
    
    /**
     * 
     * @param owner
     * @param empty 
     */
    public SaveSlot(BaseState owner, boolean empty)
    {
        super(owner);
        m_empty = empty;
    }
    
    /**
     * 
     * @param pandaSex
     * @param pandaSpecies
     * @param score
     * @param freeCages
     * @param percentage
     * @param difficulty
     * @param levelNumber
     * @param name 
     */
    public void setData(int pandaSex, int pandaSpecies, int score, int freeCages, int percentage, int difficulty, int levelNumber, String name)
    {
        m_pandaSex = pandaSex;
        m_pandaSpecies = pandaSpecies;
        m_score = score;
        m_freeCages = freeCages;
        m_percentage = percentage;
        m_difficulty = difficulty;
        m_levelNumber = levelNumber;
        m_name = name;
    }
    
    /**
     * 
     * @return 
     */
    public int getPandaSex()
    {
        return m_pandaSex;
    }

    /**
     * 
     * @param pandaSex 
     */
    public void setPandaSex(int pandaSex)
    {
        m_pandaSex = pandaSex;
    }

    /**
     * 
     * @return 
     */
    public int getPandaSpecies()
    {
        return m_pandaSpecies;
    }

    /**
     * 
     * @param pandaSpecies 
     */
    public void setPandaSpecies(int pandaSpecies)
    {
        m_pandaSpecies = pandaSpecies;
    }

    /**
     * 
     * @return 
     */
    public int getScore()
    {
        return m_score;
    }

    /**
     * 
     * @param score 
     */
    public void setScore(int score)
    {
        m_score = score;
    }

    /**
     * 
     * @return 
     */
    public int getFreeCages()
    {
        return m_freeCages;
    }

    /**
     * 
     * @param freeCages 
     */
    public void setFreeCages(int freeCages)
    {
        m_freeCages = freeCages;
    }

    /**
     * 
     * @return 
     */
    public int getPercentage()
    {
        return m_percentage;
    }

    /**
     * 
     * @param percentage 
     */
    public void setPercentage(int percentage)
    {
        m_percentage = percentage;
    }

    /**
     * 
     * @return 
     */
    public int getDifficulty()
    {
        return m_difficulty;
    }

    /**
     * 
     * @param difficulty 
     */
    public void setDifficulty(int difficulty)
    {
        m_difficulty = difficulty;
    }

    /**
     * 
     * @return 
     */
    public int getLevelNumber()
    {
        return m_levelNumber;
    }

    /**
     * 
     * @param levelNumber 
     */
    public void setLevelNumber(int levelNumber)
    {
        m_levelNumber = levelNumber;
    }

    /**
     * 
     * @return 
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name)
    {
        m_name = name;
    }

    /**
     * 
     * @return 
     */
    public boolean isEmpty()
    {
        return m_empty;
    }

    /**
     * 
     * @param empty 
     */
    public void setEmpty(boolean empty)
    {
        m_empty = empty;
    }
    
    @Override
    public void update(double dt)
    {
    }

    @Override
    public void render(Graphics2D g)
    {
        g.setColor(Color.BLACK);
        if (m_appearances.get(m_status) == null)
        {
            return;
        }
        
        ResourceManager rm = m_owner.getStateManager().getContext().m_resourceManager;
        BufferedImage gui = rm.getSpritesheets("spritesheetGui2");
        
        g.drawImage(
            m_appearances.get(m_status), 
            m_checked ? m_position.m_x - 2 : m_position.m_x,
            m_checked ? m_position.m_y - 2 : m_position.m_y, 
            null
        );
        
        if(m_empty)
        {
            I18nManager i18nManager = m_owner.getStateManager().getContext().m_I18nManager;
            g.setFont(m_font);
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics(m_font);
            String emptyStr = i18nManager.trans("empty");
            int emptyStrW = fm.stringWidth(emptyStr);
            g.drawString(emptyStr, m_position.m_x + (217 - emptyStrW) / 2, m_position.m_y + (216 - fm.getDescent()) / 2);
            return;
        }
        
        g.setColor(Color.BLACK);
        g.setFont(m_font);
        FontMetrics fm = g.getFontMetrics(m_font);
        int sw = fm.stringWidth(m_name);
        g.drawString(m_name, m_position.m_x + (217 - sw) / 2, m_position.m_y + 50);
        
        g.drawImage(gui.getSubimage(413, 104, 16, 20), m_position.m_x + 115, m_position.m_y + 77 ,null);
        
        g.setFont(m_font.deriveFont(Font.PLAIN, 14.0f));
        
        g.drawString(Integer.toString(m_score), m_position.m_x + 133, m_position.m_y + 92);
        g.drawImage(gui.getSubimage(384, 101, 27, 25), m_position.m_x + 115, m_position.m_y + 108, null);
        g.drawString(Integer.toString(m_freeCages) + "/30", m_position.m_x + 142, m_position.m_y + 127);
        g.drawImage(gui.getSubimage(362, 107, 19, 16), m_position.m_x + 40, m_position.m_y + 155, null);
        g.drawString(Integer.toString(m_levelNumber), m_position.m_x + 60, m_position.m_y + 167);
        
        fm = g.getFontMetrics(m_font.deriveFont(Font.PLAIN, 14.0f));
        sw = fm.stringWidth(Integer.toString(m_percentage) + "%");
        g.drawString(Integer.toString(m_percentage) + "%", m_position.m_x + (217 - sw) / 2, m_position.m_y + 166);
        
        switch(m_difficulty)
        {
            case 0:
                g.drawImage(gui.getSubimage(285, 69, 17, 16), m_position.m_x + 150, m_position.m_y + 146, null);
                break;
            case 2:
                g.drawImage(gui.getSubimage(325, 69, 35, 16), m_position.m_x + 150, m_position.m_y + 146, null);
                break;
            case 4:
                g.drawImage(gui.getSubimage(285, 89, 33, 32), m_position.m_x + 150, m_position.m_y + 146, null);
                break;
            case 5:
                g.drawImage(gui.getSubimage(326, 90, 33, 32), m_position.m_x + 150, m_position.m_y + 146, null);
                break;
            default:
                break;
        }
        
        int offset = 0;
        switch(m_levelNumber)
        {
            case 1:
            case 2:
                offset = 0;
                break;
            case 3:
            case 4:
                offset = 2;
                break;
            case 5:
            case 6:
                offset = 4;
                break;
        }
        
        g.setColor(new Color(193, 182, 129));
        g.fillRoundRect(m_position.m_x + 40, m_position.m_y + 70, 70, 70, 8, 8);
        g.drawImage(rm.getSpritesheets("littles_pandas").getSubimage(((m_pandaSex + offset ) + ( 6 * m_pandaSpecies)) * 64, 0, 64, 64), m_position.m_x + 42, m_position.m_y + 75, null);
    }
}
