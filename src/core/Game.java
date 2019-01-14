package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.swing.JPanel;
import ld34.profile.Save;
import ld34.profile.Settings;
import profiler.Profiler;

/**
 * Game class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class Game extends JPanel implements Runnable
{
    private boolean m_running;

    private Thread m_tGame;
    private int m_nbEntities;
    private int m_elapsedTime, m_lastTime, m_pauseTime;
    private Runtime m_instance;
    private int m_frame, m_memoryUsed;
    private StreamHandler m_handler;

    private Context m_context;
    private StateManager m_stateManager;

    /**
     *
     * @param profileName
     */
    public Game(String profileName)
    {
        init(profileName);
        m_stateManager.switchTo(StateType.MAIN_MENU);
        start();
    }

    /**
     * 
     * @param profileName 
     */
    private void init(String profileName)
    {
        m_running = false;
        m_instance = Runtime.getRuntime();
        
        Settings.init(profileName);
        Save.init(profileName);
        
        int res = Integer.parseInt(Settings.getInstance().getConfigValue("resolution"));
        int width, height;
        switch(res)
        {
            case Screen.RES_2X:
                width = Screen.RES_2X_WIDTH;
                height = Screen.RES_2X_HEIGHT;
                break;
            case Screen.RES_15X:
                width = Screen.RES_15X_WIDTH;
                height = Screen.RES_15X_HEIGHT;
                break;
            default:
            case Screen.RES_1X:
                width = Screen.RES_1X_WIDTH;
                height = Screen.RES_1X_HEIGHT;
                break;
        }
        
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        m_frame = m_memoryUsed = m_nbEntities = 0;

        m_context = new Context();
        m_context.m_inputsListener = new InputsListeners(this);
        m_context.m_I18nManager = I18nManager.getInstance();
        m_context.m_I18nManager.setLanguage(
                (Integer.parseInt(Settings.getInstance().getConfigValue("lang")) == 1) ? I18nManager.Language.FRENCH : I18nManager.Language.ENGLISH
        );
        m_context.m_resourceManager = ResourceManager.getInstance();
        m_context.m_profileName = profileName;
        m_context.m_logger = Logger.getLogger("logger");
        
        if(Defines.DEV)
        {
            m_handler = new ConsoleHandler();
            m_context.m_logger.addHandler(m_handler);
        }
        else
        {
            try
            {
                File logDir = new File(Defines.LOGS_DIRECTORY); 
		if(!logDir.exists())
                {
                    logDir.mkdir();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                m_handler = new FileHandler(Defines.LOGS_DIRECTORY + sdf.format(new Date()) + ".log", true);
                m_handler.setFormatter(new SimpleFormatter()
                {
                    @Override
                    public String format(LogRecord record)
                    {
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                        return String.format("[%s] [%s] %s%n", sdf2.format(new Date()), record.getLevel(), record.getMessage());
                    }
                });
                m_context.m_logger.addHandler(m_handler);
            }
            catch(IOException e)
            {
                m_handler = new ConsoleHandler();
                m_context.m_logger.addHandler(m_handler);
            }
        }

        m_stateManager = new StateManager(m_context);
        m_context.m_screen = new Screen(this);
        m_context.m_profiler = Profiler.getInstance();
        m_context.m_profiler.init(this, m_context.m_logger);
    }
    
    /**
     *
     */
    private void start()
    {
        m_context.m_logger.log(Level.INFO, "Starting Story Into Jungle v" + Defines.VERSION);
        if(m_running)
        {
            return;
        }

        m_running = true;
        m_tGame = new Thread(this, "gameThread");
        m_tGame.start();
    }

    /**
     *
     */
    public void stop()
    {
        m_context.m_logger.log(Level.INFO, "Stopping game.");
        m_running = false;
    }

    @Override
    public void run()
    {
        int frameCpt = 0;
        long lastFpsTime = 0;
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        while(m_running)
        {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            lastFpsTime += updateLength;
            frameCpt++;

            if(lastFpsTime >= 1000000000)
            {
                m_memoryUsed = (int) ((m_instance.totalMemory() - m_instance.freeMemory()) / 1024) / 1024;
                m_frame = frameCpt;
                System.out.println("(FPS: " + frameCpt + ")");
                lastFpsTime = 0;
                frameCpt = 0;
            }
            
            update(delta);
            repaint();

            m_context.m_profiler.update(Integer.toString(m_frame), Integer.toString(m_memoryUsed));
            
            try
            {
                Thread.sleep((System.nanoTime() - lastLoopTime + OPTIMAL_TIME) / 1000000);
            }
            catch(InterruptedException e)
            {
                m_context.m_logger.log(Level.SEVERE, e.toString());
            }
        }
        
        m_handler.close();
    }

    /**
     *
     * @param dt
     */
    public void update(double dt)
    {
        m_stateManager.update(dt);
        
        m_context.m_inputsListener.update();

        if(m_context.m_inputsListener.fullscreen.typed)
        {
            m_context.m_screen.setFullscreen(!m_context.m_screen.isFullscreen());
        }
    }

    @Override
    public void paint(Graphics g)
    {   
        if(!m_running)
        {
            return;
        }
        
        requestFocus();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, m_context.m_screen.getWidth(), m_context.m_screen.getHeight());
        
        m_stateManager.render(g2d);
        
        if(m_context.m_profiler.isVisible())
        {
            m_context.m_profiler.render(g);
        }
    }
    
    /**
     * 
     * @return 
     */
    public String getProfileName()
    {
        return m_context.m_profileName;
    }
}