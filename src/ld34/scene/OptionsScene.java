package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import ld34.Game;

public class OptionsScene extends Scene {
    
    public Font font, fontL, fontU;
    public String title, btnBack, difficulty, easy, medium, hard;
    public Color darkGreen;
    public BufferedImage spritesheetGui, bgBtn, bgBtnSmall, background, forground;
    public int[][] btnCoords;
    public int selectedItem;
    
    public OptionsScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("/fonts/amburegul.otf").openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.fontU = this.font;
            this.fontU = this.fontU.deriveFont(fontAttributes);
            this.spritesheetGui = ImageIO.read(getClass().getResource("/gui.png"));
            this.background = ImageIO.read(getClass().getResource("/background.png"));
            this.forground = ImageIO.read(getClass().getResource("/foreground2.png"));
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        this.darkGreen = new Color(128, 0, 19);
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        this.bgBtnSmall = this.spritesheetGui.getSubimage(0, 71, 107, 40);
        
        this.title = "Settings";
        this.btnBack = "Back to main";
        this.difficulty = "Difficulty : - Change player speed -";
        this.easy = "Easy";
        this.medium = "Medium";
        this.hard = "Hard";
    }

    @Override
    public Scene update() {
        processHover();
        
        if(this.game.listener.mousePressed){
            int mouseX = this.game.listener.mouseX;
            int mouseY = this.game.listener.mouseY;
            
            if(mouseX > this.w/2 - 54 && mouseX < (this.w/2 - 54) + 107 &&
                    mouseY > 210 && mouseY < 210 + 40){
                this.game.difficulty = 0;
            }
            else if(mouseX > this.w/2 - 54 && mouseX < (this.w/2 - 54) + 107 &&
                    mouseY > 270 && mouseY < 270 + 40){
                this.game.difficulty = 2;
            }
            else if(mouseX > this.w/2 - 54 && mouseX < (this.w/2 - 54) + 107 &&
                    mouseY > 330 && mouseY < 330 + 40){
                this.game.difficulty = 4;
            }
        }
        
        return processClick();
    }

    @Override
    public void render(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        g.drawImage(this.background, 0, 0, null);
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 60);
        
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        int difficultyWidth = metrics.stringWidth(this.difficulty);
        g.drawString(this.difficulty, this.w/2 - difficultyWidth/2, 150);
        
        if(this.game.difficulty == 0){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }
        
        int easyWidth = metrics.stringWidth(this.easy);
        g.drawImage(this.bgBtnSmall, this.w/2 - easyWidth/2 - 30, 210, null);
        g.setColor(new Color(0, 64, 0));
        g.drawString(this.easy, this.w/2 - easyWidth/2, 233);
        
        if(this.game.difficulty == 2){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }
        
        int mediumWidth = metrics.stringWidth(this.medium);
        g.drawImage(this.bgBtnSmall, this.w/2 - easyWidth/2 - 30, 270, null);
        g.setColor(new Color(0, 0, 128));
        g.drawString(this.medium, this.w/2 - mediumWidth/2, 297);
        
        if(this.game.difficulty == 4){
            g.setFont(this.fontU);
        }else{
            g.setFont(this.font);
        }

        int hardWidth = metrics.stringWidth(this.hard);
        g.drawImage(this.bgBtnSmall, this.w/2 - easyWidth/2 - 30, 330, null);
        g.setColor(new Color(136, 0, 21));
        g.drawString(this.hard, this.w/2 - hardWidth/2, 355);
        
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
