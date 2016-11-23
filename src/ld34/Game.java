package ld34;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import ld34.scene.GameScene;
import ld34.scene.MapScene;
import ld34.scene.MenuScene;
import ld34.scene.Scene;
import ld34.scene.SplashScene;

public class Game extends Canvas implements Runnable {

    public boolean running, paused;
    public Thread tgame;
    public Scene gs;
    public InputsListeners listener;
    public Locale langs[] = {new Locale("en","EN"), new Locale("fr", "FR")};
    public Font font, fontD;
    public int w;
    public int h;
    public ResourceBundle bundle;
    public int elapsedTime, lastTime, pauseTime;
    public Runtime instance;
    public boolean DEBUG_MODE = false;
    public int frame, memoryUsed;
    
    public Game(int w, int h){
        
        this.running = false;
        this.paused = false;
        this.instance = Runtime.getRuntime();
        this.w = w;
        this.h = h;
        this.setMinimumSize(new Dimension(w, h));
        this.setMaximumSize(new Dimension(w, h));
        this.setPreferredSize(new Dimension(w, h));
        this.setSize(new Dimension(w, h));
        this.frame = this.memoryUsed = 0;
        
        this.listener = new InputsListeners(this);
        
        this.gs = new MenuScene(w, h, this);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
            
            url = this.getClass().getResource("/fonts/arial.ttf");
            this.fontD = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontD = this.fontD.deriveFont(Font.PLAIN, 18.0f);
        }
        catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
    }
    
    public void start(){
        
        if(this.running)
            return;
        
        this.running = true;
        this.tgame = new Thread(this, "gameThread");
        this.tgame.start();
        
    }
    
    public void stop(){
        this.running = false;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double nsms = 1000000000 / 60;
        int frameCpt = 0;
        
        boolean needUpdate = true;
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
                //System.out.println("Used Memory: " + this.memoryUsed + " Mo");
                this.frame = frameCpt;
                //System.out.println("FPS : " + this.frame);
                frameCpt = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
    
    public void update(double dt){
        this.elapsedTime = TimerThread.MILLI - this.lastTime;
        this.lastTime = TimerThread.MILLI;
        if(this.paused  && this.gs instanceof GameScene){
            this.gs = ((GameScene)(this.gs)).updatePause(this.elapsedTime);
        }
        
        if(this.hasFocus() && !this.paused){
            this.gs = this.gs.update(dt);
        }
        this.listener.update();
        
        if(this.listener.profiler.typed )
        {
            this.DEBUG_MODE = !this.DEBUG_MODE;
        }
    }
    
    public void render(){
        
        BufferStrategy bs = this.getBufferStrategy();
        
        if(bs == null){
            this.createBufferStrategy(2);
            requestFocus();
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        
        this.gs.render(g);
        
        if(!this.paused && (!this.hasFocus() || this.listener.mouseExited || this.listener.pause.enabled) && this.gs instanceof GameScene && (TimerThread.MILLI - this.pauseTime) > 400)
        {
            this.pauseTime = TimerThread.MILLI;
            this.paused = true;
        }
        if(this.paused && this.listener.pause.enabled && this.gs instanceof GameScene && (TimerThread.MILLI - this.pauseTime) > 400)
        {
            this.pauseTime = TimerThread.MILLI;
            this.paused = false;
        }

        if(this.paused && this.gs instanceof GameScene)
        {
            ((GameScene)(this.gs)).renderPause(g);
        }
        
        if(this.DEBUG_MODE)
        {
            this.renderDebug(g);
        }
        
        g.dispose();
        bs.show();
    }
    
    public void renderDebug(Graphics g)
    {
        FontMetrics fm = g.getFontMetrics(this.fontD);
        String text = "FPS : ";
        text += this.frame;
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setFont(this.fontD);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 30 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 30);
        
        text = "Memory : " + this.memoryUsed + "Mo";
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 57 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 60);
        
        text = "Java : " + System.getProperty("java.version") + "  x" + System.getProperty("sun.arch.data.model") + " bit";
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(this.w - (int)rect.getWidth() - 40, 30 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, this.w - (int)rect.getWidth() - 30, 30);
                
        text = "Story Into Jungle : v" + Defines.version;
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(this.w - (int)rect.getWidth() - 40, 60 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, this.w - (int)rect.getWidth() - 30, 60);
    }
}
