package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ld34.Game;

public class EndScene extends Scene {

    public Font font, fontS;
    public String text1, text2, text3, text4, btnBack;
    public BufferedImage spritesheetGui, bgBtn, foreground, background;
    public Color beige, darkGreen;
    public int[][] btnCoords;
    public int selectedItem;
    
    public EndScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("gfx/fonts/amburegul.otf"));
            this.font = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 17.0f);
            this.spritesheetGui = ImageIO.read(new File("gfx/gui.png"));
            this.foreground = ImageIO.read(new File("gfx/foreground3.png"));
            this.background = ImageIO.read(new File("gfx/background.png"));
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        this.beige = new Color(239, 228, 176);
        this.darkGreen = new Color(128, 0, 19);
        
        //new game
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;

        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        
        this.text1 = "The little panda has recover his mom,";
        this.text2 = "but he is no longer a child...";
        this.text3 = "Now it is he who will watch over his mother.";
        this.text4 = "Thanks a lot having played my game";
        this.btnBack = "Back to main";
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
        
        g.setFont(this.font);
        g.setColor(Color.BLACK);
        FontMetrics metrics = g.getFontMetrics(this.font);
        
        int text1Width = metrics.stringWidth(this.text1);
        g.drawString(this.text1, this.w/2 - text1Width/2, 240);
        int text2Width = metrics.stringWidth(this.text2);
        g.drawString(this.text2, this.w/2 - text2Width/2, 300);
        int text3Width = metrics.stringWidth(this.text3);
        g.drawString(this.text3, this.w/2 - text3Width/2, 360);
        
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
        
        g.setFont(this.fontS);
        g.setColor(Color.BLACK);
        metrics = g.getFontMetrics(this.fontS);
        int text4Width = metrics.stringWidth(this.text4);
        g.drawString(this.text4, this.w/3 - text4Width/2 + 40, 490);
        
        g.drawImage(this.foreground, 0, 0, null);
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
