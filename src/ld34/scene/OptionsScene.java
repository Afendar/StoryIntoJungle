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
    
    public Font font, fontL, fontU;
    public String title, btnBack, difficulty, easy, medium, hard, hardcore, language, french, english, commands,
            name, sexe, type, volume, controlJump, controlWalk;
    public BufferedImage background2, btnCharacter, btnConfig, btnControls, sGirl, sBoy, sPanda, spRoux, bgHeads, 
            bgHeadsRed, soundBar;
    public CustomTextField nameField;
    public int[][] btnCoords;
    public int selectedItem, currentTab, posBar;
    public ArrayList<OptionButton> optionButtons = new ArrayList<>();
   
    public OptionsScene(int w, int h, Game game){
        
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.fontU = this.font;
            this.fontU = this.fontU.deriveFont(fontAttributes);
            
            url = this.getClass().getResource("/backgroundoptions.png");
            this.background2 = ImageIO.read(url);

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
        
        this.btnCharacter = this.spritesheetGui.getSubimage(0, 110, 50, 50);
        this.btnConfig = this.spritesheetGui.getSubimage(50, 110, 50, 50);
        this.btnControls = this.spritesheetGui.getSubimage(100, 110, 50, 50);
        this.sGirl = this.spritesheetGui.getSubimage(0, 160, 50, 48);
        this.sBoy = this.spritesheetGui.getSubimage(50, 160, 50, 48);
        this.sPanda = this.spritesheetGui.getSubimage(50, 160, 50, 48);
        this.spRoux = this.spritesheetGui.getSubimage(50, 208, 50, 48);
        this.bgHeads = this.spritesheetGui.getSubimage(100, 160, 50, 48);
        this.bgHeadsRed = this.spritesheetGui.getSubimage(100, 208, 50, 48);
        this.soundBar = this.spritesheetGui.getSubimage(0, 256, 210, 25);
        
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
        
        this.nameField = new CustomTextField("name", Settings.getInstance().getConfigValue("Name"), 230, 150, 250, 15);
        this.nameField.setFont(this.font);
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
                    if(mouseX > 230 && mouseX < 280 && mouseY > 190 && mouseY < 240){
                        //male btn
                        this.sPanda = this.spritesheetGui.getSubimage(50, 160, 50, 48);
                        this.spRoux = this.spritesheetGui.getSubimage(50, 208, 50, 48);
                        Settings.getInstance().setConfigValue("Sex", "0");
                    }
                    else if(mouseX > 310 && mouseX < 360 && mouseY > 190 && mouseY < 240){
                        //female btn
                        this.sPanda = this.spritesheetGui.getSubimage(0, 160, 50, 48);
                        this.spRoux = this.spritesheetGui.getSubimage(0, 208, 50, 48);
                        Settings.getInstance().setConfigValue("Sex", "1");
                    }
                    else if(mouseX > 230 && mouseX < 280 && mouseY > 270 && mouseY < 320){
                        //grandpanda btn
                        this.sBoy = this.spritesheetGui.getSubimage(50, 160, 50, 48);
                        this.sGirl = this.spritesheetGui.getSubimage(0, 160, 50, 48);
                        Settings.getInstance().setConfigValue("Spicies", "0");
                    }
                    else if(mouseX > 310 && mouseX < 360 && mouseY > 270 && mouseY < 320){
                        //panda roux btn
                        this.sBoy = this.spritesheetGui.getSubimage(50, 208, 50, 48);
                        this.sGirl = this.spritesheetGui.getSubimage(0, 208, 50, 48);
                        Settings.getInstance().setConfigValue("Spicies", "1");
                    }
                    break;
                case 1:
                    if(mouseX > this.w/5 && mouseX < (this.w/5) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        Settings.getInstance().setConfigValue("Difficulty", "0");
                    }
                    else if(mouseX > 2*this.w/5 && mouseX < (2*this.w/4) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        Settings.getInstance().setConfigValue("Difficulty", "2");
                    }
                    else if(mouseX > 3*this.w/5 && mouseX < (3*this.w/5) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        Settings.getInstance().setConfigValue("Difficulty", "4");
                    }
                    else if(mouseX > 4*this.w/5 && mouseX < (4*this.w/5) + 107 &&
                            mouseY > 200 && mouseY < 200 + 40){
                        Settings.getInstance().setConfigValue("Difficulty", "5");
                    }
                    else if(mouseX > this.w/5 && mouseX < (this.w/5) + 107 &&
                            mouseY > 290 && mouseY < 290 + 40){
                        Settings.getInstance().setConfigValue("Lang", "0");
                        this.reloadLangs();
                    }
                    else if(mouseX > 2*this.w/5 && mouseX < (2*this.w/5) + 107 &&
                            mouseY > 290 && mouseY < 290 + 40){
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
        g.drawImage(this.background2, 0, 0, null);
        
        //draw menu options
        g.setColor(new Color(146, 171, 63));
        switch(this.currentTab){
            case 0:
                g.fillRect(0, 165, 92, 80);
                break;
            case 1:
                g.fillRect(0, 245, 92, 80);
                break;
            case 2:
                g.fillRect(0, 325, 92, 80);
                break;
        }
        g.drawImage(this.btnCharacter, 21, 180, null);
        g.drawImage(this.btnConfig, 21, 260, null);
        g.drawImage(this.btnControls, 21, 340, null);
        
        //End menu options
        
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
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 60);

        //end btn
        metrics = g.getFontMetrics(this.font);
        g.setFont(this.font);
        int backWidth = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, this.btnCoords[0][0], this.btnCoords[0][1], null);
        if(this.selectedItem == 1){
            g2d.rotate(-0.1, (3*this.w/4)+25, 475);
            g.setColor(this.darkGreen);
            g.drawString(this.btnBack, (3*this.w/4) + 25 - backWidth/2, 495);
            g2d.rotate(0.1, (3*this.w/4)+25, 475);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnBack, (3*this.w/4) + 25 - backWidth/2, 495);
        }
        g.drawImage(this.foreground2, 0, 0, null);
    }
    
    public void processHover(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        if(mouseX > this.btnCoords[0][0] && mouseX < this.btnCoords[0][0] + 214 &&
                mouseY > this.btnCoords[0][1] && mouseY < this.btnCoords[0][1] + 70){
            //if btn Back
            if(this.selectedItem != 1)
                new Thread(Sound.hover::play).start();
            this.selectedItem = 1;
        }
        else if(mouseX > 21 && mouseX < 71 &&
                mouseY > 180 && mouseY < 230){
            if(this.selectedItem != 2)
            this.selectedItem = 2;
        }
        else if(mouseX > 21 && mouseX < 71 &&
                mouseY > 260 && mouseY < 310){
            if(this.selectedItem != 3)
            this.selectedItem = 3;
        }
        else if(mouseX > 21 && mouseX < 71 &&
                mouseY > 340 && mouseY < 390){
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
                    new Thread(Sound.select::play).start();
                    this.nameField.processClick(this.game.listener.mouseX, this.game.listener.mouseY);
                    break;
                case 1:
                    new Thread(Sound.select::play).start();
                    break;
                case 2:
                    this.processButtonsClick();
                    break;
            }
            
            
            switch(this.selectedItem){
                case 1:
                    Settings.getInstance().saveConfig();
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                case 2:
                    //player infos tab
                    this.currentTab = 0;
                    Settings.getInstance().saveConfig();
                    break;
                case 3:
                    //settings tab
                    this.currentTab = 1;
                    Settings.getInstance().saveConfig();
                    break;
                case 4:
                    //controls tab
                    this.currentTab = 2;
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
        g.setColor(Color.BLACK);
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int nameW = metrics.stringWidth(this.name);
        g.drawString(this.name, 150, 150);
        
        int sexeW = metrics.stringWidth(this.sexe);
        g.drawString(this.sexe, 150, 220);
        
        switch(Integer.parseInt(Settings.getInstance().getConfigValue("Sex"))){
            case 0:
                g.drawImage(this.bgHeadsRed, 230, 191, null);
                g.drawImage(this.bgHeads, 310, 191, null);
                break;
            case 1:
                g.drawImage(this.bgHeads, 230, 191, null);
                g.drawImage(this.bgHeadsRed, 310, 191, null);
                break;
        }

        g.drawImage(this.sBoy, 230, 191, null);
        g.drawImage(this.sGirl, 310, 191, null);
        
        g.setColor(Color.BLACK);
        int typeW = metrics.stringWidth(this.type);
        g.drawString(this.type, 150, 300);

        switch(Integer.parseInt(Settings.getInstance().getConfigValue("Spicies"))){
            case 0:
                g.drawImage(this.bgHeadsRed, 230, 271, null);
                g.drawImage(this.bgHeads, 310, 271, null);
                break;
            case 1:
                g.drawImage(this.bgHeads, 230, 271, null);
                g.drawImage(this.bgHeadsRed, 310, 271, null);
                break;
        }
        
        //Grand panda
        g.drawImage(this.sPanda, 230, 271, null);
        //Panda roux
        g.drawImage(this.spRoux, 310, 271, null);
        
        this.nameField.render(g);
    }
    
    public void renderGameSettings(Graphics g){
        //difficulty
        g.setColor(Color.BLACK);
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int difficultyWidth = metrics.stringWidth(this.difficulty);
        g.drawString(this.difficulty, 150, 180);
        int easyWidth = metrics.stringWidth(this.easy);
        
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 0){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, this.w/5, 200, null);
        }else{
            g.setFont(this.font);
            g.drawImage(this.bgBtnSmall, this.w/5, 200, null);
        }
        
        g.drawString(this.easy, this.w/5 - easyWidth/2 + 50, 225);
        
        int mediumWidth = metrics.stringWidth(this.medium);
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 2){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, 2*(this.w/5), 200, null);
        }else{
            g.setFont(this.font);
            g.drawImage(this.bgBtnSmall, 2*(this.w/5), 200, null);
        }
        g.drawString(this.medium, 2*(this.w/5) - mediumWidth/2 + 50, 225);
        
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 4){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, 3*(this.w/5), 200, null);
        }else{
            g.setFont(this.font);
            g.drawImage(this.bgBtnSmall, 3*(this.w/5), 200, null);
        }

        int hardWidth = metrics.stringWidth(this.hard);
        g.drawString(this.hard, 3*(this.w/5) - hardWidth/2 + 50, 225);
        
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Difficulty")) == 5){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, 4*this.w/5, 200, null);
        }else{
            g.setFont(this.font);
            g.drawImage(this.bgBtnSmall, 4*this.w/5, 200, null);
        }
        
        int hardcoreW = metrics.stringWidth(this.hardcore);
        g.drawString(this.hardcore, 4*this.w/5 - hardcoreW/2 + 50, 225);
        
        //languages
        g.setFont(this.font);
        g.setColor(Color.BLACK);
        int languageW = metrics.stringWidth(this.language);
        g.drawString(this.language, 150, 270);
        
        int englishW = metrics.stringWidth(this.english);
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 0){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, this.w/5, 290, null);
        }else{
            g.setFont(this.font);
            g.drawImage(this.bgBtnSmall, this.w/5, 290, null);
        }
        g.drawString(this.english, this.w/4 - englishW/2 + 10, 315);
        
        int frenchW = metrics.stringWidth(this.french);
        if(Integer.parseInt(Settings.getInstance().getConfigValue("Lang")) == 1){
            g.setFont(this.fontU);
            g.drawImage(this.bgBtnSmallRed, 2*this.w/5, 290, null);
        }else{
            g.setFont(this.font);
            g.drawImage(this.bgBtnSmall, 2*this.w/5, 290, null);
        }
        g.drawString(this.french, 2*this.w/5 - frenchW/2 + 52, 315);
        
        //Volume
        
        g.setFont(this.font);
        g.drawString(this.volume, 150, 380);
        
        int red = 255;
        int green = 0;
        for(int i=0;i<255;i++){
            g.setColor(new Color(red, green, 0));
            g.fillRect((int)(153 + (i * 0.8)), 403, 1, 19);
            red--;
            green++;
        }
        g.drawImage(this.soundBar, 150, 400, null);
        g.setColor(Color.BLACK);
        g.fillRect(posBar, 405, 4, 15);
    }
    
    public void renderControlsSettings(Graphics g){
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 60);
        
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