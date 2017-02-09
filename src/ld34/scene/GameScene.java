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
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import core.Camera;
import core.CustomDialog;
import core.CustomTextField;
import ld34.profile.Settings;
import core.Defines;
import core.Game;
import core.Minimap;
import core.OptionButton;
import core.TimerThread;
import ld34.profile.BestScores;
import ld34.profile.Save;
import level.Level;
import org.json.simple.JSONObject;

public class GameScene extends Scene {

    public Font font, fontS, fontM, fontB, fontSM, fontU;
    public Player player;
    public Level level;
    public Camera cam;
    public String deathMsg, startTxt1, startTxt2, startTxt3, startTxt4, respawn, btnSettings, btnBack, btnSave, pausemsg, btnContinue, warningTxt, title,
            easy, medium, hard, hardcore;
    public BufferedImage background2, bgGui, gui, bgGui2, clockGui, backgroundBottom, backgroundTop,
            backgroundBottomAll, backgroundBottom2, backgroundTop2, backgroundTopAll, guiAssets, scoreIcon, timeIcon, levelIcon, cagesIcon;
    public int nbLevel, selectedItemMenu, selectedItemSaves, selectedItemSettings;
    public boolean displayEnd, displayStart;
    public int alpha, alphaMax;
    public int time = 0, glueX = 0, glueX2 = 0, glueTopX = 0, glueTopX2 = 0;
    public int timeF = 0;
    public int minutes = 0;
    public int secondes = 0;
    public int maxTimeHardcore = 1, soundPlayed, timeSound;
    public boolean timer = false;
    public Minimap minimap;
    public popinsScenes currentScene;
    public enum popinsScenes { NONE, MENU, SETTINGS, SAVES };
    
    public ArrayList<OptionButton> optionButtons = new ArrayList<>();
    public String language, french, english, commands, volume, controlJump, controlWalk, emptyTxt;
    public CustomTextField nameField;
    public int posBar, selectedSave;
    public BufferedImage soundBar, bgSave, cageSavesIcon, levelSavesIcon, dollardSavesIcon;
    public JSONObject jsonSaves;
    public Color kaki;
    public CustomDialog dialog;
    
    public GameScene(int w, int h, Game game, Level level, Player player){
        super(w, h, game);
        this.dialog = null;
        this.displayStart = false;
        this.displayEnd = false;
        this.level = level;
        this.level.setNbTilesInScreenX(game.w);
        this.level.setNbTilesInScreenY(game.h);
        this.level.addPlayer(player);
        this.nbLevel = level.nbLevel;
        this.cam = new Camera((int)player.getPosX(), (int)player.getPosY(), w, h, this.level);
        this.player = player;
        this.player.cam = this.cam;
        this.kaki = new Color(176, 173, 137);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.fontSM = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontM = this.font.deriveFont(Font.PLAIN, 24.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 17.0f);
            this.fontB = this.font.deriveFont(Font.BOLD, 17.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.fontU = this.fontS;
            this.fontU = this.fontU.deriveFont(fontAttributes);
            
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
            
            url = this.getClass().getResource("/gui.png");
            this.guiAssets = ImageIO.read(url);
            this.timeIcon = this.guiAssets.getSubimage(0, 0, 75, 75);
            this.scoreIcon = this.guiAssets.getSubimage(75, 0, 75, 75);
            this.levelIcon = this.guiAssets.getSubimage(150, 0, 75, 75);
            this.cagesIcon = this.guiAssets.getSubimage(225, 0, 75, 75);
            
            url = this.getClass().getResource("/gui.png");
            this.gui = ImageIO.read(url);
            this.cageSavesIcon = this.gui.getSubimage(150, 130, 33, 32);
            this.levelSavesIcon = this.gui.getSubimage(183, 135, 32, 26);
            this.dollardSavesIcon = this.gui.getSubimage(154, 188, 20, 24);
            
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        this.bgGui = this.spritesheetGui.getSubimage(0, 20, 214, 50);
        this.bgGui2 = this.spritesheetGui.getSubimage(0, 0, 214, 50);
        this.clockGui = this.spritesheetGui.getSubimage(0, 281, 55, 55);
        this.soundBar = this.spritesheetGui.getSubimage(0, 256, 210, 25);
        this.bgSave = this.spritesheetGui.getSubimage(0, 282, 500, 118);
        
        this.initLocales();
        
        this.selectedItemMenu = this.selectedItemSaves = this.selectedItemSettings = 0;
        this.currentScene = popinsScenes.NONE;
        
        this.alpha = 0;
        this.alphaMax = 128;
        
        if(this.player.difficulty == 5){
            this.timer = true;
            this.timeF = TimerThread.MILLI;
        }
        
        this.glueX2 = this.backgroundBottom.getWidth();
        this.glueTopX2 = this.backgroundTop2.getWidth();
        
        this.minimap = new Minimap(this.w, this.h, (int)this.player.getPosX(), (int)this.player.getPosY(), this.level);
        
        this.timeSound = TimerThread.MILLI;
        this.soundPlayed = 1;
        new Thread(Sound.sf_jungle01::play).start();
        
        int volume = Integer.parseInt(Settings.getInstance().getConfigValue("Sound"));
        this.posBar = (int)(123 + (2*volume));
        
        this.jsonSaves = Save.getInstance().getSaves();
        
        OptionButton btn1 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))), 
                "Jump", 
                580, 
                230
        );
        btn1.setFont(this.fontS);
        this.optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                580, 
                290
        );
        btn2.setFont(this.font);
        this.optionButtons.add(btn2);
        this.selectedSave = 0;
    }
    
    public GameScene(int w, int h, Game game, int lvl, int score){
        super(w, h, game);
        
        this.nbLevel = lvl;
        this.displayEnd = false;
        this.displayStart = true;
        this.kaki = new Color(176, 173, 137);
        this.dialog = null;
        
        if(this.nbLevel > 1){
            this.displayStart = false;
        }
        
        this.level = new Level(this.nbLevel);
        this.level.setNbTilesInScreenX(game.w);
        this.level.setNbTilesInScreenY(game.h);
        
        this.cam = new Camera(0, 0, w, h, this.level);
        this.player = new Player(32, 445, this.level, this.game.listener, this.cam, Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")));
        this.player.score = score;
        
        this.level.addPlayer(this.player);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.fontSM = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontM = this.font.deriveFont(Font.PLAIN, 24.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 17.0f);
            this.fontB = this.font.deriveFont(Font.BOLD, 17.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.fontU = this.fontS;
            this.fontU = this.fontU.deriveFont(fontAttributes);
            
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
            
            url = this.getClass().getResource("/gui.png");
            this.gui = ImageIO.read(url);
            this.cageSavesIcon = this.gui.getSubimage(150, 130, 33, 32);
            this.levelSavesIcon = this.gui.getSubimage(183, 135, 32, 26);
            this.dollardSavesIcon = this.gui.getSubimage(154, 188, 20, 24);
            
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        this.bgGui = this.spritesheetGui.getSubimage(0, 20, 214, 50);
        this.bgGui2 = this.spritesheetGui.getSubimage(0, 0, 214, 50);
        this.clockGui = this.spritesheetGui.getSubimage(0, 281, 55, 55);
        this.soundBar = this.spritesheetGui.getSubimage(0, 256, 210, 25);
        this.bgSave = this.spritesheetGui.getSubimage(0, 282, 500, 118);
        
        this.initLocales();
        
        this.selectedItemMenu = this.selectedItemSaves = this.selectedItemSettings = 0;
        
        this.alpha = 255;
        if(lvl > 1){
            this.alpha = 0;
        }
        this.alphaMax = 128;
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 5){
            this.timer = true;
            this.timeF = TimerThread.MILLI;
        }
        
        this.glueX2 = this.backgroundBottom.getWidth();
        this.glueTopX2 = this.backgroundTop2.getWidth();
        
        this.minimap = new Minimap(this.w, this.h, (int)this.player.getPosX(), (int)this.player.getPosY(), this.level);
        
        this.timeSound = TimerThread.MILLI;
        this.soundPlayed = 1;
        new Thread(Sound.sf_jungle01::play).start();
        
        int volume = Integer.parseInt(Settings.getInstance().getConfigValue("Sound"));
        this.posBar = (int)(123 + (2*volume));
        
        this.jsonSaves = Save.getInstance().getSaves();
        
        OptionButton btn1 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))), 
                "Jump", 
                580, 
                230
        );
        btn1.setFont(this.fontS);
        this.optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                580, 
                290
        );
        btn2.setFont(this.fontS);
        this.optionButtons.add(btn2);
        this.selectedSave = 0;
    }

    public GameScene(int w, int h, Game game){
        this(w, h, game, 1, 0);
    }
    
    public void initLocales(){
        this.bundle = ResourceBundle.getBundle("lang.game", this.game.langs[Integer.parseInt(Settings.getInstance().getConfigValue("Lang"))]);
        
        this.btnBack = this.bundle.getString("backToMain");
        this.title = this.pausemsg = this.bundle.getString("pauseMsg");
        this.deathMsg = this.bundle.getString("deathMsg");
        this.startTxt1 = this.bundle.getString("startTxt1");
        this.startTxt2 = this.bundle.getString("startTxt2");
        this.startTxt3 = this.bundle.getString("startTxt3");
        this.startTxt4 = this.bundle.getString("startTxt4");
        this.respawn = this.bundle.getString("respawn");
        this.btnContinue = this.bundle.getString("continue");
        this.btnSave = this.bundle.getString("save");
        this.btnSettings = this.bundle.getString("settings");
        this.warningTxt = this.bundle.getString("alert");
        
        this.language = this.bundle.getString("language");
        this.french = this.bundle.getString("french");
        this.english = this.bundle.getString("english");
        this.commands = this.bundle.getString("commands");
        this.volume = this.bundle.getString("volume");
        this.emptyTxt = this.bundle.getString("empty");
        this.easy = this.bundle.getString("easy");
        this.medium = this.bundle.getString("medium");
        this.hard = this.bundle.getString("hard");
        this.hardcore = this.bundle.getString("hardcore");
        this.controlJump = this.bundle.getString("ctrlJump");
        this.controlWalk = this.bundle.getString("ctrlWalk");
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).initLocales();
        }
    }
    
    public void reinit(int lvl){
        this.alpha = 0;
        this.timeF = TimerThread.MILLI;
        
        if(this.nbLevel < Defines.LEVEL_MAX || this.player.isDead){
            this.nbLevel += lvl;
            
            if(lvl != 0){
                this.level = new Level(this.nbLevel);
                this.level.setNbTilesInScreenX(game.w);
                this.level.setNbTilesInScreenY(game.h);
            }
            else
            {
                int[][] data = this.level.data;
                this.level = new Level(this.nbLevel);
                this.level.setNbTilesInScreenX(game.w);
                this.level.setNbTilesInScreenY(game.h);
                this.level.setData(data);
                this.player.setIsRespawning(true);
            }
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
    }
    
    @Override
    public Scene update(double dt) {
        if(this.dialog != null){
            this.dialog.update();
        }
        else{
            if(this.game.listener.mouseExited || this.game.listener.pause.typed || this.game.paused){
                this.updatePause(dt);
                return this;
            }

            if(this.soundPlayed == 1 && ( TimerThread.MILLI - this.timeSound ) > 36000){
                this.timeSound = TimerThread.MILLI;
                this.soundPlayed = 2;
                new Thread(Sound.sf_jungle01::play).start();
            }
            else if(this.soundPlayed == 2 && ( TimerThread.MILLI - this.soundPlayed ) > 28000){
                this.timeSound = TimerThread.MILLI;
                this.soundPlayed = 1;
                new Thread(Sound.sf_jungle02::play).start();
            }

            if(this.player.win){
                this.player.checkpointX = 0;
                if(this.nbLevel < Defines.LEVEL_MAX){
                    return new MapScene(this.w, this.h, this.game, this.nbLevel, this.player.score);
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
                        BestScores.getInstance().insertScore(Settings.getInstance().getConfigValue("Name"), this.player.score);
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
            
            g.setColor(new Color(180, 14, 22));
            int txt5 = metrics.stringWidth(this.warningTxt);
            g.drawString(this.warningTxt, this.w/2 - txt5/2, 550);
            
            g.setColor(new Color(0, 0, 0, this.alpha));
            g.fillRect(0, 0, this.w, this.h);
        }
        else
        {
            Boolean debug = this.game.profiler.isVisible();
            
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
            
            this.player.render(g, debug);

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
                
                this.player.render(g, debug);
            }
            
            if(this.game.paused){
                this.renderPause(g);
            }
            
            if(this.dialog != null){
                this.dialog.render(g);
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

        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 5)
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
                g.drawString((String.format("%02d", this.minutes))+":"+(String.format("%02d", this.secondes)), 650, 50);
            }
        }
    }
    
    public void renderPause(Graphics g){
        
        g.setColor(new Color(127, 127, 127, 210));
        g.fillRect(0, 0, w, h);
        
        switch(this.currentScene){
            case MENU:
                this.title = this.pausemsg;
                this.renderMenu(g);
                break;
            case SETTINGS:
                this.title = this.pausemsg + " - " + this.btnSettings;
                this.renderSettings(g);
                break;
            case SAVES:
                this.title = this.pausemsg + " - " + this.btnSave;
                this.renderSaves(g);
                break;
        }

    }

    public void renderMenu(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int msgWidth = metrics.stringWidth(this.title);
        g.setColor(Color.BLACK);
        g.drawString(this.title, this.w/2 - msgWidth/2, 100);
        
        g.setFont(this.fontSM);
        metrics = g.getFontMetrics(this.fontSM);

        int continueW = metrics.stringWidth(this.btnContinue);
        g.drawImage(this.bgBtn, this.w/2 - 107, 170, null);
        if(this.selectedItemMenu == 1)
        {
            g.setColor(this.darkGreen);
            g2d.rotate(0.1, this.w/2, 235);
            g.drawString(this.btnContinue, this.w/2 - continueW/2, 188 + metrics.getAscent());
            g2d.rotate(-0.1, this.w/2, 235);
        }
        else
        {
            g.setColor(Color.BLACK);
            g.drawString(this.btnContinue, this.w/2 - continueW/2, 188 + metrics.getAscent());
        }
        
        int saveW = metrics.stringWidth(this.btnSave);
        g.drawImage(this.bgBtn, this.w/2 - 107, 260, null);
        if(this.selectedItemMenu == 2){
            g.setColor(this.darkGreen);
            g2d.rotate(-0.1, this.w/2, 325);
            g.drawString(this.btnSave, this.w/2 - saveW/2, 278 + metrics.getAscent());
            g2d.rotate(0.1, this.w/2, 325);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnSave, this.w/2 - saveW/2, 278 + metrics.getAscent());
        }
        
        int settingsW = metrics.stringWidth(this.btnSettings);
        g.drawImage(this.bgBtn, this.w/2 - 107, 350, null);
        if(this.selectedItemMenu == 3){
            g.setColor(this.darkGreen);
            g2d.rotate(0.1, this.w/2, 415);
            g.drawString(this.btnSettings, this.w/2 - settingsW/2, 368 + metrics.getAscent());
            g2d.rotate(-0.1, this.w/2, 415);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnSettings, this.w/2 - settingsW/2, 368 + metrics.getAscent());
        }
        
        int backW = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, this.w/2 - 107, 440, null);
        if(this.selectedItemMenu == 4){
            g.setColor(this.darkGreen);
            g2d.rotate(-0.1, this.w/2, 505);
            g.drawString(this.btnBack, this.w/2 - backW/2, 458 + metrics.getAscent());
            g2d.rotate(0.1, this.w/2, 505);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnBack, this.w/2 - backW/2, 458 + metrics.getAscent());
        }
        
    }
    
    public void renderSettings(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int msgWidth = metrics.stringWidth(this.title);
        g.setColor(Color.BLACK);
        g.drawString(this.title, this.w/2 - msgWidth/2, 100);
        
        g.setFont(this.fontM);
        g.drawString(this.language, 100, 180);
        
        metrics = g.getFontMetrics(this.fontS);
        int englishW = metrics.stringWidth(this.english);
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 0){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, this.w/7, 220, null);
        }else{
            g.setFont(this.fontS);
            g.drawImage(this.bgBtnSmall, this.w/7, 220, null);
        }
        g.drawString(this.english, (this.w/7) + 53 - (englishW/2), 245);
        
        int frenchW = metrics.stringWidth(this.french);
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 1){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, 2*this.w/7, 220, null);
        }else{
            g.setFont(this.fontS);
            g.drawImage(this.bgBtnSmall, 2*this.w/7, 220, null);
        }
        g.drawString(this.french, 2*this.w/7 - frenchW/2 + 53, 245);
        
        g.setFont(this.fontM);
        g.drawString(this.volume, 100, 310);
        int red = 255;
        int green = 0;
        for(int i=0;i<255;i++){
            g.setColor(new Color(red, green, 0));
            g.fillRect((int)(123 + (i * 0.8)), 333, 1, 19);
            red--;
            green++;
        }
        g.drawImage(this.soundBar, 120, 330, null);
        g.setColor(Color.BLACK);
        g.fillRect(this.posBar, 335, 4, 15);
        
        g.drawString(this.commands, 450, 180);
        
        g.setFont(this.fontS);
        g.drawString(this.controlJump, 470, 230);
        g.drawString(this.controlWalk, 470, 290);
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).render(g);
        }
        
        g.setFont(this.fontM);
        metrics = g.getFontMetrics(this.fontM);
        int btnBackW = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, 2 * this.w/3, this.h - 120, null);
        
        if(this.selectedItemSettings == 1){
            g.setColor(this.darkGreen);
            g2d.rotate(-0.1, 2 * this.w/3 + 107, this.h - 85);
            g.drawString(this.btnBack, 2 * this.w/3 + 107 - (btnBackW / 2), (int)(this.h - 117 + metrics.getAscent()*1.5));
            g2d.rotate(0.1, 2 * this.w/3 + 107, this.h - 85);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnBack, 2 * this.w/3 + 107 - (btnBackW / 2), (int)(this.h - 117 + metrics.getAscent() * 1.5));
        }
    }
    
    public void renderSaves(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int msgWidth = metrics.stringWidth(this.title);
        g.setColor(Color.BLACK);
        g.drawString(this.title, this.w/2 - msgWidth/2, 100);
        
        for(int i=0;i<this.jsonSaves.size();i++){
            
            if(this.selectedSave == i+1){
                g.drawImage(this.gui.getSubimage(0, 400, 500, 118), 150, (i * 120) + 120, null);
            }
            else{
                g.drawImage(this.bgSave, 150, (i * 120) + 120, null);
            }
            
            JSONObject save = (JSONObject) this.jsonSaves.get("Slot" + i);
            
            g.setFont(this.fontS);
            g.setColor(Color.BLACK);
            metrics = g.getFontMetrics(this.fontS);
            if(save.isEmpty()){
                int emptyTxtWidth = metrics.stringWidth(this.emptyTxt);
                g.drawString(this.emptyTxt, (this.w - emptyTxtWidth)/2, (i * 120) + 120 + ((118 + metrics.getAscent())/2));
            }
            else{
                
                String slotNumber = "#" + (i + 1);
                g.drawString(slotNumber, 170, ( i * 120) + 155);
                
                g.setColor(this.kaki);
                g.fillRoundRect(208, (i * 120) + 144 , 70, 70, 5, 5);
                g.fillRoundRect(290, (i * 120) + 144, 150, 32, 5, 5);
                g.fillRoundRect(290, (i * 120) + 182, 70, 32, 5, 5);
                g.fillRoundRect(365, (i * 120) + 182, 90, 32, 5, 5);
                g.fillRoundRect(445, (i * 120) + 144, 150, 32, 5, 5);
                g.fillRoundRect(460, (i * 120) + 182, 90, 32, 5, 5);
                g.fillRoundRect(555, (i * 120) + 182, 40, 32, 5, 5);
                
                g.setColor(Color.BLACK);
                JSONObject player = (JSONObject) save.get("player");
                String name = (String) player.get("name");
                int sex = Integer.parseInt((String) player.get("sex"));
                int spicies = Integer.parseInt((String) player.get("spicies"));
                
                g.drawString(name, 298, (i * 120) + 149 + metrics.getAscent());
                
                int x = 0;
                int y = 160;
                if(sex == 0){
                    x = 50;
                }
                if(spicies == 1){
                    y = 208;
                }
                
                g.drawImage(this.spritesheetGui.getSubimage(x, y, 50, 48), 219, (i * 120) + 155, null);
                
                g.drawImage(this.dollardSavesIcon, 450, (i * 120) + 148 ,null);
                String score =  (String) player.get("score");
                int scoreWidth = metrics.stringWidth(score);
                g.drawString(score, this.w - 215 - scoreWidth, (i * 120) + 149 + metrics.getAscent());
                
                g.drawImage(this.levelSavesIcon, 295, (i * 120) + 184, null);
                JSONObject jsonLevel = (JSONObject) save.get("level");
                String levelNumber = (String) jsonLevel.get("number");
                int levelNumberW = metrics.stringWidth(levelNumber);
                g.drawString(levelNumber, 333, (i * 120) + 186 + metrics.getAscent());
                
                g.drawImage(this.cageSavesIcon, 367, (i * 120) + 181, null);
                String cageNumbers = (String) jsonLevel.get("freeCages") + "/30";
                g.drawString(cageNumbers, 403, (i * 120) + 186 + metrics.getAscent());
                
                String complete = (String) jsonLevel.get("complete") + "%";
                int completeW = metrics.stringWidth(complete);
                g.drawString(complete, this.w - 210 - completeW, (i * 120) + 186 + metrics.getAscent());
                
                int difficulty = Integer.parseInt((String) jsonLevel.get("difficulty"));
                switch(difficulty){
                    case 0:
                        g.drawString(this.easy, 470, (i * 120) + 186 + metrics.getAscent());
                        break;
                    case 2:
                        g.drawString(this.medium, 470, (i * 120) + 186 + metrics.getAscent());
                        break;
                    case 4:
                        g.drawString(this.hard, 470, (i * 120) + 186 + metrics.getAscent());
                        break;
                    case 5:
                        g.drawString(this.hardcore, 470, (i * 120) + 186 + metrics.getAscent());
                        break;
                }
            }
        }
        
        //Back BTN
        g.setFont(this.fontM);
        metrics = g.getFontMetrics(this.fontM);
        int btnBackW = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, 2 * this.w/3, this.h - 120, null);
        
        if(this.selectedItemSaves == 1){
            g.setColor(this.darkGreen);
            g2d.rotate(-0.1, 2 * this.w/3 + 107, this.h - 85);
            g.drawString(this.btnBack, 2 * this.w/3 + 107 - (btnBackW / 2), (int)(this.h - 117 + metrics.getAscent()*1.5));
            g2d.rotate(0.1, 2 * this.w/3 + 107, this.h - 85);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnBack, 2 * this.w/3 + 107 - (btnBackW / 2), (int)(this.h - 117 + metrics.getAscent()*1.5));
        }
        
    }
    
    public Scene updatePause(double elapsedTime){
        if(this.game.listener.pause.typed && this.currentScene != popinsScenes.MENU && this.currentScene != popinsScenes.NONE){
            this.currentScene = popinsScenes.MENU;
        }
        
        switch(this.currentScene){
            case MENU:
                break;
            case SAVES:
                break;
            case SETTINGS:
                if(this.game.listener.e != null){
                    this.processKeyPause(this.game.listener.e);
                }
                break;
        }
        
        this.hoverPause();
        this.timeF += elapsedTime;
        return this.clickPause();
    }
    
    public void hoverPause(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        switch(this.currentScene){
            case MENU:
                this.hoverMenu(mouseX, mouseY);
                break;
            case SETTINGS:
                this.hoverSettings(mouseX, mouseY);
                break;
            case SAVES:
                this.hoverSaves(mouseX, mouseY);
                break;
        }
    }
    
    public void hoverMenu(int mouseX, int mouseY){
        if(mouseX > this.w/2 - 107 && mouseX < this.w/2 + 107 && mouseY > 170 && mouseY < 240){
            if(this.selectedItemMenu != 1)
                new Thread(Sound.hover::play).start();
            this.selectedItemMenu = 1;
        }
        else if(mouseX > this.w/2 - 107 && mouseX < this.w/2 + 107 && mouseY > 260 && mouseY < 330){
            if(this.selectedItemMenu != 2)
                new Thread(Sound.hover::play).start();
            this.selectedItemMenu = 2;
        }
        else if(mouseX > this.w/2 - 107 && mouseX < this.w/2 + 107 && mouseY > 350 && mouseY < 420){
            if(this.selectedItemMenu != 3)
                new Thread(Sound.hover::play).start();
            this.selectedItemMenu = 3;
        }
        else if(mouseX > this.w/2 - 107 && mouseX < this.w/2 + 107 && mouseY > 440 && mouseY < 510){
            if(this.selectedItemMenu != 4)
                new Thread(Sound.hover::play).start();
            this.selectedItemMenu = 4;
        }
        else{
            this.selectedItemMenu = 0;
        }
    }
    
    public void hoverSettings(int mouseX, int mouseY){
        if(mouseX > (2 * this.w/3) && mouseX < 2 * this.w/3 + 214 && mouseY > this.h - 120 && mouseY < this.h - 50){
            this.selectedItemSettings = 1;
        }
        else{
            this.selectedItemSettings = 0;
        }
    }
    
    public void hoverSaves(int mouseX, int mouseY){
        if(mouseX > (2 * this.w/3) && mouseX < 2 * this.w/3 + 214 && mouseY > this.h - 120 && mouseY < this.h - 50){
            this.selectedItemSaves = 1;
        }
        else{
            this.selectedItemSaves = 0;
        }
        
        if(mouseX > 150 && mouseX < 650 && mouseY > 120 && mouseY < 238 ){
            this.selectedSave = 1;
        }
        else if(mouseX > 150 && mouseX < 650 && mouseY > 240 && mouseY < 358){
            this.selectedSave = 2;
        }
        else if(mouseX > 150 && mouseX < 650 && mouseY > 360 && mouseY < 478){
            this.selectedSave = 3;
        }
        else{
            this.selectedSave = 0;
        }
    }
    
    public Scene clickPause(){
        Scene currentScene = this;
        
        switch(this.currentScene){
            case MENU:
                currentScene = this.clickPauseMenu(currentScene);
                break;
            case SAVES:
                currentScene = this.clickPauseSaves(currentScene);
                break;
            case SETTINGS:
                currentScene = this.clickPauseSettings(currentScene);
                break;
            default:
                break;
        }
        
        return currentScene;
    }
    
    public Scene clickPauseMenu(Scene currentScene){
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItemMenu){
                case 1:
                    this.game.paused = false;
                    break;
                case 2:
                    this.currentScene = popinsScenes.SAVES;
                    break;
                case 3:
                    this.currentScene = popinsScenes.SETTINGS;
                    break;
                case 4:
                    this.game.paused = false;
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                default:
                    break;
            }
        }
        
        return currentScene;
    }
    
    public Scene clickPauseSaves(Scene currentScene){
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItemSaves){
                case 1:
                    this.currentScene = popinsScenes.MENU;
                    break;
                default:
                    break;
            }
            
            if(this.selectedSave != 0){
                JSONObject save = Save.getInstance().getSave(this.selectedSave - 1);
                if(!save.isEmpty()){
                    CustomDialog dialog = new CustomDialog();
                    dialog.setMessageText("Remove this save ?");
                    this.addDialog(dialog);
                }
                else{
                    //TODO create new save
                }
            }
        }
        
        return currentScene;
    }
    
    public void processKeyPause(KeyEvent e){
        for(int i=0;i<this.optionButtons.size();i++){
            if(this.optionButtons.get(i).isEditing()){
                this.optionButtons.get(i).processKey(e);
            }
        }
    }
    
    public Scene clickPauseSettings(Scene currentScene){
        if(this.game.listener.mousePressed){
            
            int mouseX = this.game.listener.mouseX;
            int mouseY = this.game.listener.mouseY;
            
            if(this.game.listener.mouseClickCount == 1){
                switch(this.selectedItemSettings){
                    case 1:
                        this.currentScene = popinsScenes.MENU;
                        Settings.getInstance().saveConfig();
                        break;
                    default:
                        break;
                }
                
                for(int i=0;i<this.optionButtons.size();i++){
                    if(this.optionButtons.get(i).isEditing())
                        break;
                }

                for(int i=0;i<this.optionButtons.size();i++){
                    this.optionButtons.get(i).processClick(mouseX, mouseY);
                }
            }
            
            if(mouseX > this.w/7 && mouseX < (this.w/7) + 107 && mouseY > 220 && mouseY < 260){
                Settings.getInstance().setConfigValue("Lang", "0");
                this.initLocales();
            }
            else if(mouseX > ((2*this.w)/7) && mouseX < ((2*this.w)/7) + 107 && mouseY > 220 && mouseY < 260){
                Settings.getInstance().setConfigValue("Lang", "1");
                this.initLocales();
            }
            else if(mouseX > 123 && mouseX < 323 && mouseY > 330 && mouseY < 355){
                this.posBar = mouseX;
                int newVolume = (int)((123 - posBar)/ -2);
                //posBar = (int)(153 + (2 * volume));
                //volume = (int)((153 - posBar)/(-2));
                Settings.getInstance().setConfigValue("Sound", Integer.toString(newVolume));
            }
        }
        
        return currentScene;
    }
    
    public void addDialog(CustomDialog dialog){
        this.dialog = dialog;
    }
}