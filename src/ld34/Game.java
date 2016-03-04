package ld34;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import ld34.scene.MenuScene;
import ld34.scene.Scene;

public class Game extends Canvas implements Runnable {

    public boolean running;
    public Thread tgame;
    public Scene gs;
    public InputsListeners listener;
    public Locale langs[] = {new Locale("en","EN"), new Locale("fr", "FR")};
    public String fileOptions = "settings.dat";
    public String[] configsLabel = {"Lang", "Difficulty"};
    public int[] defaultConfigs = {0, 0};
    public int[] configs;
    
    public Game(int w, int h){
        
        this.running = false;
        
        this.setMinimumSize(new Dimension(w, h));
        this.setMaximumSize(new Dimension(w, h));
        this.setSize(new Dimension(w, h));
        
        this.loadConfigs();
        
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
                //System.out.println("FPS : " + frame);
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
    
    
    public void loadConfigs(){
        
        File f = new File(this.fileOptions);
        this.configs = this.defaultConfigs;
        
        if(f.exists() && !f.isDirectory()){
            //read configurations
            try{
                BufferedReader br = new BufferedReader(new FileReader(this.fileOptions));
                String line = null;
                int i = 0;
                while((line = br.readLine()) != null){
                    String[] strSplited = line.split(":");
                    this.configs[i] = Integer.parseInt(strSplited[1]);
                    i++;
                }
                br.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            //file not exist add default configuration file
            this.saveConfigs();
        }
    }
    
    public void saveConfigs(){
        try{
            PrintWriter pw = new PrintWriter(
                                new BufferedWriter(
                                    new FileWriter(this.fileOptions)));
            for(int i=0;i<this.configs.length;i++){
                pw.println(this.configsLabel[i] + ":" + this.configs[i]);
            }
            pw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
