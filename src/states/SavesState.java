package states;

import core.I18nManager;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.gui.GuiComponent;
import core.gui.IconButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import ld34.profile.Save;
import org.json.simple.JSONObject;

public class SavesState extends BaseState
{
    private BufferedImage m_gui, m_bgSave, m_levelIcon, m_cageIcon, m_dollardIcon, m_littlesPandas, m_background, m_foreground;
    private int m_selectedSave;
    private JSONObject m_jsonSaves;
    
    private ArrayList<GuiComponent> m_guiElements;
    
    /**
     * 
     * @param stateManager 
     */
    public SavesState(StateManager stateManager)
    {
        super(stateManager);
    }

    @Override
    public void onCreate()
    {
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        m_guiElements = new ArrayList<>();
        m_gui = resourceManager.getSpritesheets("spritesheetGui2");
        m_bgSave = m_gui.getSubimage(235, 127, 217, 216);
        m_levelIcon = m_gui.getSubimage(362, 107, 19, 16);
        m_cageIcon = m_gui.getSubimage(384, 101, 27, 25);
        m_dollardIcon = m_gui.getSubimage(413, 104, 16, 20);
        m_littlesPandas = resourceManager.getSpritesheets("littles_pandas");
        
        m_background = getScaledInstance(
                m_stateManager.getContext().m_resourceManager.getSpritesheets("background"),
                screenWidth, 
                screenHeight, 
                RenderingHints.VALUE_INTERPOLATION_BICUBIC, 
                false
        );
        m_foreground = getScaledInstance(
                m_stateManager.getContext().m_resourceManager.getSpritesheets("foreground2"), 
                screenWidth, 
                screenHeight, 
                RenderingHints.VALUE_INTERPOLATION_BICUBIC, 
                false
        );

        BufferedImage[] icons = {
            m_gui.getSubimage(176, 93, 34, 35),
            m_gui.getSubimage(211, 92, 33, 35),
            m_gui.getSubimage(243, 94, 42, 34)
        };
        int[][] coords = {
            {((screenWidth/5)) + (screenWidth/5)/2 - 60, 3 * (screenHeight/4)},
            {(2*(screenWidth/5)) + (screenWidth/5)/2 - 60, 3 * (screenHeight/4)},
            {(3*(screenWidth/5)) + (screenWidth/5)/2 - 60, 3 * (screenHeight/4)}
        };
        String[] callbacks = {
            "loadSave",
            "deleteSave",
            "backToMain"
        };
        
        for(int i = 0 ; i < 3 ; i++)
        {
            IconButton b = new IconButton(icons[i], this);
            b.setPosition(coords[i][0], coords[i][1]);
            b.addApearance(GuiComponent.Status.NEUTRAL, m_gui.getSubimage(491, 1, 120, 99));
            b.addApearance(GuiComponent.Status.FOCUSED, m_gui.getSubimage(370, 1, 120, 99));
            b.addCallback(GuiComponent.Status.CLICKED, callbacks[i], this);
            m_guiElements.add(b);
        }
        
        m_selectedSave = 0;
        m_jsonSaves = Save.getInstance().getSaves();
    }

    @Override
    public void onDestroy()
    {
        
    }

    @Override
    public void activate()
    {
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        m_background = getScaledInstance(
                m_stateManager.getContext().m_resourceManager.getSpritesheets("background"),
                screenWidth, 
                screenHeight, 
                RenderingHints.VALUE_INTERPOLATION_BICUBIC, 
                false
        );
        m_foreground = getScaledInstance(
                m_stateManager.getContext().m_resourceManager.getSpritesheets("foreground2"), 
                screenWidth, 
                screenHeight, 
                RenderingHints.VALUE_INTERPOLATION_BICUBIC, 
                false
        );
        
        int[][] coords = {
            {((screenWidth/5)) + (screenWidth/5)/2 - 60, 3 * (screenHeight/4)},
            {(2*(screenWidth/5)) + (screenWidth/5)/2 - 60, 3 * (screenHeight/4)},
            {(3*(screenWidth/5)) + (screenWidth/5)/2 - 60, 3 * (screenHeight/4)}
        };
        
        for(int i = 0 ; i < coords.length ; i++)
        {
            GuiComponent ge = m_guiElements.get(i);
            ge.setPosition(coords[i][0], coords[i][1]);
        }
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
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        for(GuiComponent element : m_guiElements)
        {
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount >= 1)
                {
                    element.onClick(mouseX, mouseY);
                }
                else if(!m_stateManager.getContext().m_inputsListener.mousePressed && element.getStatus() == GuiComponent.Status.CLICKED)
                {
                    element.onRelease();
                }
                
                if(element.getStatus() != GuiComponent.Status.NEUTRAL)
                {
                    continue;
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
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        g.drawImage(m_background, 0, 0, null);
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, screenWidth, 60);
        
        Font f = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 36.0f);
        FontMetrics metrics = g.getFontMetrics(f);
        g.setFont(f);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("loadGame");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, screenWidth / 2 - titlewidth / 2, 75);
        
        f = f.deriveFont(Font.PLAIN, 18.0f);
        metrics = g.getFontMetrics(f);
        g.setFont(f);
        
        for(int i=0;i<m_jsonSaves.size();i++)
        {
            if(m_selectedSave == i + 1)
            {
                g.drawImage(
                        m_gui.getSubimage(453, 125, 221, 220), 
                        ((i + 1) * (screenWidth / 5)) + (screenWidth/5)/2 - 121, 
                        (screenHeight/3) - 40,
                        null
                );
            }
            else
            {
                g.drawImage(
                        m_bgSave, 
                        (i * ((screenWidth - 40)/ 3)) + ((screenWidth - 60)/3)/2 - 81, 
                        (screenHeight/3) - 40, 
                        null
                );
            }
            
            //get save datas
            JSONObject save = (JSONObject) m_jsonSaves.get("Slot" + i);
            if(save.isEmpty())
            {
                String empty = i18nManager.trans("empty");
                int emptyTxtWidth = metrics.stringWidth(empty);
                g.setColor(Color.BLACK);
                g.drawString(empty, i * 230 + 178 - emptyTxtWidth/2, 256 + metrics.getAscent()/2);
            }
            else
            {
                JSONObject player = (JSONObject) save.get("player");
                String name = (String) player.get("name");
                int sex = Integer.parseInt((String) player.get("sex"));
                int spicies = Integer.parseInt((String) player.get("spicies"));
                g.setFont(f);
                g.setColor(Color.BLACK);
                metrics = g.getFontMetrics(f);
                int nameW = metrics.stringWidth(name);
                g.drawString(name, i * 230 + 178 - nameW/2, 192 + (metrics.getAscent()/2));
                
                int x = 0;
                int y = 160;
                if(sex == 0)
                {
                    x = 50;
                }
                
                if(spicies == 1)
                {
                    y = 208;
                }
                
                Font fs = f.deriveFont(Font.PLAIN, 14.0f);
                g.drawImage(m_dollardIcon, i * 230 + 181, 232 ,null);
                g.setFont(fs);
                String score =  (String) player.get("score");
                g.drawString(score, i * 230 + 199, 237 + metrics.getAscent()/2);
                
                g.drawImage(m_levelIcon, i * 230 + 107, 315, null);
                JSONObject jsonLevel = (JSONObject) save.get("level");
                String levelNumber = (String) jsonLevel.get("number");
                int levelNum = Integer.parseInt((String) jsonLevel.get("number"));
                g.drawString(levelNumber, i * 230 + 130, 318 + metrics.getAscent()/2);
                
                g.drawImage(m_cageIcon, i * 230 + 176, 266, null);
                String cageNumbers = (String) jsonLevel.get("freeCages") + "/30";
                g.drawString(cageNumbers, i * 230 + 208, 274 + metrics.getAscent()/2);
                
                String complete = (String) jsonLevel.get("complete") + "%";
                int completeW = metrics.stringWidth(complete);
                g.drawString(complete, i * 230 + 178 - completeW/2, 318 + metrics.getAscent()/2);
                
                int difficulty = Integer.parseInt((String) jsonLevel.get("difficulty"));
                
                switch(difficulty){
                    case 0:
                        g.drawImage(m_gui.getSubimage(285, 69, 17, 16), i * 230 + 226, 305, null);
                        break;
                    case 2:
                        g.drawImage(m_gui.getSubimage(325, 69, 35, 16), i * 230 + 226, 305, null);
                        break;
                    case 4:
                        g.drawImage(m_gui.getSubimage(285, 89, 33, 32), i * 230 + 226, 305, null);
                        break;
                    case 5:
                        g.drawImage(m_gui.getSubimage(326, 90, 33, 32), i * 230 + 226, 305, null);
                        break;
                    default:
                        break;
                }
                
                g.setFont(f);
                metrics = g.getFontMetrics(f);
                
                int offset = 0;
                switch(levelNum)
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
                g.fillRoundRect(i * 230 + 102, 225, 70, 70, 8, 8);
                g.drawImage(m_littlesPandas.getSubimage(((sex + offset ) + ( 6 * spicies)) * 64, 0, 64, 64), i * 230 + 105, 228, null);
            }
        }
        
        m_guiElements.forEach((element) ->
        {
            element.render(g);
        });
        
        g.drawImage(m_foreground, 0, 0, null);
    }
    
    /**
     * 
     */
    public void loadSave()
    {
        
    }
    
    /**
     * 
     */
    public void deleteSave()
    {
        
    }
    
    /**
     * 
     */
    public void backToMain()
    {
        m_stateManager.switchTo(StateType.MAIN_MENU);
    }
}
