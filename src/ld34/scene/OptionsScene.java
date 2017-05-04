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

public class OptionsScene extends Scene {
    
    public Font font, fontS, fontL, fontU;
    public String title, btnBack, difficulty, easy, medium, hard, hardcore, language, french, english, commands,
            name, sexe, type, volume, controlJump, controlWalk;
    public BufferedImage iconPlayer, iconSettings, iconControls, backgroundMenu, separatorMenu, btnSmallSelected, backgroundPlayer,
            iconBoy, iconGirl, iconGP, iconRP, previewsPandas, soundBar, bottomControls;
    public CustomTextField nameField;
    public int[][] btnCoords;
    public int selectedItem, currentTab, posBar;
    public ArrayList<OptionButton> optionButtons = new ArrayList<>();
   
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
                250, 
                200
        );
        btn1.setFont(this.font);
        this.optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                250, 
                250
        );
        btn2.setFont(this.font);
        this.optionButtons.add(btn2);
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        this.currentTab = 0;
        
        int volumeVal = Integer.parseInt(Settings.getInstance().getConfigValue("Sound"));
        this.posBar = (int)(153 + (2 * volumeVal));
        
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
        
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        
        this.bundle = ResourceBundle.getBundle("lang.options", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("title");
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
    }

    @Override
    public Scene update(double dt){
        processHover();
        
        if(this.game.listener.e != null){
            this.processKey(this.game.listener.e);
        }
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            int mouseX = this.game.listener.mouseX;
            int mouseY = this.game.listener.mouseY;
            switch(this.currentTab){
                case 0:
                    if(mouseX > 199 && mouseX < 319 && mouseY > 266 && mouseY < 365){
                        //male btn
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Sex", "0");
                    }
                    else if(mouseX > 369 && mouseX < 489 && mouseY > 266 && mouseY < 365){
                        //female btn
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Sex", "1");
                    }
                    else if(mouseX > 199 && mouseX < 319 && mouseY > 386 && mouseY < 485){
                        //grandpanda btn
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Spicies", "0");
                    }
                    else if(mouseX > 369 && mouseX < 489 && mouseY > 386 && mouseY < 485){
                        //panda roux btn
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Spicies", "1");
                    }
                    break;
                case 1:
                    if(mouseX > this.w/5 && mouseX < (this.w/5) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Difficulty", "0");
                    }
                    else if(mouseX > 2*this.w/5 && mouseX < (2*this.w/4) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Difficulty", "2");
                    }
                    else if(mouseX > 3*this.w/5 && mouseX < (3*this.w/5) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Difficulty", "4");
                    }
                    else if(mouseX > 4*this.w/5 && mouseX < (4*this.w/5) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Difficulty", "5");
                    }
                    else if(mouseX > this.w/5 && mouseX < (this.w/5) + 107 &&
                            mouseY > 290 && mouseY < 290 + 40){
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Lang", "0");
                        this.reloadLangs();
                    }
                    else if(mouseX > 2*this.w/5 && mouseX < (2*this.w/5) + 107 &&
                            mouseY > 290 && mouseY < 290 + 40){
                        new Thread(Sound.select::play).start();
                        Settings.getInstance().setConfigValue("Lang", "1");
                        this.reloadLangs();
                    }
                    else if(mouseX >= 153 && mouseX <= 353 && mouseY > 400 && mouseY < 425){
                        this.posBar = mouseX;
                        int newVolume = (int)((153 - posBar)/ -2);
                        //posBar = (int)(153 + (2 * volume));
                        //volume = (int)((153 - posBar)/(-2));
                        Settings.getInstance().setConfigValue("Sound", Integer.toString(newVolume));
                    }
                    break;
                case 2:
                    break;
            }
        }
        return processClick();
    }

    public void reloadLangs(){
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.options", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("title");
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
        
        if(sex == 0 && species == 0){
            g.drawImage(this.previewsPandas.getSubimage(0, 0, 128, 128), 616, 234, null);
        }
        else if(sex == 1 && species == 0){
            g.drawImage(this.previewsPandas.getSubimage(128, 0, 128, 128), 616, 234, null);
        }
        else if(sex == 0 && species == 1){
            g.drawImage(this.previewsPandas.getSubimage(256, 0, 128, 128), 616, 234, null);
        }
        else if(sex == 1 && species == 1){
            g.drawImage(this.previewsPandas.getSubimage(384, 0, 128, 128), 616, 234, null);
        }
    }
    
    public void renderGameSettings(Graphics g){
        //difficulty
        g.setColor(Color.BLACK);
        g.setFont(this.fontS);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int difficultyWidth = metrics.stringWidth("Difficulté");
        g.drawString("Difficulté", 170, 128 + metrics.getAscent());
        
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
        int languageW = metrics.stringWidth(this.language);
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
            g.fillRect((int)(185 + (i * 0.8)), 472, 1, 19);
            red--;
            green++;
        }
        g.drawImage(this.spritesheetGui2.getSubimage(0, 438, 299, 62), 175, 444, null);
        g.setColor(Color.BLACK);
        g.fillRect(posBar, 405, 4, 15);
    }
    
    public void renderControlsSettings(Graphics g){
        g.setFont(this.font);
        g.drawString(this.controlJump, 150, 200);
        g.drawString(this.controlWalk, 150, 250);
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).render(g);
        }
    }

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