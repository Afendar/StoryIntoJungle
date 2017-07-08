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
import core.Camera;
import core.CustomDialog;
import core.CustomTextField;
import ld34.profile.Settings;
import core.Defines;
import core.Game;
import core.Minimap;
import core.OptionButton;
import core.TimerThread;
import entity.CageEntity;
import java.util.List;
import java.util.StringTokenizer;
import ld34.profile.BestScores;
import ld34.profile.Save;
import level.Level;
import org.json.simple.JSONObject;

/**
 * GameScene class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class GameScene extends Scene {

    public Font font, fontS, fontM, fontB, fontSM, fontU, fontDialog;
    public Player player;
    public Level level;
    public Camera cam;
    public String deathMsg, startTxt1, startTxt2, startTxt3, startTxt4, respawn, btnSettings, btnBack, btnSave, pausemsg, btnContinue, warningTxt, title,
            easy, medium, hard, hardcore, popupLabel;
    public BufferedImage background2, bgGui, gui, bgGui2, clockGui, backgroundBottom, backgroundTop, monkeySpriteSheet,
            backgroundBottomAll, backgroundBottom2, backgroundTop2, backgroundTopAll, guiAssets, scoreIcon, timeIcon, levelIcon, cagesIcon, cageIcon, dollardIcon, littlesPandas;
    public int nbLevel, selectedItemMenu, selectedItemSaves, selectedItemSettings;
    public boolean displayEnd, displayStart, renderSaves, displayEvent, displayDialog = false;
    public int alpha, alphaMax;
    public int time, glueX, glueX2, glueTopX, glueTopX2, eventNumber = 0;
    public int timeF = 0, timeEventFree, timeMonkey, timeDialog = 0;
    public int minutes = 0, eventY = 0;
    public int secondes = 0, pauseSettingsPanelX = 800, pauseMenuX = 0;
    public int maxTimeHardcore = 1, soundPlayed, timeSound, cageToFree;
    public double timerMenu = 0;
    public boolean timer = false, renderFreeCageAnim = false;
    public Minimap minimap;
    public popinsScenes currentScene;
    public enum popinsScenes { NONE, MENU, SETTINGS, SAVES };
    
    public ArrayList<OptionButton> optionButtons = new ArrayList<>();
    public String language, french, english, commands, volume, controlJump, controlWalk, emptyTxt, back;
    public CustomTextField nameField;
    public int posBar, selectedSave;
    public BufferedImage soundBar, bgSave, cageSavesIcon, levelSavesIcon, dollardSavesIcon;
    public JSONObject jsonSaves;
    public Color kaki;
    public CustomDialog dialog;
    public int[][] btnPosMenu = {
        {this.w/2 - 107 - (15*30), 140},
        {this.w/2 - 107 - (17*30), 240},
        {this.w/2 - 107 - (19*30), 340},
        {this.w/2 - 107 - (21*30), 440}
    };
    
    /**
     * 
     * @param w
     * @param h
     * @param game
     * @param level
     * @param player 
     */
    public GameScene(int w, int h, Game game, Level level, Player player){
        super(w, h, game);
        this.dialog = null;
        this.displayStart = false;
        this.displayEnd = false;
        this.level = level;
        this.level.setNbTilesInScreenX(game.w);
        this.level.setNbTilesInScreenY(game.h);
        this.level.addPlayer(player);
        this.nbLevel = this.level.nbCages;
        this.cageToFree = level.getFreeCages();
        this.cam = new Camera((int)player.getPosX(), (int)player.getPosY(), w, h, this.level);
        this.player = player;
        this.player.cam = this.cam;
        this.kaki = new Color(176, 173, 137);
        
        this.loadAssets();
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
        this.posBar = (int)(255 + (2.4 * volume));
        
        this.jsonSaves = Save.getInstance().getSaves();
        
        OptionButton btn1 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))), 
                "Jump", 
                230, 
                380
        );
        btn1.setFont(this.fontS);
        btn1.setSize(234, 100);
        this.optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                500, 
                380
        );
        btn2.setFont(this.font);
        btn2.setSize(234, 100);
        this.optionButtons.add(btn2);
        this.selectedSave = 0;
    }
    
    /**
     * 
     * @param w
     * @param h
     * @param game
     * @param lvl
     * @param score 
     */
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
        for(int i=1;i<this.nbLevel;i++){
            this.level.setUnlocked(i);
        }
        this.level.setNbTilesInScreenX(game.w);
        this.level.setNbTilesInScreenY(game.h);
        
        this.cageToFree = this.level.nbCages;
        
        this.cam = new Camera(0, 0, w, h, this.level);
        this.player = new Player(32, 445, this.level, this.game.listener, this.cam, Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")));
        this.player.score = score;
        
        this.level.addPlayer(this.player);

        this.loadAssets();
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
        this.posBar = (int)(255 + ( 2.4 * volume));
        
        this.jsonSaves = Save.getInstance().getSaves();
        
        OptionButton btn1 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))), 
                "Jump", 
                230, 
                380
        );
        btn1.setFont(this.fontS);
        btn1.setSize(234, 100);
        this.optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                500, 
                380
        );
        btn2.setFont(this.fontS);
        btn2.setSize(234, 100);
        this.optionButtons.add(btn2);
        this.selectedSave = 0;
    }

    /**
     * 
     * @param w
     * @param h
     * @param game 
     */
    public GameScene(int w, int h, Game game){
        this(w, h, game, 1, 0);
    }
    
    /**
     * 
     */
    public void loadAssets(){
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.fontSM = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontDialog = this.font.deriveFont(Font.PLAIN, 19.0f);
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
            this.timeIcon = this.guiAssets.getSubimage(6, 8, 61, 60);
            this.scoreIcon = this.guiAssets.getSubimage(82, 8, 61, 59);
            this.levelIcon = this.guiAssets.getSubimage(156, 7, 61, 61);
            if(this.player.species.equals("panda")){
                this.cagesIcon = this.guiAssets.getSubimage(232, 7, 61, 61);
            }
            else{
                this.cagesIcon = this.guiAssets.getSubimage(307, 7, 61, 61);
            }
            
            url = this.getClass().getResource("/gui.png");
            this.gui = ImageIO.read(url);
            this.cageSavesIcon = this.gui.getSubimage(150, 130, 33, 32);
            this.levelSavesIcon = this.gui.getSubimage(183, 135, 32, 26);
            this.dollardSavesIcon = this.gui.getSubimage(154, 188, 20, 24);
            
            url = this.getClass().getResource("/littles_pandas.png");
            this.littlesPandas = ImageIO.read(url);
            
            url = this.getClass().getResource("/monkey.png");
            this.monkeySpriteSheet = ImageIO.read(url);
            
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        this.bgGui = this.spritesheetGui.getSubimage(0, 20, 214, 50);
        this.bgGui2 = this.spritesheetGui.getSubimage(0, 0, 214, 50);
        this.clockGui = this.spritesheetGui.getSubimage(0, 281, 55, 55);
        this.soundBar = this.spritesheetGui.getSubimage(0, 256, 210, 25);
        this.bgSave = this.guiAssets.getSubimage(235, 127, 217, 216);
        this.cageIcon = this.guiAssets.getSubimage(384, 101, 27, 25); 
        this.dollardIcon = this.guiAssets.getSubimage(413, 104, 16, 20);; 
    }
    
    /**
     * 
     */
    public void initLocales(){
        this.bundle = ResourceBundle.getBundle("lang.lang", this.game.langs[Integer.parseInt(Settings.getInstance().getConfigValue("Lang"))]);
        
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
        this.popupLabel = this.bundle.getString("popupLabel");
        this.back = this.bundle.getString("back");
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).initLocales();
        }
    }
    
    /**
     * 
     * @param lvl 
     */
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
                this.cageToFree = this.level.nbCages;
                for(int i=0;i<this.nbLevel;i++){
                    this.level.setUnlocked(i);
                }
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
            this.renderFreeCageAnim = false;
        }
        else{
            this.displayEnd = true;
            this.player.win = false;
        }
    }
    
    @Override
    public Scene update(double dt) {
        
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        if(this.dialog != null){
            int value = this.dialog.getValue();
            if(value == 0){
                this.dialog.update();
            }
            else{
                switch(value){
                    case 1:
                        if(this.selectedSave != 0){
                            Save.getInstance().saveGame(this.selectedSave - 1, this.level, this.player);
                            this.jsonSaves = Save.getInstance().getSaves();
                        }
                        break;
                }
                this.dialog = null;
            }
        }
        else{
            if((this.game.listener.mouseExited || this.game.listener.pause.typed || this.game.paused) && !this.renderFreeCageAnim && !this.displayEvent){
                return this.updatePause(dt);
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

            if(this.cageToFree != this.level.nbCages && this.timeEventFree < 200){
                this.game.paused = true;
                this.renderFreeCageAnim = true;
                this.timeEventFree += dt;
                if(this.timeEventFree < 55){
                    this.eventY = this.easeOut(this.timeEventFree, -250, 290, 50);
                }
                else if(this.timeEventFree > 100){
                    this.eventY = this.cubicEaseIn(this.timeEventFree - 100, 70, -250, 50);
                }
                return this;
            }
            else{
                this.renderFreeCageAnim = false;
                this.game.paused = false;
                this.timeEventFree = 0;
                this.eventY = 0;
                this.cageToFree = this.level.nbCages;
            }
            
            if(this.level.nbLevel == 1 && !Defines.DEV){
                for(int i=0;i< this.level.eventsPos.length;i++){
                    if(this.player.getPosX() >= this.level.eventsPos[i][0] + 64 && this.player.getPosX() <= this.level.eventsPos[i][0] + 128 && !this.level.viewedEvent[i]){
                        if(!this.displayEvent){
                            this.eventNumber = i;
                            this.level.viewedEvent[i] = true;
                            this.displayEvent = true;
                            this.game.paused = true;
                        }
                    }
                }
                if(this.displayEvent){
                    this.timeMonkey += dt;
                    if(this.timeMonkey > 15){
                        this.timeMonkey = 15;
                        this.displayDialog = true;
                        if(mouseX > this.w - 60 && mouseX < this.w - 26 && mouseY > this.h - 56 && mouseY < this.h - 20){
                            if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
                                this.timeMonkey = 0;
                                this.displayDialog = false;
                                this.displayEvent = false;
                                this.game.paused = false;
                            }
                        }
                    }
                }
            }
            
            if(this.player.win){
                this.player.checkpointX = 0;
                if(this.nbLevel < Defines.LEVEL_MAX){
                    this.level.setUnlocked(this.nbLevel);
                    MapScene ms = new MapScene(this.w, this.h, this.game, this.nbLevel, this.player.score, this.level.unlockedLevels);
                    ms.setCagesMap(this.level.cagesMap);
                    return ms;
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

                    if(!this.displayEvent)
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
            if(this.player.getPosX() + this.player.getBounds().width/2 >= this.glueX + 1.5 * this.backgroundBottom.getWidth())
            {
                this.glueX += 2 * this.backgroundBottom.getWidth();
            }
            else if(this.player.getPosX() + this.player.getBounds().width/2 < this.glueX - this.backgroundBottom.getWidth()/2)
            {
                if(this.glueX > 0)
                {
                    this.glueX -= 2 * this.backgroundBottom.getWidth();
                }
            }
            if(this.player.getPosX() + (this.player.getBounds().width/2) >= this.glueX2 + 1.5 * this.backgroundBottom2.getWidth())
            {
                this.glueX2 += 2 * this.backgroundBottom2.getWidth();
            }
            else if(this.player.getPosX() + this.player.getBounds().width/2 < this.glueX2 - this.backgroundBottom2.getWidth()/2)
            {
                if(this.glueX2 >= this.backgroundBottom2.getWidth())
                {
                    this.glueX2 -= 2 * this.backgroundBottom2.getWidth();
                }
            }
            
            g.drawImage(this.backgroundBottom, (int)(this.glueX - this.cam.x), this.h - this.backgroundBottom.getHeight(), null);
            g.drawImage(this.backgroundBottom2, (int)(this.glueX2 - this.cam.x), this.h - this.backgroundBottom2.getHeight(), null);
            
            if(this.player.getPosX() + this.player.getBounds().width/2 + (this.cam.x/4) >= this.glueTopX + 1.5 * this.backgroundTop.getWidth())
            {
                this.glueTopX += 2 * this.backgroundTop.getWidth();
            }
            else if(this.player.getPosX() + this.player.getBounds().width/2  + (this.cam.x/4) < this.glueTopX - (this.background.getWidth()/2))
            {
                if(this.glueTopX > 0)
                {
                    this.glueTopX -= 2 * this.backgroundTop.getWidth();
                }
            }
            if(this.player.getPosX() + this.player.getBounds().width/2 + (this.cam.x/4) >= this.glueTopX2 + 1.5 * this.backgroundTop2.getWidth())
            {
                this.glueTopX2 += 2 * this.backgroundTop2.getWidth();
            }
            else if(this.player.getPosX() + this.player.getBounds().width/2 + (this.cam.x/4) < this.glueTopX2 - this.backgroundTop2.getWidth()/2)
            {
                if(this.glueTopX2 >= this.backgroundTop2.getWidth())
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
            if(startX > this.level.nbTilesW - this.level.getNbTilesInScreenX())startX = this.level.nbTilesW - this.level.getNbTilesInScreenX();
            if(startY > this.level.nbTilesH - this.level.getNbTilesInScreenY())startY = this.level.nbTilesH - this.level.getNbTilesInScreenY();
            if(startX < 0)startX = 0;
            if(startY < 0)startY = 0;
            
            this.level.renderFirstLayer(g, startX, startY);
            
            this.player.render(g, debug);

            this.level.renderSecondLayer(g, startX, startY);

            if(this.displayEvent){
                g.drawImage(this.monkeySpriteSheet.getSubimage(105 * (int)(this.timeMonkey / 4), 0, 105, 107), this.level.eventsPos[this.eventNumber][0] + 139, this.level.eventsPos[this.eventNumber][1] - 81, null);
            }
            
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
            
            if(this.game.paused && !this.displayEvent){
                if(this.renderFreeCageAnim){
                    this.renderFreeCageAnim(g);
                }
                else{
                    this.renderPause(g);
                }
            }
            
            if(this.displayDialog && this.displayDialog){
                int mouseX = this.game.listener.mouseX;
                int mouseY = this.game.listener.mouseY;
                g2d.rotate(-1.5708, 34, 400);
                g.drawImage(this.spritesheetGui2.getSubimage(968, 0, 152, 800), -167, 366, null);
                g.drawImage(this.spritesheetGui2.getSubimage(900, 0, 68, 800), -40, 366, null);
                g2d.rotate(1.5708, 34, 400);
                if(mouseX >= this.w - 60 && mouseX <= this.w - 26 && mouseY >= this.h - 56 && mouseY <= this.h -20)
                    g.drawImage(this.spritesheetGui2.getSubimage(726, 151, 34, 36), this.w - 60, this.h - 56, null);
                else
                    g.drawImage(this.spritesheetGui2.getSubimage(794, 151, 34, 36), this.w - 60, this.h - 56, null);
                g.setColor(Color.BLACK);
                g.setFont(this.fontDialog);
                String text = this.bundle.getString("tutoriel"+this.eventNumber);
                switch(this.eventNumber){
                    case 0:
                        text = text.replaceAll("\\[.*?\\]", this.player.getName());
                        break;
                    case 1:
                    case 7:
                        text = text.replaceAll("\\[.*?\\]", KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))));
                        break;
                    case 2:
                        text = text.replaceAll("\\[.*?\\]", KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))));
                        break;
                }
                FontMetrics metrics = g.getFontMetrics(this.fontM);
                int stringWidth = metrics.stringWidth(text);
                if(stringWidth > this.w - 40){
                    String label = this.substringLabels(text, 104);
                    int y = this.h - 100 - g.getFontMetrics().getHeight() - 5;
                    for(String line : label.split("\n")){
                        g.drawString(line, 10, y += g.getFontMetrics().getHeight() + 5);
                    }
                }
                else{
                    g.drawString(text, 10, this.h - 100);
                }
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
    
    /**
     * 
     * @param g 
     */
    public void renderFreeCageAnim(Graphics g){
        g.drawImage(this.spritesheetGui2.getSubimage(0, 547, 281, 132), this.w/2 - 140, this.eventY, null);
        for(int i=0; i<this.level.getFreeCages();i++){
            g.drawImage(this.spritesheetGui2.getSubimage(282, 548, 37, 36),this.w/2 - 140 +((i) * 40 + 39), this.eventY + 78, null);
        }
        for(int i=0;i<this.level.nbCages;i++){
            g.drawImage(this.spritesheetGui2.getSubimage(282, 586, 37, 36), this.w/2 - 140 + ((i + this.level.getFreeCages()) * 40 + 39), this.eventY + 78, null);
        }
    }
    
    /**
     * 
     * @param g 
     */
    public void renderGUI(Graphics g){
        
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(55, 85, 120, 30, 5, 5);
        g.fillRoundRect(55, 27, 60, 30, 5, 5);
        g.fillRoundRect(170, 27, 60, 30, 5, 5);
        
        g.drawImage(this.scoreIcon, 6, 70, null);
        g.drawImage(this.levelIcon, 6, 10, null);
        g.drawImage(this.cagesIcon, 120, 10, null);
        
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
    
    /**
     * 
     * @param g 
     */
    public void renderPause(Graphics g){
        
        g.setColor(new Color(127, 127, 127, 210));
        g.fillRect(0, 0, w, h);

        switch(this.currentScene){
            case MENU:
                this.title = this.pausemsg;
                this.renderMenu(g);
                if(this.pauseMenuX < 0){
                    if(this.renderSaves){
                        this.renderSaves(g);
                    }
                    else{
                        this.renderSettings(g);
                    }
                }
                break;
            case SETTINGS:
                this.title = this.pausemsg + " - " + this.btnSettings;
                this.renderSettings(g);
                if(this.pauseSettingsPanelX > 0){
                    this.renderMenu(g);
                }
                break;
            case SAVES:
                this.title = this.pausemsg + " - " + this.btnSave;
                this.renderSaves(g);
                if(this.pauseSettingsPanelX > 0){
                    this.renderMenu(g);
                }
                break;
        }
    }

    /**
     * 
     * @param g 
     */
    public void renderMenu(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int msgWidth = metrics.stringWidth(this.title);
        g.setColor(Color.BLACK);
        g.drawString(this.title, this.w/2 - msgWidth/2 + this.pauseMenuX, 100);
        
        g.setFont(this.fontM);
        metrics = g.getFontMetrics(this.fontSM);

        int continueW = metrics.stringWidth(this.btnContinue);
        
        if(this.selectedItemMenu == 1){
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 232, 234, 99);
        }
        g.drawImage(this.bgBtn, this.btnPosMenu[0][0] + this.pauseMenuX, this.btnPosMenu[0][1], null);
        g.setColor(Scene.DARKGREY);
        g.drawString(this.btnContinue, this.btnPosMenu[0][0] + 107 - continueW/2 + this.pauseMenuX, this.btnPosMenu[0][1] + 30 + metrics.getAscent());
        
        
        int saveW = metrics.stringWidth(this.btnSave);
        
        if(this.selectedItemMenu == 2){
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 232, 234, 99);
        }
        g.drawImage(this.bgBtn, this.btnPosMenu[1][0] + this.pauseMenuX, this.btnPosMenu[1][1], null);
        g.drawString(this.btnSave, this.btnPosMenu[1][0] + 107 - saveW/2 + this.pauseMenuX, this.btnPosMenu[1][1] + 30 + metrics.getAscent());
        
        int settingsW = metrics.stringWidth(this.btnSettings);
        
        if(this.selectedItemMenu == 3){
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 232, 234, 99);
        }
        g.drawImage(this.bgBtn, this.btnPosMenu[2][0] + this.pauseMenuX, this.btnPosMenu[2][1], null);
        g.drawString(this.btnSettings, this.btnPosMenu[2][0] + 107 - settingsW/2 + this.pauseMenuX, this.btnPosMenu[2][1]+ 30 + metrics.getAscent());
        
        int backW = metrics.stringWidth(this.btnBack);
        
        if(this.selectedItemMenu == 4){
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 232, 234, 99);
        }
        g.drawImage(this.bgBtn, this.btnPosMenu[3][0] + this.pauseMenuX, this.btnPosMenu[3][1], null);
        g.drawString(this.btnBack, this.btnPosMenu[3][0] + 107 - backW/2 + this.pauseMenuX, this.btnPosMenu[3][1] + 30 + metrics.getAscent());
    }
    
    /**
     * 
     * @param g 
     */
    public void renderSettings(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(Scene.BLACKSHADOW);
        g.fillRect(0, 35, this.w, 60);
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int msgWidth = metrics.stringWidth(this.title);
        g.setColor(Color.BLACK);
        g.drawString(this.title, this.w/2 - msgWidth/2 + this.pauseSettingsPanelX, 75);
        
        g.setFont(this.fontM);
        g.drawString(this.language, 100 + this.pauseSettingsPanelX, 160);
        g.drawString(this.volume, 100 + this.pauseSettingsPanelX, 310);
        g.drawString(this.commands, 100 + this.pauseSettingsPanelX, 370);
        
        metrics = g.getFontMetrics(this.fontS);
        int englishW = metrics.stringWidth(this.english);
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 0){
            g.drawImage(this.guiAssets.getSubimage(0, 332, 238, 104), 230 + this.pauseSettingsPanelX, 140, null);
        }else{
            g.drawImage(this.guiAssets.getSubimage(0, 132, 234, 100), 230 + this.pauseSettingsPanelX, 140, null);
        }
        g.drawImage(this.guiAssets.getSubimage(238, 344, 42, 28), 266 + this.pauseSettingsPanelX, 175, null);
        g.setFont(this.fontS);
        g.drawString(this.english, 329 + this.pauseSettingsPanelX, 177 + metrics.getAscent()/2 + 8);
        
        int frenchW = metrics.stringWidth(this.french);
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 1){
            g.drawImage(this.guiAssets.getSubimage(0, 332, 238, 104), 500 + this.pauseSettingsPanelX, 135, null);
        }else{
            g.drawImage(this.guiAssets.getSubimage(0, 132, 234, 100), 500 + this.pauseSettingsPanelX, 135, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(238, 372, 42, 28), 536 + this.pauseSettingsPanelX, 172, null);
        g.drawString(this.french, 599 + this.pauseSettingsPanelX, 172 + metrics.getAscent()/2 + 8);
        
        int red = 255;
        int green = 0;
        for(int i=0;i<255;i++){
            g.setColor(new Color(red, green, 0));
            g.fillRect((int)(250 + i) + this.pauseSettingsPanelX, 295, 1, 19);
            red--;
            green++;
        }
        g.drawImage(this.spritesheetGui2.getSubimage(0, 438, 299, 62), 230 + this.pauseSettingsPanelX, 265, null);
        g.setColor(Color.BLACK);
        g.fillRect(this.posBar + this.pauseSettingsPanelX, 296, 9, 14);
        
        g.setFont(this.fontS);
        g.drawString(this.controlJump, 230 + this.pauseSettingsPanelX, 370);
        g.drawString(this.controlWalk, 500 + this.pauseSettingsPanelX, 370);
        
        g.drawImage(this.guiAssets.getSubimage(0, 132, 234, 100), 230 + this.pauseSettingsPanelX, 380, null);
        g.drawImage(this.guiAssets.getSubimage(0, 132, 234, 100), 500 + this.pauseSettingsPanelX, 375, null);
        
        for(int i=0;i<this.optionButtons.size();i++){
            OptionButton btn = this.optionButtons.get(i);
            btn.setPosition((i == 0 ? 230 : 500) + this.pauseSettingsPanelX, btn.getY());
            btn.render(g);
        }
        
        if(this.selectedItemSettings == 1){
            this.bgBtn = this.guiAssets.getSubimage(491, 1, 120, 99);
        }
        else{
            this.bgBtn = this.guiAssets.getSubimage(370, 1, 120, 99);
        }
        g.drawImage(this.bgBtn, this.w/2 - 60 + this.pauseSettingsPanelX, this.h - 110, null);
        g.drawImage(this.guiAssets.getSubimage(239, 402, 39, 36), this.w/2 - 20 + this.pauseSettingsPanelX, this.h - 79, null);
    }
    
    /**
     * 
     * @param g 
     */
    public void renderSaves(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(Scene.BLACKSHADOW);
        g.fillRect(0, 35, this.w, 60);
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int msgWidth = metrics.stringWidth(this.title);
        g.setColor(Color.BLACK);
        g.drawString(this.title, this.w/2 - msgWidth/2 + this.pauseSettingsPanelX, 75);
        
        for(int i=0;i<this.jsonSaves.size();i++){
            
            if(this.selectedSave == i+1){
                g.drawImage(this.guiAssets.getSubimage(453, 125, 221, 220), (i * 230) + 68 + this.pauseSettingsPanelX, 153, null);
            }
            else{
                g.drawImage(this.bgSave, i * 230 + 70 + this.pauseSettingsPanelX, 155, null);
            }
            
            JSONObject save = (JSONObject) this.jsonSaves.get("Slot" + i);
            Font f = this.font.deriveFont(Font.PLAIN, 18.0f);
            g.setFont(f);
            g.setColor(Color.BLACK);
            metrics = g.getFontMetrics(f);
            
            if(save.isEmpty()){
                int emptyTxtWidth = metrics.stringWidth(this.emptyTxt);
                g.drawString(this.emptyTxt, i * 230 + 178 - emptyTxtWidth/2 + this.pauseSettingsPanelX, 256 + metrics.getAscent()/2);
            }
            else{
                JSONObject player = (JSONObject) save.get("player");
                String name = (String) player.get("name");
                int sex = Integer.parseInt((String) player.get("sex"));
                int spicies = Integer.parseInt((String) player.get("spicies"));
                
                f = this.font.deriveFont(Font.PLAIN, 18.0f);
                g.setFont(f);
                metrics = g.getFontMetrics(f);
                int nameW = metrics.stringWidth(name);
                g.drawString(name, i * 230 + 178 - nameW/2 + this.pauseSettingsPanelX, 192 + (metrics.getAscent()/2));
                
                int x = 0;
                int y = 160;
                if(sex == 0){
                    x = 50;
                }
                if(spicies == 1){
                    y = 208;
                }
                
                g.drawImage(this.dollardIcon, i * 230 + 181 + this.pauseSettingsPanelX, 232 ,null);
                g.setFont(this.fontS);
                metrics = g.getFontMetrics(this.fontS);
                String score =  (String) player.get("score");
                int scoreWidth = metrics.stringWidth(score);
                g.drawString(score, i * 230 + 199 + this.pauseSettingsPanelX, 237 + metrics.getAscent()/2);
                
                g.drawImage(this.guiAssets.getSubimage(362, 107, 19, 16), i * 230 + 107 + this.pauseSettingsPanelX, 315, null);
                JSONObject jsonLevel = (JSONObject) save.get("level");
                String levelNumber = (String) jsonLevel.get("number");
                int levelNum = Integer.parseInt((String) jsonLevel.get("number"));
                int levelNumberW = metrics.stringWidth(levelNumber);
                g.drawString(levelNumber, i * 230 + 130 + this.pauseSettingsPanelX, 318 + metrics.getAscent()/2);
                
                g.drawImage(this.cageIcon, i * 230 + 176 + this.pauseSettingsPanelX, 266, null);
                String cageNumbers = (String) jsonLevel.get("freeCages") + "/30";
                g.drawString(cageNumbers, i * 230 + 208 + this.pauseSettingsPanelX, 274 + metrics.getAscent()/2);
                
                String complete = (String) jsonLevel.get("complete") + "%";
                int completeW = metrics.stringWidth(complete);
                g.drawString(complete, i * 230 + 178 - completeW/2 + this.pauseSettingsPanelX, 318 + metrics.getAscent()/2);
                
                int difficulty = Integer.parseInt((String) jsonLevel.get("difficulty"));
                
                switch(difficulty){
                    case 0:
                        g.drawImage(this.guiAssets.getSubimage(285, 69, 17, 16), i * 230 + 226 + this.pauseSettingsPanelX, 305, null);
                        break;
                    case 2:
                        g.drawImage(this.guiAssets.getSubimage(325, 69, 35, 16), i * 230 + 226 + this.pauseSettingsPanelX, 305, null);
                        break;
                    case 4:
                        g.drawImage(this.guiAssets.getSubimage(285, 89, 33, 32), i * 230 + 226 + this.pauseSettingsPanelX, 305, null);
                        break;
                    case 5:
                        g.drawImage(this.guiAssets.getSubimage(326, 90, 33, 32), i * 230 + 226 + this.pauseSettingsPanelX, 305, null);
                        break;
                    default:
                        break;
                }

                int offset = 0;
                switch(levelNum){
                    case 1:
                    case 2:
                        offset = 0;
                        break;
                    case 3:
                    case 4:
                        offset = 2;
                        break;
                    case 5:
                    case 6:
                        offset = 4;
                        break;
                }
                g.setColor(new Color(193, 182, 129));
                g.fillRoundRect(i * 230 + 102 + this.pauseSettingsPanelX, 225, 70, 70, 8, 8);
                g.drawImage(this.littlesPandas.getSubimage(((sex + offset ) + ( 6 * spicies)) * 64, 0, 64, 64), i * 230 + 105 + this.pauseSettingsPanelX, 228, null);
            }
        }

        if(this.selectedItemSaves == 1){
            this.bgBtn = this.guiAssets.getSubimage(491, 1, 120, 99);
        }
        else{
            this.bgBtn = this.guiAssets.getSubimage(370, 1, 120, 99);
        }

        g.drawImage(this.bgBtn, this.w/3 - 60 + this.pauseSettingsPanelX, this.h - 110, null);
        g.drawImage(this.guiAssets.getSubimage(176, 93, 34, 35), this.w/3 - 20 + this.pauseSettingsPanelX, this.h - 79, null);
        
        if(this.selectedItemSaves == 2){
            this.bgBtn = this.guiAssets.getSubimage(491, 1, 120, 99);
        }
        else{
            this.bgBtn = this.guiAssets.getSubimage(370, 1, 120, 99);
        }
        
        g.drawImage(this.bgBtn, 2*this.w/3 - 60 + this.pauseSettingsPanelX, this.h - 110, null);
        g.drawImage(this.guiAssets.getSubimage(239, 402, 39, 36), 2*this.w/3 - 20 + this.pauseSettingsPanelX, this.h - 79, null);
    }
    
    /**
     * 
     * @param elapsedTime
     * @return 
     */
    public Scene updatePause(double elapsedTime){
        if(this.game.listener.pause.typed && this.currentScene != popinsScenes.MENU && this.currentScene != popinsScenes.NONE){
            this.currentScene = popinsScenes.MENU;
        }
        
        switch(this.currentScene){
            case MENU:
                for(int i=0;i<this.btnPosMenu.length;i++){
                    if(this.btnPosMenu[i][0] < this.w/2 - 107){
                        this.btnPosMenu[i][0] += 30;
                    }
                }
                
                this.timerMenu += elapsedTime;
                if(this.pauseMenuX < 0 && this.timerMenu > 3){
                    this.pauseSettingsPanelX += 80;
                    this.pauseMenuX += 80;
                    this.timerMenu = 0;
                }
                
                if(this.pauseMenuX == 0 && this.renderSaves){
                    this.renderSaves = false;
                }
                
                break;
            case SAVES:
                this.timerMenu += elapsedTime;
                if(this.pauseSettingsPanelX > 0 && this.timerMenu > 3){
                    this.pauseSettingsPanelX -= 80;
                    this.pauseMenuX -= 80;
                    this.timerMenu = 0;
                }
                break;
            case SETTINGS:
                this.timerMenu += elapsedTime;
                if(this.pauseSettingsPanelX > 0 && this.timerMenu > 3){
                    this.pauseSettingsPanelX -= 80;
                    this.pauseMenuX -= 80;
                    this.timerMenu = 0;
                }
                
                if(this.game.listener.e != null){
                    this.processKeyPause(this.game.listener.e);
                }
                break;
        }
        
        this.hoverPause();
        this.timeF += elapsedTime;
        return this.clickPause();
    }
    
    /**
     * 
     */
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
    
    /**
     * 
     * @param mouseX
     * @param mouseY 
     */
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
    
    /**
     * 
     * @param mouseX
     * @param mouseY 
     */
    public void hoverSettings(int mouseX, int mouseY){
        if(mouseX > this.w/2 - 60 && mouseX < this.w/2 + 60 && mouseY > this.h - 110 && mouseY < this.h - 11){
            this.selectedItemSettings = 1;
        }
        else{
            this.selectedItemSettings = 0;
        }
    }
    
    /**
     * 
     * @param mouseX
     * @param mouseY 
     */
    public void hoverSaves(int mouseX, int mouseY){
        if(mouseX > this.w/3 - 60 && mouseX < this.w/3 + 60 && mouseY > this.h - 110 && mouseY < this.h - 11){
            this.selectedItemSaves = 1;
        }
        else if(mouseX > 2*this.w/3 - 60 && mouseX < 2*this.w/3 + 60 && mouseY > this.h - 110 && mouseY < this.h - 11){
            this.selectedItemSaves = 2;
        }
        else{
            this.selectedItemSaves = 0;
        }
    }
    
    /**
     * 
     * @return 
     */
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
    
    /**
     * 
     * @param currentScene
     * @return 
     */
    public Scene clickPauseMenu(Scene currentScene){
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItemMenu){
                case 1:
                    int[][] posBtns = {
                        {this.w/2 - 107 - (15*30), 140},
                        {this.w/2 - 107 - (17*30), 240},
                        {this.w/2 - 107 - (19*30), 340},
                        {this.w/2 - 107 - (21*30), 440}
                    };
                    this.btnPosMenu = posBtns;
                    this.game.paused = false;
                    this.renderSaves = false;
                    break;
                case 2:
                    this.currentScene = popinsScenes.SAVES;
                    this.renderSaves = true;
                    break;
                case 3:
                    this.currentScene = popinsScenes.SETTINGS;
                    this.renderSaves = false;
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
    
    /**
     * 
     * @param currentScene
     * @return 
     */
    public Scene clickPauseSaves(Scene currentScene){
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItemSaves){
                case 1:
                    //save
                    if(this.selectedSave != 0){
                        JSONObject save = Save.getInstance().getSave(this.selectedSave - 1);
                        if(!save.isEmpty()){
                            CustomDialog dialog = new CustomDialog();
                            dialog.setMessageText(this.popupLabel);
                            dialog.setGame(this.game);
                            this.addDialog(dialog);
                        }
                        else{
                            Save.getInstance().saveGame(this.selectedSave - 1, this.level, this.player);
                        }
                    }
                    break;
                case 2:
                    this.currentScene = popinsScenes.MENU;
                    break;
                default:
                    break;
            }
            
            int mouseX = this.game.listener.mouseX;
            int mouseY = this.game.listener.mouseY;
            
            if(mouseX > 70 && mouseX < 70 + 217 && mouseY > 155 && mouseY < 155 + 216){
                this.selectedSave = 1;
            }
            else if(mouseX > 230 + 70 && mouseX < 230 + 70 + 217 && mouseY > 155 && mouseY < 155 + 216){
                this.selectedSave = 2;
            }
            else if(mouseX > 460 + 70 && mouseX < 460 + 70 + 217 && mouseY > 155 && mouseY < 155 + 216){
                this.selectedSave = 3;
            }
            else if(this.selectedItemSaves == 0){
                this.selectedSave = 0;
            }
        }
        
        return currentScene;
    }
    
    /**
     * 
     * @param e 
     */
    public void processKeyPause(KeyEvent e){
        for(int i=0;i<this.optionButtons.size();i++){
            if(this.optionButtons.get(i).isEditing()){
                this.optionButtons.get(i).processKey(e);
            }
        }
    }
    
    /**
     * 
     * @param currentScene
     * @return 
     */
    public Scene clickPauseSettings(Scene currentScene){
        if(this.game.listener.mousePressed){
            
            int mouseX = this.game.listener.mouseX;
            int mouseY = this.game.listener.mouseY;
            
            if(this.game.listener.mouseClickCount == 1){
                switch(this.selectedItemSettings){
                    case 1:
                        this.eventY = 0;
                        this.timeEventFree = 0;
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
            else if(mouseX > 255 && mouseX < 495 && mouseY > 296 && mouseY < 310){
                this.posBar = mouseX;
                int newVolume = (int)((255 - posBar)/ -2.4);
                Settings.getInstance().setConfigValue("Sound", Integer.toString(newVolume));
            }
        }
        
        return currentScene;
    }
    
    /**
     * 
     * @param dialog 
     */
    public void addDialog(CustomDialog dialog){
        this.dialog = dialog;
    }
    
    /**
     * 
     * @param cagesMap 
     */
    public void setLevelCagesMap(List<List<CageEntity>> cagesMap){
        for(int i=0;i<cagesMap.size();i++){
            if(i + 1 == this.nbLevel)
                this.level.setCagesInLevel(cagesMap.get(i));
            else
                this.level.cagesMap.set(i, cagesMap.get(i));
        }
    }
    
    /**
     * 
     * @param t time elapsed from start of animation
     * @param b start value
     * @param c value change
     * @param d duration of animation
     * @return 
     */
    public int easeOut(float t, float b, float c, float d){
        if((t /= d) < (1 / 2.75f))
            return (int)((c * 7.5625f * t * t) + b);
        else if(t < (2 / 2.75f))
            return (int)(c * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f) + b);
        else if(t < (2.5 / 2.75))
            return (int)(c * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f) + b);
        else
            return (int)(c * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f) + b);
    }
    
    /**
     * 
     * @param t
     * @param b
     * @param c
     * @param d
     * @return 
     */
    public int cubicEaseIn (float t,float b , float c, float d) {
        return (int)(c * (t /= d) * t * t + b);
    }
    
    /**
     * 
     * @param text
     * @param w
     * @return 
     */
    private String substringLabels(String text, int w){
        StringTokenizer tok = new StringTokenizer(text, " ");
        StringBuilder output = new StringBuilder(text.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken() + " ";

            if (lineLen + word.length() > w) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word);
            lineLen += word.length();
        }
        return output.toString();
    }
}