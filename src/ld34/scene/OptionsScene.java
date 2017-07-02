package ld34.scene;

import audio.Sound;
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
import ld34.profile.Settings;
import core.CustomTextField;
import core.Game;
import core.OptionButton;
import javax.swing.JPanel;

/**
 * OptionsScene class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class OptionsScene extends Scene {
    
    public Font font, fontS, fontL, fontU;
    public String title, btnBack, difficulty, easy, medium, hard, hardcore, language, french, english, commands,
            name, sexe, type, volume, controlJump, controlWalk;
    public BufferedImage iconPlayer, iconSettings, iconControls, backgroundMenu, separatorMenu, btnSmallSelected, backgroundPlayer,
            iconBoy, iconGirl, iconGP, iconRP, previewsPandas, oldPreview, currentPreview, soundBar, bottomControls, jumpingPlayer, walkingPlayer;
    public CustomTextField nameField;
    public int[][] btnCoords;
    public int selectedItem, currentTab, posBar, timerSlider, x2;
    public ArrayList<OptionButton> optionButtons = new ArrayList<>();
    public JPanel sliderContainer;
    public boolean startSlide;
   
    /**
     * 
     * @param w
     * @param h
     * @param game 
     */
    public OptionsScene(int w, int h, Game game){
        
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 20.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.fontU = this.font;
            this.fontU = this.fontU.deriveFont(fontAttributes);
            
            url = this.runtimeClass.getResource("/previews_pandas.png");
            this.previewsPandas = ImageIO.read(url);
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        
        OptionButton btn1 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))), 
                "Jump", 
                219, 
                342
        );
        btn1.setFont(this.font);
        this.optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                476, 
                342
        );
        btn2.setFont(this.font);
        this.optionButtons.add(btn2);
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        this.currentTab = 0;
        this.timerSlider = 0;
        this.x2 = 616;
        
        this.startSlide = false;
        
        int volumeVal = Integer.parseInt(Settings.getInstance().getConfigValue("Sound"));
        this.posBar = (int)(200 + (2.4 * volumeVal));
        
        this.iconPlayer = this.spritesheetGui2.getSubimage(679, 187, 50, 50);
        this.iconSettings = this.spritesheetGui2.getSubimage(729, 187, 50, 50); 
        this.iconControls = this.spritesheetGui2.getSubimage(779, 187, 50, 50);
        this.backgroundMenu = this.spritesheetGui2.getSubimage(968, 0, 114, 600);
        this.separatorMenu = this.spritesheetGui2.getSubimage(900, 0, 68, 600);
        this.btnSmallSelected = this.spritesheetGui2.getSubimage(612, 0, 124, 103);
        this.backgroundPlayer = this.spritesheetGui2.getSubimage(235, 127, 217, 216);
        this.iconBoy = this.spritesheetGui2.getSubimage(820, 1, 35, 36);
        this.iconGirl = this.spritesheetGui2.getSubimage(858, 1, 27, 40);
        this.iconGP = this.spritesheetGui2.getSubimage(674, 103, 47, 38);
        this.iconRP = this.spritesheetGui2.getSubimage(676, 147, 49, 38);
        this.soundBar = this.spritesheetGui2.getSubimage(0, 468, 299, 62);
        this.bottomControls = this.spritesheetGui2.getSubimage(680, 255, 212, 88);
        this.jumpingPlayer = this.spritesheetGui2.getSubimage(739, 65, 112, 84);
        this.walkingPlayer = this.spritesheetGui2.getSubimage(737, 1, 79, 64);
        
        int sex = Integer.parseInt(Settings.getInstance().getConfigValue("Sex"));
        int spicies = Integer.parseInt(Settings.getInstance().getConfigValue("Spicies"));
        
        this.currentPreview = this.previewsPandas.getSubimage(128 * (sex + spicies), 0, 128, 128);
        
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        
        this.bundle = ResourceBundle.getBundle("lang.lang", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("settings_title");
        this.btnBack = this.bundle.getString("backToMain");
        this.difficulty = this.bundle.getString("difficulty");
        this.easy = this.bundle.getString("easy");
        this.medium = this.bundle.getString("medium");
        this.hard = this.bundle.getString("hard");
        this.hardcore = this.bundle.getString("hardcore");
        this.language = this.bundle.getString("language");
        this.french = this.bundle.getString("french");
        this.english = this.bundle.getString("english");
        this.commands = this.bundle.getString("commands");
        this.name = this.bundle.getString("name");
        this.sexe = this.bundle.getString("sexe");
        this.type = this.bundle.getString("type");
        this.volume = this.bundle.getString("volume");
        this.controlJump = this.bundle.getString("ctrlJump");
        this.controlWalk = this.bundle.getString("ctrlWalk");
        
        this.nameField = new CustomTextField("name", Settings.getInstance().getConfigValue("Name"), 203, 183, 287, 46);
        this.nameField.setFont(this.font);
        
        this.sliderContainer = new JPanel();
    }

    @Override
    public Scene update(double dt){
        processHover();
        
        if(this.game.listener.e != null){
            this.processKey(this.game.listener.e);
        }
        
        if(this.game.listener.mousePressed){
            int mouseX = this.game.listener.mouseX;
            int mouseY = this.game.listener.mouseY;
            switch(this.currentTab){
                case 0:
                    if(this.game.listener.mouseClickCount == 1){
                        if(mouseX > 199 && mouseX < 319 && mouseY > 266 && mouseY < 365){
                            //male btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Sex").equals("1")){
                                Settings.getInstance().setConfigValue("Sex", "0");
                                this.startSlide = true;
                                this.oldPreview = this.currentPreview;
                                this.currentPreview = this.previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Spicies")) * 2), 0, 128, 128);
                            }
                        }
                        else if(mouseX > 369 && mouseX < 489 && mouseY > 266 && mouseY < 365){
                            //female btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Sex").equals("0")){
                                Settings.getInstance().setConfigValue("Sex", "1");
                                this.startSlide = true;
                                this.oldPreview = this.currentPreview;
                                this.currentPreview = this.previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Spicies")) * 2 + 1), 0, 128, 128);
                            }
                        }
                        else if(mouseX > 199 && mouseX < 319 && mouseY > 386 && mouseY < 485){
                            //grand panda btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Spicies").equals("1")){
                                Settings.getInstance().setConfigValue("Spicies", "0");
                                this.startSlide = true;
                                this.oldPreview = this.currentPreview;
                                this.currentPreview = this.previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Sex"))), 0, 128, 128);
                            }
                        }
                        else if(mouseX > 369 && mouseX < 489 && mouseY > 386 && mouseY < 485){
                            //panda roux btn
                            new Thread(Sound.select::play).start();
                            if(Settings.getInstance().getConfigValue("Spicies").equals("0")){
                                Settings.getInstance().setConfigValue("Spicies", "1");
                                this.startSlide = true;
                                this.oldPreview = this.currentPreview;
                                this.currentPreview = this.previewsPandas.getSubimage(128 * (Integer.parseInt(Settings.getInstance().getConfigValue("Sex")) + 2), 0, 128, 128);
                            }
                        }
                    }
                    break;
                case 1:
                    if(this.game.listener.mouseClickCount == 1){
                        if(mouseX > 189 && mouseX < 189 + 120 &&
                                mouseY > 166 && mouseY < 166 + 99){
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "0");
                        }
                        else if(mouseX > 339 && mouseX < 339 + 120 &&
                                mouseY > 166 && mouseY < 166 + 99){
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "2");
                        }
                        else if(mouseX > 479 && mouseX < 479 + 120 &&
                                mouseY > 166 && mouseY < 166 + 99){
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "4");
                        }
                        else if(mouseX > 629 && mouseX < 629 + 120 &&
                                mouseY > 166 && mouseY < 166 + 99){
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Difficulty", "5");
                        }
                        else if(mouseX > 221 && mouseX < 221 + 234 &&
                                mouseY > 310 && mouseY < 310 + 100){
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Lang", "0");
                            this.reloadLangs();
                        }
                        else if(mouseX > 481 && mouseX < 481 + 234 &&
                                mouseY > 305 && mouseY < 305 + 100){
                            new Thread(Sound.select::play).start();
                            Settings.getInstance().setConfigValue("Lang", "1");
                            this.reloadLangs();
                        }
                    }
                    
                    if(mouseX >= 200 && mouseX <= 440 && mouseY > 475 && mouseY < 489){
                        this.posBar = mouseX;
                        int newVolume = (int)((200 - posBar)/ - 2.4);
                        Settings.getInstance().setConfigValue("Sound", Integer.toString(newVolume));
                    }
                    break;
                case 2:
                    break;
            }
        }
        
        if(this.startSlide){
            this.timerSlider += dt;
            if(this.timerSlider >= 2){
                this.timerSlider = 0;
                this.x2 -= 18;
                if(this.x2 <= 570 - 46 - 128){
                    this.startSlide = false;
                    this.x2 = 616;
                }
            }
        }
        
        return processClick();
    }

    /**
     * 
     */
    public void reloadLangs(){
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.lang", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("settings_title");
        this.btnBack = this.bundle.getString("backToMain");
        this.difficulty = this.bundle.getString("difficulty");
        this.easy = this.bundle.getString("easy");
        this.medium = this.bundle.getString("medium");
        this.hard = this.bundle.getString("hard");
        this.hardcore = this.bundle.getString("hardcore");
        this.language = this.bundle.getString("language");
        this.french = this.bundle.getString("french");
        this.english = this.bundle.getString("english");
        this.commands = this.bundle.getString("commands");
        this.name = this.bundle.getString("name");
        this.sexe = this.bundle.getString("sexe");
        this.type = this.bundle.getString("type");
        this.volume = this.bundle.getString("volume");
        this.controlJump = this.bundle.getString("ctrlJump");
        this.controlWalk = this.bundle.getString("ctrlWalk");
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).initLocales();
        }
    }
    
    @Override
    public void render(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.background, 0, 0, null);
        
        g.drawImage(this.backgroundMenu, 0, 0, null);

        //draw menu options
        g.setColor(new Color(124, 180, 94));
        switch(this.currentTab){
            case 0:
                g.fillRect(0, 205, 113, 80);
                break;
            case 1:
                g.fillRect(0, 286, 113, 80);
                break;
            case 2:
                g.fillRect(0, 367, 113, 80);
                break;
        }
        
        g.drawImage(this.iconPlayer, 25, 220, null);
        g.drawImage(this.iconSettings, 25, 300, null);
        g.drawImage(this.iconControls, 25, 380, null);
        
        g.drawImage(this.separatorMenu, 89, 0, null);
        
        g.setColor(Scene.BLACKSHADOW);
        g.fillRect(0, 35, this.w, 60);
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 75);
        
        switch(this.currentTab){
            case 0:
                this.renderPlayerSettings(g);
                break;
            case 1:
                this.renderGameSettings(g);
                break;
            case 2:
                this.renderControlsSettings(g);
                break;
            default:
                break;
        }

        //end btn
        metrics = g.getFontMetrics(this.font);
        g.setFont(this.font);
        int backWidth = metrics.stringWidth(this.btnBack);
        if(this.selectedItem == 1){
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 232, 234, 99);
        }

        if(this.selectedItem == 1){
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 595, 471, null);
        }
        else{
            g.drawImage(this.spritesheetGui2.getSubimage(370, 1, 120, 99), 595, 471, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(243, 94, 42, 34), 633, 502, null);
        
        g.drawImage(this.foreground2, 0, 0, null);
    }
    
    /**
     * 
     */
    public void processHover(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        if(mouseX > 595 && mouseX < 715 &&
                mouseY > 471 && mouseY < 570){
            //if btn Back
            if(this.selectedItem != 1)
                new Thread(Sound.hover::play).start();
            this.selectedItem = 1;
        }
        else if(mouseX > 0 && mouseX < 113 &&
                mouseY > 205 && mouseY < 285){
            if(this.selectedItem != 2)
            this.selectedItem = 2;
        }
        else if(mouseX > 0 && mouseX < 113 &&
                mouseY > 286 && mouseY < 366){
            if(this.selectedItem != 3)
            this.selectedItem = 3;
        }
        else if(mouseX > 0 && mouseX < 113 &&
                mouseY > 367 && mouseY < 447){
            //if btn controls
            if(this.selectedItem != 4)
            this.selectedItem = 4;
        }
        else{
            this.selectedItem = 0;
        }
    }
    
    /**
     * 
     * @return 
     */
    public Scene processClick(){
        
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.currentTab){
                case 0:
                    this.nameField.processClick(this.game.listener.mouseX, this.game.listener.mouseY);
                    break;
                case 1:
                    break;
                case 2:
                    this.processButtonsClick();
                    break;
            }
            
            
            switch(this.selectedItem){
                case 1:
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                case 2:
                    //player infos tab
                    this.currentTab = 0;
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    break;
                case 3:
                    //settings tab
                    this.currentTab = 1;
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    break;
                case 4:
                    //controls tab
                    this.currentTab = 2;
                    new Thread(Sound.select::play).start();
                    Settings.getInstance().saveConfig();
                    break;
                default:
                    currentScene = this;
                    break;
            }
        }
        return currentScene;
    }
    
    /**
     * 
     * @param g 
     */
    public void renderPlayerSettings(Graphics g){
        
        this.nameField.render(g);
        
        int sex = Integer.parseInt(Settings.getInstance().getConfigValue("Sex"));
        if(sex == 0){
            g.drawImage(this.btnSmallSelected, 199, 266, null);
        }
        else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 199, 266, null);
        }
        if(sex == 1){
            g.drawImage(this.btnSmallSelected, 368, 265, null);
        }
        else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 368, 265, null);
        }
        g.drawImage(this.iconBoy, 239, 297, null);
        g.drawImage(this.iconGirl, 413, 293, null);

        int species = Integer.parseInt(Settings.getInstance().getConfigValue("Spicies"));
        if(species == 0){
            g.drawImage(this.btnSmallSelected, 199, 386, null);
        }
        else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 199, 386, null);
        }
        if(species == 1){
            g.drawImage(this.btnSmallSelected, 369, 386, null);
        }
        else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 369, 386, null);
        }
        g.drawImage(this.iconGP, 233, 416, null);
        g.drawImage(this.iconRP, 402, 416, null);

        g.drawImage(this.backgroundPlayer, 570, 190, null);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(0,0,0,25));
        g2d.fillOval(607, 327, 150, 40);
        
        if(this.startSlide && this.x2 <= 570 + 46 && this.x2 > 570 + 46 - 128){
            int w = 128 - ( 570 - this.x2 + 46);
            int w2 = w + 20;
            if(w > 0){
                BufferedImage previewOldPlayer = this.oldPreview.getSubimage(570 - this.x2 + 46, 0, w, 128);
                g.drawImage(previewOldPlayer, 616, 234, null);
            }
            if(128 - w2 > 0 && 128 - w2 < 128 ){
                BufferedImage previewCurrentPlayer = this.currentPreview.getSubimage(0, 0, 128 - w2, 128);
                g.drawImage(previewCurrentPlayer, this.x2 + 128 + 20, 234, null);
            }
        }
        else{
            g.drawImage(this.currentPreview, 616, 234, null); 
        }
    }
    
    /**
     * 
     * @param g 
     */
    public void renderGameSettings(Graphics g){
        //difficulty
        g.setColor(Color.BLACK);
        g.setFont(this.fontS);
        FontMetrics metrics = g.getFontMetrics(this.font);
        g.drawString(this.difficulty, 170, 128 + metrics.getAscent());
        
        //easy
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 0){
            g.drawImage(this.btnSmallSelected, 189, 166, null);
        }else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 189, 166, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(285, 69, 17, 16), 239, 205, null);
        
        //medium
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 2){
            g.drawImage(this.btnSmallSelected, 339, 166, null);
        }else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 339, 166, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(325, 69, 35, 16), 380, 206, null);

        //hard
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 4){
            g.drawImage(this.btnSmallSelected, 479, 166, null);
        }else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 479, 166, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(285, 89, 33, 32), 521, 197, null);
        
        //hardcore
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 5){
            g.drawImage(this.btnSmallSelected, 629, 166, null);
        }else{
            g.drawImage(this.spritesheetGui2.getSubimage(491, 1, 120, 99), 629, 166, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(326, 90, 33, 32), 672, 197, null);
        
        //languages
        g.setFont(this.fontS);
        g.setColor(Color.BLACK);
        g.drawString(this.language, 170, 278 + metrics.getAscent());
        //english
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 0){
            g.drawImage(this.spritesheetGui2.getSubimage(0, 332, 238, 104), 221, 310, null);
        }else{
            g.drawImage(this.spritesheetGui2.getSubimage(0, 132, 234, 100), 221, 310, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(238, 344, 42, 28), 256, 345, null);
        g.setFont(this.font);
        g.drawString(this.english, 317, 346 + metrics.getAscent()/2 + 8);
        
        //french
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 1){
            g.drawImage(this.spritesheetGui2.getSubimage(0, 332, 238, 104), 481, 305, null);
        }else{
            g.drawImage(this.spritesheetGui2.getSubimage(0, 132, 234, 100), 481, 305, null);
        }
        g.drawImage(this.spritesheetGui2.getSubimage(238, 372, 42, 28), 517, 342, null);
        g.setFont(this.font);
        g.drawString(this.french, 580, 342 + metrics.getAscent()/2 + 8);

        
        //Volume
        
        g.setFont(this.fontS);
        g.drawString(this.volume, 170, 424 + metrics.getAscent());
        
        int red = 255;
        int green = 0;
        for(int i=0;i<255;i++){
            g.setColor(new Color(red, green, 0));
            g.fillRect((int)(200 + i), 472, 1, 19);
            red--;
            green++;
        }
        g.drawImage(this.spritesheetGui2.getSubimage(0, 438, 299, 62), 175, 444, null);
        g.setColor(Color.BLACK);
        g.fillRect(posBar, 475, 9, 14);
    }
    
    /**
     * 
     * @param g 
     */
    public void renderControlsSettings(Graphics g){
        g.drawImage(this.bottomControls, 218, 345, null);
        g.drawImage(this.bottomControls, 475, 345, null);
        
        g.drawImage(this.backgroundPlayer, 213, 147, null);
        g.drawImage(this.backgroundPlayer, 470, 147, null);
        
        g.drawImage(this.jumpingPlayer, 275, 227, null);
        g.drawImage(this.walkingPlayer, 542, 240, null);
        
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        g.drawString(this.controlJump, 290, 180 + metrics.getAscent());
        g.drawString(this.controlWalk, 543, 180 + metrics.getAscent());
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).render(g);
        }
    }

    /**
     * 
     * @param e 
     */
    public void processKey(KeyEvent e){
        
        switch(this.currentTab){
            case 0:
                this.nameField.processKey(e);
                break;
            case 1:
                break;
            case 2:
                for(int i=0;i<this.optionButtons.size();i++){
                    if(this.optionButtons.get(i).isEditing()){
                        this.optionButtons.get(i).processKey(e);
                    }
                }
                break;
        }
    }
    
    /**
     * 
     */
    public void processButtonsClick(){
        for(int i=0;i<this.optionButtons.size();i++){
            if(this.optionButtons.get(i).isEditing())
                return;
        }
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).processClick(this.game.listener.mouseX, this.game.listener.mouseY);
        }
    }
}