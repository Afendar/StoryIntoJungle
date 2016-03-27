package ld34.scene;

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
import ld34.Game;
import level.Level;

public class GameScene extends Scene {
    
    public Font font, fontS, fontM, fontB, fontSM;
    public Player player;
    public Level level;
    public Camera cam;
    public String deathMsg, startTxt1, startTxt2, startTxt3, startTxt4, respawn, btnBack, pausemsg, btnContinue;
    public BufferedImage background2, bgGui, gui, bgGui2;
    public int nbLevel, selectedItem;
    public String bestScores = "best_scores.dat";
    public boolean displayEnd, displayStart;
    public int alpha, alphaMax;
    
    public GameScene(int w, int h, Game game){
        super(w, h, game);
        
        this.nbLevel = 1;
        this.displayEnd = false;
        this.displayStart = true;
        
        this.level = new Level(this.nbLevel);
        this.cam = new Camera(0, 0, w, h, this.level);
        this.player = new Player(32, 460, this.level, this.game.listener, this.cam, (int)Configs.getInstance().getConfigValue("Difficulty"));
        
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
            
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        this.bgGui = this.spritesheetGui.getSubimage(0, 20, 214, 50);
        this.bgGui2 = this.spritesheetGui.getSubimage(0, 0, 214, 50);
        
        this.bundle = ResourceBundle.getBundle("lang.game", this.game.langs[(int)Configs.getInstance().getConfigValue("Lang")]);
        
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
        this.alphaMax = 128;
    }

    public void reinit(int lvl){
        this.alpha = 0;
        if(this.nbLevel < 3 || this.player.isDead){
            this.nbLevel+= lvl;
            this.level = new Level(this.nbLevel);
            this.player.level = this.level;
            this.player.setPosX(32);
            if(lvl != 0){
                this.player.setPosY(460 - ((this.nbLevel-1)*8));
                this.player.PLAYER_SIZE += 8;
                this.player.reloadSpritesheet(this.nbLevel);
            }
            else{
                this.player.setPosY(460);
            }
            this.player.win = false;
        }
        else{
            this.displayEnd = true;
            this.player.win = false;
        }
    }
    
    @Override
    public Scene update() {
        if(!this.game.listener.mouseExited){
            
            if(this.player.win){
                reinit(1);
            }
            else{
                if(this.displayEnd){
                    return new EndScene(this.w, this.h, this.game);
                }
                else if(this.displayStart){
                    if(this.game.listener.next.enabled){
                        this.displayStart = false;
                        this.alpha = 0;
                    }
                    return this;
                }
                else if(this.player.isDead){
                    if(this.game.listener.next.enabled){
                        saveScore();
                        reinit(0);
                        this.player.isDead = false;
                        this.player.score = 0;
                    }
                    this.player.update();
                }
                else{
                    this.level.update();

                    this.player.update();

                    this.cam.update(this.player);
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
            
            g.drawImage(this.background2, 0, -10, null);
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

            //Backoud render
            g.drawImage(this.background, 0, this.h - 24 - this.background.getHeight(), null);

            g2d.translate(-this.cam.x, -this.cam.y);

            //Map Render
            this.level.render(g, 0, 0);
            
            this.player.render(g);

            g2d.translate(this.cam.x, this.cam.y);

            g.drawImage(this.bgGui, this.w/2 - 210, 0, null);
            g.setColor(Color.BLACK);
            g.setFont(this.fontS);
            g.drawString("Score : " + this.player.score, this.w/2 - 190  , 23);
            
            g.drawImage(this.bgGui, this.w/2 + 20, 0, null);
            g.drawString(this.bundle.getString("levelTxt") + this.nbLevel, this.w/2 + 90, 26);
            
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
            g.drawImage(this.foreground2, 0, 0, null);
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

                while((line = br.readLine()) != null){
                    savedScores.add(line);
                    //String[] lineSplited = line.split(":");
                    if(this.player.score >= Integer.parseInt(line)){
                        insertion = index;
                    }
                    index++;
                }

                int initSize = savedScores.size();
                
                if(insertion != -1){

                    for(int i = initSize - 1 ; i>=0 ; i--){
                        if(i != insertion){
                            if(i == initSize){
                                savedScores.add(savedScores.get(i));
                            }
                            else{
                                savedScores.add(i+1, savedScores.get(i));
                            }
                        }
                        else{
                            if(insertion == initSize){
                                savedScores.add(Integer.toString(this.player.score));
                            }
                            else{
                                savedScores.add(insertion, Integer.toString(this.player.score));
                            }
                            break;
                        }
                    }
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
            
            pw.print(Integer.toString(this.player.score));
            
            pw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }   
    
    public void renderPause(Graphics g){
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
        g.drawString(this.btnContinue, this.w/3 - continueW/2, this.h - 188);
        
        int backW = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, 2*this.w/3 - 107, this.h - 230, null);
        g.drawString(this.btnBack, 2*this.w/3 - backW/2, this.h - 188);
    }
    
    public Scene updatePause(){
        this.hoverPause();

        return this.clickPause();
    }
    
    public void hoverPause(){
        
    }
    
    public Scene clickPause(){
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItem){
                case 0:
                    this.game.paused = false;
                    break;
                case 1:
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
