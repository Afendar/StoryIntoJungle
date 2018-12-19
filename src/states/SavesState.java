package states;

import core.I18nManager;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.gui.Button;
import core.gui.ButtonGroup;
import core.gui.Dialog;
import core.gui.GuiComponent;
import core.gui.IconButton;
import core.gui.SaveSlot;
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
    private BufferedImage m_gui, m_background, m_foreground;
    private JSONObject m_jsonSaves;
    private ArrayList<GuiComponent> m_guiElements;
    private Dialog m_dialog;
    
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
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        m_guiElements = new ArrayList<>();
        m_gui = resourceManager.getSpritesheets("spritesheetGui2");
        
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
            if(i < 2)
            {
                b.setStatus(GuiComponent.Status.DISABLED);
                b.setDisabled(true);
            }
            b.setPosition(coords[i][0], coords[i][1]);
            b.addApearance(GuiComponent.Status.NEUTRAL, m_gui.getSubimage(491, 1, 120, 99));
            b.addApearance(GuiComponent.Status.FOCUSED, m_gui.getSubimage(370, 1, 120, 99));
            b.addApearance(GuiComponent.Status.DISABLED, m_gui.getSubimage(665, 551, 120, 99));
            b.addCallback(GuiComponent.Status.CLICKED, callbacks[i], this);
            m_guiElements.add(b);
        }
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 18.0f);
        ButtonGroup bg = new ButtonGroup(this);
        m_jsonSaves = Save.getInstance().getSaves();
        
        int offset = (screenWidth - 217) / 2 - 227;
        for(int i = 0 ; i < m_jsonSaves.size() ; i++)
        {
            JSONObject saveData = (JSONObject)m_jsonSaves.get("slot" + (i + 1));
            SaveSlot ss = new SaveSlot(this, false);
            if(saveData.isEmpty())
            {
                ss.setEmpty(true);
            }
            else
            {
                JSONObject playerData = (JSONObject)saveData.get("player");
                JSONObject levelData = (JSONObject)saveData.get("level");
                ss.setData(
                    (int)(long)playerData.get("sex"), 
                    (int)(long)playerData.get("species"), 
                    (int)(long)playerData.get("score"), 
                    (int)(long)levelData.get("nbFreeCages"), 
                    (int)(long)levelData.get("complete"), 
                    (int)(long)playerData.get("difficulty"), 
                    (int)(long)levelData.get("number"), 
                    (String)playerData.get("name")
                );
            }
            ss.setFont(font);
            ss.addApearance(GuiComponent.Status.NEUTRAL, m_gui.getSubimage(235, 127, 217, 216));
            ss.addApearance(GuiComponent.Status.FOCUSED, m_gui.getSubimage(235, 127, 217, 216));
            ss.addApearance(GuiComponent.Status.CHECKED, m_gui.getSubimage(453, 125, 221, 220));
            ss.setPosition(offset, screenHeight * 170 / Screen.RES_1X_HEIGHT);
            bg.add(ss);
            offset += 227;
        }
        m_guiElements.add(bg);
        
        m_dialog = new Dialog(this, i18nManager.trans("alert_remove_save"), Dialog.YES_NO_OPTION);
        m_dialog.addCallbackOption(0, "deleteSave", 1);
        m_dialog.addCallbackOption(1, "dispose");
        m_dialog.setPosition((screenWidth - 300) / 2, (screenHeight - 200) / 2);
    }

    @Override
    public void onDestroy()
    {
        
    }

    @Override
    public void activate()
    {
        super.activate();
        
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
        
        if(m_dialog != null && m_dialog.isVisible())
        {
            m_dialog.update(dt);
            return;
        }
        
        m_guiElements.stream().map((element) ->
        {
            element.update(dt);
            return element;            
        }).forEachOrdered((element) ->
        {
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount >= 1)
                {
                    element.onClick(mouseX, mouseY);
                    
                    m_guiElements.stream().filter((gc) -> (gc instanceof ButtonGroup)).map((gc) -> (ButtonGroup)gc).map((bg) -> bg.getButtons()).forEachOrdered((bl) ->
                    {
                        bl.stream().map((b) -> (SaveSlot)b).forEachOrdered((ss) ->
                        {
                            if(ss.isChecked() && ss.isEmpty())
                            {
                                m_guiElements.get(0).setStatus(GuiComponent.Status.DISABLED);
                                m_guiElements.get(0).setDisabled(true);
                                m_guiElements.get(1).setStatus(GuiComponent.Status.DISABLED);
                                m_guiElements.get(1).setDisabled(true);
                            }
                            else if(ss.isChecked())
                            {
                                m_guiElements.get(0).setDisabled(false);
                                m_guiElements.get(0).setStatus(GuiComponent.Status.NEUTRAL);
                                m_guiElements.get(1).setDisabled(false);
                                m_guiElements.get(1).setStatus(GuiComponent.Status.NEUTRAL);
                            }
                        });
                    });
                }
                else if(!m_stateManager.getContext().m_inputsListener.mousePressed && element.getStatus() == GuiComponent.Status.CLICKED)
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
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        
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
        
        m_guiElements.forEach((element) ->
        {
            element.render(g);
        });
        
        g.drawImage(m_foreground, 0, 0, null);
        
        if(m_dialog != null)
        {
            m_dialog.render(g);
        }
    }
    
    public void rebuildGui()
    {
        m_jsonSaves = Save.getInstance().getSaves();
        ButtonGroup bg = (ButtonGroup)m_guiElements.get(m_guiElements.size() - 1);
        
        for(int i = 0 ; i <  bg.size(); i++)
        {
            JSONObject saveData = (JSONObject)m_jsonSaves.get("slot" + (i + 1));
            SaveSlot ss = (SaveSlot)bg.getButton(i);
            ss.setChecked(false);
            if(saveData.isEmpty())
            {
                ss.setEmpty(true);
            }
            else
            {
                JSONObject playerData = (JSONObject)saveData.get("player");
                JSONObject levelData = (JSONObject)saveData.get("level");
                ss.setData(
                    (int)(long)playerData.get("sex"), 
                    (int)(long)playerData.get("species"), 
                    (int)(long)playerData.get("score"), 
                    (int)(long)levelData.get("nbFreeCages"), 
                    (int)(long)levelData.get("complete"), 
                    (int)(long)playerData.get("difficulty"), 
                    (int)(long)levelData.get("number"), 
                    (String)playerData.get("name")
                );
            }
        }
        
        m_guiElements.get(0).setStatus(GuiComponent.Status.DISABLED);
        m_guiElements.get(0).setDisabled(true);
        m_guiElements.get(1).setStatus(GuiComponent.Status.DISABLED);
        m_guiElements.get(1).setDisabled(true);
    }
    
    /**
     * 
     */
    public void loadSave()
    {
        int index = 0;
        
        for(GuiComponent gc : m_guiElements)
        {
            if(gc instanceof ButtonGroup)
            {
                ButtonGroup bg = (ButtonGroup)gc;
                ArrayList<Button> buttons = bg.getButtons();
                for(int i = 0 ; i < buttons.size() ; i++)
                {
                    SaveSlot ss = (SaveSlot)buttons.get(i);
                    if(ss.isChecked())
                    {
                        index = i + 1;
                    }
                }
            }
        }
        
        System.out.println("load save: " + index);
        
        //TODO load save and switch to game state.
    }
    
    public void deleteSave()
    {
        deleteSave(null);
    }
    
    public void deleteSave(Integer answer)
    {
        if(answer == null && m_dialog != null)
        {
            m_dialog.showDialog();
            return;
        }
        
        m_dialog.hideDialog();
        int index = 0;
        
        for(GuiComponent gc : m_guiElements)
        {
            if(gc instanceof ButtonGroup)
            {
                ButtonGroup bg = (ButtonGroup)gc;
                ArrayList<Button> buttons = bg.getButtons();
                for(int i = 0 ; i < buttons.size() ; i++)
                {
                    SaveSlot ss = (SaveSlot)buttons.get(i);
                    if(ss.isChecked() && !ss.isEmpty())
                    {
                        index = i + 1;
                    }
                }
            }
        }
        
        Save.getInstance().removeSave(index);
        rebuildGui();
    }
    
    public void dispose()
    {
        m_dialog.hideDialog();
    }
    
    /**
     * 
     */
    public void backToMain()
    {
        m_stateManager.switchTo(StateType.MAIN_MENU);
    }
}
