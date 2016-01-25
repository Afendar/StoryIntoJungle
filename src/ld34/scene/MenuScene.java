package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.Game;

public class MenuScene extends Scene {
    
    public Font font, fontL, fontS;
    public String title, btnNewGame, btnOptions, btnCredits, btnQuit;
    public BufferedImage spritesheetGui, bgBtn, bgBtnSmall, foreground, background;
    public int[][] btnCoords;
    public int selectedItem;
    
    public MenuScene(int w, int h, Game game){
        
        super(w, h, game);
        
        try{
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("gfx/fonts/kaushanscriptregular.ttf"));
            this.fontS = this.font.deriveFont(18.0f);
            this.font = this.font.deriveFont(22.0f);
            this.fontL = this.font.deriveFont(52.0f);
            this.spritesheetGui = ImageIO.read(new File("gfx/gui.png"));
            this.foreground = ImageIO.read(new File("gfx/foreground1.png"));
            this.background = ImageIO.read(new File("gfx/background.png"));
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        this.bundle = ResourceBundle.getBundle("lang.menu", this.game.langs[this.game.configs[0]]);
        
        //new game
        int [][]coords = {
            {this.w/2 - 107, 185},
            {this.w/2 - 107, 275},
            {(this.w/2) - 130, 410},
            {(this.w/2) + 20, 410}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;

        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        this.bgBtnSmall = this.spritesheetGui.getSubimage(0, 71, 107, 40);
        
        this.title = "Story Into Jungle";
        this.btnNewGame = bundle.getString("newGame");
        this.btnOptions = bundle.getString("settings");
        this.btnCredits = bundle.getString("about");
        this.btnQuit = bundle.getString("quit");
    }

    @Override
    public Scene update() {
        
        processHover();

        return processClick();
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
        g.drawString(this.title, this.w/2 - titlewidth/2, 80);

        //draw btn
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        int newGameWidth = metrics.stringWidth(this.btnNewGame);
        
        g.drawImage(this.bgBtn, this.btnCoords[0][0], this.btnCoords[0][1], null);
        if(this.selectedItem == 1){
            g2d.rotate(0.07, this.w/2, 200 );
            g.setColor(this.darkGreen);
            g.drawString(this.btnNewGame, this.w/2 - newGameWidth/2, 225);
            g2d.rotate(-0.07, this.w/2, 200);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnNewGame, this.w/2 - newGameWidth/2, 225);
        }
        
        int optionsWidth = metrics.stringWidth(this.btnOptions);
        g.drawImage(this.bgBtn, this.btnCoords[1][0], this.btnCoords[1][1], null);
        if(this.selectedItem == 2){
            g2d.rotate(-0.1, this.w/2, 325);
            g.setColor(this.darkGreen);
            g.drawString(this.btnOptions, this.w/2 - optionsWidth/2, 317);
            g2d.rotate(0.1, this.w/2, 325);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnOptions, this.w/2 - optionsWidth/2, 317);
        }
        
        //small btns
        g.setFont(this.fontS);
        metrics = g.getFontMetrics(this.fontS);
        int creditsWidth = metrics.stringWidth(this.btnCredits);
        g.drawImage(this.bgBtnSmall, this.btnCoords[2][0], this.btnCoords[2][1], null);
        if(this.selectedItem == 4){
            g2d.rotate(0.11, this.w/2 - 75, 435 );
            g.setColor(this.darkGreen);
            g.drawString(this.btnCredits, this.w/2 - creditsWidth/2 - 80, 435);
            g2d.rotate(-0.11, this.w/2 - 75, 435);
        }else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnCredits, this.w/2 - creditsWidth/2 - 80, 435);
        }
        
        int quitWidth = metrics.stringWidth(this.btnQuit);

        g.drawImage(this.bgBtnSmall, this.btnCoords[3][0], this.btnCoords[3][1], null);
        if(this.selectedItem == 5){
            g2d.rotate(-0.13, this.w/2 + 75, 435);
            g.setColor(this.darkGreen);
            g.drawString(this.btnQuit, this.w/2 - quitWidth/2 + 70, 435);
            g2d.rotate(0.13, this.w/2 + 75, 435);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnQuit, this.w/2 - quitWidth/2 + 70, 435);
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
        else if(mouseX > this.btnCoords[2][0] && mouseX < this.btnCoords[2][0] + 107 &&
                mouseY > this.btnCoords[2][1] && mouseY < this.btnCoords[2][1] + 40){
            //if btn credits
            this.selectedItem = 4;
        }
        else if(mouseX > this.btnCoords[3][0] && mouseX < this.btnCoords[3][0] + 107 &&
                mouseY > this.btnCoords[3][1] && mouseY < this.btnCoords[3][1] + 40){
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
