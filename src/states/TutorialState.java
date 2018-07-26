package states;

import core.I18nManager;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import ld34.profile.Settings;

public class TutorialState extends BaseState
{
    private int m_tutorialNumber;
    
    private String m_text, m_tmpText;
    private float m_timer, m_timeFloating;
    private int m_counter, m_yBtn;
    private boolean m_levit, m_goingDown;
    
    public TutorialState(StateManager stateManager)
    {
        super(stateManager);
        
        setTransparent(true);
        
        m_tutorialNumber = 0;
        m_timeFloating = 0;
        m_timer = 0;
        m_counter = 0;
        m_text = "";
        m_levit  = false;
        m_goingDown = true;
    }
    
    public void setTutorialNumber(int number)
    {
        m_tutorialNumber = number;
        m_timer = 0;
        m_timeFloating = 0;
        m_counter = 0;
        m_text = "";
        m_levit = false;
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        m_tmpText = i18nManager.trans("tutoriel" + (number));
        
        switch(m_tutorialNumber)
        {
            case 0:
                m_tmpText = m_tmpText.replaceAll("\\[.*?\\]", Settings.getInstance().getConfigValue("Name"));
                break;
            case 1:
            case 7:
                m_tmpText = m_tmpText.replaceAll("\\[.*?\\]", KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))));
                break;
            case 2:
                m_tmpText = m_tmpText.replaceAll("\\[.*?\\]", KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))));
                break;
        }
    }
    
    public int getTutorialNumber()
    {
        return m_tutorialNumber;
    }
    
    @Override
    public void onCreate()
    {
        m_tutorialNumber = 0;
        m_yBtn = m_stateManager.getContext().m_screen.getHeight() - 60;
    }

    @Override
    public void onDestroy() 
    {
        
    }

    @Override
    public void activate()
    {
        
    }

    @Override
    public void desactivate()
    {
        
    }

    @Override
    public void reloadLocales()
    {
        
    }

    @Override
    public void update(double dt)
    {
        if(m_stateManager.getContext().m_inputsListener.next.enabled)
        {
            m_stateManager.switchTo(StateType.GAME);
        }
        
        m_timer += dt;
        if(m_timer > 1.3f && m_counter <  m_tmpText.length())
        {
            m_timer -= 1.3f;
            m_counter += 2;
            m_counter = Math.min(m_counter, m_tmpText.length());
            m_text = m_tmpText.substring(0, m_counter);
        }
        else if(m_levit && m_timer > 5.0f)
        {
            m_timer -= 5.0f;
            int screenH = m_stateManager.getContext().m_screen.getHeight();
            if (m_goingDown == true)
            {
                if (m_yBtn <= screenH - 63)
                {
                    m_goingDown = false;
                    m_yBtn++;
                }
                else
                {
                    m_yBtn--;
                }
            }
            else
            {
                if (m_yBtn >= screenH - 57)
                {
                    m_goingDown = true;
                    m_yBtn--;
                }
                else
                {
                    m_yBtn++;
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        ResourceManager ressourceManager = m_stateManager.getContext().m_resourceManager;
        Screen screen = m_stateManager.getContext().m_screen;
        
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        g.setColor(Color.WHITE);
        g.fillRect(10, screenHeight - 150, 780, 140);
        
        g.setColor(Color.BLACK);
        Font font = ressourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 16.0f);
        FontMetrics m = g.getFontMetrics(font);
        g.setFont(font);
        
        FontMetrics metrics = g.getFontMetrics(font);
        int stringWidth = metrics.stringWidth(m_text);
        if(stringWidth > screenWidth - 20)
        {
            String label = this.substringLabels(m_text, 120);
            int y = screenHeight - 140 - 10;
            for(String line : label.split("\n"))
            {
                g.drawString(line, 20, y += m.getAscent() + 10);
            }
        }
        else
        {
            g.drawString(m_text, 20, screenHeight - 140 + m.getAscent());
        }
        
        if(m_text.length() == m_tmpText.length())
        {
            m_levit = true;
            g.drawString("Enter", (screenWidth / 2) + 330, m_yBtn);
        }
    }
    
    /**
     * 
     * @param text
     * @param w
     * @return 
     */
    private String substringLabels(String text, int w)
    {
        StringTokenizer tok = new StringTokenizer(text, " ");
        StringBuilder output = new StringBuilder(text.length());
        int lineLen = 0;
        while (tok.hasMoreTokens())
        {
            String word = tok.nextToken() + " ";

            if (lineLen + word.length() > w)
            {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word);
            lineLen += word.length();
        }
        return output.toString();
    }
}
