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
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.Camera;
import ld34.Game;
import level.Level;

public class GameScene extends Scene {
    
    public Font font, fontS, fontM, fontB;
    public Player player;
    public Level level;
    public Camera cam;
    public String deathMsg, startTxt1, startTxt2, startTxt3, startTxt4, respawn;
    public BufferedImage background, foreground, background2, bgGui, gui, bgGui2;
    public int nbLevel;
    public boolean displayEnd, displayStart;
    public int alpha, alphaMax;
    
    public GameScene(int w, int h, Game game){
        super(w, h, game);
        
        this.nbLevel = 1;
        this.displayEnd = false;
        this.displayStart = true;
        
        this.level = new Level(this.nbLevel);
        this.cam = new Camera(0, 0, w, h, this.level);
        this.player = new Player(32, 460, this.level, this.game.listener, this.cam, this.game.difficulty);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("gfx/fonts/kaushanscriptregular.ttf"));
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.fontM = this.font.deriveFont(Font.PLAIN, 24.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 17.0f);
            this.fontB = this.font.deriveFont(Font.BOLD, 17.0f);
            this.background = ImageIO.read(new File("gfx/background.png"));
            this.background2 = ImageIO.read(new File("gfx/background2.png"));
            this.foreground = ImageIO.read(new File("gfx/foreground2.png"));
            this.gui = ImageIO.read(new File("gfx/gui.png"));
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        this.bgGui = this.gui.getSubimage(0, 20, 214, 50);
        this.bgGui2 = this.gui.getSubimage(0, 0, 214, 50);
        
        this.bundle = ResourceBundle.getBundle("lang.game", this.game.langs[this.game.lang]);
        
        this.deathMsg = this.bundle.getString("deathMsg");
        this.startTxt1 = this.bundle.getString("startTxt1");
        this.startTxt2 = this.bundle.getString("startTxt2");
        this.startTxt3 = this.bundle.getString("startTxt3");
        this.startTxt4 = this.bundle.getString("startTxt4");
        this.respawn = this.bundle.getString("respawn");
        
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
            g.drawImage(this.foreground, 0, 0, null);
        }
    }
    
}
