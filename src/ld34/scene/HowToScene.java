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

public class HowToScene extends Scene {
    
    public Font font, fontL;
    public String title, btnBack, txt1, txt2, txt3, txt4, txt5;
    public Color darkGreen;
    public BufferedImage spritesheetGui, bgBtn, background, forground, tileset, pious, bridge, leaves, apple;
    public int[][] btnCoords;
    public int selectedItem;
    
    public HowToScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("gfx/fonts/amburegul.otf"));
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.spritesheetGui = ImageIO.read(new File("gfx/gui.png"));
            this.tileset = ImageIO.read(new File("gfx/tileset.png"));
            this.background = ImageIO.read(new File("gfx/background.png"));
            this.forground = ImageIO.read(new File("gfx/foreground2.png"));
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        this.darkGreen = new Color(128, 0, 19);
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        this.pious = this.tileset.getSubimage(6*32, 0, 32, 32);
        this.bridge = this.tileset.getSubimage(3*32, 0, 32, 32);
        this.apple = this.tileset.getSubimage(4*32, 0, 32, 32);
        this.leaves = this.tileset.getSubimage(5*32, 0, 32, 32);
        
        this.title = "How to play";
        this.txt1 = "press SPACE to JUMP and CTRL to WALK SLOWLY";
        this.txt2 = "Apples add 10 points";
        this.txt3 = "Leaves add 100 points";
        this.txt4 = "Bridges break if not walk slowly";
        this.txt5 = "Spikes kill you";
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
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 60);

        //explains
        
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        int txt1W = metrics.stringWidth(this.txt1);
        g.drawString(this.txt1, this.w/2 - txt1W/2, 170);
        
        g.drawImage(this.apple, 100, 225, null);
        g.drawString(this.txt2, 145, 250);
        
        g.drawImage(this.leaves, 420, 225, null);
        g.drawString(this.txt3, 470, 250);
        
        g.drawImage(this.bridge, 100, 320, null);
        g.drawString(this.txt4, 150, 326);
        
        g.drawImage(this.pious, 100, 365, null);
        g.drawString(this.txt5, 150, 390);
        
        //explains end
        
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
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
            }
        }
        
        return currentScene;
    }
}
