package ld34.scene;

import audio.Sound;
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
import ld34.profile.Settings;
import core.Game;

public class CreditsScene extends Scene {
    
    public Font font, fontM, fontL;
    public String title, btnBack, text1, text2, text3;
    public int[][] btnCoords;
    public int selectedItem;
    
    public CreditsScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontM = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.credits", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("title");
        this.btnBack = this.bundle.getString("backToMain");
        this.text1 = this.bundle.getString("text1");
        this.text2 = this.bundle.getString("text2");
        this.text3 = this.bundle.getString("text3");
    }

    @Override
    public Scene update(double dt) {
        
        processHover();
        
        return processClick();
    }

    @Override
    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.background, 0, 0, null);
        
        g.setColor(Scene.BLACKSHADOW);
        g.fillRect(0, 35, this.w, 60);
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 75);
        
        g.setFont(this.fontM);
        metrics = g.getFontMetrics(this.fontM);
        
        int text1Width = metrics.stringWidth(this.text1);
        g.drawString(this.text1, this.w/2 - text1Width/2, 200);
        
        int text2Width = metrics.stringWidth(this.text2);
        g.drawString(this.text2, this.w/2 - text2Width/2, 250);
        
        int text3Width = metrics.stringWidth(this.text3);
        g.drawString(this.text3, this.w/2 - text3Width/2, 300);
        
        int backWidth = metrics.stringWidth(this.btnBack);
        if(this.selectedItem == 1){
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 133, 234, 99);
        }
        else{
            this.bgBtn = this.spritesheetGui2.getSubimage(0, 232, 234, 99);
        }
        g.drawImage(this.bgBtn, this.btnCoords[0][0], this.btnCoords[0][1], null);
        g.setColor(Scene.DARKGREY);
        g.drawString(this.btnBack, (3 * this.w/4)+32 - backWidth/2, 510);
        
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
        else{
            this.selectedItem = 0;
        }
    }
    
    public Scene processClick(){
        
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed){
            switch(this.selectedItem){
                case 1:
                    new Thread(Sound.select::play).start();
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
