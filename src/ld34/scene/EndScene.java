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

public class EndScene extends Scene {

    public Font font, fontS;
    public String text1, text2, text3, text4, btnBack;
    public int[][] btnCoords;
    public int selectedItem;
    public int alpha;
    
    public EndScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 22.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 17.0f);

        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }

        //new game
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        int localeIndex = Integer.parseInt(Configs.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.end", this.game.langs[localeIndex]);
        
        this.text1 = this.bundle.getString("text1");
        this.text2 = this.bundle.getString("text2");
        this.text3 = this.bundle.getString("text3");
        this.text4 = this.bundle.getString("text4");
        this.btnBack = this.bundle.getString("backToMain");
        
        this.alpha = 255;
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
        
        g.setFont(this.font);
        g.setColor(Color.BLACK);
        FontMetrics metrics = g.getFontMetrics(this.font);
        
        int text1Width = metrics.stringWidth(this.text1);
        g.drawString(this.text1, this.w/2 - text1Width/2, 240);
        int text2Width = metrics.stringWidth(this.text2);
        g.drawString(this.text2, this.w/2 - text2Width/2, 300);
        int text3Width = metrics.stringWidth(this.text3);
        g.drawString(this.text3, this.w/2 - text3Width/2, 360);
        
        if(this.alpha > 0){
            this.alpha--;
        }else
        {
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
        }
        
        g.setFont(this.fontS);
        g.setColor(Color.BLACK);
        metrics = g.getFontMetrics(this.fontS);
        int text4Width = metrics.stringWidth(this.text4);
        g.drawString(this.text4, this.w/3 - text4Width/2 + 40, 490);
        
        g.drawImage(this.foreground3, 0, 0, null);
        
        g.setColor(new Color(0, 0, 0, this.alpha));
        g.fillRect(0, 0, this.w, this.h);
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
