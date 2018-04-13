package core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import profiler.Profiler;

/**
 * Game class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Game extends Canvas implements Runnable
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
     * @param w
     * @param h 
     */
    public Game()
    {
        this.running = false;
        this.paused = false;
        this.instance = Runtime.getRuntime();
        this.setMinimumSize(new Dimension(Defines.SCREEN_WIDTH, Defines.SCREEN_WIDTH));
        this.setMaximumSize(new Dimension(Defines.SCREEN_WIDTH, Defines.SCREEN_WIDTH));
        this.setPreferredSize(new Dimension(Defines.SCREEN_WIDTH, Defines.SCREEN_WIDTH));
        this.setSize(new Dimension(Defines.SCREEN_WIDTH, Defines.SCREEN_WIDTH));
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
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }
        
        m_context = new Context();
        m_context.m_inputsListener = new InputsListeners(this);
        m_context.m_I18nManager = I18nManager.getInstance();
        m_context.m_resourceManager = ResourceManager.getInstance();

        m_stateManager = new StateManager(m_context);
        m_stateManager.switchTo(StateType.INTRO);
    }
    
    /**
     * 
     */
    public void start(){
        
        if(this.running)
            return;
        
        this.running = true;
        this.tgame = new Thread(this, "gameThread");
        this.tgame.start();
        
    }
    
    /**
     * 
     */
    public void stop(){
        this.running = false;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double nsms = 1000000000 / 60;
        int frameCpt = 0;
        
        boolean needUpdate;
        this.lastTime = TimerThread.MILLI;
        this.pauseTime= 0;
        
        while(this.running)
        {
            long current = System.nanoTime(); 
            
            try{
                Thread.sleep(2);
            }
            catch(InterruptedException e){}
            
            needUpdate = false;
            double delta = (current - lastTime) / nsms;
            
            if((current - lastTime) / nsms  >= 1)
            {         
                //tick
                frameCpt++;
                lastTime = current;
                needUpdate = true;
            }
            
            this.render();
            
            if(needUpdate)
            {
                this.update(delta);
            }
            
            if(System.currentTimeMillis() - startTime >= 1000)
            {
                this.memoryUsed = (int)((instance.totalMemory() - instance.freeMemory()) / 1024) / 1024;
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
    public void update(double dt){

        /*if(this.hasFocus()){
            this.gs = this.gs.update(dt);
        }*/
        
        m_stateManager.update(dt);
        
        m_context.m_inputsListener.update();
        
        if(m_context.m_inputsListener.profiler.typed )
        {
            this.profiler.toggleVisible();
        }
    }
    
    /**
     * 
     */
    public void render(){
        
        BufferStrategy bs = this.getBufferStrategy();
        
        if(bs == null){
            this.createBufferStrategy(2);
            requestFocus();
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        
        //this.gs.render(g);
        
        /*if(!this.paused && (!this.hasFocus() || this.listener.mouseExited || this.listener.pause.enabled) && this.gs instanceof GameScene && (TimerThread.MILLI - this.pauseTime) > 400)
        {
            GameScene scene = (GameScene)this.gs;
            this.pauseTime = TimerThread.MILLI;
            this.paused = true;
            scene.currentScene = GameScene.popinsScenes.MENU;
        }
        if(this.paused && this.listener.pause.enabled && this.gs instanceof GameScene && (TimerThread.MILLI - this.pauseTime) > 400)
        {
            GameScene gameScene = (GameScene)this.gs;
            if(gameScene.dialog != null){
                gameScene.dialog = null;
            }
            else{
                if(gameScene.currentScene != GameScene.popinsScenes.MENU && ((GameScene)this.gs).currentScene != GameScene.popinsScenes.NONE){
                    this.listener.pause.enabled = false;
                    gameScene.currentScene = GameScene.popinsScenes.MENU;
                }
                else{
                    this.pauseTime = TimerThread.MILLI;
                    int[][] posBtns = {
                        {this.w/2 - 107 - (15*30), 140},
                        {this.w/2 - 107 - (17*30), 240},
                        {this.w/2 - 107 - (19*30), 340},
                        {this.w/2 - 107 - (21*30), 440}
                    };
                    gameScene.btnPosMenu = posBtns;
                    this.paused = false;
                }
            }
        }*/
        
        if(this.profiler.isVisible())
        {
            this.profiler.render(g);
        }
        
        Graphics2D g2d = (Graphics2D)g;
        m_stateManager.render(g2d);
        
        g.dispose();
        bs.show();
    }
}