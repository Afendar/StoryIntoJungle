package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.Configs;
import ld34.Game;

public class MenuScene extends Scene {
    
    public BufferedImage background2;
    public Font font, fontL, fontS;
    public String title, btnNewGame, btnOptions, btnBestScores, btnCredits, btnQuit;
    public int[][] btnCoords;
    public int selectedItem;
    
    public MenuScene(int w, int h, Game game){
        
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontS = this.font.deriveFont(18.0f);
            this.font = this.font.deriveFont(22.0f);
            this.fontL = this.font.deriveFont(52.0f);
            
            url = runtimeClass.getResource("/background3.png");
            this.background2 = ImageIO.read(url);

        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }

        this.bundle = ResourceBundle.getBundle("lang.menu", this.game.langs[Integer.parseInt(Configs.getInstance().getConfigValue("Lang"))]);
        
        //new game
        int [][]coords = {
            {this.w/2 - 107, 165},
            {this.w/2 - 107, 255},
            {this.w/2 - 107, 345},
            {(this.w/2) - 130, 440},
            {(this.w/2) + 20, 440}
        };
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        
//        this.title = "Story Into Jungle";
        this.btnNewGame = bundle.getString("newGame");
        this.btnOptions = bundle.getString("settings");
        this.btnBestScores = bundle.getString("bestScores");
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
        g.drawImage(this.background2, 0, 0, null);
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
//        g.setFont(this.fontL);
//        g.setColor(Color.BLACK);
//        int titlewidth = metrics.stringWidth(this.title);
//        g.drawString(this.title, this.w/2 - titlewidth/2, 80);

        //draw btn
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        int newGameWidth = metrics.stringWidth(this.btnNewGame);
        
        g.drawImage(this.bgBtn, this.btnCoords[0][0], this.btnCoords[0][1], null);
        if(this.selectedItem == 1){
            g2d.rotate(0.07, this.w/2, 180 );
            g.setColor(this.darkGreen);
            g.drawString(this.btnNewGame, this.w/2 - newGameWidth/2, 208);
            g2d.rotate(-0.07, this.w/2, 180);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnNewGame, this.w/2 - newGameWidth/2, 208);
        }
        
        int optionsWidth = metrics.stringWidth(this.btnOptions);
        g.drawImage(this.bgBtn, this.btnCoords[1][0], this.btnCoords[1][1], null);
        if(this.selectedItem == 2){
            g2d.rotate(-0.1, this.w/2, 315);
            g.setColor(this.darkGreen);
            g.drawString(this.btnOptions, this.w/2 - optionsWidth/2, 297);
            g2d.rotate(0.1, this.w/2, 315);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnOptions, this.w/2 - optionsWidth/2, 297);
        }
        
        //btn Hightest score
        int scoreW = metrics.stringWidth(this.btnBestScores);
        g.drawImage(this.bgBtn, this.btnCoords[2][0], this.btnCoords[2][1], null);
        if(this.selectedItem == 3){
            g2d.rotate(0.07, this.w/2, 450);
            g.setColor(this.darkGreen);
            g.drawString(this.btnBestScores, this.w/2 - scoreW/2, 389);
            g2d.rotate(-0.07, this.w/2, 450);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnBestScores, this.w/2 - scoreW/2, 389);
        }
        
        //small btns
        g.setFont(this.fontS);
        metrics = g.getFontMetrics(this.fontS);
        int creditsWidth = metrics.stringWidth(this.btnCredits);
        g.drawImage(this.bgBtnSmall, this.btnCoords[3][0], this.btnCoords[3][1], null);
        if(this.selectedItem == 4){
            g2d.rotate(0.11, this.w/2 - 75, 465 );
            g.setColor(this.darkGreen);
            g.drawString(this.btnCredits, this.w/2 - creditsWidth/2 - 80, 465);
            g2d.rotate(-0.11, this.w/2 - 75, 465);
        }else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnCredits, this.w/2 - creditsWidth/2 - 80, 465);
        }
        
        int quitWidth = metrics.stringWidth(this.btnQuit);

        g.drawImage(this.bgBtnSmall, this.btnCoords[4][0], this.btnCoords[4][1], null);
        if(this.selectedItem == 5){
            g2d.rotate(-0.13, this.w/2 + 75, 465);
            g.setColor(this.darkGreen);
            g.drawString(this.btnQuit, this.w/2 - quitWidth/2 + 70, 465);
            g2d.rotate(0.13, this.w/2 + 75, 465);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnQuit, this.w/2 - quitWidth/2 + 70, 465);
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
            //if btn meilleur scores
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
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItem){
                case 1:
                    currentScene =new GameScene(this.w, this.h, this.game);
                    break;
                case 2:
                    currentScene = new OptionsScene(this.w, this.h, this.game);
                    break;
                case 3:
                    currentScene = new BestScores(this.w, this.h, this.game);
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
