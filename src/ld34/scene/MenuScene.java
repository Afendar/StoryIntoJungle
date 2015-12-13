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

public class MenuScene extends Scene {
    
    public Font font, fontL, fontS;
    public String title, btnNewGame, btnOptions, btnCredits, btnQuit, btnHowTo;
    public BufferedImage spritesheetGui, bgBtn, bgBtnSmall, foreground, background;
    public Color darkGreen;
    public int[][] btnCoords;
    public int selectedItem;
    
    public MenuScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("gfx/fonts/amburegul.otf"));
            this.fontS = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.font = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.spritesheetGui = ImageIO.read(new File("gfx/gui.png"));
            this.foreground = ImageIO.read(new File("gfx/foreground1.png"));
            this.background = ImageIO.read(new File("gfx/background.png"));
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        this.darkGreen = new Color(128, 0, 19);
        
        //new game
        int [][]coords = {
            {this.w/2 - 107, 155},
            {this.w/2 - 107, 245},
            {this.w/2 - 107, 335},
            {(this.w/2) - 130, 480},
            {(this.w/2) + 20, 480}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;

        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        this.bgBtnSmall = this.spritesheetGui.getSubimage(0, 71, 107, 40);
        
        this.title = "Story Into Jungle";
        this.btnNewGame = "New Game";
        this.btnOptions = "Settings";
        this.btnHowTo = "How to play";
        this.btnCredits = "About";
        this.btnQuit = "Quit";
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

        //draw btn
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        int newGameWidth = metrics.stringWidth(this.btnNewGame);
        
        g.drawImage(this.bgBtn, this.btnCoords[0][0], this.btnCoords[0][1], null);
        if(this.selectedItem == 1){
            g2d.rotate(0.07, this.w/2, 180 );
            g.setColor(this.darkGreen);
            g.drawString(this.btnNewGame, this.w/2 - newGameWidth/2, 200);
            g2d.rotate(-0.07, this.w/2, 180);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnNewGame, this.w/2 - newGameWidth/2, 200);
        }
        
        int optionsWidth = metrics.stringWidth(this.btnOptions);
        g.drawImage(this.bgBtn, this.btnCoords[1][0], this.btnCoords[1][1], null);
        if(this.selectedItem == 2){
            g2d.rotate(-0.1, this.w/2, 270);
            g.setColor(this.darkGreen);
            g.drawString(this.btnOptions, this.w/2 - optionsWidth/2, 282);
            g2d.rotate(0.1, this.w/2, 270);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnOptions, this.w/2 - optionsWidth/2, 282);
        }
        
        int howToWidth = metrics.stringWidth(this.btnHowTo);
        g.drawImage(this.bgBtn, this.btnCoords[2][0], this.btnCoords[2][1], null);
        if(this.selectedItem == 3){
            g2d.rotate(0.08, this.w/2, 400 );
            g.setColor(this.darkGreen);
            g.drawString(this.btnHowTo, this.w/2 - howToWidth/2, 375);
            g2d.rotate(-0.08, this.w/2, 400);
        }else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnHowTo, this.w/2 - howToWidth/2, 375);
        }
        
        //small btns
        g.setFont(this.fontS);
        metrics = g.getFontMetrics(this.fontS);
        int creditsWidth = metrics.stringWidth(this.btnCredits);
        g.drawImage(this.bgBtnSmall, this.btnCoords[3][0], this.btnCoords[3][1], null);
        if(this.selectedItem == 4){
            g2d.rotate(0.11, this.w/2 - 75, 525 );
            g.setColor(this.darkGreen);
            g.drawString(this.btnCredits, this.w/2 - creditsWidth/2 - 75, 505);
            g2d.rotate(-0.11, this.w/2 - 75, 525);
        }else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnCredits, this.w/2 - creditsWidth/2 - 75, 505);
        }
        
        int quitWidth = metrics.stringWidth(this.btnQuit);

        g.drawImage(this.bgBtnSmall, this.btnCoords[4][0], this.btnCoords[4][1], null);
        if(this.selectedItem == 5){
            g2d.rotate(-0.13, this.w/2 + 75, 505);
            g.setColor(this.darkGreen);
            g.drawString(this.btnQuit, this.w/2 - quitWidth/2 + 75, 505);
            g2d.rotate(0.13, this.w/2 + 75, 505);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnQuit, this.w/2 - quitWidth/2 + 75, 505);
        }
        
        g.drawImage(this.foreground, 0, -25, null);
    }
    
    public void processHover(){
        
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        if(mouseX > this.btnCoords[0][0] && mouseX < this.btnCoords[0][0] + 214 &&
                mouseY > this.btnCoords[0][1] && mouseY < this.btnCoords[0][1] + 70){
            //if btn new game
            this.selectedItem = 1;
        }
        else if(mouseX > this.btnCoords[1][0] && mouseX < this.btnCoords[1][0] + 214 &&
                mouseY > this.btnCoords[1][1] && mouseY < this.btnCoords[1][1] + 70){
            //if btn settings
            this.selectedItem = 2;
        }
        else if(mouseX > this.btnCoords[2][0] && mouseX < this.btnCoords[2][0] + 214 &&
                mouseY > this.btnCoords[2][1] && mouseY < this.btnCoords[2][1] + 70){
            this.selectedItem = 3;
        }
        else if(mouseX > this.btnCoords[3][0] && mouseX < this.btnCoords[3][0] + 107 &&
                mouseY > this.btnCoords[3][1] && mouseY < this.btnCoords[3][1] + 40){
            //if btn credits
            this.selectedItem = 4;
        }
        else if(mouseX > this.btnCoords[4][0] && mouseX < this.btnCoords[4][0] + 107 &&
                mouseY > this.btnCoords[4][1] && mouseY < this.btnCoords[4][1] + 40){
            //if btn quit
            this.selectedItem = 5;
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
                    currentScene =new GameScene(this.w, this.h, this.game);
                    break;
                case 2:
                    currentScene = new OptionsScene(this.w, this.h, this.game);
                    break;
                case 3:
                    currentScene = new HowToScene(this.w, this.h, this.game);
                    break;
                case 4:
                    currentScene = new CreditsScene(this.w, this.h, this.game);
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    currentScene = this;
            }
        }
        
        return currentScene;
    }
}
