package ld34.scene;

import audio.Sound;
import entity.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.Camera;
import ld34.Configs;
import ld34.Defines;
import ld34.Game;
import ld34.Minimap;
import ld34.TimerThread;
import level.Level;
import profiler.Profiler;

public class GameScene extends Scene {

    public Font font, fontS, fontM, fontB, fontSM;
    public Player player;
    public Profiler profiler;
    public Level level;
    public Camera cam;
    public String deathMsg, startTxt1, startTxt2, startTxt3, startTxt4, respawn, btnBack, pausemsg, btnContinue;
    public BufferedImage background2, bgGui, gui, bgGui2, clockGui, backgroundBottom, backgroundTop,
            backgroundBottomAll, backgroundBottom2, backgroundTop2, backgroundTopAll, guiAssets, scoreIcon, timeIcon, levelIcon, cagesIcon;
    public int nbLevel, selectedItem;
    public String bestScores = "best_scores.dat";
    public boolean displayEnd, displayStart;
    public int alpha, alphaMax;
    public int time = 0, glueX = 0, glueX2 = 0, glueTopX = 0, glueTopX2 = 0;
    public int timeF = 0;
    public int minutes = 0;
    public int secondes = 0;
    public int maxTimeHardcore = 1, soundPlayed, timeSound;
    public boolean timer = false;
    public Minimap minimap;
    
    public GameScene(int w, int h, Game game, int lvl){
        super(w, h, game);
        
        this.nbLevel = lvl;
        this.displayEnd = false;
        this.displayStart = true;
        System.out.println("level:"+this.nbLevel);
        if(this.nbLevel > 1){
            this.displayStart = false;
        }
        
        this.level = new Level(this.nbLevel);
        this.level.setNbTilesInScreenX(game.w);
        this.level.setNbTilesInScreenY(game.h);
        
        this.cam = new Camera(0, 0, w, h, this.level);
        this.player = new Player(32, 445, this.level, this.game.listener, this.cam, Integer.parseInt(Configs.getInstance().getConfigValue("Difficulty")));
        
        this.profiler = new Profiler();
        
        this.level.addPlayer(this.player);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.fontSM = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontM = this.font.deriveFont(Font.PLAIN, 24.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 17.0f);
            this.fontB = this.font.deriveFont(Font.BOLD, 17.0f);
            
            url = this.getClass().getResource("/background2.png");
            this.background2 = ImageIO.read(url);
            
            url = this.getClass().getResource("/background_bottom.png");
            this.backgroundBottomAll = ImageIO.read(url);
            this.backgroundBottom = this.backgroundBottomAll.getSubimage(0, 0, 800, 600);
            this.backgroundBottom2 = this.backgroundBottomAll.getSubimage(800, 0, 800, 600);
            url = this.getClass().getResource("/background_top.png");
            this.backgroundTopAll = ImageIO.read(url);
            this.backgroundTop = this.backgroundTopAll.getSubimage(0, 0, 800, 600);
            this.backgroundTop2 = this.backgroundTopAll.getSubimage(800, 0, 800, 600);
            
            url = this.getClass().getResource("/gui2.png");
            this.guiAssets = ImageIO.read(url);
            this.timeIcon = this.guiAssets.getSubimage(0, 0, 75, 75);
            this.scoreIcon = this.guiAssets.getSubimage(75, 0, 75, 75);
            this.levelIcon = this.guiAssets.getSubimage(150, 0, 75, 75);
            this.cagesIcon = this.guiAssets.getSubimage(225, 0, 75, 75);
            
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        this.bgGui = this.spritesheetGui.getSubimage(0, 20, 214, 50);
        this.bgGui2 = this.spritesheetGui.getSubimage(0, 0, 214, 50);
        this.clockGui = this.spritesheetGui.getSubimage(0, 281, 55, 55);
        
        this.bundle = ResourceBundle.getBundle("lang.game", this.game.langs[Integer.parseInt(Configs.getInstance().getConfigValue("Lang"))]);
        
        this.btnBack = this.bundle.getString("backToMain");
        this.pausemsg = this.bundle.getString("pauseMsg");
        this.deathMsg = this.bundle.getString("deathMsg");
        this.startTxt1 = this.bundle.getString("startTxt1");
        this.startTxt2 = this.bundle.getString("startTxt2");
        this.startTxt3 = this.bundle.getString("startTxt3");
        this.startTxt4 = this.bundle.getString("startTxt4");
        this.respawn = this.bundle.getString("respawn");
        this.btnContinue = this.bundle.getString("continue");
        
        this.selectedItem = 0;
        
        this.alpha = 255;
        if(lvl > 1){
            this.alpha = 0;
        }
        this.alphaMax = 128;
        if(Integer.parseInt(Configs.getInstance().getConfigValue("Difficulty")) == 5){
            this.timer = true;
            this.timeF = TimerThread.MILLI;
        }
        
        this.glueX2 = this.backgroundBottom.getWidth();
        this.glueTopX2 = this.backgroundTop2.getWidth();
        
        this.minimap = new Minimap(this.w, this.h, (int)this.player.getPosX(), (int)this.player.getPosY(), this.level);
        
        this.timeSound = TimerThread.MILLI;
        this.soundPlayed = 1;
        new Thread(Sound.sf_jungle01::play).start();
    }

    public GameScene(int w, int h, Game game){
        this(w, h, game, 1);
    }
    
    public void reinit(int lvl){
        this.alpha = 0;
        this.timeF = TimerThread.MILLI;
        
        if(this.nbLevel < Defines.LEVEL_MAX || this.player.isDead){
            this.nbLevel += lvl;
            this.level = new Level(this.nbLevel);
            this.level.setNbTilesInScreenX(game.w);
            this.level.setNbTilesInScreenY(game.h);
            this.player.level = this.level;
            this.level.addPlayer(this.player);
            if(this.player.checkpointX != 0){
                this.player.setPosX(this.player.checkpointX);
            }
            else{
                this.player.setPosX(32);
            }
            
            if(lvl != 0){
                this.player.setPosY(460 - ((this.nbLevel-1)*8));
                this.player.reloadSpritesheet(this.nbLevel);
            }
            else{
                if(this.player.checkpointY != 0){
                    this.player.setPosY(this.player.checkpointY);
                }
                else{
                    this.player.setPosY(460);
                }
            }
            this.player.win = false;
        }
        else{
            this.displayEnd = true;
            this.player.win = false;
        }
        System.out.println("alpha:" + this.alpha);
    }
    
    @Override
    public Scene update(double dt) {
        if(this.game.listener.mouseExited || this.game.listener.pause.typed){
            return this;
        }
        
        if(this.soundPlayed == 1 && ( TimerThread.MILLI - this.timeSound ) > 36000)
        {
            this.timeSound = TimerThread.MILLI;
            this.soundPlayed = 2;
            new Thread(Sound.sf_jungle01::play).start();
        }
        else if(this.soundPlayed == 2 && ( TimerThread.MILLI - this.soundPlayed ) > 28000)
        {
            this.timeSound = TimerThread.MILLI;
            this.soundPlayed = 1;
            new Thread(Sound.sf_jungle02::play).start();
        }
        
        if(this.game.listener.profiler.enabled){
            this.profiler.toggleVisible();
        }
        
        if(this.player.win){
            this.player.checkpointX = 0;
            if(this.nbLevel < Defines.LEVEL_MAX){
                return new MapScene(this.w, this.h, this.game, this.nbLevel);
            }
            else{
                reinit(this.nbLevel);
            }
        }
        else{
            if(this.displayEnd){
                return new EndScene(this.w, this.h, this.game);
            }
            else if(this.displayStart){
                if(this.game.listener.next.typed){
                    this.displayStart = false;
                    this.alpha = 0;
                }
                return this;
            }
            else if(this.player.isDead){
                if(this.game.listener.next.typed){
                    saveScore();
                    reinit(0);
                    this.player.isDead = false;
                    this.player.score = 0;
                }
                this.player.update(dt);
            }
            else{
                int startX = (int)(this.player.getPosX() / Defines.TILE_SIZE) - (this.level.getNbTilesInScreenX() / 2);
                int startY = (int)(this.player.getPosY() / Defines.TILE_SIZE) - (this.level.getNbTilesInScreenY() / 2);
                if(startX < 0)startX = 0;
                if(startY < 0)startY = 0;
                this.level.update(dt, startX, startY);

                this.player.update(dt);
                
                this.minimap.update((int)this.player.getPosX(), (int)this.player.getPosY());
                this.cam.update(this.player);
                
                if(this.minutes >= this.maxTimeHardcore)
                {
                    this.player.isDead = true;
                }
            }
        }
        return this;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //RENDERING
        ///////////////////////////////////////////
        if(this.displayStart){
            if(this.alpha > 0)
                this.alpha--;
            
            g.drawImage(this.background2, 0, 0, null);
            g.setColor(Color.BLACK);
            g.setFont(this.fontM);
            FontMetrics metrics = g.getFontMetrics(this.fontM);
            int txt1W = metrics.stringWidth(this.startTxt1);
            g.drawString(this.startTxt1, this.w/2 - txt1W/2, 330);
            int txt2W = metrics.stringWidth(this.startTxt2);
            g.drawString(this.startTxt2, this.w/2 - txt2W/2, 390);
            int txt3W = metrics.stringWidth(this.startTxt3);
            g.drawString(this.startTxt3, this.w/2-txt3W/2, 450);
            
            g.setFont(this.fontS);
            metrics = g.getFontMetrics(this.fontS);
            int txt4W = metrics.stringWidth(this.startTxt4);
            g.drawString(this.startTxt4, this.w/2-txt4W/2, 520);
            
            g.setColor(new Color(0, 0, 0, this.alpha));
            g.fillRect(0, 0, this.w, this.h);
        }
        else
        {
            //Clear Screen
            g.setColor(new Color(153, 217, 234));
            g.fillRect(0, 0, this.w, this.h);

            //Background render
            if(this.player.getPosX() + (this.player.getBounds().width/2) > this.glueX + 1.5 * this.backgroundBottom.getWidth())
            {
                this.glueX += 2 * this.backgroundBottom.getWidth();
            }
            else if(this.player.getPosX() < this.glueX - (this.background.getWidth()/2))
            {
                if(this.glueX > 0)
                {
                    this.glueX -= 2 * this.backgroundBottom.getWidth();
                }
            }
            if(this.player.getPosX() + (this.player.getBounds().width/2) > this.glueX2 + 1.5 * this.backgroundBottom2.getWidth())
            {
                this.glueX2 += 2 * this.backgroundBottom2.getWidth();
            }
            else if(this.player.getPosX() < this.glueX2 - (this.background2.getWidth()/2))
            {
                if(this.glueX2 > this.background.getWidth())
                {
                    this.glueX2 -= 2 * this.backgroundBottom2.getWidth();
                }
            }
            
            g.drawImage(this.backgroundBottom, (int)(this.glueX - this.cam.x), this.h - this.backgroundBottom.getHeight(), null);
            g.drawImage(this.backgroundBottom2, (int)(this.glueX2 - this.cam.x), this.h - this.backgroundBottom2.getHeight(), null);
            
            if(this.player.getPosX() + (this.player.getBounds().width/2) > this.glueTopX + 1.5 * this.backgroundTop.getWidth() - (this.cam.x/4))
            {
                this.glueTopX += 2 * this.backgroundTop.getWidth();
            }
            else if(this.player.getPosX() < this.glueTopX - (this.background.getWidth()/2) - (this.cam.x/4))
            {
                if(this.glueTopX > 0)
                {
                    this.glueTopX -= 2 * this.backgroundTop.getWidth();
                }
            }
            if(this.player.getPosX() + (this.player.getBounds().width/2) > this.glueTopX2 + 1.5 * this.backgroundTop2.getWidth() - (this.cam.x/4))
            {
                this.glueTopX2 += 2 * this.backgroundTop2.getWidth();
            }
            else if(this.player.getPosX() < this.glueTopX2 - (this.backgroundTop2.getWidth()/2) - (this.cam.x/4))
            {
                if(this.glueTopX2 > this.background.getWidth())
                {
                    this.glueTopX2 -= 2 * this.backgroundTop2.getWidth();
                }
            }
            
            g2d.translate(-this.cam.x/4, 0);
            
            g.drawImage(this.backgroundTop, (int)(this.glueTopX - (this.cam.x)), this.h - this.backgroundTop.getHeight(), null);
            g.drawImage(this.backgroundTop2, (int)(this.glueTopX2 - (this.cam.x)), this.h - this.backgroundTop2.getHeight(), null);
            
            g2d.translate(this.cam.x/4, 0);
            
            
            g2d.translate(-this.cam.x, -this.cam.y);

            //Map Render
            
            int startX = (int)(this.player.getPosX() / Defines.TILE_SIZE) - (this.level.getNbTilesInScreenX() / 2);
            int startY = (int)(this.player.getPosY() / Defines.TILE_SIZE) - (this.level.getNbTilesInScreenY() / 2);
            if(startX < 0)startX = 0;
            if(startY < 0)startY = 0;
            
            this.level.renderFirstLayer(g, startX, startY);
            
            this.player.render(g);

            this.level.renderSecondLayer(g, startX, startY);
            
            g2d.translate(this.cam.x, this.cam.y);

            
            g.drawImage(this.foregroundGame, 0, 300, null);
            
            //Render GUI
            this.renderGUI(g);
            
            if(this.player.isDead){
                if(this.alpha < this.alphaMax){
                    this.alpha += 1;
                }
                
                g.setColor(new Color(206, 0, 31, this.alpha));
                g.fillRect(0, 0, this.w, this.h);
                
                FontMetrics metrics = g.getFontMetrics(this.font);
                int deathMsgWidth = metrics.stringWidth(this.deathMsg);
                g.setFont(this.font);
                g.setColor(Color.BLACK);
                g.drawString(this.deathMsg, this.w/2 - deathMsgWidth/2, this.h/2 - 60);
                
                metrics = g.getFontMetrics(this.fontM);
                int respawnW = metrics.stringWidth(this.respawn);
                g.setFont(this.fontM);
                g.drawString(this.respawn, this.w/2-respawnW/2, this.h/2);
                
                this.player.render(g);
            }
            //END REDNERING
            ///////////////////////////////////////////
            
            
            //MINIMAP RENDERING
            if(this.game.listener.minimap.enabled){
                minimap.render(g);
            }
        }
    }
    
    public void renderGUI(Graphics g){
        
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(55, 85, 120, 30, 5, 5);
        g.fillRoundRect(55, 27, 60, 30, 5, 5);
        g.fillRoundRect(170, 27, 60, 30, 5, 5);
        
        g.drawImage(this.scoreIcon, 6, 62, null);
        g.drawImage(this.levelIcon, 6, 3, null);
        g.drawImage(this.cagesIcon, 120, 3, null);
        
        g.setColor(Color.WHITE);
        g.setFont(this.fontS);
        FontMetrics m = g.getFontMetrics(this.fontS);
        int scoreW = m.stringWidth(""+this.player.score);
        g.drawString("" + this.player.score, 165 - scoreW, 106);

        g.drawString("" + this.nbLevel, 95, 47);
        
        g.drawString("" + this.level.nbCages, 210, 47);

        if(Integer.parseInt(Configs.getInstance().getConfigValue("Difficulty")) == 5)
        { 
            g.setColor(new Color(0,0,0,160));
            g.fillRoundRect(640, 27, 100, 30, 5, 5);
            g.drawImage(this.timeIcon, 722, 3, null);
            if(this.timer){
                if(!this.player.isDead){
                    this.minutes = (int)((double)(TimerThread.MILLI - this.timeF)/60000);
                    this.secondes = (int)((double)(TimerThread.MILLI - this.timeF - this.minutes*60000)/1000);
                }
                g.setColor(Color.WHITE);
                g.setFont(this.fontSM);
                g.drawString((String.format("%02x", this.minutes))+":"+(String.format("%02x", this.secondes)), 650, 50);
            }
        }
    }
    
    public void saveScore(){
        
        File f = new File(this.bestScores);
         
        if(f.exists() && !f.isDirectory()){
            //save best scores
            try{
                String line = null;
                ArrayList<String> savedScores = new ArrayList<>();
                BufferedReader br = new BufferedReader(new FileReader(this.bestScores));

                int index = 0;
                int insertion = -1;

                while((line = br.readLine()) != null && index < 4){
                    savedScores.add(line);
                    String[] lineSplited = line.split(":");
                    if(this.player.score >= Integer.parseInt(lineSplited[1]) && insertion == -1){
                        insertion = index;
                    }
                    index++;
                }

                int initSize = savedScores.size();
                
                if(insertion != -1){
                    if(insertion == initSize)
                    {
                        savedScores.add(Configs.getInstance().getConfigValue("Name") + ":" + Integer.toString(this.player.score));
                    }
                    else{
                        savedScores.add(insertion, Configs.getInstance().getConfigValue("Name") + ":" + Integer.toString(this.player.score));
                    }
                }
                else
                {
                    savedScores.add(Configs.getInstance().getConfigValue("Name") + ":" + Integer.toString(this.player.score));
                }
                
                //Ecriture
                PrintWriter pw = new PrintWriter(
                                    new BufferedWriter(
                                        new FileWriter(this.bestScores)));

                for(int i=0;i<savedScores.size();i++){
                    pw.println(savedScores.get(i));
                }
                
                pw.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            //file not exist add default configuration file
            this.createFile();
        }
    }
    
    public void createFile(){
        try{
            PrintWriter pw = new PrintWriter(
                                new BufferedWriter(
                                    new FileWriter(this.bestScores)));
            
            pw.print(Configs.getInstance().getConfigValue("Name") + ":" + Integer.toString(this.player.score));
            
            pw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }   
    
    public void renderPause(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(new Color(127, 127, 127, 210));
        g.fillRect(0, 0, w, h);
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int msgWidth = metrics.stringWidth(this.pausemsg);
        g.setColor(Color.BLACK);
        g.drawString(this.pausemsg, this.w/2 - msgWidth/2, this.h/2 - 25);
        
        g.setFont(this.fontSM);
        metrics = g.getFontMetrics(this.fontSM);
        
        int continueW = metrics.stringWidth(this.btnContinue);
        g.drawImage(this.bgBtn, this.w/3 - 107, this.h - 230, null);
        if(this.selectedItem == 1)
        {
            g.setColor(this.darkGreen);
            g2d.rotate(0.1, this.w/3, this.h - 195);
            g.drawString(this.btnContinue, this.w/3 - continueW/2, this.h - 188);
            g2d.rotate(-0.1, this.w/3, this.h - 195);
        }
        else
        {
            g.setColor(Color.BLACK);
            g.drawString(this.btnContinue, this.w/3 - continueW/2, this.h - 188);
        }
        
        int backW = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, 2*this.w/3 - 107, this.h - 230, null);
        if(this.selectedItem == 2){
            g.setColor(this.darkGreen);
            g2d.rotate(-0.1, 2*this.w/3, this.h - 195);
            g.drawString(this.btnBack, 2*this.w/3 - backW/2, this.h - 188);
            g2d.rotate(0.1, 2*this.w/3, this.h - 195);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnBack, 2*this.w/3 - backW/2, this.h - 188);
        }
    }
    
    public Scene updatePause(int elapsedTime){
        this.hoverPause();
        this.timeF += elapsedTime;
        return this.clickPause();
    }
    
    public void hoverPause(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        if(mouseX > this.w/3 - 107 && mouseX < this.w/3 + 107 && mouseY > this.h - 230 && mouseY < this.h - 160){
            if(this.selectedItem != 1)
                new Thread(Sound.hover::play).start();
            this.selectedItem = 1;
        }
        else if(mouseX > 2*this.w/3 - 107 && mouseX < 2*this.w/3 + 107 && mouseY > this.h - 230 && mouseY < this.h - 160){
            if(this.selectedItem != 2)
                new Thread(Sound.hover::play).start();
            this.selectedItem = 2;
        }
        else{
            this.selectedItem = 0;
        }
    }
    
    public Scene clickPause(){
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItem){
                case 1:
                    new Thread(Sound.select::play).start();
                    this.game.paused = false;
                    break;
                case 2:
                    new Thread(Sound.select::play).start();
                    this.game.paused = false;
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                default:
                    break;
            }
        }
        
        return currentScene;
    }
}
