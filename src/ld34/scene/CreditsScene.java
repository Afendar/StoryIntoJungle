package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import ld34.Game;

public class CreditsScene extends Scene {
    
    public Font font, fontL;
    public String title, btnBack, text1, text2, text3;
    public Color darkGreen;
    public BufferedImage spritesheetGui, bgBtn, background, forground;
    public int[][] btnCoords;
    public int selectedItem;
    
    public CreditsScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("/fonts/amburegul.otf").openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.spritesheetGui = ImageIO.read(getClass().getResource("/gui.png"));
            this.background = ImageIO.read(getClass().getResource("/background.png"));
            this.forground = ImageIO.read(getClass().getResource("/foreground2.png"));
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        this.darkGreen = new Color(128, 0, 19);
        
        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        this.title = "About Story Into Jungle";
        this.btnBack = "Back to main";
        this.text1 = "This game was made in 48 hours by Afendar";
        this.text2 = "for the LudumDare 34 competition.";
        this.text3 = "Edited in 12-13 december 2015.";
    }

    @Override
    public Scene update() {
        
        processHover();
        
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
        g.drawString(this.title, this.w/2 - titlewidth/2, 130);
        
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        
        int text1Width = metrics.stringWidth(this.text1);
        g.drawString(this.text1, this.w/2 - text1Width/2, 240);
        
        int text2Width = metrics.stringWidth(this.text2);
        g.drawString(this.text2, this.w/2 - text2Width/2, 320);
        
        int text3Width = metrics.stringWidth(this.text3);
        g.drawString(this.text3, this.w/2 - text3Width/2, 400);
        
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
