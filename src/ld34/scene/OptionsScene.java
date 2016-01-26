package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.Game;

public class OptionsScene extends Scene {
    
    public Font font, fontL, fontU;
    public String title, btnBack, difficulty, easy, medium, hard, language, french, english;
    public BufferedImage spritesheetGui, bgBtn, bgBtnSmall, background, forground;
    public int[][] btnCoords;
    public int selectedItem;
    
    public OptionsScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("gfx/fonts/kaushanscriptregular.ttf"));
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.fontU = this.font;
            this.fontU = this.fontU.deriveFont(fontAttributes);
            this.spritesheetGui = ImageIO.read(new File("gfx/gui.png"));
            this.background = ImageIO.read(new File("gfx/background.png"));
            this.forground = ImageIO.read(new File("gfx/foreground2.png"));
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        this.bgBtnSmall = this.spritesheetGui.getSubimage(0, 71, 107, 40);
        
        this.bundle = ResourceBundle.getBundle("lang.options", this.game.langs[this.game.configs[0]]);
        
        this.title = this.bundle.getString("title");
        this.btnBack = this.bundle.getString("backToMain");
        this.difficulty = this.bundle.getString("difficulty");
        this.easy = this.bundle.getString("easy");
        this.medium = this.bundle.getString("medium");
        this.hard = this.bundle.getString("hard");
        this.language = this.bundle.getString("language");
        this.french = this.bundle.getString("french");
        this.english = this.bundle.getString("english");
    }

    @Override
    public Scene update() {
        processHover();
        
        if(this.game.listener.mousePressed){
            int mouseX = this.game.listener.mouseX;
            int mouseY = this.game.listener.mouseY;
            
            if(mouseX > this.w/4 - 54 && mouseX < (this.w/4 - 54) + 107 &&
                    mouseY > 210 && mouseY < 210 + 40){
                this.game.configs[1] = 0;
            }
            else if(mouseX > this.w/4 - 54 && mouseX < (this.w/4 - 54) + 107 &&
                    mouseY > 270 && mouseY < 270 + 40){
                this.game.configs[1] = 2;
            }
            else if(mouseX > this.w/4 - 54 && mouseX < (this.w/4 - 54) + 107 &&
                    mouseY > 330 && mouseY < 330 + 40){
                this.game.configs[1] = 4;
            }
            else if(mouseX > 3*this.w/4 - 51 && mouseX < (3*this.w/4 - 51) + 107 &&
                    mouseY > 210 && mouseY < 210 + 40){
                this.game.configs[0] = 0;
                this.game.configs[0] = 0;
                this.reloadLangs();
            }
            else if(mouseX > 3*this.w/4 - 51 && mouseX < (3*this.w/4 - 51) + 107 &&
                    mouseY > 270 && mouseY < 270 + 40){
                this.game.configs[0] = 1;
                this.game.configs[0] = 1;
                this.reloadLangs();
            }
        }
        
        return processClick();
    }

    public void reloadLangs(){
        this.bundle = ResourceBundle.getBundle("lang.options", this.game.langs[this.game.configs[0]]);
        
        this.title = this.bundle.getString("title");
        this.btnBack = this.bundle.getString("backToMain");
        this.difficulty = this.bundle.getString("difficulty");
        this.easy = this.bundle.getString("easy");
        this.medium = this.bundle.getString("medium");
        this.hard = this.bundle.getString("hard");
        this.language = this.bundle.getString("language");
        this.french = this.bundle.getString("french");
        this.english = this.bundle.getString("english");
    }
    
    @Override
    public void render(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.background, 0, 0, null);
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 60);
        
        //difficulty
        
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        int difficultyWidth = metrics.stringWidth(this.difficulty);
        g.drawString(this.difficulty, this.w/4 - difficultyWidth/2, 180);
        
        if(this.game.configs[1] == 0){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }
        
        int easyWidth = metrics.stringWidth(this.easy);
        g.drawImage(this.bgBtnSmall, this.w/4 - easyWidth/2 - 30, 210, null);
        g.setColor(new Color(0, 64, 0));
        g.drawString(this.easy, this.w/4 - easyWidth/2, 235);
        
        if(this.game.configs[1] == 2){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }
        
        int mediumWidth = metrics.stringWidth(this.medium);
        g.drawImage(this.bgBtnSmall, this.w/4 - easyWidth/2 - 30, 270, null);
        g.setColor(new Color(0, 0, 128));
        g.drawString(this.medium, this.w/4 - mediumWidth/2, 295);
        
        if(this.game.configs[1] == 4){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }

        int hardWidth = metrics.stringWidth(this.hard);
        g.drawImage(this.bgBtnSmall, this.w/4 - easyWidth/2 - 30, 330, null);
        g.setColor(new Color(136, 0, 21));
        g.drawString(this.hard, this.w/4 - hardWidth/2, 355);
        
        //languages
        g.setFont(this.font);
        g.setColor(Color.BLACK);
        int languageW = metrics.stringWidth(this.language);
        g.drawString(this.language, 3*this.w/4 - difficultyWidth/2, 180);
        
        int englishW = metrics.stringWidth(this.english);
        if(this.game.configs[0] == 0){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }
        g.drawImage(this.bgBtnSmall, 3*this.w/4 - englishW/2 - 25, 210, null);
        g.drawString(this.english, 3*this.w/4 - englishW/2, 235);
        
        int frenchW = metrics.stringWidth(this.french);
        if(this.game.configs[0] == 1){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }
        g.drawImage(this.bgBtnSmall, 3*this.w/4 - englishW/2 - 25, 270, null);
        g.drawString(this.french, 3*this.w/4 - frenchW/2, 295);
        
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
        
        g.drawImage(this.forground, 0, 0, null);
    }
    
    public void processHover(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        if(mouseX > this.btnCoords[0][0] && mouseX < this.btnCoords[0][0] + 214 &&
                mouseY > this.btnCoords[0][1] && mouseY < this.btnCoords[0][1] + 70){
            //if btn Back
            this.selectedItem = 1;
        }
        else{
            this.selectedItem = 0;
        }
    }
    
    public Scene processClick(){
        
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed){
            switch(this.selectedItem){
                case 1:
                    this.game.saveConfigs();
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                default:
                    currentScene = this;
                    break;
            }
        }
        
        return currentScene;
    }
}
