package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import ld34.Configs;
import ld34.Game;

public class CreditsScene extends Scene {
    
    public Font font, fontL;
    public String title, btnBack, text1, text2, text3;
    public int[][] btnCoords;
    public int selectedItem;
    
    public CreditsScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        
        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        int localeIndex = Integer.parseInt(Configs.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.credits", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("title");
        this.btnBack = this.bundle.getString("backToMain");
        this.text1 = this.bundle.getString("text1");
        this.text2 = this.bundle.getString("text2");
        this.text3 = this.bundle.getString("text3");
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
        
        g.drawImage(this.foreground2, 0, 0, null);
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
