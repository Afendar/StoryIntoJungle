package states;

import audio.Sound;
import core.Camera;
import core.I18nManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import entity.CageEntity;
import entity.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import ld34.profile.Save;
import level.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SavesState extends BaseState
{
    private Font m_font, m_fontM, m_fontS;
    private BufferedImage m_gui, m_bgSave, m_bgMediumBtn, m_levelIcon, m_removeIcon, m_cageIcon, m_loadIcon, m_dollardIcon, m_littlesPandas, m_backIcon, m_background, m_foreground2;
    private int m_selectedItem, m_selectedSave, m_selectRemove, m_selectLoad;
    private int[][] m_btnCoords;
    private JSONObject m_jsonSaves;
    
    public SavesState(StateManager stateManager)
    {
        super(stateManager);
    }

    @Override
    public void onCreate()
    {
        try
        {
            URL url = getClass().getResource("/fonts/kaushanscriptregular.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_font = m_font.deriveFont(Font.PLAIN, 18.0f);
            m_fontS = m_font.deriveFont(Font.PLAIN, 14.0f);
            m_fontM = m_font.deriveFont(Font.PLAIN, 36.0f);
            
            url = this.getClass().getResource("/gui2.png");
            m_gui = ImageIO.read(url);
            m_bgSave = m_gui.getSubimage(235, 127, 217, 216);
            m_bgMediumBtn = m_gui.getSubimage(370, 1, 120, 99);
            m_levelIcon = m_gui.getSubimage(362, 107, 19, 16);
            m_removeIcon = m_gui.getSubimage(211, 92, 33, 35);
            m_cageIcon = m_gui.getSubimage(384, 101, 27, 25);
            m_loadIcon = m_gui.getSubimage(176, 93, 34, 35);
            m_dollardIcon = m_gui.getSubimage(413, 104, 16, 20);
            m_backIcon = m_gui.getSubimage(243, 94, 42, 34);
            
            url = this.getClass().getResource("/littles_pandas.png");
            m_littlesPandas = ImageIO.read(url);
            
            url = getClass().getResource("/background.png");
            m_background = ImageIO.read(url);
            
            url = getClass().getResource("/foreground2.png");
            m_foreground2 = ImageIO.read(url);
        }
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }
        
        int[][] btnCoordsDefines = {
            {190, 420},
            {340, 420},
            {490, 420}
        };
        m_btnCoords = btnCoordsDefines;
        
        m_selectedItem = 0;
        m_selectedSave = 0;
        m_selectRemove = 0;
        m_selectLoad = 0;
        
        m_jsonSaves = Save.getInstance().getSaves();
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
        System.out.println("Reload locales");
    }
    
    @Override
    public void update(double dt)
    {
        processHover();
        processClick();
    }

    @Override
    public void render(Graphics2D g)
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(m_background, 0, 0, null);
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, 35, screenWidth, 60);
        
        FontMetrics metrics = g.getFontMetrics(m_fontM);
        g.setFont(m_fontM);
        g.setColor(Color.BLACK);
        String title = i18nManager.trans("loadGame");
        int titlewidth = metrics.stringWidth(title);
        g.drawString(title, screenWidth / 2 - titlewidth / 2, 75);
        
        metrics = g.getFontMetrics(m_font);
        g.setFont(m_font);
        
        for(int i=0;i<m_jsonSaves.size();i++)
        {
            if(m_selectedSave == i + 1)
            {
                g.drawImage(m_gui.getSubimage(453, 125, 221, 220), (i * 230) + 68, 153, null);
            }
            else
            {
                g.drawImage(m_bgSave, i * 230 + 70, 155, null);
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
                g.setFont(m_font);
                g.setColor(Color.BLACK);
                metrics = g.getFontMetrics(m_font);
                int nameW = metrics.stringWidth(name);
                g.drawString(name, i * 230 + 178 - nameW/2, 192 + (metrics.getAscent()/2));
                
                int x = 0;
                int y = 160;
                if(sex == 0){
                    x = 50;
                }
                if(spicies == 1){
                    y = 208;
                }
                
                g.drawImage(m_dollardIcon, i * 230 + 181, 232 ,null);
                g.setFont(m_fontS);
                String score =  (String) player.get("score");
                int scoreWidth = metrics.stringWidth(score);
                g.drawString(score, i * 230 + 199, 237 + metrics.getAscent()/2);
                
                g.drawImage(m_levelIcon, i * 230 + 107, 315, null);
                JSONObject jsonLevel = (JSONObject) save.get("level");
                String levelNumber = (String) jsonLevel.get("number");
                int levelNum = Integer.parseInt((String) jsonLevel.get("number"));
                int levelNumberW = metrics.stringWidth(levelNumber);
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
                
                g.setFont(m_font);
                metrics = g.getFontMetrics(m_font);
                
                int offset = 0;
                switch(levelNum){
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
        
        for(int i=0;i<m_btnCoords.length;i++)
        {
            if(m_selectedItem == i + 1)
            {
                m_bgMediumBtn = m_gui.getSubimage(491, 1, 120, 99);
            }
            else
            {
                m_bgMediumBtn = m_gui.getSubimage(370, 1, 120, 99);
            }
            g.drawImage(m_bgMediumBtn, i * 150 + 190, 420, null);
            switch(i)
            {
                case 0:
                    g.drawImage(m_loadIcon, i * 150 + 234, 450, null);
                    break;
                case 1:
                    g.drawImage(m_removeIcon, i * 150 + 232, 450, null);
                    break;
                case 2:
                    g.drawImage(m_backIcon, i * 150 + 227, 450, null);
                    break;
            }
        }
        g.drawImage(m_foreground2, 0, 0, null);
    }
    
    /**
     * 
     */
    public void processHover(){
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;

        int oldSelected = m_selectedItem;
        m_selectedItem = 0;
        
        for(int i=0;i<m_btnCoords.length;i++)
        {
            if(mouseX > m_btnCoords[i][0] && mouseX < m_btnCoords[i][0] + 120 && mouseY > m_btnCoords[i][1] && mouseY < m_btnCoords[i][1] + 99)
            {
                m_selectedItem = i + 1;
                break;
            }
        }
        if(m_selectedItem != 0 && m_selectedItem != oldSelected){
            new Thread(Sound.hover::play).start();
        }
    }
    
    /**
     * 
     */
    public void processClick()
    {
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount == 1)
        {
            switch(m_selectedItem)
            {
                case 1:
                    if(m_selectedSave != 0)
                    {
                        JSONObject save = Save.getInstance().getSave(m_selectedSave - 1);
                        if(save != null && !save.isEmpty())
                        {
                            JSONObject jsonLevel = (JSONObject)save.get("level");
                            JSONObject jsonPlayer = (JSONObject)save.get("player");
                            int nbLevel = Integer.parseInt((String)jsonLevel.get("number"));
                            Level level = new Level(nbLevel);
                            JSONArray jsonUnlockedLevels = (JSONArray)jsonLevel.get("unlockedLevels");
                            boolean[] unlockedLevels = new boolean[jsonUnlockedLevels.size()];
                            for(int i = 0; i< jsonUnlockedLevels.size();i++)
                            {
                                unlockedLevels[i] = ((String)jsonUnlockedLevels.get(i)).equals("true") ? true : false;
                            }
                            
                            level.setUnlockedLevels(unlockedLevels);
                            
                            JSONArray coords = (JSONArray)jsonPlayer.get("coords");
                            Camera cam = new Camera(Integer.parseInt((String)coords.get(0)), Integer.parseInt((String)coords.get(1)), screenWidth, screenHeight, level);
                            Player player = new Player(Integer.parseInt((String)coords.get(0)), Integer.parseInt((String)coords.get(1)), level, m_stateManager.getContext().m_inputsListener, cam, Integer.parseInt((String)jsonLevel.get("difficulty")));
                            player.score = Integer.parseInt((String)jsonPlayer.get("score"));

                            JSONObject cagesMap = (JSONObject)jsonLevel.get("cages");
                            List<List<CageEntity>> cageMap = new ArrayList<>();
                            for(int i=1;i<cagesMap.size() + 1;i++)
                            {
                                JSONArray cageInLevelDatas = (JSONArray)cagesMap.get(Integer.toString(i));
                                List<CageEntity> cagesList = new ArrayList<>();
                                for(int j=0;j<cageInLevelDatas.size();j++)
                                {
                                    JSONArray cageDatas = (JSONArray)cageInLevelDatas.get(j);
                                    CageEntity ce = new CageEntity(level, Integer.parseInt((String)cageDatas.get(0)), Integer.parseInt((String)cageDatas.get(1)));
                                    ce.setBroken((boolean)cageDatas.get(2));
                                    cagesList.add(ce);
                                }
                                cageMap.add(cagesList);
                            }
                            
                            //GameScene gs = new GameScene(this.w, this.h, this.game, level, player);
                            m_stateManager.switchTo(StateType.GAME);
                            BaseState gameState = m_stateManager.getState(StateType.GAME);
                            if(gameState instanceof GameState)
                            {
                                GameState gs = (GameState)gameState;
                                gs.setPlayer(player);
                                gs.setLevel(level);
                            }
                            else
                            {
                                System.out.println("Game state cannot be loaded.");
                            }
                        }
                    }
                    break;
                case 2:
                    if(m_selectedSave != 0)
                    {
                        Save.getInstance().removeSave(m_selectedSave - 1);
                    }
                    break;
                case 3:
                    m_stateManager.switchTo(StateType.MAIN_MENU);
                    break;
            }

            if(mouseX > (0 * 230) + 70 && mouseX < (0 * 230) + 70 + 217 && mouseY > 155 && mouseY < 155 + 216)
            {
                m_selectedSave = 1;
            }
            else if(mouseX > (1 * 230) + 70 && mouseX < (1 * 230) + 70 + 217 && mouseY > 155 && mouseY < 155 + 216)
            {
                m_selectedSave = 2;
            }
            else if(mouseX > (2 * 230) + 70 && mouseX < (2 * 230) + 70 + 217 && mouseY > 155 && mouseY < 155 + 216)
            {
                m_selectedSave = 3;
            }
            else{
                m_selectedSave = 0;
            }
        }
    }
}
