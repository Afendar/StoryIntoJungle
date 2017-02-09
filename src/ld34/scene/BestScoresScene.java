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
import ld34.profile.BestScores;
import org.json.simple.JSONArray;

public class BestScoresScene extends Scene {
    
    public Font font, fontS, fontL;
    public String title, btnBack;
    public int[][] btnCoords;
    public int selectedItem;
    private final JSONArray bestScores;
    
    public BestScoresScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontS = this.font.deriveFont(18.0f);
            this.font = this.font.deriveFont(22.0f);
            this.fontL = this.font.deriveFont(52.0f);

        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.bestscores", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("title");
        this.btnBack = this.bundle.getString("backToMain");
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        this.bestScores = BestScores.getInstance().getBestScores();
    }
    
    @Override
    public Scene update(double dt) {
        
        this.processHover();
        
        return this.processClick();
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
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
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
    
    @Override
    public void render(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.background, 0, 0, null);
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 60);
        
        g.setFont(this.font);
        g.setColor(Color.BLACK);
        
        for(int i=0;i<this.bestScores.size();i++){
            if(this.bestScores.get(i) instanceof JSONArray){
                JSONArray array = (JSONArray) this.bestScores.get(i);
                if(array.size() == 2){
                    String name = (String) array.get(0);
                    String score = (String) array.get(1);
                    g.drawString((i + 1) +". " + name + " : " + score, 200, (i * 50) + 170 );
                }
            }
        }
        
        metrics = g.getFontMetrics(this.font);
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
        
        g.drawImage(this.foreground2, 0, 0, null);
    }
}
