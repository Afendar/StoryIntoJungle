/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import ld34.Configs;
import ld34.Game;

/**
 *
 * @author Mickaël
 */
public class BestScores extends Scene {
    
    public Font font, fontS, fontL;
    public String title, btnBack;
    public int[][] btnCoords;
    public int selectedItem;
    public ArrayList<String> savedScores = new ArrayList<>();
    public String bestScores = "best_scores.dat";
    
    public BestScores(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontS = this.font.deriveFont(18.0f);
            this.font = this.font.deriveFont(22.0f);
            this.fontL = this.font.deriveFont(52.0f);

        }catch(FontFormatException|IOException e){
            e.printStackTrace();
        }
        int localeIndex = Integer.parseInt(Configs.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.bestscores", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("title");
        this.btnBack = this.bundle.getString("backToMain");
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        loadScores();
    }
    
    public Scene update() {
        
        this.processHover();
        
        return this.processClick();
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
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItem){
                case 1:
                    Configs.getInstance().saveConfig();
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
        int max = this.savedScores.size() > 5 ? 5 : this.savedScores.size();
        for(int i=0; i<max;i++){
            g.drawString(i+1 +".", 200, (i * 50) + 170 );
            g.drawString(this.savedScores.get(i), 250, (i * 50) + 170);
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
    
    public void loadScores(){
        File f = new File(this.bestScores);
        
        if(f.exists() && !f.isDirectory()){
            //save best scores
            try{
                
                String line = null;

                BufferedReader br = new BufferedReader(new FileReader(this.bestScores));

                while((line = br.readLine()) != null){
                    savedScores.add(line);
                }

                br.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
