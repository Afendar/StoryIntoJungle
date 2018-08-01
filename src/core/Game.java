package core;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
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
    private boolean m_running, m_paused;

    private Thread m_tGame;
    private Font m_font, m_fontD;
    private int m_nbEntities;
    private ResourceBundle m_bundle;
    private int m_elapsedTime, m_lastTime, m_pauseTime;
    private Runtime m_instance;
    private Profiler m_profiler;
    private int m_frame, m_memoryUsed;

    private final Context m_context;
    private final StateManager m_stateManager;

    /**
     *
     * @param profileName
     */
    public Game(String profileName)
    {
        m_running = false;
        m_paused = false;
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

        m_profiler = Profiler.getInstance();
        m_profiler.addGame(this);

        try
        {
            URL url = getClass().getResource("/fonts/kaushanscriptregular.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_font = m_font.deriveFont(Font.PLAIN, 36.0f);

            url = getClass().getResource("/fonts/arial.ttf");
            m_fontD = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_fontD = m_fontD.deriveFont(Font.PLAIN, 18.0f);
        }
        catch(FontFormatException | IOException e)
        {
            e.getMessage();
        }

        m_context = new Context();
        m_context.m_inputsListener = new InputsListeners(this);
        m_context.m_I18nManager = I18nManager.getInstance();
        m_context.m_resourceManager = ResourceManager.getInstance();
        
        m_context.m_profileName = profileName;
        
        m_context.m_logger = Logger.getLogger("logger");
        if(Defines.DEV)
        {
            m_context.m_logger.addHandler(new ConsoleHandler());
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
                FileHandler handler = new FileHandler(Defines.LOGS_DIRECTORY + sdf.format(new Date()) + ".log", true);
                handler.setFormatter(new SimpleFormatter()
                {
                    @Override
                    public String format(LogRecord record)
                    {
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                        return String.format("[%s] [%s] %s%n", sdf2.format(new Date()), record.getLevel(), record.getMessage());
                    }
                });
                m_context.m_logger.addHandler(handler);
            }
            catch(IOException e)
            {
                m_context.m_logger.addHandler(new ConsoleHandler());
            }
        }

        m_stateManager = new StateManager(m_context);
        m_context.m_screen = new Screen(this);

        m_stateManager.switchTo(StateType.MAP);
        start();
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
        m_running = false;
    }

    @Override
    public void run()
    {
        long startTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double nsms = 1000000000 / 60;
        int frameCpt = 0;

        boolean needUpdate;
        m_lastTime = TimerThread.MILLI;
        m_pauseTime = 0;

        while(m_running)
        {
            long current = System.nanoTime();

            try
            {
                Thread.sleep(2);
            }
            catch(InterruptedException e)
            {
            }

            needUpdate = false;
            double delta = (current - lastTime) / nsms;

            if((current - lastTime) / nsms >= 1)
            {
                frameCpt++;
                lastTime = current;
                needUpdate = true;
            }

            repaint();

            if(needUpdate)
            {
                this.update(delta);
            }

            if(System.currentTimeMillis() - startTime >= 1000)
            {
                m_memoryUsed = (int) ((m_instance.totalMemory() - m_instance.freeMemory()) / 1024) / 1024;
                m_frame = frameCpt;
                frameCpt = 0;
                startTime = System.currentTimeMillis();
            }

            m_profiler.update(Integer.toString(m_frame), Integer.toString(m_memoryUsed));
        }
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

        if(m_context.m_inputsListener.profiler.typed)
        {
            m_profiler.toggleVisible();
        }
    }

    @Override
    public void paint(Graphics g)
    {
        requestFocus();

        if(m_profiler.isVisible())
        {
            m_profiler.render(g);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        m_stateManager.render(g2d);
    }
    
    public String getProfileName()
    {
        return m_context.m_profileName;
    }
}