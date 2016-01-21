package ld34;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Locale;
import ld34.scene.MenuScene;
import ld34.scene.Scene;

public class Game extends Canvas implements Runnable {

    public boolean running;
    public Thread tgame;
    public Scene gs;
    public InputsListeners listener;
    public int difficulty, lang;
    public Locale langs[] = {new Locale("en","EN"), new Locale("fr", "FR")};
    
    public Game(int w, int h){
        
        this.running = false;
        
        this.setMinimumSize(new Dimension(w, h));
        this.setMaximumSize(new Dimension(w, h));
        this.setSize(new Dimension(w, h));
        this.difficulty = 0;
        this.lang = 0;
        
        this.listener = new InputsListeners(this);
        
        this.gs = new MenuScene(w, h, this);
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
        
        this.gs = this.gs.update();
    }
    
    public void render(){
        
        BufferStrategy bs = this.getBufferStrategy();
        
        if(bs == null){
            this.createBufferStrategy(2);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        
        this.gs.render(g);

        g.dispose();
        bs.show();
    }
    
}
