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
import java.util.StringTokenizer;

public class TutorialState extends BaseState
{
    private int m_tutorialNumber;
    
    private String m_text;
    
    public TutorialState(StateManager stateManager)
    {
        super(stateManager);
        
        setTransparent(true);
        
        m_tutorialNumber = 0;
        m_text = "";
    }
    
    public void setTutorialNumber(int number)
    {
        m_tutorialNumber = number;
        
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        m_text = i18nManager.trans("tutoriel" + (number - 1));
    }
    
    public int getTutorialNumber()
    {
        return m_tutorialNumber;
    }
    
    @Override
    public void onCreate()
    {
        m_tutorialNumber = 0;
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
        if(m_stateManager.getContext().m_inputsListener.pause.enabled)
        {
            m_stateManager.switchTo(StateType.GAME);
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
        g.fillRect(10, screenHeight - 150, screenWidth - 20, 140);
        
        g.setColor(Color.BLACK);
        Font font = ressourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 16.0f);
        FontMetrics m = g.getFontMetrics(font);
        g.setFont(font);
        g.drawString(m_text, 20, screenHeight - 140 + m.getAscent());
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
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken() + " ";

            if (lineLen + word.length() > w) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word);
            lineLen += word.length();
        }
        return output.toString();
    }
}
