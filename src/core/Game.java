package core;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import profiler.Profiler;

/**
 * Game class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class Game extends JPanel implements Runnable
{
    public boolean running, paused;

    public Thread tgame;
    public Font font, fontD;
    public int nbEntities;
    public ResourceBundle bundle;
    public int elapsedTime, lastTime, pauseTime;
    public Runtime instance;
    public Profiler profiler;
    public int frame, memoryUsed;

    private final Context m_context;
    private final StateManager m_stateManager;

    /**
     *
     */
    public Game()
    {
        this.running = false;
        this.paused = false;
        this.instance = Runtime.getRuntime();
        this.setMinimumSize(new Dimension(Defines.DEFAULT_SCREEN_WIDTH, Defines.DEFAULT_SCREEN_HEIGHT));
        this.setMaximumSize(new Dimension(Defines.DEFAULT_SCREEN_WIDTH, Defines.DEFAULT_SCREEN_HEIGHT));
        this.setPreferredSize(new Dimension(Defines.DEFAULT_SCREEN_WIDTH, Defines.DEFAULT_SCREEN_HEIGHT));
        this.setSize(new Dimension(Defines.DEFAULT_SCREEN_WIDTH, Defines.DEFAULT_SCREEN_HEIGHT));
        this.frame = this.memoryUsed = this.nbEntities = 0;

        this.profiler = Profiler.getInstance();
        this.profiler.addGame(this);

        try
        {
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);

            url = this.getClass().getResource("/fonts/arial.ttf");
            this.fontD = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontD = this.fontD.deriveFont(Font.PLAIN, 18.0f);
        }
        catch (FontFormatException | IOException e)
        {
            e.getMessage();
        }

        m_context = new Context();
        m_context.m_inputsListener = new InputsListeners(this);
        m_context.m_I18nManager = I18nManager.getInstance();
        m_context.m_resourceManager = ResourceManager.getInstance();

        m_stateManager = new StateManager(m_context);
        m_context.m_screen = new Screen(this);

        m_stateManager.switchTo(StateType.CREDITS);
        start();
    }

    /**
     *
     */
    private void start()
    {

        if (this.running)
        {
            return;
        }

        this.running = true;
        this.tgame = new Thread(this, "gameThread");
        this.tgame.start();

    }

    /**
     *
     */
    public void stop()
    {
        this.running = false;
    }

    @Override
    public void run()
    {
        long startTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double nsms = 1000000000 / 60;
        int frameCpt = 0;

        boolean needUpdate;
        this.lastTime = TimerThread.MILLI;
        this.pauseTime = 0;

        while (this.running)
        {
            long current = System.nanoTime();

            try
            {
                Thread.sleep(2);
            }
            catch (InterruptedException e)
            {
            }

            needUpdate = false;
            double delta = (current - lastTime) / nsms;

            if ((current - lastTime) / nsms >= 1)
            {
                frameCpt++;
                lastTime = current;
                needUpdate = true;
            }

            repaint();

            if (needUpdate)
            {
                this.update(delta);
            }

            if (System.currentTimeMillis() - startTime >= 1000)
            {
                this.memoryUsed = (int) ((instance.totalMemory() - instance.freeMemory()) / 1024) / 1024;
                this.frame = frameCpt;
                frameCpt = 0;
                startTime = System.currentTimeMillis();
            }

            this.profiler.update(Integer.toString(this.frame), Integer.toString(this.memoryUsed));
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

        if (m_context.m_inputsListener.fullscreen.typed)
        {
            m_context.m_screen.setFullscreen(!m_context.m_screen.isFullscreen());
        }

        if (m_context.m_inputsListener.profiler.typed)
        {
            this.profiler.toggleVisible();
        }
    }

    @Override
    public void paint(Graphics g)
    {

        requestFocus();

        if (this.profiler.isVisible())
        {
            this.profiler.render(g);
        }

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        m_stateManager.render(g2d);
    }
}
