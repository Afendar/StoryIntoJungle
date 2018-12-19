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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import ld34.profile.Save;
import org.json.simple.JSONObject;

/**
 * 
 */
public class PausedSavesState extends BaseState
{   
    private ArrayList<GuiComponent> m_guiElements;
    private JSONObject m_jsonSaves;
    private Dialog m_dialog;
    
    /**
     * 
     * @param stateManager 
     */
    public PausedSavesState(StateManager stateManager)
    {
        super(stateManager);
    }

    @Override
    public void onCreate()
    {
        setTransparent(true);
        
        m_guiElements = new ArrayList<>();
        Screen screen = m_stateManager.getContext().m_screen;
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        BufferedImage gui = resourceManager.getSpritesheets("spritesheetGui2");
        
        IconButton ib = new IconButton(gui.getSubimage(176, 93, 34, 35), this);
        ib.setPosition((screenWidth - 217) / 2 - 227, screenHeight - 120);
        ib.addCallback(GuiComponent.Status.CLICKED, "save", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, gui.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, gui.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, gui.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.DISABLED, gui.getSubimage(665, 551, 120, 99));
        ib.setStatus(GuiComponent.Status.DISABLED);
        ib.setDisabled(true);
        m_guiElements.add(ib);
        
        ib = new IconButton(gui.getSubimage(211, 92, 33, 35), this);
        ib.setPosition((screenWidth - 217) / 2 - 107, screenHeight - 120);
        ib.addCallback(GuiComponent.Status.CLICKED, "deleteSave", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, gui.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, gui.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, gui.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.DISABLED, gui.getSubimage(665, 551, 120, 99));
        ib.setStatus(GuiComponent.Status.DISABLED);
        ib.setDisabled(true);
        m_guiElements.add(ib);
        
        ib = new IconButton(gui.getSubimage(760, 151, 34, 34), this);
        ib.setPosition((3 * screenWidth / 4), screenHeight - 120);
        ib.addCallback(GuiComponent.Status.CLICKED, "backToMenu", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, gui.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, gui.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, gui.getSubimage(491, 1, 120, 99));
        m_guiElements.add(ib);
        
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
            ss.addApearance(GuiComponent.Status.NEUTRAL, gui.getSubimage(235, 127, 217, 216));
            ss.addApearance(GuiComponent.Status.FOCUSED, gui.getSubimage(235, 127, 217, 216));
            ss.addApearance(GuiComponent.Status.CHECKED, gui.getSubimage(453, 125, 221, 220));
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
        if(m_stateManager.getContext().m_inputsListener.pause.typed)
        {
            backToMenu();
            return;
        }
        
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        if(m_dialog != null && m_dialog.isVisible())
        {
            m_dialog.update(dt);
            return;
        }
        
        m_guiElements.stream().map((element) ->
        {
            return element;            
        }).map((element) ->
        {
            element.update(dt);
            return element;            
        }).forEachOrdered((element) ->
        {
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
                                m_guiElements.get(0).setDisabled(false);
                                m_guiElements.get(0).setStatus(GuiComponent.Status.NEUTRAL);
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
        Screen screen = m_stateManager.getContext().m_screen;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        
        int sWidth = screen.getContentPane().getWidth();
        int sHeight = screen.getContentPane().getHeight();
        double scale = screen.getScale();
        
        g.setColor(new Color(70, 70, 70, 150));
        g.fillRect(0, 0, sWidth, sHeight);
        
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, (int)(800 * scale), 60);
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 36.0f);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("pauseMsg");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, sWidth/2 - titlewidth/2, 75);
        
        m_guiElements.forEach((element) ->
        {
            element.render(g);
        });
        
        if(m_dialog != null)
        {
            m_dialog.render(g);
        }
    }
    
    /**
     * 
     */
    public void backToMenu()
    {
        m_stateManager.remove(StateType.PAUSED_SAVES);
        m_stateManager.switchTo(StateType.PAUSED);
    }
    
    public void save()
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
                        if(!ss.isEmpty())
                        {
                            m_dialog.setMessage(m_stateManager.getContext().m_I18nManager.trans("alert_overwrite_save"));
                            m_dialog.addCallbackOption(0, "save", 1);
                            m_dialog.showDialog();
                            return;
                        }
                        break;
                    }
                }
                break;
            }
        }
        
        System.out.println("save without overwrite slot: " + index);
        //Todo save
    }
    
    /**
     * 
     * @param answer
     */
    public void save(Integer answer)
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
                        break;
                    }
                }
                break;
            }
        }
        
        System.out.println("save overwrite slot: " + index);
        //TODO save
    }
    
    public void deleteSave()
    {
        deleteSave(null);
    }
    
    /**
     * 
     */
    public void deleteSave(Integer answer)
    {
        if(answer == null && m_dialog != null)
        {
            m_dialog.setMessage(m_stateManager.getContext().m_I18nManager.trans("alert_remove_save"));
            m_dialog.addCallbackOption(0, "deleteSave", 1);
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
                    if(ss.isChecked())
                    {
                        index = i + 1;
                    }
                }
            }
        }
        
        Save.getInstance().removeSave(index);
        rebuildGui();
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
        
        m_guiElements.get(0).setDisabled(false);
        m_guiElements.get(0).setStatus(GuiComponent.Status.NEUTRAL);
        m_guiElements.get(1).setStatus(GuiComponent.Status.DISABLED);
        m_guiElements.get(1).setDisabled(true);
    }
    
    public void dispose()
    {
        m_dialog.hideDialog();
    }
}
