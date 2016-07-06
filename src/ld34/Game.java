package ld34;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import ld34.scene.GameScene;
import ld34.scene.MenuScene;
import ld34.scene.Scene;

public class Game extends Canvas implements Runnable {

    public boolean running, paused;
    public Thread tgame;
    public Scene gs;
    public InputsListeners listener;
    public Locale langs[] = {new Locale("en","EN"), new Locale("fr", "FR")};
    public Font font;
    public int w;
    public int h;
    public ResourceBundle bundle;
    public int elapsedTime, lastTime, pauseTime;
    
    public Game(int w, int h){
        
        this.running = false;
        this.paused = false;
        
        this.w = w;
        this.h = h;
        this.setMinimumSize(new Dimension(w, h));
        this.setMaximumSize(new Dimension(w, h));
        this.setPreferredSize(new Dimension(w, h));
        this.setSize(new Dimension(w, h));
        
        this.listener = new InputsListeners(this);
        
        this.gs = new MenuScene(w, h, this);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
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
        int frame = 0;
        double nsms = 1000000000 / 60;
        
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
            
            if((current - lastTime) / nsms  >= 1)
            {         
                //tick
                frame++;
                lastTime = current;
                needUpdate = true;
            }
            
            this.render();
            
            if(needUpdate)
            {
                this.update();
            }
            
            if(System.currentTimeMillis() - startTime >= 1000)
            {
                System.out.println("FPS : " + frame);
                frame = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
    
    public void update(){
        this.requestFocus();
        this.elapsedTime = TimerThread.MILLI - this.lastTime;
        this.lastTime = TimerThread.MILLI;
        if(this.paused  && this.gs instanceof GameScene){
            this.gs = ((GameScene)(this.gs)).updatePause(this.elapsedTime);
        }
        
        if(this.hasFocus() && !this.paused){
            this.gs = this.gs.update();
        }
        this.listener.update();
    }
    
    public void render(){
        
        BufferStrategy bs = this.getBufferStrategy();
        
        if(bs == null){
            this.createBufferStrategy(2);
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
        
        g.dispose();
        bs.show();
    }
}
