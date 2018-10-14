package core.gui;

import core.I18nManager;
import core.Position;
import core.ResourceManager;
import core.Screen;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.StringTokenizer;
import states.BaseState;

/**
 * 
 */
public class Dialog extends GuiComponent
{
    private Object m_answer;
    private ArrayList<Button> m_options;
    private boolean m_visible;
    private String m_message;
    private BufferedImage m_background;
    
    public static final int YES_NO_OPTION = 0;
    public static final Object UNINITIALIZED_VALUE = null;
    
    /**
     * 
     * @param owner
     * @param message
     * @param optionType 
     */
    public Dialog(BaseState owner, String message, int optionType)
    {
        super(owner);
        m_answer = UNINITIALIZED_VALUE;
        m_options = new ArrayList<>();
        m_visible = false;
        m_message = message;
        
        ResourceManager resourceManager = m_owner.getStateManager().getContext().m_resourceManager;
        BufferedImage gui = resourceManager.getSpritesheets("spritesheetGui2");
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 18.0f);
        I18nManager i18nManager = m_owner.getStateManager().getContext().m_I18nManager;
        
        m_background = gui.getSubimage(1121, 0, 275, 205);
        
        switch(optionType)
        {
            case YES_NO_OPTION:
                Button b = new Button(i18nManager.trans("yes"), owner);
                b.setFont(font);
                b.setTextCenter(true);
                b.setPadding(12, 0);
                b.setPosition(m_position.m_x + 30, m_position.m_y + 200 - 70);
                b.addApearance(GuiComponent.Status.NEUTRAL, gui.getSubimage(1121, 210, 101, 34));
                b.addApearance(GuiComponent.Status.FOCUSED, gui.getSubimage(1121, 246, 101, 34));
                m_options.add(b);
                
                b = new Button(i18nManager.trans("no"), owner);
                b.setFont(font);
                b.setTextCenter(true);
                b.setPadding(12, 0);
                b.setPosition(m_position.m_x + 140, m_position.m_y + 200 - 70);
                b.addApearance(GuiComponent.Status.NEUTRAL, gui.getSubimage(1121, 210, 101, 34));
                b.addApearance(GuiComponent.Status.FOCUSED, gui.getSubimage(1121, 246, 101, 34));
                m_options.add(b);
                break;
        }
    }
    
    public void addCallbackOption(int option, String methodName, Object... params)
    {
        m_options.get(option).addCallback(GuiComponent.Status.CLICKED, methodName, m_owner, params);
    }
    
    /**
     * 
     */
    public void showDialog()
    {
        m_visible = true;
    }
    
    @Override
    public void setPosition(Position position)
    {
        super.setPosition(position);
        m_options.get(0).setPosition(m_position.m_x + 30, m_position.m_y + 200 - 70);
        m_options.get(1).setPosition(m_position.m_x + 140, m_position.m_y + 200 - 70);
    }

    @Override
    public void setPosition(int x, int y)
    {
        super.setPosition(x, y);
        m_options.get(0).setPosition(m_position.m_x + 30, m_position.m_y + 200 - 70);
        m_options.get(1).setPosition(m_position.m_x + 140, m_position.m_y + 200 - 70);
    }
    
    /**
     * 
     */
    public void hideDialog()
    {
        m_visible = false;
    }

    /**
     * 
     * @return 
     */
    public boolean isVisible()
    {
        return m_visible;
    }
    
    @Override
    public void update(double dt)
    {
        int mouseX = m_owner.getStateManager().getContext().m_inputsListener.mouseX;
        int mouseY = m_owner.getStateManager().getContext().m_inputsListener.mouseY;
        
        m_options.stream().map((element) ->
        {
            element.update(dt);
            return element;            
        }).forEachOrdered((element) ->
        {
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_owner.getStateManager().getContext().m_inputsListener.mousePressed && m_owner.getStateManager().getContext().m_inputsListener.mouseClickCount >= 1)
                {
                    element.onClick(mouseX, mouseY);
                }
                else if(!m_owner.getStateManager().getContext().m_inputsListener.mousePressed && element.getStatus() == GuiComponent.Status.CLICKED)
                {
                    element.onRelease();
                }
                
                if(element.getStatus() != GuiComponent.Status.NEUTRAL)
                {
                    return;
                }

                element.onHover();
            }
            else if(element.getStatus() == GuiComponent.Status.FOCUSED)
            {
                element.onLeave();
            }
            else if(element.getStatus() == GuiComponent.Status.CLICKED)
            {
                element.onRelease();
            }
        });
    }

    @Override
    public void render(Graphics2D g)
    {
        if(!m_visible)
        {
            return;
        }
        
        Screen screen = m_owner.getStateManager().getContext().m_screen;
        int screenWidth = screen.getWidth();
        int screenHeight = screen.getHeight();
        
        g.setColor(new Color(147, 147, 147, 130));
        g.fillRect(0, 0, screenWidth, screenHeight);
        
        g.drawImage(m_background, m_position.m_x, m_position.m_y, null);
        g.setColor(Color.BLACK);
        
        ResourceManager resourceManager = m_owner.getStateManager().getContext().m_resourceManager;
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 18.0f);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);
        int stringWidth = fm.stringWidth(m_message);
        
        if(stringWidth > 240)
        {
            String label = this.substringLabels(m_message, 30);
            int y = m_position.m_y + 25;
            for(String line : label.split("\n"))
            {
                g.drawString(line, m_position.m_x + 30, y += fm.getAscent() + 10);
            }
        }
        else
        {
            g.drawString(m_message, m_position.m_x + 30, m_position.m_y + fm.getAscent() + 35);
        }
        
        m_options.forEach((gc) ->
        {
            gc.render(g);
        });
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
        while(tok.hasMoreTokens())
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
    
    public void setMessage(String message)
    {
        m_message = message;
    }
}
