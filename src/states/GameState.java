package states;

import core.Camera;
import core.Defines;
import core.I18nManager;
import core.Minimap;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.TimerThread;
import core.Context;
import core.WeatherManager;
import core.gui.GuiConsole;
import entity.CageEntity;
import entity.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import ld34.profile.BestScores;
import ld34.profile.Settings;
import level.Level;
import level.LevelManager;

/**
 * GameState class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class GameState extends BaseState
{
    public Player m_player;
    public Level m_level;
    public Camera m_cam;
    private LevelManager m_levelManager;
    private WeatherManager m_weatherManager;
    public GuiConsole m_guiConsole;
    public BufferedImage m_background2, m_bgGui, m_gui, m_bgGui2, m_clockGui, m_backgroundBottom, m_backgroundTop, m_monkeySpriteSheet,
            m_backgroundBottomAll, m_backgroundBottom2, m_backgroundTop2, m_backgroundTopAll, m_guiAssets, m_scoreIcon, m_timeIcon, m_levelIcon, m_cagesIcon,
            m_littlesPandas, m_spritesheetGui, m_background, m_foregroundGame;
    public int m_nbLevel, m_selectedItemMenu, m_selectedItemSaves, m_selectedItemSettings;
    public boolean m_displayEnd, m_displayStart, m_renderSaves, m_displayEvent, m_displayDialog = false;
    public int m_alpha, m_alphaMax;
    public int m_time, m_glueX, m_glueX2, m_glueTopX, m_glueTopX2, m_eventNumber = 0;
    public int m_timeF = 0, m_timeEventFree, m_timeMonkey, m_timeDialog = 0;
    public int m_minutes = 0, m_eventY = 0;
    public int m_secondes = 0, m_pauseSettingsPanelX = 800, m_pauseMenuX = 0;
    public int m_maxTimeHardcore = 1, m_soundPlayed, m_timeSound, m_cageToFree;
    public double m_timerMenu = 0;
    public boolean m_timer = false, m_renderFreeCageAnim = false;
    public Minimap m_minimap;
    
    public GameState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate() 
    {
        Context c = m_stateManager.getContext();
        Screen screen = c.m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        m_guiConsole = new GuiConsole(0, 400, 350, 200, c.m_inputsListener);
        m_levelManager = new LevelManager(c);
        m_weatherManager = new WeatherManager(c);
        
        m_nbLevel = 1;
        m_displayEnd = false;
        m_displayStart = true;
        
        if(m_nbLevel > 1)
        {
            m_displayStart = false;
        }
        
        m_level = m_levelManager.loadLevel(m_nbLevel);
        m_weatherManager.setWeather(m_level.getWeather());
        
        m_cam = new Camera(0, 0, screenWidth, screenHeight, m_level);
        
        m_player = new Player(
            32, 
            0, 
            m_level, 
            c.m_inputsListener, 
            m_cam, 
            Integer.parseInt(
                    Settings.getInstance().getConfigValue("difficulty")
            ),
            c
        );
        m_player.setScore(0);
        
        c.m_profiler.addObjectToDebug(m_player, new String[]{"getPosX", "getPosY", "getState"});
        c.m_profiler.addObjectToDebug(m_level, new String[]{"getWeather"});
        
        m_level.addPlayer(m_player);
        
        m_alpha = 255;
        m_alphaMax = 128;
        if(Integer.parseInt(Settings.getInstance().getConfigValue("difficulty")) == 5)
        {
            m_timer = true;
            m_timeF = TimerThread.MILLI;
        }
        
        m_glueX2 = m_level.getBackgrounds().get(0).getWidth();
        m_glueTopX2 = m_level.getBackgrounds().get(0).getWidth();
        m_backgroundBottom = m_level.getBackgrounds().get(1);
        m_backgroundBottom2 = m_level.getBackgrounds().get(1);
        m_backgroundTop = m_stateManager.getContext().m_resourceManager.getSpritesheets("background");
        m_background2 = m_stateManager.getContext().m_resourceManager.getSpritesheets("background2");
        
        m_scoreIcon = m_stateManager.getContext().m_resourceManager.getSpritesheets("spritesheetGui2").getSubimage(82, 8, 61, 59);
        m_levelIcon = m_stateManager.getContext().m_resourceManager.getSpritesheets("spritesheetGui2").getSubimage(156, 7, 61, 61);
        
        if(m_player.getSpecies().equals("panda")){
            m_cagesIcon = m_stateManager.getContext().m_resourceManager.getSpritesheets("spritesheetGui2").getSubimage(232, 7, 61, 61);
        }
        else{
            m_cagesIcon = m_stateManager.getContext().m_resourceManager.getSpritesheets("spritesheetGui2").getSubimage(307, 7, 61, 61);
        }
        
        //m_minimap = new Minimap(screenWidth, screenHeight, (int)m_player.getPosX(), (int)m_player.getPosY(), m_level);
        
        m_timeSound = TimerThread.MILLI;
        m_soundPlayed = 0;
    }

    @Override
    public void onDestroy()
    {
        
    }

    @Override
    public void activate()
    {
        super.activate();
        
        /*ResourceManager rm = m_stateManager.getContext().m_resourceManager;
        if(m_soundPlayed == 0)
        {
            m_timeSound = TimerThread.MILLI;
            m_soundPlayed = 2;
            Sound jungle1 = rm.getMusic("jungle1");
            new Thread(jungle1::play).start();
        }
        else if(m_soundPlayed == 1 && ( TimerThread.MILLI - m_timeSound ) > 36000)
        {
            m_timeSound = TimerThread.MILLI;
            m_soundPlayed = 2;
            Sound jungle1 = rm.getMusic("jungle1");
            new Thread(jungle1::play).start();
        }
        else if(m_soundPlayed == 2 && ( TimerThread.MILLI - m_soundPlayed ) > 28000)
        {
            m_timeSound = TimerThread.MILLI;
            m_soundPlayed = 1;
            Sound jungle2 = rm.getMusic("jungle2");
            new Thread(jungle2::play).start();
        }*/
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
        /*ResourceManager rm = m_stateManager.getContext().m_resourceManager;
        
        m_guiConsole.update(dt);
        */
        if(m_stateManager.getContext().m_inputsListener.profiler.typed)
        {
            m_stateManager.getContext().m_profiler.toggleVisible();
            //m_guiConsole.toggleVisible();
        }
        /*
        if(m_soundPlayed == 1 && ( TimerThread.MILLI - m_timeSound ) > 36000)
        {
            m_timeSound = TimerThread.MILLI;
            m_soundPlayed = 2;
            Sound jungle1 = rm.getMusic("jungle1");
            new Thread(jungle1::play).start();
        }
        else if(m_soundPlayed == 2 && ( TimerThread.MILLI - m_soundPlayed ) > 28000)
        {
            m_timeSound = TimerThread.MILLI;
            m_soundPlayed = 1;
            Sound jungle2 = rm.getMusic("jungle2");
            new Thread(jungle2::play).start();
        }

        if(m_cageToFree != m_level.m_nbCages && m_timeEventFree < 200){
            m_renderFreeCageAnim = true;
            m_timeEventFree += dt;
            if(m_timeEventFree < 55)
            {
                m_eventY = Easing.bounceEaseOut(m_timeEventFree, -250, 290, 50);
            }
            else if(m_timeEventFree > 100)
            {
                m_eventY = Easing.cubicEaseIn(m_timeEventFree - 100, 70, -250, 50);
            }
        }
        else
        {
            m_renderFreeCageAnim = false;
            m_timeEventFree = 0;
            m_eventY = 0;
            m_cageToFree = m_level.m_nbCages;
        }*/

        if(m_level.getNumber() == 1)
        {
            for(int i=0;i< m_level.getEventsPos().length;i++)
            {
                if(m_player.getPosX() >= m_level.getEventsPos()[i][0] + 64 && m_player.getPosX() <= m_level.getEventsPos()[i][0] + 128 && !m_level.isViewedEvent(i))
                {
                    TutorialState ts = (TutorialState)m_stateManager.getState(StateType.TUTORIAL);
                    if(ts == null)
                    {
                        m_stateManager.switchTo(StateType.TUTORIAL);
                        ts = (TutorialState)m_stateManager.getState(StateType.TUTORIAL);
                        ts.setTutorialNumber(i);
                    }
                    else if(ts.getTutorialNumber() != i)
                    {
                        ts.setTutorialNumber(i);
                        m_stateManager.switchTo(StateType.TUTORIAL);
                    }
                }
            }
        }

        if(m_player.hasWon())
        {
            m_player.setCheckpointX(0);
            if(m_nbLevel < Defines.LEVEL_MAX)
            {
                m_stateManager.switchTo(StateType.MAP);
                MapState ms = (MapState)m_stateManager.getState(StateType.MAP);
                List<Boolean> unlockedLevels = new ArrayList<>();
                unlockedLevels.add(true);
                unlockedLevels.add(true);
                unlockedLevels.add(true);
                unlockedLevels.add(false);
                unlockedLevels.add(false);
                unlockedLevels.add(false);
                
                ms.setUnlockedLevels(unlockedLevels);
                /*m_level.setUnlocked(m_nbLevel);
                MapScene ms = new MapScene(Defines.SCREEN_WIDTH, Defines.SCREEN_HEIGHT, m_game, m_nbLevel, m_player.score, m_level.unlockedLevels);
                ms.setCagesMap(m_level.cagesMap);
                return ms;*/
            }
            else
            {
                reinit(m_nbLevel);
            }
        }
        else
        {
            if(m_displayEnd)
            {
                m_stateManager.switchTo(StateType.END);
            }
            else if(m_displayStart)
            {
                if(m_stateManager.getContext().m_inputsListener.next.typed)
                {
                    m_displayStart = false;
                    m_alpha = 0;
                }
            }
            else if(m_player.isDead())
            {
                if(m_stateManager.getContext().m_inputsListener.next.typed)
                {
                    BestScores.getInstance().insertScore(Settings.getInstance().getConfigValue("name"), m_player.getScore());
                    reinit(0);
                    m_player.die(false);
                    m_player.setScore(0);
                }
                m_player.update(dt);
            }
            else{
                if(!m_displayEvent){
                    int startX = (int)(m_player.getPosX() / Defines.TILE_SIZE) - (m_level.getNbTilesInScreenX() / 2);
                    int startY = (int)(m_player.getPosY() / Defines.TILE_SIZE) - (m_level.getNbTilesInScreenY() / 2);
                    if(startX < 0)startX = 0;
                    if(startY < 0)startY = 0;
                    m_level.update(dt, startX, startY);
                    m_player.update(dt);

                    //m_minimap.update((int)m_player.getPosX(), (int)m_player.getPosY());
                    m_cam.update(m_player);
                    m_weatherManager.update(dt);
                }

                if(m_minutes >= m_maxTimeHardcore)
                {
                    m_player.die(true);
                }
                
                if(m_stateManager.getContext().m_inputsListener.pause.enabled)
                {
                    m_stateManager.switchTo(StateType.PAUSED);
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        I18nManager i18nManager = m_stateManager.getContext().m_I18nManager;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        Screen screen = m_stateManager.getContext().m_screen;
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 36.0f);
        Font fontM = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 24.0f);
        Font fontS = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 17.0f);
        
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        //RENDERING
        if(m_displayStart)
        {
            if(m_alpha > 0)
                m_alpha--;
            
            g.drawImage(m_background2, 0, 0, screenWidth, screenHeight, null);
            g.setColor(Color.BLACK);
            g.setFont(fontM);
            FontMetrics metrics = g.getFontMetrics(fontM);
            String startTxt1 = i18nManager.trans("startTxt1");
            int txt1W = metrics.stringWidth(startTxt1);
            g.drawString(startTxt1, screenWidth/2 - txt1W/2, 330);
            
            String startTxt2 = i18nManager.trans("startTxt2");
            int txt2W = metrics.stringWidth(startTxt2);
            g.drawString(startTxt2, screenWidth/2 - txt2W/2, 390);
            
            String startTxt3 = i18nManager.trans("startTxt3");
            int txt3W = metrics.stringWidth(startTxt3);
            g.drawString(startTxt3, screenWidth/2-txt3W/2, 450);
            
            g.setFont(fontS);
            metrics = g.getFontMetrics(fontS);
            String startTxt4 = i18nManager.trans("startTxt4");
            int txt4W = metrics.stringWidth(startTxt4);
            g.drawString(startTxt4, screenWidth/2-txt4W/2, 520);
            
            g.setColor(new Color(0, 0, 0, m_alpha));
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
        else
        {
            //Clear Screen
            g.setColor(new Color(153, 217, 234));
            g.fillRect(0, 0, screenWidth, screenHeight);

            //Background render
            if(m_player.getPosX() + m_player.getBounds().width/2 >= m_glueX + 1.5 * m_backgroundBottom.getWidth())
            {
                m_glueX += 2 * m_backgroundBottom.getWidth();
            }
            else if(m_player.getPosX() + m_player.getBounds().width/2 < m_glueX - m_backgroundBottom.getWidth()/2)
            {
                if(m_glueX > 0)
                {
                    m_glueX -= 2 * m_backgroundBottom.getWidth();
                }
            }
            if(m_player.getPosX() + (m_player.getBounds().width/2) >= m_glueX2 + 1.5 * m_backgroundBottom2.getWidth())
            {
                m_glueX2 += 2 * m_backgroundBottom2.getWidth();
            }
            else if(m_player.getPosX() + m_player.getBounds().width/2 < m_glueX2 - m_backgroundBottom2.getWidth()/2)
            {
                if(m_glueX2 >= m_backgroundBottom2.getWidth())
                {
                    m_glueX2 -= 2 * m_backgroundBottom2.getWidth();
                }
            }
            
            g.drawImage(m_backgroundBottom, (int)(m_glueX - m_cam.m_x), screenHeight - m_backgroundBottom.getHeight(), null);
            g.drawImage(m_backgroundBottom2, (int)(m_glueX2 - m_cam.m_x), screenHeight - m_backgroundBottom2.getHeight(), null);
            
            /*if(m_player.getPosX() + m_player.getBounds().width/2 + (m_cam.m_x/4) >= m_glueTopX + 1.5 * m_backgroundTop.getWidth())
            {
                m_glueTopX += 2 * m_backgroundTop.getWidth();
            }
            else if(m_player.getPosX() + m_player.getBounds().width/2  + (m_cam.m_x/4) < m_glueTopX - (m_background.getWidth()/2))
            {
                if(m_glueTopX > 0)
                {
                    m_glueTopX -= 2 * m_backgroundTop.getWidth();
                }
            }
            if(m_player.getPosX() + m_player.getBounds().width/2 + (m_cam.m_x/4) >= m_glueTopX2 + 1.5 * m_backgroundTop2.getWidth())
            {
                m_glueTopX2 += 2 * m_backgroundTop2.getWidth();
            }
            else if(m_player.getPosX() + m_player.getBounds().width/2 + (m_cam.m_x/4) < m_glueTopX2 - m_backgroundTop2.getWidth()/2)
            {
                if(m_glueTopX2 >= m_backgroundTop2.getWidth())
                {
                    m_glueTopX2 -= 2 * m_backgroundTop2.getWidth();
                }
            }*/
            
            g.translate(-m_cam.m_x/4, 0);
            BufferedImage bg = m_level.getBackgrounds().get(0);
            g.drawImage(bg, (int)(m_glueTopX - (m_cam.m_x)), screenHeight - bg.getHeight(), null);
            g.drawImage(bg, (int)(m_glueTopX2 - (m_cam.m_x)), screenHeight - bg.getHeight(), null);
            g.translate(m_cam.m_x/4, 0);
            g.translate(-m_cam.m_x, -m_cam.m_y);
            
            //Map Render
            int startX = (int)(m_player.getPosX() / Defines.TILE_SIZE) - (m_level.getNbTilesInScreenX() / 2);
            int startY = (int)(m_player.getPosY() / Defines.TILE_SIZE) - (m_level.getNbTilesInScreenY() / 2);
            if(startX > m_level.getNbTilesW() - m_level.getNbTilesInScreenX())startX = m_level.getNbTilesW() - m_level.getNbTilesInScreenX();
            if(startY > m_level.getNbTilesH() - m_level.getNbTilesInScreenY())startY = m_level.getNbTilesH() - m_level.getNbTilesInScreenY();
            if(startX < 0)startX = 0;
            if(startY < 0)startY = 0;
            
            m_level.render(g, startX, startY);
            
            m_player.render(g, false);

            if(m_displayEvent)
            {
                g.drawImage(m_monkeySpriteSheet.getSubimage(105 * (int)(m_timeMonkey / 4), 0, 105, 107), m_level.getEventsPos()[m_eventNumber][0] + 139, m_level.getEventsPos()[m_eventNumber][1] - 81, null);
            }
            
            g.translate(m_cam.m_x, m_cam.m_y);
            
            //g.drawImage(m_foregroundGame, 0, 300, null);
            
            m_weatherManager.render(g);
            
            //Render GUI
            renderGUI(g);
            
            if(m_player.isDead())
            {
                if(m_alpha < m_alphaMax)
                {
                    m_alpha += 1;
                }
                
                g.setColor(new Color(206, 0, 31, m_alpha));
                g.fillRect(0, 0, screenWidth, screenHeight);
                
                FontMetrics metrics = g.getFontMetrics(font);
                String deathMsg = i18nManager.trans("deathMsg");
                int deathMsgWidth = metrics.stringWidth(deathMsg);
                g.setFont(font);
                g.setColor(Color.BLACK);
                g.drawString(deathMsg, screenWidth/2 - deathMsgWidth/2, screenHeight/2 - 60);
                
                metrics = g.getFontMetrics(fontM);
                String respawn = i18nManager.trans("respawn");
                int respawnW = metrics.stringWidth(respawn);
                g.setFont(fontM);
                g.drawString(respawn, screenWidth/2-respawnW/2, screenHeight/2);
            }
            //END REDNERING
            ///////////////////////////////////////////
            
            //MINIMAP RENDERING
            /*if(m_stateManager.getContext().m_inputsListener.minimap.enabled)
            {
                m_minimap.render(g);
            }*/
        }
        
        //m_guiConsole.render(g);
    }
    
    public void setPlayer(Player player)
    {
        m_player = player;
    }
    
    public void setLevel(Level level)
    {
        m_level = level;
    }
    
    /**
     * 
     * @param lvl 
     */
    public void reinit(int lvl)
    {
        /*System.out.println("ici");
        m_alpha = 0;
        m_timeF = TimerThread.MILLI;
        
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        int screenHeight = screen.getContentPane().getHeight();
        
        if(m_nbLevel < Defines.LEVEL_MAX || m_player.isDead()){
            m_nbLevel += lvl;
            
            if(lvl != 0)
            {
                m_level = new LevelOld(m_nbLevel);
                m_level.setNbTilesInScreenX(screenWidth);
                m_level.setNbTilesInScreenY(screenHeight);
            }
            else
            {
                m_player.setIsRespawning(true);
            }
            m_player.setLevel(m_level);
            m_level.addPlayer(m_player);
            if(m_player.getCheckpointX() != 0)
            {
                m_player.setPosX(m_player.getCheckpointX());
            }
            else
            {
                m_player.setPosX(32);
            }
            
            if(lvl != 0)
            {
                m_player.setPosY(460 - ((m_nbLevel-1)*8));
                m_player.reloadSpritesheet(m_nbLevel);
            }
            else
            {
                if(m_player.getCheckpointY() != 0)
                {
                    m_player.setPosY(m_player.getCheckpointY());
                }
                else
                {
                    m_player.setPosY(460);
                }
            }
            m_player.die(false);
            m_player.setWin(false);
            m_renderFreeCageAnim = false;
        }
        else
        {
            m_displayEnd = true;
            m_player.setWin(false);
        }*/
    }
    
    public void renderFreeCageAnim(Graphics g)
    {
        /*Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        
        g.drawImage(m_spritesheetGui2.getSubimage(0, 547, 281, 132), screenWidth/2 - 140, m_eventY, null);
        for(int i=0; i<m_level.getFreeCages();i++)
        {
            g.drawImage(m_spritesheetGui2.getSubimage(282, 548, 37, 36), screenWidth/2 - 140 +((i) * 40 + 39), m_eventY + 78, null);
        }
        for(int i=0;i<m_level.m_nbCages;i++)
        {
            g.drawImage(m_spritesheetGui2.getSubimage(282, 586, 37, 36), screenWidth/2 - 140 + ((i + m_level.getFreeCages()) * 40 + 39), m_eventY + 78, null);
        }*/
    }
    
    /**
     * 
     * @param g 
     */
    public void renderGUI(Graphics g)
    {
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 22.0f);
        Font fontS = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 17.0f);
        
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(55, 85, 120, 30, 5, 5);
        g.fillRoundRect(55, 27, 60, 30, 5, 5);
        g.fillRoundRect(170, 27, 60, 30, 5, 5);
        
        g.drawImage(m_scoreIcon, 6, 70, null);
        g.drawImage(m_levelIcon, 6, 10, null);
        g.drawImage(m_cagesIcon, 120, 10, null);
        
        g.setColor(Color.WHITE);
        g.setFont(fontS);
        FontMetrics m = g.getFontMetrics(fontS);
        int scoreW = m.stringWidth("" + m_player.getScore());
        g.drawString("" + m_player.getScore(), 165 - scoreW, 106);
        g.drawString("" + m_nbLevel, 95, 47);
        g.drawString("" + m_level.getFreeCages(), 210, 47);

        if(Integer.parseInt(Settings.getInstance().getConfigValue("difficulty")) == Settings.DIFFICULTY_HARDCORE)
        {
            g.setColor(new Color(0,0,0,160));
            g.fillRoundRect(640, 27, 100, 30, 5, 5);
            g.drawImage(m_timeIcon, 722, 3, null);
            if(m_timer)
            {
                if(!m_player.isDead())
                {
                    m_minutes = (int)((double)(TimerThread.MILLI - m_timeF)/60000);
                    m_secondes = (int)((double)(TimerThread.MILLI - m_timeF - m_minutes*60000)/1000);
                }
                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString((String.format("%02d", m_minutes))+":"+(String.format("%02d", m_secondes)), 650, 50);
            }
        }
    }
    
    /**
     * 
     * @param cagesMap 
     */
    public void setLevelCagesMap(List<List<CageEntity>> cagesMap)
    {
        /*for(int i=0;i<cagesMap.size();i++)
        {
            if(i + 1 == m_nbLevel)
                m_level.setCagesInLevel(cagesMap.get(i));
            else
                m_level.m_cagesMap.set(i, cagesMap.get(i));
        }*/
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
    
    public boolean save(int slotId)
    {
        /*Save s = Save.getInstance();
        s.saveGame(1, m_level, m_player);*/
        return true;
    }
}
